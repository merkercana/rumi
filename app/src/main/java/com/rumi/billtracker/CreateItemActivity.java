package com.rumi.billtracker;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import android.widget.ExpandableListAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateItemActivity extends AppCompatActivity {

//    private Context context;
//    private HashMap<String, List<String>> detailsHashMap;
//    private List<String> detailsList;
//
//    public void adapter(Context context,
//                   HashMap<String, List<String>> hashMap,
//                   List<String> list){
//        detailsHashMap = hashMap;
//        this.context = context;
//        this.detailsHashMap = detailsHashMap;
//        this.detailsList = list;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded,
//                             View convertView, ViewGroup parent){
//        String groupTitle  = (String) getGroup(groupPosition);
//
//    }
    public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> parentDataSource;
        private HashMap<String, List<String>> childDataSource;
        public ExpandableListViewAdapter(Context context, List<String> childParent, HashMap<String, List<String>> child) {
            this.context = context;
            this.parentDataSource = childParent;
            this.childDataSource = child;
        }
        @Override
        public int getGroupCount() {
            return this.parentDataSource.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return this.childDataSource.get(this.parentDataSource.get(groupPosition)).size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return parentDataSource.get(groupPosition);
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.childDataSource.get(parentDataSource.get(groupPosition)).get(childPosition);
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return false;
        }
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.parent_layout, parent, false);
            }
            String parentHeader = (String)getGroup(groupPosition);
            TextView parentItem = (TextView)view.findViewById(R.id.parent_layout);
            parentItem.setText(parentHeader);
            return view;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.child_layout, parent, false);
            }
            String childName = (String)getChild(groupPosition, childPosition);
            TextView childItem = (TextView)view.findViewById(R.id.child_layout);
            childItem.setText(childName);
            return view;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
    private ExpandableListView expandableListView;
    private List<String> parentHeaderInformation;

    private HashMap<String, List<String>> returnGroupChildItems(){
        HashMap<String, List<String>> childContent = new HashMap<String,List<String>>();
        List<String> includedMembers = new ArrayList<String>();
        includedMembers.add("michael");
        childContent.put(parentHeaderInformation.get(0), includedMembers);
        childContent.put(parentHeaderInformation.get(1), includedMembers);
        childContent.put(parentHeaderInformation.get(2), includedMembers);
        return childContent;
    }


    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

// Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

// Handle action bar item clicks here. The action bar will

// automatically handle clicks on the Home/Up button, so long

// as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

//noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        parentHeaderInformation = new ArrayList<String>();
        parentHeaderInformation.add("Amount");
        parentHeaderInformation.add("Included members");
        parentHeaderInformation.add("blah");
        HashMap<String, List<String>> allChildItems = returnGroupChildItems();
        expandableListView = (ExpandableListView)findViewById(R.id.elv_ItemDetails);
        ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getApplicationContext(), parentHeaderInformation, allChildItems);
        expandableListView.setAdapter(expandableListViewAdapter);

        Firebase.setAndroidContext(this);
        final Firebase rootRef = new Firebase("https://blazing-inferno-7973.firebaseio.com");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
