package jp.mobs.gogo.android.dictionary;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.mobs.gogo.android.dictionary.model.HistoryModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryDbAdapter {
    public static final String TABLE_NAME = "history";

    public static enum COL {
        DATE("date", 0), WORD("word", 1);
        private final String colName;
        private final int colNum;

        private COL(final String colName, final int colNum) {
            this.colName = colName;
            this.colNum = colNum;
        }

        public String getColName() {
            return colName;
        }

        public int getColNum() {
            return colNum;
        }
    }

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public HistoryDbAdapter(final Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private HistoryDbAdapter openWritable() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    private HistoryDbAdapter openReadable() {
        db = dbHelper.getReadableDatabase();
        return this;
    }

    private void close() {
        dbHelper.close();
    }

    private boolean deleteByWord(final String word) {
        return db.delete(TABLE_NAME, COL.WORD.getColName() + "=?",
                new String[] { word }) > 0;
    }

    private boolean deleteAll() {
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    private Cursor getAll() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public List<HistoryModel> getAllHistory() {
        openReadable();
        final Cursor c = getAll();
        final List<HistoryModel> models = new ArrayList<HistoryModel>();
        boolean isEof = c.moveToFirst();
        while (isEof) {
            Date date = null;
            try {
                date = Constants.DATE_FORMAT.parse(c.getString(COL.DATE
                        .getColNum()));
            } catch (final ParseException e) {
            }
            final String word = c.getString(COL.WORD.getColNum());
            final HistoryModel model = new HistoryModel(date, word);
            models.add(model);
            isEof = c.moveToNext();
        }
        c.close();
        close();
        return models;
    }

    private void save(final Date date, final String word) {
        final ContentValues values = new ContentValues();
        values.put(COL.DATE.getColName(), Constants.DATE_FORMAT.format(date));
        values.put(COL.WORD.getColName(), word);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    public void addHistory(final String word) {
        openWritable();
        deleteByWord(word);
        save(new Date(), word);
        close();

        clearOutOfRange();
    }

    public void clearAllHistory() {
        openWritable();
        deleteAll();
        close();
    }

    public void clearOutOfRange() {
        final int maxHistory = new PrefHolder(context).getMaxHistory();
        openReadable();
        final int rowCount = getRowCount();
        close();
        final int overCount = rowCount - maxHistory;
        if (overCount > 0) {
            openWritable();
            db.execSQL(
                    "DELETE FROM " + TABLE_NAME + " WHERE "
                            + COL.DATE.getColName() + " IN (SELECT "
                            + COL.DATE.getColName() + " FROM " + TABLE_NAME
                            + " ORDER BY " + COL.DATE.getColName()
                            + " ASC LIMIT ?)",
                    new String[] { String.valueOf(overCount) });
            close();
        }
    }

    private int getRowCount() {
        final Cursor mCount = db.rawQuery("select count(*) from " + TABLE_NAME,
                null);
        mCount.moveToFirst();
        final int count = mCount.getInt(0);
        return count;
    }
}
