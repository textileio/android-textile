package io.textile.textile;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.File;
import java.util.HashSet;

import io.textile.pb.Mobile.MobileWalletAccount;
import io.textile.pb.View.Summary;
import mobile.InitConfig;
import mobile.MigrateConfig;
import mobile.Mobile;
import mobile.Mobile_;
import mobile.RunConfig;

/**
 * Provides top level access to the Textile API
 */
public class Textile implements LifecycleObserver {

    enum AppState {
        None, Background, Foreground, BackgroundFromForeground
    }

    /**
     * Provides access to Textile account related APIs
     */
    public Account account;

    /**
     * Provides access to Textile cafes related APIs
     */
    public Cafes cafes;

    /**
     * Provides access to Textile comments related APIs
     */
    public Comments comments;

    /**
     * Provides access to Textile contacts related APIs
     */
    public Contacts contacts;

    /**
     * Provides access to Textile feed related APIs
     */
    public Feed feed;

    /**
     * Provides access to Textile files related APIs
     */
    public Files files;

    /**
     * Provides access to Textile flags related APIs
     */
    public Flags flags;

    /**
     * Provides access to Textile ignores related APIs
     */
    public Ignores ignores;

    /**
     * Provides access to Textile invites related APIs
     */
    public Invites invites;

    /**
     * Provides access to Textile IPFS related APIs
     */
    public Ipfs ipfs;

    /**
     * Provides access to Textile likes related APIs
     */
    public Likes likes;

    /**
     * Provides access to Textile logs related APIs
     */
    public Logs logs;

    /**
     * Provides access to Textile messages related APIs
     */
    public Messages messages;

    /**
     * Provides access to Textile notifications related APIs
     */
    public Notifications notifications;

    /**
     * Provides access to Textile profile related APIs
     */
    public Profile profile;

    /**
     * Provides access to Textile schemas related APIs
     */
    public Schemas schemas;

    /**
     * Provides access to Textile threads related APIs
     */
    public Threads threads;

    /**
     * The path to the local Textile repository
     */
    public String repoPath;

    private HashSet<TextileEventListener> eventsListeners = new HashSet();
    private MessageHandler messageHandler = new MessageHandler(eventsListeners);

    private Mobile_ node;
    private Context applicationContext;
    private Intent lifecycleServiceIntent;
    private LifecycleService lifecycleService;
    private AppState appState = AppState.None;

    private static class TextileHelper {
        private static final Textile INSTANCE = new Textile();
    }

    /**
     * Initialize the shared Textile instace, possibly returning the wallet recovery phrase
     * @param applicationContext The host app's application context
     * @param debug Sets the log level to debug or not
     * @param logToDisk Whether or not to write Textile logs to disk
     * @return The wallet recovery phrase if it's the first time the node is being initialize, nil otherwise
     * @throws Exception The exception that occurred
     */
    public static String initialize(Context applicationContext, boolean debug, boolean logToDisk) throws Exception {
        if (Textile.instance().node != null) {
            return null;
        }
        Textile.instance().applicationContext = applicationContext;
        File filesDir = applicationContext.getFilesDir();
        String path = new File(filesDir, "textile-go").getAbsolutePath();
        Textile.instance().repoPath = path;
        try {
            Textile.instance().newTextile(path, debug);
            Textile.instance().createNodeDependents();
            Textile.instance().lifecycleServiceIntent = new Intent(applicationContext, LifecycleService.class);
            applicationContext.bindService(Textile.instance().lifecycleServiceIntent, Textile.instance().connection, Context.BIND_AUTO_CREATE);
            applicationContext.startService(Textile.instance().lifecycleServiceIntent);
            return null;
        } catch (Exception e) {
            if (e.getMessage().equals("repo does not exist, initialization is required")) {
                try {
                    String recoveryPhrase = Textile.instance().newWallet(12);
                    MobileWalletAccount account = Textile.instance().walletAccountAt(recoveryPhrase, 0, "");
                    Textile.instance().initRepo(account.getSeed(), path, logToDisk, debug);
                    Textile.instance().newTextile(path, debug);
                    Textile.instance().createNodeDependents();
                    Textile.instance().lifecycleServiceIntent = new Intent(applicationContext, LifecycleService.class);
                    applicationContext.bindService(Textile.instance().lifecycleServiceIntent, Textile.instance().connection, Context.BIND_AUTO_CREATE);
                    applicationContext.startService(Textile.instance().lifecycleServiceIntent);
                    return recoveryPhrase;
                } catch (Exception innerError) {
                    throw innerError;
                }
            } else {
                throw e;
            }
        }
    }

    /**
     * The shared Textile instance, should be used for all Textile API access
     */
    public static Textile instance() {
        return TextileHelper.INSTANCE;
    }

    private Textile () {
    }

    private String newWallet(long wordCount) throws Exception {
        return Mobile.newWallet(wordCount);
    }

