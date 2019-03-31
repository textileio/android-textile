package io.textile.textile;

import io.textile.pb.View.LogLevel;
import mobile.Mobile_;

public class Logs extends NodeDependent {

    Logs(Mobile_ node) {
        super(node);
    }

    public void setLevel(LogLevel level) throws Exception {
        node.setLogLevel(level.toByteArray());
    }
}
