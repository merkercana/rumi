package com.rumi.billtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    EditText etb_username;
    EditText etb_email;
    EditText etb_password;
    Button btn_create;
    String email, username, password;
    Map<String, Object> newMember = new HashMap<String, Object>();
    List<String> usernames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Firebase.setAndroidContext(this);
        final Firebase rootRef = new Firebase("https://blazing-inferno-7973.firebaseio.com");

        etb_email = (EditText) findViewById(R.id.etb_loginEmail);
        etb_username = (EditText) findViewById(R.id.etb_username);
        etb_password = (EditText) findViewById(R.id.etb_password);
        btn_create = (Button) findViewById(R.id.btn_createUser);

        Query queryRef = rootRef.child("users").orderByValue();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                usernames.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etb_email.getText().toString();
                username = etb_username.getText().toString();
                password = etb_password.getText().toString();

                if (!username.equals("")) {
                    rootRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {
                            rootRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    Firebase userRef = rootRef.child("UserID").child(authData.getUid());
                                    userRef.child(username).setValue("exists");

                                    rootRef.child("Users").child(username).setValue("");

                                    SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Utility.USERNAME_KEY, username);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "The specified username is invalid.", Toast.LENGTH_LONG).show();
            }
        });
    }
}