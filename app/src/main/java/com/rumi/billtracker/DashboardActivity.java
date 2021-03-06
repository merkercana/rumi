package com.rumi.billtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button btn_group;
    Button btn_createGroup;
    EditText etb_createGroupName;
    String uid;
    String displayName;
    String groupName;
    boolean groupCreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btn_group = (Button) findViewById(R.id.btn_Group);
        btn_createGroup = (Button) findViewById(R.id.btn_createGroup);
        etb_createGroupName = (EditText) findViewById(R.id.etb_createGroupName);

        Firebase.setAndroidContext(this);
        final Firebase rootRef = new Firebase("https://blazing-inferno-7973.firebaseio.com");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        backdrop.setImageResource(R.drawable.blue_poly);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        displayName = sharedPreferences.getString(Utility.USERNAME_KEY, null);

        setTitle(displayName);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
                startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return true;
            }
        });

        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupName = etb_createGroupName.getText().toString();
                if (!groupName.equals("")) {
                    Query verify = rootRef.child("Users").child(displayName);
                    verify.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(rootRef.child("Users").child(displayName).child(groupName).getKey()))
                                startGroupActivity(groupName);
                            else
                                Toast.makeText(getApplicationContext(), "You are not a part of " + groupName, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        btn_createGroup.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupCreated = false;
                groupName = etb_createGroupName.getText().toString();
                if (!groupName.equals("")) {
                    Query validate = rootRef.child("Groups");
                    validate.addValueEventListener((new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!groupCreated) {
                                if (!dataSnapshot.hasChild(rootRef.child("Groups").child(groupName).getKey())) {
                                    groupCreated = true;
                                    Utility.createGroup(rootRef, groupName, displayName);
                                    startGroupActivity(groupName);
                                } else
                                    Toast.makeText(getApplicationContext(), groupName + " already exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    }));
                } else
                    Toast.makeText(getApplicationContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void startGroupActivity(String groupName) {
        SharedPreferences sharedPreferences = getSharedPreferences(Utility.GROUP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Utility.GROUP_NAME, groupName);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}