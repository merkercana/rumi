package com.rumi.billtracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends android.support.v4.app.Fragment {

    public Button btn_addMember;
    public EditText etb_addMemberName;

    public MemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_member, container, false);

        final Firebase rootRef = new Firebase("https://blazing-inferno-7973.firebaseio.com");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Utility.GROUP_NAME, Context.MODE_PRIVATE);
        final String groupName = sharedPreferences.getString(Utility.GROUP_NAME, null);

        btn_addMember = (Button)v.findViewById(R.id.btn_addMember);
        etb_addMemberName = (EditText)v.findViewById(R.id.etb_addMemberName);

        btn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = Utility.addMember(rootRef, groupName, etb_addMemberName.getText().toString());
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


}
