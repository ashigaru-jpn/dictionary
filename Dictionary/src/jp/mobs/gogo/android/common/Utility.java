package jp.mobs.gogo.android.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utility {
    public static void closeKeyBoard(final Activity a, final View v) {
        final InputMethodManager inputMethodManager = (InputMethodManager) a
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
