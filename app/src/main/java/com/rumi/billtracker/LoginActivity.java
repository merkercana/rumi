package com.rumi.billtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText editText;
    Button btn_login;
    Button btn_signUp;
    String username;
    String name;
    Map<String, Object> newMember = new HashMap<String, Object>();
    List<String> usernames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        final Firebase rootRef = new Firebase("https://blazing-inferno-7973.firebaseio.com");

        editText = (EditText)findViewById(R.id.editText);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_signUp = (Button)findViewById(R.id.btn_signUp);

        final Firebase ref = rootRef.child("users");
        Query queryRef = ref.orderByValue();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                usernames.add(dataSnapshot.getKey());
                name = dataSnapshot.getKey();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //do nothing
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //do nothing
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //do nothing
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //do nothing
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editText.getText().toString();
                //Firebase checkRef = rootRef.child("users").child(username);
                //Query query = checkRef.equalTo(true);
                SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Utility.USERNAME_KEY, username);
                editor.apply();

                Firebase userRef = rootRef.child("users").child(username);
                Firebase memberRef = userRef.child("memberRelations");

                memberRef.setValue("");
                userRef.child("net").setValue(0);

                for (int i = 0; i < usernames.size(); i++) {
                    if (usernames.get(i) != username) {
                        memberRef.child(usernames.get(i)).setValue(0);
                        newMember = new HashMap<String, Object>();
                        newMember.put(username, 0);
                        rootRef.child("users").child(usernames.get(i)).child("memberRelations").updateChildren(newMember);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
