package com.algoritma.recruitmenttest.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.algoritma.recruitmenttest.model.StockInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "az_algoritma_stock.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STOCK_CREATE =
            "CREATE TABLE " + TablesInfo.TABLE_NAME + " (" +
                    TablesInfo.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TablesInfo.COLUMN_DATA_0 + " TEXT ," +
                    TablesInfo.COLUMN_DATA_1 + " TEXT ," +
                    TablesInfo.COLUMN_DATA_2 + " TEXT ," +
                    TablesInfo.COLUMN_DATA_3 + " TEXT ," +
                    TablesInfo.COLUMN_DATA_4 + " TEXT ," +
                    TablesInfo.COLUMN_DATA_5 + " TEXT ," +
                    TablesInfo.COLUMN_DATA_6 + " NUMBER ," +
                    TablesInfo.COLUMN_DATA_7 + " TEXT)";

    private static final String TABLE_STOCK_DELETE_ALL = "DELETE FROM " + TablesInfo.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_STOCK_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TablesInfo.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    private void deleteAllRows(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(TABLE_STOCK_DELETE_ALL);
    }

    public void insertData(JSONArray stockDataList) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        deleteAllRows();

        sqLiteDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();

            for (int i = 0; i < stockDataList.length(); i++) {
                JSONObject STOCKInfo = stockDataList.getJSONObject(i);
                contentValues.put(TablesInfo.COLUMN_DATA_0, STOCKInfo.getString("0"));
                contentValues.put(TablesInfo.COLUMN_DATA_1, STOCKInfo.getString("1"));
                contentValues.put(TablesInfo.COLUMN_DATA_2, STOCKInfo.getString("2"));
                contentValues.put(TablesInfo.COLUMN_DATA_3, STOCKInfo.getString("3"));
                contentValues.put(TablesInfo.COLUMN_DATA_4, STOCKInfo.getString("4"));
                contentValues.put(TablesInfo.COLUMN_DATA_5, STOCKInfo.getString("5"));
                contentValues.put(TablesInfo.COLUMN_DATA_6, STOCKInfo.getInt("6"));
                contentValues.put(TablesInfo.COLUMN_DATA_7, STOCKInfo.getString("7"));

                sqLiteDatabase.insert(TablesInfo.TABLE_NAME, null, contentValues);
            }

            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e(TAG, "insertData: ", ex);
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @SuppressLint("Range")
    public List<StockInfo> getStockList() {
        List<StockInfo> stockInfoList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] selectColumnList = {
                TablesInfo.COLUMN_DATA_0,
                TablesInfo.COLUMN_DATA_1,
                TablesInfo.COLUMN_DATA_2,
                TablesInfo.COLUMN_DATA_3,
                TablesInfo.COLUMN_DATA_4,
                TablesInfo.COLUMN_DATA_5,
                TablesInfo.COLUMN_DATA_6,
                TablesInfo.COLUMN_DATA_7
        };

        Cursor cursor = sqLiteDatabase.query(TablesInfo.TABLE_NAME, selectColumnList, null, null, null, null, TablesInfo.COLUMN_DATA_1);

        while (cursor.moveToNext()) {
            stockInfoList.add(new StockInfo(cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_0)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_1)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_2)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_3)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_4)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_4)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_6)),
                    cursor.getString(cursor.getColumnIndex(TablesInfo.COLUMN_DATA_7))
                    ));
        }

        cursor.close();

        sqLiteDatabase.close();

        return stockInfoList;
    }
}
