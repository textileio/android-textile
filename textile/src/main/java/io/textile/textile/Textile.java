package io.textile.textile;

import mobile.Mobile_;

public class Textile {

    public Account account;

    Mobile_ node;

    private Textile () {

    }


    private static class TextileHelper {
        private static final Textile INSTANCE = new Textile();
    }

    public static Textile instance() {
        return TextileHelper.INSTANCE;
    }
}
