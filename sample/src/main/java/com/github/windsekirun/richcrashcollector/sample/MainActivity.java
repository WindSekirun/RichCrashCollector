package com.github.windsekirun.richcrashcollector.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnNull;
    private Button btnRuntime;
    private Button btnIllegal;
    private Button btnIndex;
    private Button btnArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNull = (Button) findViewById(R.id.btnNull);
        btnRuntime = (Button) findViewById(R.id.btnRuntime);
        btnIllegal = (Button) findViewById(R.id.btnIllegal);
        btnIndex = (Button) findViewById(R.id.btnIndex);
        btnArray = (Button) findViewById(R.id.btnArray);

        btnNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new NullPointerException("test");
            }
        });

        btnRuntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("test");
            }
        });

        btnIllegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IllegalStateException("test");
            }
        });

        btnIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IndexOutOfBoundsException("test");
            }
        });

        btnArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new ArrayIndexOutOfBoundsException("test");
            }
        });

    }
}
