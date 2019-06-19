package io.textile.textile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import io.textile.pb.Model;
import io.textile.pb.View;
import io.textile.pb.View.AddThreadConfig;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

/**
 * Textile tests.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextileTest {

    @Test
    public void test0_initialize() throws Exception {
        final Context appContext = InstrumentationRegistry.getTargetContext();
        final String phrase = Textile.initialize(appContext, true, false);
        Textile.instance().addEventListener(new TestTextileListener());
    }

    @Test
    public void test1_start() {
        Textile.instance().start();
    }

    @Test
    public void test2_version() {
        assertNotEquals("", Textile.instance().version());
    }

    @Test
    public void test2_gitSummary() {
        assertNotEquals("", Textile.instance().gitSummary());
    }

    @Test
    public void test2_summary() throws Exception {
        assertNotEquals("", Textile.instance().summary().getAddress());
    }

//    @Test
//    public void test3_waitForOnline() throws Exception {
//        Textile.instance().
//        await().atMost(5, SECONDS).untilTrue(ready);
//    }

//    @Test
//    public void test3_registerCafe() throws Exception {
//        Textile.instance().cafes.register(
//                "12D3KooWLaJnBr1bqWkZCDhaFeGxKiCP91rt2gQ8rn7Lx7kcKAMY",
//                "ukbN5nU1BhhiDwBPq3XrbUnqakzKnrVRBXc5u2oj1Np3DBttmn757PYsN2u2");
//    }

    @Test
    public void test3_addThread() throws Exception {
        final Model.Thread thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("test")
                .setKey(UUID.randomUUID().toString())
                .setSchema(AddThreadConfig.Schema.newBuilder()
                        .setPreset(AddThreadConfig.Schema.Preset.MEDIA)
                        .build())
                .setType(Model.Thread.Type.OPEN)
                .setSharing(Model.Thread.Sharing.SHARED)
                .build());
        assertNotEquals("", thread.getId());
    }

    @Test
    public void test3_addData() throws Exception {
        final Model.Thread thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("data")
                .setKey(UUID.randomUUID().toString())
                .setSchema(AddThreadConfig.Schema.newBuilder()
                        .setPreset(AddThreadConfig.Schema.Preset.BLOB)
                        .build())
                .setType(Model.Thread.Type.PRIVATE)
                .setSharing(Model.Thread.Sharing.NOT_SHARED)
                .build());

        final AtomicBoolean ready = new AtomicBoolean();
        Textile.instance().files.addData("test".getBytes(), thread.getId(), "caption", new Files.BlockHandler() {
            @Override
            public void onComplete(Model.Block b) {
                assertNotEquals("", b.getId());
                ready.getAndSet(true);
            }

            @Override
            public void onError(Exception e) {
                assertNull(e);
                ready.getAndSet(true);
            }
        });

        await().atMost(5, SECONDS).untilTrue(ready);
    }

    @Test
    public void test3_addFiles() throws Exception {
        final Model.Thread thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("test")
                .setKey(UUID.randomUUID().toString())
                .setSchema(AddThreadConfig.Schema.newBuilder()
                        .setPreset(AddThreadConfig.Schema.Preset.MEDIA)
                        .build())
                .setType(Model.Thread.Type.OPEN)
                .setSharing(Model.Thread.Sharing.SHARED)
                .build());

        // 1. Add a single file
        final View.Strings input1 = View.Strings.newBuilder()
                .addValues(TextileTest.getCacheFile(
                        InstrumentationRegistry.getContext(), "TEST0.JPG").getAbsolutePath())
                .build();

        final AtomicBoolean ready = new AtomicBoolean();
        Textile.instance().files.addFiles(input1, thread.getId(), "caption", new Files.BlockHandler() {
            @Override
            public void onComplete(Model.Block block) {
                assertNotEquals("", block.getId());
                ready.getAndSet(true);
            }

            @Override
            public void onError(Exception e) {
                assertNull(e);
                ready.getAndSet(true);
            }
        });

        await().untilTrue(ready);

        // 2. Add two files at once
        final View.Strings input2 = View.Strings.newBuilder()
                .addValues(TextileTest.getCacheFile(
                        InstrumentationRegistry.getContext(), "TEST0.JPG").getAbsolutePath())
                .addValues(TextileTest.getCacheFile(
                        InstrumentationRegistry.getContext(), "TEST1.JPG").getAbsolutePath())
                .build();

        ready.getAndSet(false);
        Textile.instance().files.addFiles(input2, thread.getId(), "caption", new Files.BlockHandler() {
            @Override
            public void onComplete(Model.Block block) {
                assertNotEquals("", block.getId());
                ready.getAndSet(true);
            }

            @Override
            public void onError(Exception e) {
                assertNull(e);
                ready.getAndSet(true);
            }
        });

        await().untilTrue(ready);
    }

    @Test
    public void test99_destroy() throws Exception {
        Textile.instance().destroy();
    }

    private static File getCacheFile(Context context, String filename) throws IOException {
        File file = new File(context.getCacheDir(), filename);
        InputStream inputStream = context.getAssets().open(filename);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
            } finally {
                outputStream.close();
            }
        }
        return file;
    }
}
