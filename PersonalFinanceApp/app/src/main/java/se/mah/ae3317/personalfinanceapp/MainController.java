package se.mah.ae3317.personalfinanceapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xisth on 2016-09-13.
 */
public class MainController extends States {
    public final static String ITEMS = "se.mah.ae3317.ITEMS";
    private Database db;
    private Activity activity;

    public MainController(Activity activity) {
        this.activity = activity;
        db = new Database(activity);
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout, fragment).commit();
    }

    public void addItem(String title, String type, int money, String date) {
        SQLiteDatabase db = this.db.getReadableDatabase();
        Item item = new Item(title, type, money, date);
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_TITLE, title);
        values.put(Database.COLUMN_TYPE, type);
        values.put(Database.COLUMN_MONEY, money);
        values.put(Database.COLUMN_DATE, date);
        switch (getCurrentState()) {
            case STATE_iNCOME: {
                db.insert(Database.TABLE_INCOME, null, values);
                break;
            }
            case STATE_EXPENSE: {
                db.insert(Database.TABLE_EXPENSE, null, values);
                break;
            }
        }
    }

    public void deleteId(Item item) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        switch (getCurrentState()) {
            case STATE_iNCOME: {
                db.delete(Database.TABLE_INCOME, Database.COLUMN_TITLE + "='" + item.getTitle() + "'", null);
                break;
            }
            case STATE_EXPENSE: {
                db.delete(Database.TABLE_EXPENSE, Database.COLUMN_TITLE + "='" + item.getTitle() + "'", null);
                break;
            }
        }
        showList();
    }

    public void showList() {
        int titleIndex, typeIndex, moneyIndex, dateIndex;
        Item[] items = null;
        Cursor cursor = null;
        SQLiteDatabase db = this.db.getReadableDatabase();
        switch (getCurrentState()) {
            case STATE_iNCOME: {
                cursor = db.rawQuery("SELECT * FROM " + Database.TABLE_INCOME + " ORDER BY " + Database.COLUMN_DATE, null);
                break;
            }
            case STATE_EXPENSE: {
                cursor = db.rawQuery("SELECT * FROM " + Database.TABLE_EXPENSE + " ORDER BY " + Database.COLUMN_DATE, null);
                break;
            }
        }
        items = new Item[cursor.getCount()];
        titleIndex = cursor.getColumnIndex(Database.COLUMN_TITLE);
        typeIndex = cursor.getColumnIndex(Database.COLUMN_TYPE);
        moneyIndex = cursor.getColumnIndex(Database.COLUMN_MONEY);
        dateIndex = cursor.getColumnIndex(Database.COLUMN_DATE);

        for (int i = 0; i < items.length; i++) {
            cursor.moveToPosition(i);
            items[i] = new Item(
                    cursor.getString(titleIndex),
                    cursor.getString(typeIndex),
                    cursor.getInt(moneyIndex),
                    cursor.getString(dateIndex));
        }
        Intent intent = new Intent(activity, ListActivity.class);
        intent.putExtra(ITEMS, items);
        activity.startActivity(intent);
    }

    public int getTotalMoney() {
        return getIncomes() - getExpenses();
    }

    public int getIncomes() {
        int moneyIndex;
        int money = 0;

        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Database.COLUMN_MONEY + " FROM " + Database.TABLE_INCOME + " ORDER BY " + Database.COLUMN_DATE, null);
        moneyIndex = cursor.getColumnIndex(Database.COLUMN_MONEY);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            money += cursor.getInt(moneyIndex);
        }
        return money;
    }

    public int getExpenses() {
        int moneyIndex;
        int money = 0;

        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Database.COLUMN_MONEY + " FROM " + Database.TABLE_EXPENSE + " ORDER BY " + Database.COLUMN_DATE, null);
        moneyIndex = cursor.getColumnIndex(Database.COLUMN_MONEY);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            money += cursor.getInt(moneyIndex);
        }
        return money;
    }

    public int getImage(String type) {
        int image = -1;
        switch (States.getCurrentState()) {
            case STATE_iNCOME: {
                if (type.equals("Salary")) {
                    image = R.drawable.salary;
                }
                else {
                    image = R.drawable.other;
                }
                break;
            }
            case STATE_EXPENSE: {
                if (type.equals("")) {

                    //String[] hej = activity.getResources().getStringArray(R.array.income_array);
                }
            }
        }

        return image;
    }

    // if I want to continue my work

    /*public void deleteAll() {
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.delete(Database.TABLE_INCOME, null, null);
        db.delete(Database.TABLE_EXPENSE, null, null);
    }*/

    /*public void deleteTable() {
        SQLiteDatabase db = this.db.getWritableDatabase();
        switch (getCurrentState()) {
            case STATE_iNCOME: {
                db.delete(Database.TABLE_INCOME, null, null);
                break;
            }
            case STATE_EXPENSE: {
                db.delete(Database.TABLE_EXPENSE, null, null);
                break;
            }
        }
    }*/

    /*public void updateTable(Item item) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.COLUMN_TITLE, item.getTitle());
        values.put(Database.COLUMN_TYPE, item.getType());
        values.put(Database.COLUMN_MONEY, item.getMoney());
        values.put(Database.COLUMN_DATE, item.getDate());
        switch (getCurrentState()) {
            case STATE_iNCOME: {
                db.update(Database.TABLE_INCOME, values, Database.COLUMN_TITLE + "='" + item.getTitle() + "'", null);
                break;
            }
            case STATE_EXPENSE: {
                db.update(Database.TABLE_EXPENSE, values, Database.COLUMN_TITLE + "='" + item.getTitle() + "'", null);
                break;
            }
        }
    }*/
}
