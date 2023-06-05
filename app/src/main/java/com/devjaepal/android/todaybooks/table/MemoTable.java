package com.devjaepal.android.todaybooks.table;

public class MemoTable {
    // 테이블명과 컬럼명 상수 정의
    public static final String TABLE_NAME = "memo";
    public static final String COLUMN_MEMO_ID = "memo_id";
    public static final String COLUMN_REFERENCE_BOOK_ID = "book_id";
    public static final String COLUMN_BOOK_COMMENT = "book_comment";

    // 테이블 생성 쿼리문
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_REFERENCE_BOOK_ID + " INTEGER REFERENCES " + BookTable.TABLE_NAME + "(" + BookTable.COLUMN_BOOK_ID+ "), " +
            COLUMN_BOOK_COMMENT + " TEXT)";

    private int memoID;
    private int bookID;
    private String bookComment;

    public MemoTable(int memoID, int bookID, String bookComment) {
        this.memoID = memoID;
        this.bookID = bookID;
        this.bookComment = bookComment;
    }

    public int getMemoID() {
        return memoID;
    }

    public void setMemoId(int memoID) {
        this.memoID = memoID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookId(int bookID) {
        this.bookID = bookID;
    }

    public String getBookComment() {
        return bookComment;
    }

    public void setBookComment(String bookComment) {
        this.bookComment = bookComment;
    }

    public static String getCreateTableQuery() {
        return CREATE_TABLE_QUERY;
    }

}
