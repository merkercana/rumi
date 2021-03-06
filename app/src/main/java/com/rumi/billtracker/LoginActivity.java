package com.rumi.billtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText etb_loginEmail;
    EditText etb_loginPassword;
    Button btn_login;
    TextView text_signUp;
    String email;
    String password;
    Button button2;
    List<String> usernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        final Firebase rootRef = new Firebase("https://blazing-inferno-7973.firebaseio.com");

        etb_loginEmail = (EditText)findViewById(R.id.etb_loginEmail);
        etb_loginPassword = (EditText)findViewById(R.id.etb_loginPassword);
        btn_login = (Button)findViewById(R.id.btn_login);
        text_signUp = (TextView)findViewById(R.id.text_signUp);
        button2 = (Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = rootRef.child("Users").orderByKey();
                query.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(rootRef.child("Users").child("a").getKey()))
                            button2.setText("yes");
                        else
                            button2.setText("no");
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        button2.setText("cancelled");
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        final Firebase ref = rootRef.child("users");
        Query queryRef = ref.orderByValue();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                usernames.add(dataSnapshot.getKey());
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
                email = etb_loginEmail.getText().toString();
                password = etb_loginPassword.getText().toString();
                //Firebase checkRef = rootRef.child("users").child(username);
                //Query query = checkRef.equalTo(true);

                rootRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(final AuthData authData) {
                        Query queryRef = rootRef.child("UserID").child(authData.getUid()).orderByKey();
                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                                SharedPreferences sharedPreferences = getSharedPreferences(Utility.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Utility.USERNAME_KEY, dataSnapshot.getKey());
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(intent);
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
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        text_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
