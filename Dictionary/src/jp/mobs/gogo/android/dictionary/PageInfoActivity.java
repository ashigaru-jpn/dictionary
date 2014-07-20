package jp.mobs.gogo.android.dictionary;

import jp.mobs.gogo.android.common.GoogleShortenURL;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class PageInfoActivity extends Activity {
    public static String TITLE = "jp.mobs.gogo.android.dictionary.pagetitle";
    public static String URL = "jp.mobs.gogo.android.dictionary.pageurl";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_info);

        final String titleStr = (String) getIntent().getExtras().get(TITLE);
        final String urlStr = (String) getIntent().getExtras().get(URL);

        final EditText titleEdit = (EditText) findViewById(R.id.l_page_title_value);
        titleEdit.setText(titleStr);
        final EditText urlEdit = (EditText) findViewById(R.id.l_url_value);
        urlEdit.setText(urlStr);

        final GoogleShortenURL shortenUrl = new GoogleShortenURL(this);
        shortenUrl.execute(urlStr);
    }
}
