package com.rumi.billtracker;


import android.content.Context;
import android.widget.Toast;

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
import java.util.Objects;

public class Utility {
    public static final String SHARED_PREF_NAME = "SharedPreferences";
    public static final String USERNAME_KEY = "username";
    public static final String GROUP_NAME = "groupName";

    static Map<String, Object> user;
    static String message;
    static boolean error;
    static boolean validated;

    public static void createGroup(Firebase rootRef, String groupName, String groupCreator) {
        rootRef.child("Groups").child(groupName).child("Bills").setValue("");
        rootRef.child("Groups").child(groupName).child("Members").child(groupCreator).setValue(0);

        rootRef.child("Users").child(groupCreator).child(groupName).child("MemberRelations").setValue("");
    }

    public static void addMember(final Firebase rootRef, final String groupName, final String member/*, final Context context*/) {
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
                                if (!dataSnapshot2./*hasChild(rootRef.child("Users").child(member).child(groupName).*/getKey().equals(groupName)) {
                                    validated = true;
                                    user = new HashMap<String, Object>();
                                    user.put(member, 0);
                                    rootRef.child("Users").child(member).child(groupName).setValue("");
                                    rootRef.child("Groups").child(groupName).child("Members").updateChildren(user);

                                    Query queryRef = rootRef.child("Groups").child(groupName).child("Members");
                                    queryRef.addChildEventListener(new ChildEventListener() {

                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot3, String s) {
                                            if (!dataSnapshot3.getKey().equals(member)) {
                                                user = new HashMap<String, Object>();
                                                user.put(dataSnapshot3.getKey().toString(), 0);
                                                rootRef.child("Users").child(member).child(groupName).child("MemberRelations").updateChildren(user);

                                                Map<String, Object> newUser = new HashMap<String, Object>();
                                                newUser.put(member, 0);
                                                rootRef.child("Users").child(dataSnapshot3.getKey()).child(groupName).child("MemberRelations").updateChildren(newUser);
                                                error = false;
                                            }

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
                                } else {
                                    //message = "The specified user is already part of the group.";
                                    //error = true;
                                    //Toast.makeText(context,"The specified user is already part of the group.",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    } else {
                        //message = "The specified user does not exist.";
                        //error = true;

                        //Toast.makeText(context, "The specified user does not exist.", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        if (!error) {
            //message = "The specified user has been added to the group.";
            //return message;

            //Toast.makeText(context, "The specified user has been added to the group", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean addBill(Firebase rootRef, String groupName, String billName, int sum, String billCreator, Map<String, Object> members){
        if (sum == 0)
            return false;

        final Firebase billRef = rootRef.child("Groups").child(groupName).child("Bills").child(billName);
        billRef.child("Members").updateChildren(members);
        billRef.child("isResolved").setValue(false);



        return true;
    }

    public static void editBill(){



    }
}