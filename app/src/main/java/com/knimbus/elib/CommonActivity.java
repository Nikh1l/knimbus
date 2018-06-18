package com.knimbus.elib;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommonActivity extends AppCompatActivity {

    private static final String TAG = "CommonActivity";
    RecyclerView rvCommonList;
    List<CommonListing> commonListingList = new ArrayList<>();
    CommonListingAdapter commonListingAdapter = new CommonListingAdapter(commonListingList);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        Intent i = getIntent();
        String type = i.getStringExtra("type");
        String subject = i.getStringExtra("subject");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(subject);
        getSupportActionBar().setSubtitle(type);

        rvCommonList = findViewById(R.id.rv_common_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCommonList.setLayoutManager(layoutManager);
        rvCommonList.setAdapter(commonListingAdapter);


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(subject).child(type);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: "+dataSnapshot);
                Log.d(TAG, "onDataChange: Children : "+dataSnapshot.getValue());
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: for: "+dataSnapshot1);
                    String title = dataSnapshot1.getKey();
                    String author = (String) dataSnapshot1.child("author").getValue();
                    String type = (String) dataSnapshot1.child("type").getValue();
                    String url = (String) dataSnapshot1.child("url").getValue();
                    String size = (String) dataSnapshot1.child("size").getValue();
                    commonListingList.add(new CommonListing(title,author,type,url,size));
                    commonListingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
