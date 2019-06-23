package io.textile.textileexample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import io.textile.pb.Model;
import io.textile.textile.BuildConfig;
import io.textile.textile.Handlers;
import io.textile.textile.Textile;
import io.textile.textile.TextileLoggingListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextile();
    }

    public void onButtonClick(View v) {
        // destroyTextile();
        // initTextile();

        // Uncomment below for a small sync test
        Textile.instance().cafes.register(
                BuildConfig.TEST_CAFE_ID, BuildConfig.TEST_CAFE_TOKEN, new Handlers.ErrorHandler() {
            @Override
            public void onComplete() {
                try {
                    final Model.Thread thread =
                            Textile.instance().threads.add(io.textile.pb.View.AddThreadConfig.newBuilder()
                                    .setName("data")
                                    .setKey(UUID.randomUUID().toString())
                                    .setSchema(io.textile.pb.View.AddThreadConfig.Schema.newBuilder()
                                            .setPreset(io.textile.pb.View.AddThreadConfig.Schema.Preset.BLOB)
                                            .build())
                                    .build());

                    Textile.instance().files.addData(
                            "test".getBytes(), thread.getId(), "caption", new Handlers.BlockHandler() {
                        @Override
                        public void onComplete(Model.Block block) {
                            System.out.println("Added data with block ID: " + block.getId());
                        }

                        @Override
                        public void onError(Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }

    private void initTextile() {
        try {
            String phrase = Textile.initialize(getApplicationContext(), true, false);
            Textile.instance().addEventListener(new TextileLoggingListener());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void destroyTextile() {
        try {
            Textile.instance().destroy();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
