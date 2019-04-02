package io.textile.textile;

import android.content.Context;
import android.content.Intent;
import java.io.File;
import java.util.HashSet;

import io.textile.pb.Mobile.MobileWalletAccount;
import io.textile.pb.View.Summary;
import mobile.InitConfig;
import mobile.MigrateConfig;
import mobile.Mobile;
import mobile.Mobile_;
import mobile.RunConfig;

public class Textile {

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

    private HashSet<TextileEventListener> eventsListeners = new HashSet();

    private Mobile_ node;

    private Textile () {
    }

    private static class TextileHelper {
        private static final Textile INSTANCE = new Textile();
    }

    public static String initialize(Context applicationContext, boolean debug, boolean logToDisk) throws Exception {
        File filesDir = applicationContext.getFilesDir();
        String path = new File(filesDir, "textile-repo").getAbsolutePath();
        try {
            Textile.instance().newTextile(path, debug);
            Textile.instance().createNodeDependents();
            Intent intent = new Intent(applicationContext, LifecycleManager.class);
            applicationContext.startService(intent);
            return null;
        } catch (Exception e) {
            if (e.getMessage().equals("repo does not exist, initialization is required")) {
                try {
                    String recoveryPhrase = Textile.instance().newWallet(12);
                    MobileWalletAccount account = Textile.instance().walletAccountAt(recoveryPhrase, 0, "");
                    Textile.instance().initRepo(account.getSeed(), path, logToDisk, debug);
                    Textile.instance().newTextile(path, debug);
                    Textile.instance().createNodeDependents();
                    Intent intent = new Intent(applicationContext, LifecycleManager.class);
                    applicationContext.startService(intent);
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
        if (node == null) {
            RunConfig config = new RunConfig();
            config.setRepoPath(repoPath);
            config.setDebug(debug);
            node = Mobile.newTextile(config, new MessageHandler(eventsListeners));
        }
    }

    void start() throws Exception {
        node.start();
    }

    void stop() throws Exception {
        node.stop();
    }

    public String version() {
        return node.version();
    }

    public String gitSummary() {
        return node.gitSummary();
    }

    public Summary summary() throws Exception {
        byte[] bytes = node.summary();
        return Summary.parseFrom(bytes);
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
}
