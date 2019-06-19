package io.textile.textile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.textile.pb.Model.CafeRequest.Status;
import io.textile.pb.View.*;
import mobile.Callback;

/**
 * RequestsHandler tests.
 */
@RunWith(AndroidJUnit4.class)
public class RequestsHandlerTest {

    private static int total = 24;
    private static int batchSize = 24;

    @Test
    public void flush() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        TestRequests api = new TestRequests(RequestsHandlerTest.total);
        RequestsHandler handler = new RequestsHandler(api, appContext, RequestsHandlerTest.batchSize);

        handler.flush();
    }
}

/**
 * Implements a subset of Mobile_ used by RequestsHandler
 */
class TestRequests implements Requests {
    private Map<String,Status> requests;

    TestRequests(final int total) {
        requests = new HashMap<>();
        for (int i = 0; i < total; i++) {
            requests.put(UUID.randomUUID().toString(), Status.NEW);
        }
    }

    public byte[] cafeRequests(long limit) throws Exception {
        Strings ids = Strings.newBuilder().addAllValues(requests.keySet()).build();
        return ids.toByteArray();
    }

    public void cafeRequestPending(String id) throws Exception {
        requests.put(id, Status.PENDING);
    }

    public void completeCafeRequest(String id) throws Exception {
        requests.put(id, Status.COMPLETE);
    }

    public void failCafeRequest(String id, String reason) throws Exception {
        requests.remove(id);
    }

    public void writeCafeRequest(String id, Callback cb) {
        // @todo
    }
}