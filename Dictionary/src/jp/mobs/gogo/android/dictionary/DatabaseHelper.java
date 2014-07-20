package jp.mobs.gogo.android.dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "jp_mobs_gogo_android_dictionary.db";
    private final static int DATABASE_VERSION = 9;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + HistoryDbAdapter.TABLE_NAME
                + " (" + HistoryDbAdapter.COL.DATE.getColName()
                + " DATETIME PRIMARY KEY,"
                + HistoryDbAdapter.COL.WORD.getColName() + " TEXT NOT NULL);");
        // db.execSQL(
        // "CREATE TABLE IF NOT EXISTS " + TagDefDbAdapter.TABLE_NAME + " ("
        // + TagDefDbAdapter.COL.ID.getColName() +
        // " INTEGER PRIMARY KEY AUTOINCREMENT,"
        // + TagDefDbAdapter.COL.NAME.getColName() + " TEXT NOT NULL);");
        // db.execSQL(
        // "CREATE TABLE IF NOT EXISTS " + PageTagDbAdapter.TABLE_NAME + " ("
        // + PageTagDbAdapter.COL.ID.getColName() +
        // " INTEGER PRIMARY KEY AUTOINCREMENT,"
        // + PageTagDbAdapter.COL.URL.getColName() + " TEXT NOT NULL,"
        // + PageTagDbAdapter.COL.TITLE.getColName() + " TEXT NOT NULL,"
        // + PageTagDbAdapter.COL.TAG_ID.getColName() + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
            final int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + HistoryDbAdapter.TABLE_NAME);
        onCreate(db);
    }
}
