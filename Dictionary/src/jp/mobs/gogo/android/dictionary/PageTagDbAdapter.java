package jp.mobs.gogo.android.dictionary;

import java.util.ArrayList;
import java.util.List;

import jp.mobs.gogo.android.dictionary.model.TagDefModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PageTagDbAdapter {
    public static final String TABLE_NAME = "page_tag";

    public static enum COL {
        ID("id", 0), URL("url", 1), TITLE("title", 2), TAG_ID("tag_id", 3);
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

    public PageTagDbAdapter(final Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    public PageTagDbAdapter openWritable() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public PageTagDbAdapter openReadable() {
        db = dbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // public boolean deleteAll(){
    // openWritable();
    // boolean result = db.delete(TABLE_NAME, null, null) > 0;
    // close();
    // return result;
    // }

    public boolean deleteByTagID(final int tagId) {
        openWritable();
        final boolean result = db.delete(TABLE_NAME, COL.TAG_ID.getColName()
                + " = " + tagId, null) > 0;
        close();
        return result;
    }

    public Cursor getAll() {
        openReadable();
        final Cursor c = db.query(TABLE_NAME, null, null, null, null, null,
                null);
        close();
        return c;
    }

    // public void save(String url, String title, int tagId){
    // ContentValues values = new ContentValues();
    // values.put(COL.URL.getColName(), url);
    // values.put(COL.TITLE.getColName(), title);
    // values.put(COL.TAG_ID.getColName(), String.valueOf(tagId));
    // db.insertOrThrow(TABLE_NAME, null, values);
    // }

    public boolean[] getFlags(final List<TagDefModel> tagDefModels,
            final String url) {
        final List<Boolean> flags = new ArrayList<Boolean>();
        openReadable();
        // Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
        // COL.URL.getColName() + " = " + url + ";", null);
        final Cursor c = db.query(TABLE_NAME, null, COL.URL.getColName()
                + " = ?", new String[] { url }, null, null, null);
        if (c != null) {
            final List<Integer> tagNums = new ArrayList<Integer>();
            boolean isEof = c.moveToFirst();
            while (isEof) {
                final int tagNum = c.getInt(COL.TAG_ID.getColNum());
                tagNums.add(Integer.valueOf(tagNum));
                isEof = c.moveToNext();
            }
            c.close();

            for (final TagDefModel model : tagDefModels) {
                if (tagNums.contains(Integer.valueOf(model.getId()))) {
                    flags.add(true);
                } else {
                    flags.add(false);
                }
            }
        }
        close();
        final boolean[] retFlags = new boolean[flags.size()];
        for (int i = 0; i < flags.size(); i++) {
            retFlags[i] = flags.get(i).booleanValue();
        }

        return retFlags;
    }

    public void editTag(final String url, final TagDefModel model,
            final boolean isAdd, final String title) {
        openWritable();
        if (isAdd) {
            // insert
            final ContentValues values = new ContentValues();
            values.put(COL.URL.getColName(), url);
            values.put(COL.TITLE.getColName(), title);
            values.put(COL.TAG_ID.getColName(), String.valueOf(model.getId()));
            db.insertOrThrow(TABLE_NAME, null, values);
        } else {
            // delete
            db.delete(TABLE_NAME, COL.URL.getColName() + " = ? AND "
                    + COL.TAG_ID + " = " + model.getId(), new String[] { url });
        }
        close();
    }
}
