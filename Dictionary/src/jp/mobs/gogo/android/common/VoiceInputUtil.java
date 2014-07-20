package jp.mobs.gogo.android.common;

import java.util.ArrayList;
import java.util.Locale;

import jp.mobs.gogo.android.dictionary.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class VoiceInputUtil {
    public static final int VOICE_REQUEST_CODE = 10;

    public VoiceInputUtil() {
    }

    public ImageButton createImageButton(final Activity activity,
            final int layout) {

        final ImageButton voiceButton = (ImageButton) activity
                .findViewById(layout);
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    final String[] str_items = {
                            activity.getResources()
                                    .getString(R.string.japanese),
                            activity.getResources().getString(R.string.english) };
                    new AlertDialog.Builder(activity)
                            .setIcon(
                                    R.drawable.ic_voice_search_api_holo_light43)
                            .setTitle(
                                    activity.getResources().getString(
                                            R.string.please_select))
                            .setItems(str_items,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                final DialogInterface dialog,
                                                final int which) {
                                            String selectLocaleStr = Locale.JAPAN
                                                    .toString();
                                            final String lang = str_items[which];
                                            if (lang != null) {
                                                if (lang.equals(activity
                                                        .getResources()
                                                        .getString(
                                                                R.string.japanese))) {
                                                    selectLocaleStr = Locale.JAPAN
                                                            .toString();
                                                } else if (lang
                                                        .equals(activity
                                                                .getResources()
                                                                .getString(
                                                                        R.string.english))) {
                                                    selectLocaleStr = Locale.ENGLISH
                                                            .toString();
                                                }
                                            }
                                            doVoiceRequest(activity,
                                                    selectLocaleStr);
                                        }
                                    }).show();

                } catch (final ActivityNotFoundException e) {
                    Toast.makeText(
                            activity,
                            activity.getResources().getString(
                                    R.string.not_found_voice_input),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return null;
    }

    private void doVoiceRequest(final Activity activity,
            final String selectLocaleStr) {
        final Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectLocaleStr);

        try {
            activity.startActivityForResult(intent, VOICE_REQUEST_CODE);
        } catch (final ActivityNotFoundException e) {
            new AlertDialog.Builder(activity)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(
                            activity.getResources().getString(
                                    R.string.jump_market_voice_input_app))
                    .setPositiveButton(
                            activity.getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        final DialogInterface dialog,
                                        final int whichButton) {
                                    String url = null;
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                                        url = "https://play.google.com/store/apps/details?id=com.google.android.voicesearch.x";
                                    } else {
                                        url = "https://play.google.com/store/apps/details?id=com.google.android.voicesearch";
                                    }
                                    final Intent browserIntent = new Intent(
                                            Intent.ACTION_VIEW, Uri.parse(url));
                                    activity.startActivity(browserIntent);
                                }
                            })
                    .setNegativeButton(
                            activity.getResources().getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        final DialogInterface dialog,
                                        final int whichButton) {
                                }
                            }).show();
        }
    }

    public ArrayList<String> getResult(final Intent data) {
        final ArrayList<String> results = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        return results;
    }
}
