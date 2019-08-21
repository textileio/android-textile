package io.textile.textile;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

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

    private static final String TAG = "Textile";
    private static final String WAIT_SRC = "Textile.flush";

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
     * The number of words to use in the wallet's mnemonic phrase
     */
    public static int WALLET_WORD_COUNT = 12;

    /**
     * The BIP39 Passphrase (optional) to use when creating the wallet seed
     */
    public static String WALLET_PASSPHRASE = "";

    /**
     * The wallet's default account derivation path index
     */
    public static int WALLET_ACCOUNT_INDEX = 0;

    /**
     * The number of threads to use to handle concurrent uploads
     */
    public static int UPLOAD_POOL_SIZE = 8;

    /**
     * The number of requests to write to disk before adding to the background queue
     */
    public static int REQUESTS_BATCH_SIZE = 16;

    RequestsHandler requestsHandler;

    private Context applicationContext;

    private HashSet<TextileEventListener> eventListeners = new HashSet<>();
    private MessageHandler messageHandler = new MessageHandler(eventListeners);

    private Mobile_ node;

    private Intent lifecycleServiceIntent;
    private LifecycleService lifecycleService;

    private AppState appState = AppState.None;

    private static final RequestsBroadcastReceiver broadcastReceiver = new RequestsBroadcastReceiver();

    private static class TextileHelper {
        private static final Textile INSTANCE = new Textile();
    }

    /**
     * Create a new Textile wallet
     * @param wordCount The number of words the wallet recovery phrase should contain
     * @return The new wallet recovery phrase
     * @throws Exception The exception that occurred
     */
    public static String newWallet(final long wordCount) throws Exception {
        return Mobile.newWallet(wordCount);
    }

    /**
     * Resolve a wallet account
     * @param phrase The wallet recovery phrase
     * @param index The index of the account to resolve
     * @param password The wallet password or nil if there is no password
     * @return The wallet account
     * @throws Exception The exception that occurred
     */
    public static MobileWalletAccount walletAccountAt(final String phrase, final long index, final String password) throws Exception {
        final byte[] bytes = Mobile.walletAccountAt(phrase, index, password);
        return MobileWalletAccount.parseFrom(bytes);
    }

    /**
     * Check if Textile is already initialized
     * @param repoPath The path to the Textile repo
     * @return A boolean value indicating if Textile is initialized or not
     */
    public static Boolean isInitialized(final String repoPath) {
        File file = new File(repoPath);
        return file.exists() && file.isDirectory();
    }

    /**
     * Initialize the shared Textile instance with an existing account seed
     * @param repoPath The path to the Textile repo
     * @param seed The account seed
     * @param debug Sets the log level to debug or not
     * @param logToDisk Whether or not to write Textile logs to disk
     * @throws Exception The exception that occurred
     */
    public static void initialize(final String repoPath, final String seed, final boolean debug, final boolean logToDisk) throws Exception {
        final InitConfig config = new InitConfig();
        config.setSeed(seed);
        config.setRepoPath(repoPath);
        config.setLogToDisk(logToDisk);
        config.setDebug(debug);
        Mobile.initRepo(config);
    }

    /**
     * Initialize the shared Textile instance, creating a new wallet
     * @param repoPath The path to the Textile repo
     * @param debug Sets the log level to debug or not
     * @param logToDisk Whether or not to write Textile logs to disk
     * @return The wallet recovery phrase
     * @throws Exception The exception that occurred
     */
    public static String initializeCreatingNewWalletAndAccount(final String repoPath, final boolean debug, final boolean logToDisk) throws Exception {
        final String recoveryPhrase = Textile.newWallet(WALLET_WORD_COUNT);
        final MobileWalletAccount account = Textile.walletAccountAt(recoveryPhrase, WALLET_ACCOUNT_INDEX, WALLET_PASSPHRASE);
        Textile.initialize(repoPath, account.getSeed(), debug, logToDisk);
        return recoveryPhrase;
    }

    /**
     * After initialization is complete, launch Textile
     * @param applicationContext The application context
     * @param repoPath The path to the Textile repo
     * @param debug Sets the log level to debug or not
     * @throws Exception The exception that occurred
     */
    public static void launch(final Context applicationContext, final String repoPath, final boolean debug) throws Exception {
        Textile.instance().applicationContext = applicationContext;
        Textile.instance().requestsHandler = new RequestsHandler(REQUESTS_BATCH_SIZE);
        Textile.instance().node = Textile.newTextile(repoPath, debug, Textile.instance().requestsHandler, Textile.instance().messageHandler);
        Textile.instance().createNodeDependents();

        if (debug) {
            Logger.setLogLevel(Logger.LogLevel.DEBUG);
        }

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.HTTP_STACK = new OkHttpStack();
        UploadService.UPLOAD_POOL_SIZE = Textile.UPLOAD_POOL_SIZE;

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BuildConfig.APPLICATION_ID + ".uploadservice.broadcast.status");
        applicationContext.registerReceiver(broadcastReceiver, intentFilter);

        Textile.instance().lifecycleServiceIntent = new Intent(applicationContext, LifecycleService.class);
        applicationContext.bindService(Textile.instance()
                .lifecycleServiceIntent, Textile.instance().connection, Context.BIND_AUTO_CREATE);
        applicationContext.startService(Textile.instance().lifecycleServiceIntent);
    }

    /**
     * The shared Textile instance, should be used for all Textile API access
     * @return The shared Textile instance
     */
    public static Textile instance() {
        return TextileHelper.INSTANCE;
    }

    private static void migrateRepo(final String repoPath) throws Exception {
        final MigrateConfig config = new MigrateConfig();
        config.setRepoPath(repoPath);
        Mobile.migrateRepo(config);
    }

    private static Mobile_ newTextile(final String repoPath, final boolean debug, final RequestsHandler requestsHandler, final MessageHandler messenger) throws Exception {
        final RunConfig config = new RunConfig();
        config.setRepoPath(repoPath);
        config.setDebug(debug);
        // Having this part of the config is not ideal. We should instead make it a setter
        // which can be used after node creation.
        config.setCafeOutboxHandler(requestsHandler);
        return Mobile.newTextile(config, messenger);
    }

    private Textile () {
    }

    void start() {
        try {
            node.start();
        } catch (final Exception e) {
            for (final TextileEventListener listener : eventListeners) {
                listener.nodeFailedToStart(e);
            }
        }
    }

    void stop(Handlers.ErrorHandler handler) {
        if (node == null) {
            handler.onComplete();
            return;
        }
        node.stop((Exception e) -> {
            if(e != null) {
                handler.onError(e);
                for (final TextileEventListener listener : eventListeners) {
                    listener.nodeFailedToStop(e);
                }
            } else {
                handler.onComplete();
            }
        });
    }

    void notifyListenersOfPendingNodeStop(final int seconds) {
        for (final TextileEventListener listener : eventListeners) {
            listener.willStopNodeInBackgroundAfterDelay(seconds);
        }
    }

    void notifyListenersOfCanceledPendingNodeStop() {
        for (final TextileEventListener listener : eventListeners) {
            listener.canceledPendingNodeStop();
        }
    }

    /**
     * @return The application context
     */
    public Context getApplicationContext() {
        return applicationContext;
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
        final byte[] bytes = node.summary();
        return Summary.parseFrom(bytes);
    }

    /**
     * Return whether or not the node is online
     * @return A boolean indicating the online status of the node
     */
    public boolean online() {
        return node.online();
    }

    /**
     * Reset the local Textile node instance so it can be re-initialized
     */
    public void destroy() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        applicationContext.unbindService(connection);
        lifecycleService.stopNodeImmediately();
        lifecycleService = null;
        lifecycleServiceIntent = null;

        requestsHandler = null;

        eventListeners.clear();
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
    }

    /**
     * Register an listener to receive callbacks about Textile events
     * @param listener The listener that will be called back
     */
    public void addEventListener(TextileEventListener listener) {
        eventListeners.add(listener);
    }

    /**
     * Acquire a flush lock
     */
    protected void flushLock() {
        node.waitAdd(1, WAIT_SRC);
    }

    /**
     * Release a flush lock
     */
    protected void flushUnlock() {
        node.waitDone(WAIT_SRC);
    }

    /**
     * Remove a previously registered event listener
     * @param listener The listener to remove
     */
    public void removeEventListener(TextileEventListener listener) {
        eventListeners.remove(listener);
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
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            LifecycleService.LifecycleBinder binder = (LifecycleService.LifecycleBinder) service;
            lifecycleService = binder.getService();
            lifecycleService.nodeStoppedListener = () -> Textile.this.appState = AppState.None;
            ProcessLifecycleOwner.get().getLifecycle().addObserver(Textile.this);
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            Logger.info(TAG, name + " disconnected");
        }
    };
}
