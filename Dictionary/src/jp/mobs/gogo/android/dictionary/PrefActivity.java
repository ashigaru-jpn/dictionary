package jp.mobs.gogo.android.dictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PrefActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        setResult(RESULT_OK, null);

        Preference historyClearButton = (Preference) findPreference("setting_history_clear");
        historyClearButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {

                new AlertDialog.Builder(PrefActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(PrefActivity.this.getResources().getString(R.string.is_it_ok))
                .setPositiveButton(PrefActivity.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        new HistoryDbAdapter(PrefActivity.this).clearAllHistory();
                        Toast.makeText(
                                PrefActivity.this,
                                getResources().getString(R.string.cleared_history),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton(PrefActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

                //code for what you want it to do
                return true;
            }
        });

    }
}