    private MobileWalletAccount walletAccountAt(String phrase, long index, String password) throws Exception {
        byte[] bytes = Mobile.walletAccountAt(phrase, index, password);
        return MobileWalletAccount.parseFrom(bytes);
    }

    private void initRepo(String seed, String repoPath, boolean logToDisk, boolean debug) throws Exception {
        InitConfig config = new InitConfig();
        config.setSeed(seed);
        config.setRepoPath(repoPath);
        config.setLogToDisk(logToDisk);
        config.setDebug(debug);
        Mobile.initRepo(config);
    }

    private void migrateRepo(String repoPath) throws Exception {
        MigrateConfig config = new MigrateConfig();
        config.setRepoPath(repoPath);
        Mobile.migrateRepo(config);
    }

    private void newTextile(String repoPath, boolean debug) throws Exception {
        RunConfig config = new RunConfig();
        config.setRepoPath(repoPath);
        config.setDebug(debug);
        node = Mobile.newTextile(config, messageHandler);
    }

    void start() {
        try {
            node.start();
        } catch (Exception e) {
            for (TextileEventListener listener : eventsListeners) {
                listener.nodeFailedToStart(e);
            }
        }
    }

    void stop() {
        try {
            node.stop();
        } catch (Exception e) {
            for (TextileEventListener listener : eventsListeners) {
                listener.nodeFailedToStop(e);
            }
        }
    }

    void notifyListenersOfPendingNodeStop(int seconds) {
        for (TextileEventListener listener : eventsListeners) {
            listener.willStopNodeInBackgroundAfterDelay(seconds);
        }
    }

    void notifyListenersOfCanceledPendingNodeStop() {
        for (TextileEventListener listener : eventsListeners) {
            listener.canceledPendingNodeStop();
        }
    }

    /**
     * @return The version of the Textile node running locally
     */
    public String version() {
        return node.version();
    }

    /**
     * @return The git summary of the Textile node running locally
     */
    public String gitSummary() {
        return node.gitSummary();
    }

    /**
     * Get a summary of the local Textile node and it's data
     * @return A summary of the Textile node running locally
     * @throws Exception The exception that occurred
     */
    public Summary summary() throws Exception {
        byte[] bytes = node.summary();
        return Summary.parseFrom(bytes);
    }

    /**
     * Reset the local Textile node instance so it can be re-initialized
     * @throws Exception The exception that occurred
     */
    public void destroy() throws Exception {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        applicationContext.unbindService(connection);
        lifecycleService.stopNodeImmediately();
        lifecycleService = null;
        lifecycleServiceIntent = null;

        eventsListeners.clear();
        node = null;
        applicationContext = null;

        account = null;
        cafes = null;
        comments = null;
        contacts = null;
        feed = null;
        files = null;
        flags = null;
        ignores = null;
        invites = null;
        ipfs = null;
        likes = null;
        logs = null;
        messages = null;
        notifications = null;
        profile = null;
        schemas = null;
        threads = null;

        repoPath = null;
    }

    /**
     * Register an listener to receive callbacks about Textile events
     * @param listener The listener that will be called back
     */
    public void addEventListener(TextileEventListener listener) {
        eventsListeners.add(listener);
    }

    /**
     * Remove a previously registered event listener
     * @param listener The listener to remove
     */
    public void removeEventListener(TextileEventListener listener) {
        eventsListeners.remove(listener);
    }

    private void createNodeDependents() {
        account = new Account(node);
        cafes = new Cafes(node);
        comments = new Comments(node);
        contacts = new Contacts(node);
        feed = new Feed(node);
        files = new Files(node);
        flags = new Flags(node);
        ignores = new Ignores(node);
        invites = new Invites(node);
        ipfs = new Ipfs(node);
        likes = new Likes(node);
        logs = new Logs(node);
        messages = new Messages(node);
        notifications = new Notifications(node);
        profile = new Profile(node);
        schemas = new Schemas(node);
        threads = new Threads(node);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onForeground() {
        if (appState.equals(AppState.Foreground)) {
            return;
        }
        if (appState.equals(AppState.BackgroundFromForeground)) {
            lifecycleService.cancelPendingNodeStop();
        } else {
            lifecycleService.startNode();
        }
        appState = AppState.Foreground;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onBackground() {
        if (appState.equals(AppState.Background)) {
            return;
        }
        if (appState.equals(AppState.None)) {
            appState = AppState.Background;
            lifecycleService.startNode();
            lifecycleService.stopNodeAfterDelay();
        } else if (appState.equals(AppState.Foreground)) {
            appState = AppState.BackgroundFromForeground;
            lifecycleService.stopNodeAfterDelay();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LifecycleService.LifecycleBinder binder = (LifecycleService.LifecycleBinder) service;
            lifecycleService = binder.getService();
            lifecycleService.nodeStoppedListener = new LifecycleService.NodeStoppedListener() {
                @Override
                public void onNodeStopped() {
                    Textile.this.appState = AppState.None;
                }
            };
            ProcessLifecycleOwner.get().getLifecycle().addObserver(Textile.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
