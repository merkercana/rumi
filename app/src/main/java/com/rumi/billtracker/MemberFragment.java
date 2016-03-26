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
import android.widget.TextView;

import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends android.support.v4.app.Fragment {

    public Button btn_addMember;
    public EditText etb_addMemberName;

    static Map<String, Object> user;
    static Map<String, Object> addMember;
    static Map<String, Object> newUser;
    static String message;
    static boolean error;
    static boolean validated;


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

        btn_addMember = (Button) v.findViewById(R.id.btn_addMember);
        etb_addMemberName = (EditText) v.findViewById(R.id.etb_addMemberName);

        //region Add Member
        btn_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String member = etb_addMemberName.getText().toString();
                validated = false;
                error = false;

                Query validateRef = rootRef.child("Users");
                validateRef.orderByKey();
                validateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (!validated)
                            if (dataSnapshot.hasChild(rootRef.child("Users").child(member).getKey())) {
                                Query validateGroupRef = rootRef.child("Users").child(member);
                                validateGroupRef.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        if (!validated)
                                            if (!dataSnapshot2.hasChild(rootRef.child("Users").child(member).child(groupName).getKey()))/*.equals(groupName))*/ {
                                                Toast.makeText(getActivity(), member + " has been added to " + groupName, Toast.LENGTH_SHORT).show();

                                                validated = true;
                                                user = new HashMap<String, Object>();
                                                user.put(member, 0);
                                                rootRef.child("Groups").child(groupName).child("Members").updateChildren(user);

                                                Query queryRef = rootRef.child("Groups").child(groupName).child("Members");
                                                queryRef.addChildEventListener(new ChildEventListener() {

                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot3, String s) {
                                                        if (!dataSnapshot3.getKey().equals(member)) {
                                                            addMember = new HashMap<String, Object>();
                                                            addMember.put(dataSnapshot3.getKey().toString(), 0);
                                                            rootRef.child("Users").child(member).child(groupName).child("MemberRelations").updateChildren(addMember);

                                                            newUser = new HashMap<String, Object>();
                                                            newUser.put(member, 0);
                                                            rootRef.child("Users").child(dataSnapshot3.getKey()).child(groupName).child("MemberRelations").updateChildren(newUser);
                                                            error = false;
                                                        }

                                                    }

                                                    //region Override Functions
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
                                                    //endregion

                                                });
                                            }
                                            else {
                                                Toast.makeText(getActivity(), "The specified user is already part of the group.", Toast.LENGTH_SHORT).show();
                                                error = true;
                                            }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), "The specified user does not exist.", Toast.LENGTH_SHORT).show();
                                error = true;
                            }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
        //endregion

        // Inflate the layout for this fragment
        return v;
    }
}