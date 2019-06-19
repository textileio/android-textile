package io.textile.textile;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.textile.pb.Model.CafeHTTPRequest;
import io.textile.pb.Model.CafeRequest;
import io.textile.pb.View.Strings;
import mobile.Callback;

/**
 * Implements Request for RequestsHandlerTest
 */
class TestRequests implements Requests {
    private Map<String,CafeRequest.Status> requests;

    TestRequests(final int total) {
        requests = new HashMap<>();
        for (int i = 0; i < total; i++) {
            requests.put(UUID.randomUUID().toString(), CafeRequest.Status.NEW);
        }
    }

    public byte[] cafeRequests(long limit) throws Exception {
        Strings ids = Strings.newBuilder()
                .addAllValues(requests.keySet()).build();
        return ids.toByteArray();
    }

    public void cafeRequestPending(String id) throws Exception {
        requests.put(id, CafeRequest.Status.PENDING);
    }

    public void completeCafeRequest(String id) throws Exception {
        requests.put(id, CafeRequest.Status.COMPLETE);
    }

    public void failCafeRequest(String id, String reason) throws Exception {
        requests.remove(id);
    }

    public void writeCafeRequest(String id, Callback cb) {
        URL url = this.getClass().getClassLoader().getResource("tmp/1Mqja2M42E75Y8qgI0RlUypMgw7");
        CafeHTTPRequest req = CafeHTTPRequest.newBuilder()
                .setType(CafeHTTPRequest.Type.PUT)
                .setUrl("http://127.0.0.1:")
                .build();
    }
}
