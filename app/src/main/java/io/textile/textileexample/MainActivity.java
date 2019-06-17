package io.textile.textileexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.textile.textile.Textile;

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
            String phrase = Textile.initialize(getApplicationContext(), true, false);
            Textile.instance().addEventListener(new TextileListener());
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
