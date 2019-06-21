package io.textile.textile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.Callable;
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
        final Context ctx = InstrumentationRegistry.getTargetContext();
        final File repo = new File(ctx.getFilesDir(), Textile.REPO_NAME);
        if (repo.exists()) {
            FileUtils.deleteDirectory(repo);
        }

        final String phrase = Textile.initialize(ctx, true, false);
        assertNotEquals("", phrase);

        Textile.instance().addEventListener(new TextileTestListener());
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

    @Test
    public void test3_online() {
        await().atMost(30, SECONDS).until(isOnline());
    }

    @Test
    public void test4_registerCafe() {
        final AtomicBoolean ready = new AtomicBoolean();
        Textile.instance().cafes.register(BuildConfig.TEST_CAFE_ID, BuildConfig.TEST_CAFE_TOKEN, new Handlers.ErrorHandler() {
            @Override
            public void onComplete() {
                ready.getAndSet(true);
            }

            @Override
            public void onError(Exception e) {
                assertNull(e);
                ready.getAndSet(true);
            }
        });

        await().atMost(10, SECONDS).untilTrue(ready);
    }

    @Test
    public void test5_addThread() throws Exception {
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
    public void test5_addMessage() throws Exception {
        final Model.Thread thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("data")
                .setKey(UUID.randomUUID().toString())
                .build());

        String blockId = Textile.instance().messages.add(thread.getId(), "hello");
        assertNotEquals("", blockId);
    }

    @Test
    public void test5_addData() throws Exception {
        final Model.Thread thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("data")
                .setKey(UUID.randomUUID().toString())
                .setSchema(AddThreadConfig.Schema.newBuilder()
                        .setPreset(AddThreadConfig.Schema.Preset.BLOB)
                        .build())
                .build());

        final AtomicBoolean ready = new AtomicBoolean();
        Textile.instance().files.addData("test".getBytes(), thread.getId(), "caption", new Handlers.BlockHandler() {
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

        await().atMost(10, SECONDS).untilTrue(ready);
    }

    @Test
    public void test5_addFiles() throws Exception {
        final Context ctx = InstrumentationRegistry.getTargetContext();

        final Model.Thread thread = Textile.instance().threads.add(AddThreadConfig.newBuilder()
                .setName("test")
                .setKey(UUID.randomUUID().toString())
                .setSchema(AddThreadConfig.Schema.newBuilder()
                        .setPreset(AddThreadConfig.Schema.Preset.MEDIA)
                        .build())
                .build());

        // 1. Add a single file
        final String input1 = TextileTest.getCacheFile(ctx, "TEST0.JPG").getAbsolutePath();

        final AtomicBoolean ready = new AtomicBoolean();
        Textile.instance().files.addFiles(input1, thread.getId(), "caption", new Handlers.BlockHandler() {
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

        await().atMost(30, SECONDS).untilTrue(ready);

        // 2. Add two files at once
        final String input2 = TextileTest.getCacheFile(ctx, "TEST1.JPG").getAbsolutePath();

        ready.getAndSet(false);
        Textile.instance().files.addFiles(
                input1 + "," + input2, thread.getId(), "caption", new Handlers.BlockHandler() {
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

        await().atMost(60, SECONDS).untilTrue(ready);
    }

    @Test
    public void test99_destroy() throws Exception {
        Thread.sleep(20000);
        Textile.instance().destroy();
    }

    private Callable<Boolean> isOnline() {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Textile.instance().online();
            }
        };
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
