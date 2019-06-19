package io.textile.textile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * RequestHandler tests.
 */
@RunWith(AndroidJUnit4.class)
public class RequestHandlerTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();

//        RequestHandler handler = new RequestHandler(appContext)
    }
}
