package com.devjaepal.android.todaybooks.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devjaepal.android.todaybooks.table.BookTable;
import com.devjaepal.android.todaybooks.table.LikeBooksTable;
import com.devjaepal.android.todaybooks.table.MemoTable;
import com.devjaepal.android.todaybooks.table.ReadingHistoryTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todayBooks.db"; // DB 명
    private static final int DATABASE_VERSION = 1; // DB 버전

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 테이블 생성 쿼리 실행
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookTable.CREATE_TABLE_QUERY);
        db.execSQL(MemoTable.CREATE_TABLE_QUERY);
        db.execSQL(LikeBooksTable.CREATE_TABLE_QUERY);
        db.execSQL(ReadingHistoryTable.CREATE_TABLE_QUERY);
    }

    // 테이블 업그레이드 등의 작업 수행
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MemoTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LikeBooksTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReadingHistoryTable.TABLE_NAME);
        onCreate(db);
    }
}
