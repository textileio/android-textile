package io.textile.textile;

public class Textile {

    public Account account;

    private Textile () {

    }

    private static class TextileHelper {
        private static final Textile INSTANCE = new Textile();
    }

    public static Textile instance() {
        return TextileHelper.INSTANCE;
    }
}
