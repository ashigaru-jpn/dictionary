package jp.mobs.gogo.android.dictionary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;

public enum DictSite {
    EIJIRO_M(
            "eijiro_m",
            "http://eow.alc.co.jp/sp/search.html?q={keyword}",
            R.string.site_eijiro_m, null),
    EIJIRO(
            "eijiro",
            "http://eow.alc.co.jp/search?q={keyword}",
            R.string.site_eijiro, null),
    WEBLIO(
            "weblio",
            "http://www.weblio.jp/content/{keyword}",
            R.string.site_weblio, null),
    WEBLIO_EJJE(
            "weblio_ejje",
            "http://ejje.weblio.jp/content/{keyword}",
            R.string.site_weblio_ejje, null),
    WEBLIO_THESAURUS(
            "weblio_thesaurus",
            "http://thesaurus.weblio.jp/content/{keyword}",
            R.string.site_weblio_thesaurus, null),
    GOO(
            "goo",
            "http://dictionary.goo.ne.jp/srch/all/{keyword}/m0u/",
            R.string.site_goo, null),
    YAHOO(
            "yahoo",
            "http://dic.search.yahoo.co.jp/search?ei=UTF-8&p={keyword}",
            R.string.site_yahoo, null),
    DICTIONARYCOM(
            "dictionarycom",
            "http://dictionary.reference.com/browse/{keyword}",
            R.string.site_dictionarycom, null),
    THEFREEDICTIONARY(
            "the_free_dictionary",
            "http://www.thefreedictionary.com/s/{keyword}",
            R.string.site_free_dict, null),
    URBAN(
            "urban",
            "http://m.urbandictionary.com/#define?term={keyword}",
            R.string.site_urban, null),
    WIKI_M_JP(
            "wiki_m_jp",
            "http://ja.m.wikipedia.org/wiki/{keyword}",
            R.string.site_wiki_m_jp, null),
    WIKI_M_EN(
            "wiki_m_en",
            "http://en.m.wikipedia.org/wiki/{keyword}",
            R.string.site_wiki_m_en, null);

    private final String id;
    private final String url;
    private final int nameInt;
    private final String userAgent;
    private DictSite(String id, String url, int nameInt, String userAgent) {
        this.id = id;
        this.url = url;
        this.nameInt = nameInt;
        this.userAgent = userAgent;
    }
    public String getId() {
        return id;
    }
    public String getUrl(String keyword) {
        String retVal = url.replaceAll("\\{keyword\\}", keyword);
        return retVal;
    }
    public int getNameInt() {
        return nameInt;
    }
    public static DictSite getById(String targetId) {
        DictSite retVal = null;
        for (DictSite site : values()) {
            String id = site.getId();
            if (id.equals(targetId)) {
                retVal = site;
                break;
            }
        }
        return retVal;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public int getPosition() {
        int i = 0;
        for (DictSite site : values()) {
            if (this == site) {
                return i;
            } else {
                i++;
            }
        }
        return 0;
    }

    public static List<String> getNameList(Activity a) {
        List<String> names = new ArrayList<String>();
        for (DictSite site : values()) {
            names.add(a.getResources().getString(site.getNameInt()));
        }
        return names;
    }
    public static DictSite getByPosition(int position) {
        return values()[position];
    }
    public static DictSite getByName(Context c, String selectedDictStr) {
        if (selectedDictStr == null) {
            return null;
        }
        for (DictSite site : values()) {
            String tempName = c.getResources().getString(site.getNameInt());
            if (tempName.equals(selectedDictStr)) {
                return site;
            }
        }
        return null;
    }
}
