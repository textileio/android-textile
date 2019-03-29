package io.textile.textileexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.textile.textile.Textile;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String address = Textile.instance().account.address();
    }
}
