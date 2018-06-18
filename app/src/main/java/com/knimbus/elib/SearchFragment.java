package com.knimbus.elib;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment {


    RecyclerView rvSearchResult;
    List<HashMap<String ,String >> resultList = new ArrayList<>(), filteredList = new ArrayList<>();
//    List<SearchResult> searchResultList = new ArrayList<>(), filteredList = new ArrayList<>();
    SearchResultAdapter searchResultAdapter;
    EditText etSearchBar;
    String TAG = "SearchFragment";

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        rvSearchResult = v.findViewById(R.id.rv_search_result);
        etSearchBar = v.findViewById(R.id.et_search_all);

        rvSearchResult = v.findViewById(R.id.rv_search_result);
        searchResultAdapter = new SearchResultAdapter(filteredList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvSearchResult.setLayoutManager(layoutManager);
        rvSearchResult.setAdapter(searchResultAdapter);

        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        DatabaseReference dbMath = FirebaseDatabase.getInstance().getReference().child("Engineering Mathematics 3");
        dbMath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        Log.d(TAG, "onDataChange: "+dataSnapshot2);
                        String title = dataSnapshot2.getKey();
                        String author = (String) dataSnapshot2.child("author").getValue();

                        HashMap<String, String > res = new HashMap<>();
                        res.put("title", title);
                        res.put("author", author);

                        resultList.add(res);
                    }
                }

                filteredList.addAll(resultList);
                searchResultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

    private void filter(String s) {
        filteredList.clear();
        for (HashMap<String,String> m : resultList){
            if(m.get("title").toLowerCase().contains(s.toLowerCase())){
                filteredList.add(m);
            }
        }
        if(filteredList.size()==0){
            filteredList.addAll(resultList);
        }
        searchResultAdapter.notifyDataSetChanged();
    }

}
