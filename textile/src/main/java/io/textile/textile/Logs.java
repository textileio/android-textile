package io.textile.textile;

import io.textile.pb.View.LogLevel;
import mobile.Mobile_;

/**
 * Provides access to Textile logs related APIs
 */
public class Logs extends NodeDependent {

    Logs(final Mobile_ node) {
        super(node);
    }

    /**
     * Set the log level for the Textile node
     * @param level Object containing a dictionary of log level for each logging system
     * @throws Exception The exception that occurred
     */
    public void setLevel(final LogLevel level) throws Exception {
        node.setLogLevel(level.toByteArray());
    }
}
