package com.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.app.core.BusEvent;
import com.app.core.CropperMan;
import com.app.core.Globals;
import com.app.core.Storage;
import com.app.fragments.BlankFragment;
import com.app.fragments.Fragments;
import com.app.rum.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        ButterKnife.bind(this);

        thisContext = MainActivity.this;

        Storage.setContext(thisContext);
        Globals.app = Storage.get();
        findAllViews();

        setCropper();
        setupDrawer();
        showFragment(Fragments.home);
    }

    private void setCropper() {
        cMan.setCropper(this, new CropperMan.CropperManCallBack() {
            @Override
            public void onImageSelected(String imagePath) {
                postEvent(new BusEvent(BusEvent.ET_CROPPER));
            }
        });
    }

    public void showDatePicker() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void showDatePicker(int d, int m, int y) {

        Calendar now = Calendar.getInstance();

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this, y, m, d
        );

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        final String DMY = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        postEvent(new BusEvent(BusEvent.ET_DATE_PICK, DMY));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cMan.onActivityResult(requestCode, resultCode, data);
    }

    public void showFragment(int fragmentId) {

        Fragment fragToShow = null;
        switch (fragmentId) {

            case Fragments.home:
                fragToShow = BlankFragment.newInstance();
                break;

            case Fragments.blank:
                fragToShow = BlankFragment.newInstance();
                break;

            default:
                fragToShow = BlankFragment.newInstance();
                break;
        }


        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment

        ft.replace(R.id.fragmentHolder, fragToShow);
        // or ft.add(R.id.your_placeholder, new FooFragment());

        // Complete the changes added above
        ft.commit();
    }

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.dlDrawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name,
                R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nvDrawerNav);
        View drawerView = navigationView.inflateHeaderView(R.layout.drawer_layout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BusEvent event) {
        log("Event Received On MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // broadcast main activity onResume
        EventBus.getDefault().post(new BusEvent(BusEvent.ET_ON_RESUME));
    }

    @Override
    public void onBackPressed() {
        if (Globals.exitApp) {
            super.onBackPressed();
        } else {
            // broadcast back pressed
            postEvent(new BusEvent(BusEvent.ET_BACK_PRESS));
        }
    }

    private void findAllViews() {
        fragmentHolder = (FrameLayout) findViewById(R.id.fragmentHolder);
    }

    public void postEvent(final BusEvent event) {
        new Handler().post(new Runnable() {
            public void run() {
                EventBus.getDefault().post(event);
            }
        });
    }

    // var and objects
    public CropperMan cMan = new CropperMan();
    public Context thisContext;
    RecyclerView.Adapter mDrawerListAdapter;

    // views
    FrameLayout fragmentHolder;

}
