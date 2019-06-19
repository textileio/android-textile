package io.textile.textile;

import mobile.Callback;

/**
 * Defines a mockable subset of Mobile_ used by RequestsHandler
 */
interface Requests {
    byte[] cafeRequests(long limit) throws Exception;
    void cafeRequestPending(String id) throws Exception;
    void completeCafeRequest(String id) throws Exception;
    void failCafeRequest(String id, String reason) throws Exception;
    void writeCafeRequest(String var1, Callback var2);
}
