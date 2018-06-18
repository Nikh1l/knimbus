package com.knimbus.elib;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Todo: add icons to bottom navigation view and navigation drawer

        mAuth = FirebaseAuth.getInstance();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView nvDrawer = findViewById(R.id.nav_view);
        mDrawerLayout.addDrawerListener(mToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle.syncState();
        setDrawerContent(nvDrawer);

        View header = nvDrawer.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.tv_nav_header_display_name);
        if(mAuth!=null){
            String mName = mAuth.getCurrentUser().getDisplayName();
            tvName.setText(mName);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void selectDrawerItem(MenuItem item){
        //Todo: fragment transaction to be done here
        android.support.v4.app.Fragment selectFragment = null;
        Class fragmentClass = HomeFragment.class;
        switch (item.getItemId()) {
            case R.id.nav_item_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_item_search:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.nav_item_notes:
                fragmentClass = NotesFragment.class;
                break;
            case R.id.nav_item_settings:
                fragmentClass = NotesFragment.class;
                break;
            case R.id.dd_menu_signOut:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
                break;
            case R.id.nav_item_chat:
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                Toast.makeText(this, "Be nice to everyone in the chatroom", Toast.LENGTH_SHORT).show();
                break;
        }
        try {
            selectFragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_activity_main, selectFragment).commit();
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    private void setDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }


}
