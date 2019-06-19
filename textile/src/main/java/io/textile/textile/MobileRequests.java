package io.textile.textile;

import mobile.Callback;
import mobile.Mobile_;

/**
 * Implements a subset of Mobile_ used by RequestsHandler
 */
class MobileRequests extends NodeDependent implements Requests {

    MobileRequests(final Mobile_ node) {
        super(node);
    }

    public byte[] cafeRequests(long limit) throws Exception {
        return node.cafeRequests(limit);
    }

    public void cafeRequestPending(String id) throws Exception {
        node.cafeRequestPending(id);
    }

    public void completeCafeRequest(String id) throws Exception {
        node.completeCafeRequest(id);
    }

    public void failCafeRequest(String id, String reason) throws Exception {
        node.failCafeRequest(id, reason);
    }

    public void writeCafeRequest(String id, Callback cb) {
        node.writeCafeRequest(id, cb);
    }
}
