package jp.mobs.gogo.android.common;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.os.Build;

public class MyClipBoardManager {
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static String get(final Context c) {
        if (Build.VERSION.SDK_INT < 11) {
            final android.text.ClipboardManager cm = (android.text.ClipboardManager) c
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            return cm.getText().toString();
        } else {
            final android.content.ClipboardManager cm = (android.content.ClipboardManager) c
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            final ClipData cd = cm.getPrimaryClip();
            if (cd != null) {
                final ClipData.Item item = cd.getItemAt(0);
                return item.getText().toString();
            } else {
                return null;
            }
        }
    }
}
