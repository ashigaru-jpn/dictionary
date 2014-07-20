package jp.mobs.gogo.android.dictionary;

import java.util.ArrayList;
import java.util.List;

import jp.mobs.gogo.android.dictionary.model.TagDefModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TagDefDbAdapter {
    public static final String TABLE_NAME = "tag_def";

    public static enum COL {
        ID("id", 0), NAME("name", 1);
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

    public TagDefDbAdapter(final Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private TagDefDbAdapter openWritable() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    private TagDefDbAdapter openReadable() {
        db = dbHelper.getReadableDatabase();
        return this;
    }

    private void close() {
        dbHelper.close();
    }

    // private boolean deleteAll(){
    // return db.delete(TABLE_NAME, null, null) > 0;
    // }

    private boolean delete(final int id) {
        return db.delete(TABLE_NAME, COL.ID.getColName() + " = " + id, null) > 0;
    }

    private Cursor getAll() {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    private Cursor getByRowId(final long rowId) {
        final Cursor c = db.query(TABLE_NAME, null, "ROWID = " + rowId, null,
                null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    private long save(final String name) {
        final ContentValues values = new ContentValues();
        values.put(COL.NAME.getColName(), name);
        final long rowid = db.insertOrThrow(TABLE_NAME, null, values);
        return rowid;
    }

    private boolean update(final TagDefModel model) {
        final ContentValues values = new ContentValues();
        values.put(COL.NAME.getColName(), model.getName());
        final int result = db.update(TABLE_NAME, values, COL.ID.getColName()
                + " = " + model.getId(), null);
        return (result > 0);
    }

    public TagDefModel add(final String name) {
        openWritable();
        final long rowId = save(name);// FIXME
        final Cursor c = getByRowId(rowId);
        final int id = c.getInt(0);
        final String tagName = c.getString(1);
        final TagDefModel model = new TagDefModel(id, tagName);
        close();
        return model;
    }

    public List<TagDefModel> getAllTagDefs() {
        openReadable();
        final Cursor c = getAll();
        final List<TagDefModel> models = new ArrayList<TagDefModel>();
        boolean isEof = c.moveToFirst();
        while (isEof) {
            final int id = c.getInt(0);
            final String name = c.getString(1);
            final TagDefModel model = new TagDefModel(id, name);
            models.add(model);
            isEof = c.moveToNext();
        }
        c.close();
        close();
        return models;
    }

    public boolean remove(final TagDefModel model) {
        openWritable();
        final boolean result = delete(model.getId());
        close();
        return result;

        // TODO
        // �폜�����^�O���t����ꂽ�P����폜
    }

    public boolean rename(final TagDefModel model) {
        openWritable();
        final boolean result = update(model);
        close();
        return result;
    }

    public String[] getTagNames() {
        final List<TagDefModel> tagModels = getAllTagDefs();
        final List<String> tagStrs = new ArrayList<String>();
        for (final TagDefModel model : tagModels) {
            tagStrs.add(model.getName());
        }
        return tagStrs.toArray(new String[0]);
    }
}
