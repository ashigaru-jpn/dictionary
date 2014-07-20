package jp.mobs.gogo.android.common;

import java.lang.reflect.Method;

import jp.mobs.gogo.android.dictionary.R;
import us.costan.chrome.ChromeView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChromeViewFinder {
    private LinearLayout container;
    private EditText findEdit;
    private ChromeView webView;

    public ChromeViewFinder(final Activity a, final int lFindTextBase,
            final ChromeView webView) {
        this.webView = webView;

        container = (LinearLayout) a.findViewById(lFindTextBase);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            container.setVisibility(View.INVISIBLE);
        } else {
            container.setVisibility(View.INVISIBLE);

            findEdit = (EditText) a.findViewById(R.id.l_find_word);
            findEdit.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(final View v, final int keyCode,
                        final KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN)
                            && ((keyCode == KeyEvent.KEYCODE_ENTER))) {
                        find();
                        Utility.closeKeyBoard(a, v);
                    }
                    return false;
                }

                private void find() {
                    webView.findAllAsync(findEdit.getText().toString());
                    try {
                        final Method m = ChromeView.class.getMethod(
                                "setFindIsUp", Boolean.TYPE);
                        m.invoke(webView, true);
                    } catch (final Exception e) {
                    }
                }
            });

            final ImageButton findButton = (ImageButton) a
                    .findViewById(R.id.l_find_exe);
            findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Utility.closeKeyBoard(a, v);
                    findAndNext();
                }

                private void findAndNext() {
                    webView.findAllAsync(findEdit.getText().toString());
                    try {
                        final Method m = ChromeView.class.getMethod(
                                "setFindIsUp", Boolean.TYPE);
                        m.invoke(webView, true);
                    } catch (final Exception e) {
                    }
                    webView.findNext(true);
                }
            });
            final ImageButton closeFindButton = (ImageButton) a
                    .findViewById(R.id.l_find_close);
            closeFindButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    findEdit.setText("");
                    webView.clearMatches();
                    container.setVisibility(View.INVISIBLE);
                    Utility.closeKeyBoard(a, v);
                }
            });
        }
    }

    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.showFindDialog(null, true);
        } else {
            container.setVisibility(View.VISIBLE);
        }
    }
}
