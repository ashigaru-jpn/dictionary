package jp.mobs.gogo.android.dictionary.model;

import java.util.Date;

public class HistoryModel {
    private final Date date;
    private final String word;

    public HistoryModel(final Date date, final String word) {
        this.date = date;
        this.word = word;
    }

    public Date getDate() {
        return date;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
