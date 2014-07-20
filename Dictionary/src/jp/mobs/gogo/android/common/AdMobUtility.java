package jp.mobs.gogo.android.common;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdMobUtility {
    private static final String MY_AD_UNIT_ID = "ca-app-pub-1933926286241349/3379166111";

    public static View getView(final Activity activity) {
        final AdView view = new AdView(activity);
        view.setAdUnitId(MY_AD_UNIT_ID);
        view.setAdSize(AdSize.BANNER);
        final AdRequest req = new AdRequest.Builder().build();
        view.loadAd(req);
        return view;
    }
}
