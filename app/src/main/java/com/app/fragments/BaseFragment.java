package com.app.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.EditText;

import com.app.activity.MainActivity;
import com.app.core.BusEvent;
import com.app.core.LogEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;

public class BaseFragment extends Fragment {

    public MainActivity app = null;
    public Context mainActivityThis = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = ((MainActivity) getActivity());
        mainActivityThis = app.thisContext;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            onFragAttached();
        } else {
            // fragment is no longer visible
            onFragDettached();
        }
    }

    protected void onFragAttached() {

    }

    public String getBase64(String imageFile) {
        String encodedImage = "";

        try {
            Bitmap bm = BitmapFactory.decodeFile(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            LogEx.print(e);
        }

        return encodedImage;
    }

    public String getBase64(Bitmap bm) {
        String encodedImage = "";

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            LogEx.print(e);
        }

        return encodedImage;
    }


    protected void onFragDettached() {
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void t(int msg) {
        app.toast(msg);
    }

    public void t(final String msg) {
        app.toast(msg);
    }

    public String s(int stringId) {
        return app.s(stringId);
    }

    public String et(EditText et) {
        return app.et(et);
    }

    public void log(String log) {
        app.log(log);
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

    @Subscribe
    public void onMessageEvent(BusEvent event) {
        log("Event Received On Base Fragment | Type : " + event.EventType);
        onBusEvent(event);
    }

    public void onBusEvent(BusEvent event) {
        // override to use in child fragments
    }

    public interface YesNoCallback {
        void onConfirmClick(boolean positive);
    }

    public void confirm(final YesNoCallback ynCallback) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clickedynCallbac
                        ynCallback.onConfirmClick(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        ynCallback.onConfirmClick(false);
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityThis);
        builder.setMessage("Are you sure ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}