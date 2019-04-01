package io.textile.textile;

import io.textile.pb.View.Summary;
import mobile.Mobile_;

public class Textile {

    public Account account;

    Mobile_ node;

    private Textile () {
    }

    private static class TextileHelper {
        private static final Textile INSTANCE = new Textile();
    }

    public static void initialize(boolean debug, boolean logToDisk) throws Exception {

    }

    public static Textile instance() {
        return TextileHelper.INSTANCE;
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
}
