package com.devjaepal.android.todaybooks.table;

public class ReadingHistoryTable {
    public static final String TABLE_NAME = "reading_history";
    public static final String COLUMN_ID = "history_id";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_READ_DATE = "read_date";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BOOK_ID + " INTEGER REFERENCES " + BookTable.TABLE_NAME + "(" + BookTable.COLUMN_BOOK_ID + "), " +
            COLUMN_READ_DATE + " TEXT)";

    private int historyID;
    private int bookID;
    private String readDate;

    public ReadingHistoryTable(int historyID, int bookID, String readDate) {
        this.historyID = historyID;
        this.bookID = bookID;
        this.readDate = readDate;
    }

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryId(int historyID) {
        this.historyID = historyID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public static String getCreateTableQuery() {
        return CREATE_TABLE_QUERY;
    }
}
