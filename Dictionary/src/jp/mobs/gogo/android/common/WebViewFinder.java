package jp.mobs.gogo.android.common;

import java.lang.reflect.Method;

import jp.mobs.gogo.android.dictionary.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WebViewFinder {
    private LinearLayout container;
    private EditText findEdit;
    private WebView webView;

    public WebViewFinder(final Activity a, final int lFindTextBase,
            final WebView webView) {
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

                @SuppressWarnings("deprecation")
                private void find() {
                    webView.findAll(findEdit.getText().toString());
                    try {
                        final Method m = WebView.class.getMethod("setFindIsUp",
                                Boolean.TYPE);
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

                @SuppressWarnings("deprecation")
                private void findAndNext() {
                    webView.findAll(findEdit.getText().toString());
                    try {
                        final Method m = WebView.class.getMethod("setFindIsUp",
                                Boolean.TYPE);
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
