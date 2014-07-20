//package jp.mobs.gogo.android.dictionary;
//
//import us.costan.chrome.ChromeView;
//
//public class DictChromeView {
//    private static String BLANK = "about:blank";
//    private final ChromeView webView;
//    private DictSite site;
//    private String keyword;
//
//    public DictChromeView(final ChromeView webView) {
//        this.webView = webView;
//    }
//
//    public void setSite(final DictSite site) {
//        this.site = site;
//    }
//
//    public void searchWord(final String word) {
//        this.keyword = word;
//        final String url = site.getUrl(word);
//        webView.loadUrl(url);
//    }
//
//    public String getWord() {
//        return keyword;
//    }
//
//    public boolean close() {
//        keyword = null;
//        webView.stopLoading();
//        if (BLANK.equals(webView.getUrl())) {
//            return false;
//        } else {
//            webView.loadUrl(BLANK);
//            return true;
//        }
//    }
//
//    public ChromeView getWebView() {
//        return webView;
//    }
//
//    public boolean isBlank() {
//        final String url = webView.getUrl();
//        if (url == null || url.equals(BLANK)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public DictSite getSite() {
//        return site;
//    }
// }
