package com.sala7khaled.todo_auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemActivity extends AppCompatActivity {

    TextView title_TV, desc_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        title_TV = (TextView) findViewById(R.id.title_TV);
        desc_TV = (TextView) findViewById(R.id.desc_TV);

        Intent intent = getIntent();
        String title_intent = intent.getStringExtra("title");
        String desc_intent = intent.getStringExtra("desc");

        title_TV.setText(title_intent);
        desc_TV.setText(desc_intent);

    }

}
