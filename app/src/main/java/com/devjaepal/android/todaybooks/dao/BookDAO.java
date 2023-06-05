package com.devjaepal.android.todaybooks.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devjaepal.android.todaybooks.table.BookTable;

public class BookDAO {
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    public BookDAO(SQLiteDatabase database) {
        this.database = database;
    }

    // 해당 DB를 쓰기 가능한 상태로 만드는 메소드
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // DB 닫는 메소드
    public void close() {
        dbHelper.close();
    }

    /* CRUD 파트 */
    // Create
    public long insertBook(BookTable book) {
        ContentValues values = new ContentValues();
        values.put(BookTable.COLUMN_TITLE, book.getTitle());
        values.put(BookTable.COLUMN_AUTHOR, book.getAuthor());
        values.put(BookTable.COLUMN_DISCOUNT, book.getDiscount());
        values.put(BookTable.COLUMN_PUBLISHER, book.getPublisher());
        values.put(BookTable.COLUMN_PUBDATE, book.getPubdate());
        values.put(BookTable.COLUMN_CATEGORY, book.getCategory());
        values.put(BookTable.COLUMN_DESCRIPT, book.getDescript());

        return database.insert(BookTable.TABLE_NAME, null, values);
    }
}
