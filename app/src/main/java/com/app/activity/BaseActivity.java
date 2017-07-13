package com.app.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import com.app.core.Globals;
import com.app.core.LogEx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import trikita.log.Log;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toast(int msg) {
        try {
            Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    public void toast(final String msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            LogEx.print(e);
        }
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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public String s(int stringId) {
        return getResources().getString(stringId);
    }

    public void tApp(Context ctx, final String msg) {
        try {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    public String et(EditText et) {
        return et.getText().toString().trim();
    }

    public void log(String log) {
        Log.d(Globals.tag, log);
    }

    public void log(String... strs) {

        for (int i = 0; i < strs.length; i++) {
            Log.d(Globals.tag, strs[i] + " ");
        }

    }

    // share bitmap to facebook and twitter
    int errorCameShareing = 0;
    String noFB = "Please install facebook app to share";
    String noTw = "Please install twitter app to share";

    private void shareAsync() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // shareBitmap(whichShare);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                if (errorCameShareing == 1) {
                    toast(noFB);
                    errorCameShareing = 0;
                } else if (errorCameShareing == 2) {
                    toast(noTw);
                    errorCameShareing = 0;
                }
            }
        }.execute();
    }

    private void shareBitmap(Bitmap bitmap, int whichShare) {
        try {

            // 1 = fb // 2 = twitter

            String shareText = "";

            errorCameShareing = 0;
            //save bitmap as file which is to be shared
            File myFile = new File(Environment.getExternalStorageDirectory(), "share.png");
            if (!myFile.exists()) {
                // myFile.mkdirs();
                myFile.createNewFile();
            }

            // Bitmap bitmap = ((BitmapDrawable) livTopical.getDrawable()).getBitmap();
            FileOutputStream fOut = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            myFile.setReadable(true, false);

            //share now
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);

            //all
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            if (whichShare == 0) {
                startActivity(Intent.createChooser(shareIntent, "Share using"));

            } else if (whichShare == 1) { //fb

                boolean hasFB = false;
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList) {
                    if ((app.activityInfo.name).trim().contains("facebook")) {

                        final ActivityInfo activity = app.activityInfo;
                        if ((activity.applicationInfo.packageName).trim().contains("katana") || (activity.applicationInfo.packageName).trim().contains("lite")) {
                            hasFB = true;
                            final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                            shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            shareIntent.setComponent(name);
                            startActivity(shareIntent);
                            break;
                        }
                    }
                }

                if (!hasFB) {
                    errorCameShareing = 1;
                }

            } else if (whichShare == 2) { //twitter
                final PackageManager pm = this.getApplicationContext().getPackageManager();
                final List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

                boolean hasTwitter = false;
                for (final ResolveInfo app : activityList) {
                    if (("com.twitter.android.composer.ComposerActivity".equals(app.activityInfo.name)) || ("com.twitter.android.PostActivity".equals(app.activityInfo.name))) {
                        hasTwitter = true;
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        shareIntent.setComponent(name);
                        break;
                    }
                }

                if (hasTwitter) {
                    startActivity(Intent.createChooser(shareIntent, "Share using"));
                } else {
                    errorCameShareing = 2;
                }
            }

        } catch (Exception e) {
            LogEx.print(e);
        }
    }

    // multiple option dialogs
    public interface MoptionCallback {
        void onItemClick(DialogInterface dialog, int which);
    }

    public boolean showMoptionDialog(String title, ArrayList<String> optionArray, final MoptionCallback mop) {

        try {
            String[] original = new String[optionArray.size()];
            String[] dest = optionArray.toArray(original);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setItems(dest, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                    mop.onItemClick(dialog, which);
                }
            });

            builder.show();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
