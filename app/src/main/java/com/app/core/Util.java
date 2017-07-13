package com.app.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void hideKeyboard(Activity ac) {
        // Check if no view has focus:
        View view = ac.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) ac.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String ucwords(String input) {
        try {

            if (input == null || input.length() <= 0) {
                return input;
            }
            char[] chars = new char[1];
            input.getChars(0, 1, chars, 0);
            if (Character.isUpperCase(chars[0])) {
                return input;
            } else {
                StringBuilder buffer = new StringBuilder(input.length());
                buffer.append(Character.toUpperCase(chars[0]));
                buffer.append(input.toCharArray(), 1, input.length() - 1);
                return buffer.toString();
            }
        } catch (Exception e) {
            return input;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getDeviceId(Context ctx) {
        String android_id = Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return android_id;
    }

    public static String months[] = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};

}
