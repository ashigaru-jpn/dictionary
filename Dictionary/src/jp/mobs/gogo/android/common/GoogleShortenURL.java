package jp.mobs.gogo.android.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import jp.mobs.gogo.android.dictionary.R;

import org.apache.commons.lang3.ArrayUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;

import com.google.gson.Gson;

public class GoogleShortenURL extends AsyncTask<String, Void, String> {
    private static final String URL_GOOGL_SERVICE = "https://www.googleapis.com/urlshortener/v1/url";

    private static final Gson gson = new Gson();

    private final Activity mainActivity;

    public GoogleShortenURL(final Activity activity) {
        this.mainActivity = activity;
    }

    private String shorten(final String longUrl) {
        String result = new String();
        final GsonGooGl gsonGooGl = new GsonGooGl(longUrl);
        Scanner sc = null;

        try {
            final URL url = new URL(URL_GOOGL_SERVICE);

            final URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true); // Let the run-time system (RTS) know that
                                      // we want input.
            urlConn.setDoOutput(true); // Let the RTS know that we want to do
                                       // output.
            urlConn.setUseCaches(false); // No caching, we want the real thing.
            urlConn.setRequestProperty("Content-Type", "application/json"); // Specify
                                                                            // the
                                                                            // content
                                                                            // type.

            final DataOutputStream printout = new DataOutputStream(
                    urlConn.getOutputStream()); // Send POST output.
            final String content = gson.toJson(gsonGooGl);
            printout.writeBytes(content);
            printout.flush();
            printout.close();

            final DataInputStream input = new DataInputStream(
                    urlConn.getInputStream()); // Get response data.

            sc = new Scanner(input);
            while (sc.hasNext()) {
                result += sc.next();
            }

            final GooGlResult gooGlResult = gson.fromJson(result,
                    GooGlResult.class);

            return gooGlResult.getId();
        } catch (final Exception ex) {
            System.out.println(ex);
            return null;
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }

    @Override
    protected String doInBackground(final String... params) {
        if (ArrayUtils.isNotEmpty(params)) {
            return shorten(params[0]);
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(final String result) {
        final EditText shortenUrlEdit = (EditText) mainActivity
                .findViewById(R.id.l_shorten_url_value);
        shortenUrlEdit.setText(result);
    }
}

class GsonGooGl {
    public GsonGooGl() {
    }

    public GsonGooGl(final String longUrl) {
        this.longUrl = longUrl;
    }

    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(final String longUrl) {
        this.longUrl = longUrl;
    }
}

class GooGlResult {
    public GooGlResult() {
    }

    private String kind;
    private String id;
    private String longUrl;

    public String getKind() {
        return kind;
    }

    public void setKind(final String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(final String longUrl) {
        this.longUrl = longUrl;
    }
}
