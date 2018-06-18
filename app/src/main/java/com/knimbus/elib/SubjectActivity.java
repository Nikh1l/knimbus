package com.knimbus.elib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class SubjectActivity extends AppCompatActivity {

    LinearLayout llTextbooks, llNotes, llQuestionBanks;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Intent i = getIntent();
        subject = i.getStringExtra("subName");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subject);

        llTextbooks = findViewById(R.id.ll_subject_textbooks);
        llNotes = findViewById(R.id.ll_subject_notes);
        llQuestionBanks = findViewById(R.id.ll_subject_question_banks);

        llTextbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectActivity.this, CommonActivity.class);
                intent.putExtra("type","TextBooks");
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });

        llQuestionBanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectActivity.this, CommonActivity.class);
                intent.putExtra("type","QuestionBanks");
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });

        llNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectActivity.this, CommonActivity.class);
                intent.putExtra("type","Notes");
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}