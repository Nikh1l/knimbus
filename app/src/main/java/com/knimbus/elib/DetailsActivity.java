package com.knimbus.elib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView tvTitle,tvAuthor,tvType,tvSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String author = i.getStringExtra("author");
        String type = i.getStringExtra("type");
        String size = i.getStringExtra("size");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);


        tvTitle = findViewById(R.id.tv_details_title);
        tvAuthor = findViewById(R.id.tv_details_author);
        tvType = findViewById(R.id.tv_details_type);
        tvSize = findViewById(R.id.tv_details_size);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvType.setText(type);
        tvSize.setText(size + "MB");

    }
}
