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

public class Textile implements LifecycleObserver {

    enum AppState {
        None, Background, Foreground, BackgroundFromForeground
    }

    public Account account;
    public Cafes cafes;
    public Comments comments;
    public Contacts contacts;
    public Feed feed;
    public Files files;
    public Flags flags;
    public Ignores ignores;
    public Invites invites;
    public Ipfs ipfs;
    public Likes likes;
    public Logs logs;
    public Messages messages;
    public Notifications notifications;
    public Profile profile;
    public Schemas schemas;
    public Threads threads;
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
        return bytes != null ? MobileWalletAccount.parseFrom(bytes) : null;
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

    public String version() {
        return node.version();
    }

    public String gitSummary() {
        return node.gitSummary();
    }

    public Summary summary() throws Exception {
        byte[] bytes = node.summary();
        return bytes != null ? Summary.parseFrom(bytes) : null;
    }

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

    public void addEventListener(TextileEventListener listener) {
        eventsListeners.add(listener);
    }

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
