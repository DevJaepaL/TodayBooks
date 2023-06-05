package com.devjaepal.android.todaybooks.table;

public class LikeBooksTable {
    public static final String TABLE_NAME = "likebooks";
    public static final String COLUMN_LIKEBOOKS_ID = "likebooks_id";
    public static final String COLUMN_BOOK_ID = "book_id";
    public static final String COLUMN_LIKE = "like";

    // 쿼리 생성 메소드
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_LIKEBOOKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_BOOK_ID + " INTEGER REFERENCES " + BookTable.TABLE_NAME + "(" + BookTable.COLUMN_BOOK_ID + "), " +
            COLUMN_LIKE + " INTEGER)";


    // 생성자 멤버 변수
    private int likeBooksID;
    private int bookID;
    private int like;

    // 생성자
    public LikeBooksTable(int likeBooksID, int bookID, int like) {
        this.likeBooksID = likeBooksID;
        this.bookID = bookID;
        this.like = like;
    }

    public int getLikeBooksId() {
        return likeBooksID;
    }

    public void setLikeBooksID(int likeBooksID) {
        this.likeBooksID = likeBooksID;
    }

    public int getBookId() {
        return bookID;
    }

    public void setBookId(int bookID) {
        this.bookID = bookID;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public static String getCreateTableQuery() {
        return CREATE_TABLE_QUERY;
    }
}
