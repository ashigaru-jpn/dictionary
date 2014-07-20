package jp.mobs.gogo.android.dictionary;

import android.webkit.WebView;

public class DictWebView {
    private static String BLANK = "about:blank";
    private final WebView webView;
    private DictSite site;
    private String keyword;

    public DictWebView(final WebView webView) {
        this.webView = webView;
    }

    public void setSite(final DictSite site) {
        this.site = site;
    }

    public void searchWord(final String word) {
        this.keyword = word;
        final String url = site.getUrl(word);
        webView.loadUrl(url);
    }

    public String getWord() {
        return keyword;
    }

    public boolean close() {
        keyword = null;
        webView.stopLoading();
        if (BLANK.equals(webView.getUrl())) {
            return false;
        } else {
            webView.loadUrl(BLANK);
            return true;
        }
    }

    public WebView getWebView() {
        return webView;
    }

    public boolean isBlank() {
        final String url = webView.getUrl();
        if (url == null || url.equals(BLANK)) {
            return true;
        } else {
            return false;
        }
    }

    public DictSite getSite() {
        return site;
    }
}
