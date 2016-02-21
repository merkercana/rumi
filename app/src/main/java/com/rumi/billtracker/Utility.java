package com.rumi.billtracker;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utility {
    public static final String SHARED_PREF_NAME = "SharedPreferences";
    public static final String USERNAME_KEY = "username";

    static Map<String, Object> user;

    public static void createGroup(Firebase rootRef, String groupName, String groupCreator) {
        rootRef.child("Groups").child(groupName).child("Bills").setValue("");
        rootRef.child("Groups").child(groupName).child("Members").child(groupCreator).setValue(0);
        rootRef.child("Groups").child(groupName).child("isNull").setValue(true);

        rootRef.child("Users").child(groupCreator).child("Groups").child("MemberRelations").setValue("");
        rootRef.child("Users").child(groupCreator).child("Groups").child("Net").setValue(0);
    }

    public static void addMember(final Firebase rootRef, final String groupName, final String member) {
        Query queryRef = rootRef.child("Groups").child(groupName).child("Members").orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!member.equals(dataSnapshot.getKey())) {
                    rootRef.child("Groups").child(groupName).child("Members").child(member).setValue(0);

                    user = new HashMap<String, Object>();
                    user.put(member, 0);
                    rootRef.child("Users").child(dataSnapshot.getKey()).child(groupName).child("MemberRelations").updateChildren(user);
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
/*
    for (int i = 0; i <groupSize; i++){
        final Map<String, Object> user = new HashMap<String,Object>();
        user.put(members.get(i),0);
        groupRef.updateChildren(user);

        rootRef.child("users").child(members.get(i)).child(groupName).child("MemberRelations").setValue(0);*/
    }

    public static boolean addBill(Firebase rootRef, String groupName, String billName, int sum, String billCreator, Map<String, Object> members){
        if (sum == 0)
            return false;

        final Firebase billRef = rootRef.child("Groups").child(groupName).child("Bills").child(billName);
        billRef.child("Members").updateChildren(members);
        billRef.child("isResolved").setValue(false);



        return true;
    }
}