package com.knimbus.elib;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView rvSubjects;
    List<HomeSubjects> subjects = new ArrayList<>();
    HomeSubjectsAdapter mAdapter = new HomeSubjectsAdapter(subjects);
    DatabaseReference dbRefSubject = FirebaseDatabase.getInstance().getReference().child("subjects");
    String TAG = "HomeFragment";


    public HomeFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_home, container, false);

        rvSubjects = v.findViewById(R.id.rv_home_subjects);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);

        rvSubjects.setLayoutManager(mLayoutManager);
        rvSubjects.setAdapter(mAdapter);

        dbRefSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nos = (int) dataSnapshot.getChildrenCount();
                for(int i=1;i<nos+1;i++){
                    Log.d(TAG, "onDataChange: child "+dataSnapshot.child(i+"").getValue());
                    String subject = (String) dataSnapshot.child(i+"").getValue();
                    subjects.add(new HomeSubjects(subject));
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


}
