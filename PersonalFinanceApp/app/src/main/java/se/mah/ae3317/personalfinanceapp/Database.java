package se.mah.ae3317.personalfinanceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xisth on 2016-09-14.
 */
public class Database extends SQLiteOpenHelper {
    public static final String TABLE_INCOME = "income";
    public static final String TABLE_EXPENSE = "expense";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MONEY = "money";
    public static final String COLUMN_DATE = "date";

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_INCOME =
            "create table " + TABLE_INCOME + "(" +
                    COLUMN_ID + " integer primary key autoincrement not null, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_TYPE + " text not null, " +
                    COLUMN_MONEY + " integer, " +
                    COLUMN_DATE + " text not null);";

    private static final String CREATE_EXPENSE =
            "create table " + TABLE_EXPENSE + "(" +
                    COLUMN_ID + " integer primary key autoincrement not null, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_TYPE + " text not null, " +
                    COLUMN_MONEY + " integer, " +
                    COLUMN_DATE + " text not null);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INCOME);
        db.execSQL(CREATE_EXPENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Database.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);
    }
}
