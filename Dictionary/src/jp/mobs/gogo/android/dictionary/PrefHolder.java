package jp.mobs.gogo.android.dictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefHolder {
    private String LAST_SELECTED_DICT = "setting_last_selected_dict";
    private String START_SELECTED_DICT = "setting_start_selected_dict";
    private String HISTORY_MAX_NUM = "setting_history_max_num";
    private String CBTN_EXIT = "setting_cbtn_exit";
    private String CBTN_CLEAR_HISTORY = "setting_cbtn_clear_search_history";
    private String CBTN_CLEAR_BROWS = "setting_cbtn_clear_brows_history";
    private String CBTN_CLEAR_CASH = "setting_cbtn_clear_brows_cash";
    private final Context c;
    private final SharedPreferences pref;

    public PrefHolder(final Context c) {
        this.c = c;
        this.pref = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public DictSite getSelectedDict() {
        final String selectedDictStr = pref
                .getString(START_SELECTED_DICT, null);
        final DictSite site = DictSite.getByName(c, selectedDictStr);
        if (site == null) {
            return DictSite.EIJIRO_M;
        } else {
            return site;
        }
    }

    public void setDictSite(final DictSite site) {
        final boolean lastSelectedDictSetting = pref.getBoolean(
                LAST_SELECTED_DICT, false);
        if (lastSelectedDictSetting) {
            final String name = c.getResources().getString(site.getNameInt());
            final Editor editor = pref.edit();
            editor.putString(START_SELECTED_DICT, name);
            editor.commit();
        }
    }

    public int getMaxHistory() {
        final String value = pref.getString(HISTORY_MAX_NUM, null);
        if (value == null) {
            return 30;
        } else {
            final int historyMax = Integer.valueOf(value).intValue();
            return historyMax;
        }
    }

    public boolean clearHistoryByCloseBtn() {
        return pref.getBoolean(CBTN_CLEAR_HISTORY, false);
    }

    public boolean clearBrowsHistoryByCloseBtn() {
        return pref.getBoolean(CBTN_CLEAR_BROWS, false);
    }

    public boolean clearBrowsCashByCloseBtn() {
        return pref.getBoolean(CBTN_CLEAR_CASH, false);
    }

    public boolean exitByCloseBtn() {
        return pref.getBoolean(CBTN_EXIT, false);
    }
}
