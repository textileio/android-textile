package io.textile.textileexample;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.io.File;

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
         destroyTextile();
         initTextile();
    }

    private void initTextile() {
        try {
            Context ctx = getApplicationContext();

            final File filesDir = ctx.getFilesDir();
            final String path = new File(filesDir, "textile-go").getAbsolutePath();

            if (!Textile.isInitialized(path)) {
                String phrase = Textile.initializeCreatingNewWalletAndAccount(path, true, false);
                System.out.println(phrase);
            }

            Textile.launch(ctx, path, true);

            Textile.instance().addEventListener(new TextileLoggingListener());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void destroyTextile() {
        Textile.instance().destroy();
    }
}
