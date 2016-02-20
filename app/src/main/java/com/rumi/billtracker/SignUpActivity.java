package com.rumi.billtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    EditText etb_username;
    EditText etb_email;
    EditText etb_password;
    Button btn_create;
    String email, username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://blazing-inferno-7973.firebaseio.com");
        final Firebase idRef = ref.child("userID");
        ref.child("userID").child("man").child("displayName").setValue("Michael");
        etb_email = (EditText)findViewById(R.id.etb_email);
        etb_username = (EditText)findViewById(R.id.etb_username);
        etb_password = (EditText)findViewById(R.id.etb_password);
        btn_create = (Button)findViewById(R.id.btn_createUser);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etb_email.getText().toString();
                username = etb_username.getText().toString();
                password = etb_password.getText().toString();

                if (email!=""&&password!="")
                {
                    ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {
                            ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    //Map<String,String> map = new HashMap<String, String>();
                                    //map.put("displayName",username);
                                    Firebase userRef = ref.child("UserID").child(authData.getUid());
                                    //userRef.setValue(map);
                                    userRef.child("displayName").setValue(username);

                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {

                                }
                            });
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
    }
}
