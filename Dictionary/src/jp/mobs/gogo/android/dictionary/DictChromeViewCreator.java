//package jp.mobs.gogo.android.dictionary;
//
//import us.costan.chrome.ChromeSettings;
//import us.costan.chrome.ChromeView;
//import us.costan.chrome.ChromeViewClient;
//import us.costan.chrome.ChromeWebClient;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Build;
//import android.view.View;
//import android.widget.ProgressBar;
//
//public class DictChromeViewCreator {
//    MediaPlayer mp = new MediaPlayer();
//
//    public DictChromeViewCreator() {
//    }
//
//    public DictChromeView create(final Activity activity, final int layout) {
//        final ChromeView webView = (ChromeView) activity
//                .findViewById(R.id.l_web);
//
//        final ProgressBar bar = new ProgressBar(activity);
//        final ProgressDialog loading = new ProgressDialog(activity) {
//            @Override
//            public void onBackPressed() {
//                webView.stopLoading();
//                webView.goBack();
//                cancel();
//            };
//        };
//        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        loading.setMessage("Now Loading...");
//        final ChromeViewClient client = new ChromeViewClient() {
//            @Override
//            public void onPageFinished(final ChromeView view, final String url) {
//                if (loading.isShowing()) {
//                    loading.dismiss();
//                }
//                bar.setVisibility(View.GONE);
//            };
//
//            @Override
//            public void onPageStarted(final ChromeView view, final String url,
//                    final android.graphics.Bitmap favicon) {
//                loading.show();
//                bar.setVisibility(View.VISIBLE);
//            };
//
//            @Override
//            public boolean shouldOverrideUrlLoading(final ChromeView view,
//                    final String url) {
//                if (url.endsWith(".mp3")) {
//                    final Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse(url), "audio/*");
//                    view.getContext().startActivity(intent);
//                    return true;
//                } else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
//                    final Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse(url), "video/*");
//                    view.getContext().startActivity(intent);
//                    return true;
//                } else {
//                    return super.shouldOverrideUrlLoading(view, url);
//                }
//            }
//        };
//
//        final ChromeWebClient chrome = new ChromeWebClient() {
//            @Override
//            public void onProgressChanged(final ChromeView view,
//                    final int progress) {
//                loading.setProgress(progress);
//                bar.setProgress(progress);
//            }
//        };
//
//        // PictureListener picture = new PictureListener(){
//        // @Override
//        // @Deprecated
//        // public void onNewPicture(WebView view, Picture picture) {
//        // if (loading.isShowing()) {
//        // loading.dismiss();
//        // }
//        // }
//        // };
//
//        // webView.setWebViewClient(new WebViewClient());
//        webView.setChromeViewClient(client);
//        webView.setChromeWebClient(chrome);
//        // webView.setPictureListener(picture);
//
//        final ChromeSettings webSettings = webView.getSettings();
//
//        // Enable Javascript
//        webSettings.setJavaScriptEnabled(true);
//
//        // Use WideViewport and Zoom out if there is no viewport defined
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
//
//        // Enable pinch to zoom without the zoom buttons
//        webSettings.setBuiltInZoomControls(true);
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//            // Hide the zoom controls for HONEYCOMB+
//            webSettings.setDisplayZoomControls(false);
//        }
//
//        // Enable remote debugging via chrome://inspect
//        // if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        // WebView.setWebContentsDebuggingEnabled(true);
//        // }
//
//        return new DictChromeView(webView);
//    }
// }
