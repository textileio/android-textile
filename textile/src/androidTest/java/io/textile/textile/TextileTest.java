package io.textile.textile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import io.textile.pb.Model.Block;
import io.textile.pb.Model.Thread;
import io.textile.pb.View.*;

import static org.junit.Assert.*;

/**
 * Textile tests.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextileTest {

    private static int total = 24;
    private static int batchSize = 8;

    private Thread thread;

    @Test
    public void test1_initialize() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String phrase = Textile.initialize(appContext, true, false);
        Textile.instance().addEventListener(new TestTextileListener());
    }

    @Test
    public void test2_start() {
        Textile.instance().start();
    }

    @Test
    public void test3_version() {
        assertNotEquals("", Textile.instance().version());
    }

    @Test
    public void test4_gitSummary() {
        assertNotEquals("", Textile.instance().gitSummary());
    }

    @Test
    public void test4_summary() throws Exception {
        assertNotEquals("", Textile.instance().summary().getAddress());
    }

    @Test
    public void test5_addThread() throws Exception {
        thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("test")
                .setKey(UUID.randomUUID().toString())
                .setSchema(AddThreadConfig.Schema.newBuilder()
                        .setPreset(AddThreadConfig.Schema.Preset.MEDIA)
                        .build())
                .setType(Thread.Type.OPEN)
                .setSharing(Thread.Sharing.SHARED)
                .build());
        assertNotEquals("", thread.getId());
    }

    @Test
    public void test6_addFiles() {
        Textile.instance().files.addData("test".getBytes(), thread.getId(), "caption", new Files.BlockHandler() {
            @Override
            public void onComplete(Block block) {
                block.getId();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Test
    public void test5_flush() {
//        TestRequests api = new TestRequests(TextileTest.total);
//        RequestsHandler handler = new RequestsHandler(api, appContext, TextileTest.batchSize);
//
//        handler.flush();
    }
}
