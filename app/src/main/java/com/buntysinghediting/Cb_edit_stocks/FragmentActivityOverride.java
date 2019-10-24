package com.buntysinghediting.Cb_edit_stocks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class FragmentActivityOverride extends AppCompatActivity {

    protected DrawerLayout mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_navigation);

        mDrawer =  findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //This is about creating custom listview for navigate drawer
        //Implementation for NavigateDrawer HERE !
//        ArrayList<DrawerListItem> drawerListItems = new ArrayList<DrawerListItem>();
//        drawerListItems.add(new DrawerListItem(0,"AIR° DEVICES"));
//        drawerListItems.add(new DrawerListItem(1,"A/C Device [1]"));
//        drawerListItems.add(new DrawerListItem(1,"A/C Device [2]"));
//        drawerListItems.add(new DrawerListItem(1,"A/C Device [3]"));
//        drawerListItems.add(new DrawerListItem(0,"AIR° FEATURES"));
//        drawerListItems.add(new DrawerListItem(2,"SLEEP MODE"));
//        drawerListItems.add(new DrawerListItem(2,"TRACKING MODE"));
//        drawerListItems.add(new DrawerListItem(2,"SETTINGS"));
//        DrawerAdapter mDrawerAdapter = new DrawerAdapter(this, R.layout.drawer_list_header, drawerListItems);
//        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        mDrawerList.setAdapter(mDrawerAdapter);


    }
}
