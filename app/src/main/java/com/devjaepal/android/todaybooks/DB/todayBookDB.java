package com.devjaepal.android.todaybooks.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devjaepal.android.todaybooks.API.BookItem;

import java.util.ArrayList;
import java.util.List;

// SQLite 이용.
public class todayBookDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "todaybooks.db";
    private static final int DB_VERSION = 5;

    /*  UserCategories 테이블 생성 쿼리
        유저가 선택한 카테고리를 저장하는 테이블 */
    private static final String CREATE_USER_CATEGORIES_TABLE =
            "CREATE TABLE IF NOT EXISTS UserCategories (" +
                    "category_id INTEGER PRIMARY KEY, " +
                    "category_name TEXT);";

    /*  LikedBooks 테이블 생성 쿼리
     *   유저가 좋아요한 책들을 저장하는 테이블(메모,별점 기능 있음) */
    private static final String CREATE_LIKED_BOOKS_TABLE =
            "CREATE TABLE IF NOT EXISTS LikedBooks (" +
                    "book_title TEXT PRIMARY KEY, " +
                    "book_author TEXT, " +
                    "book_publisher TEXT, " +
                    "book_description TEXT, " +
                    "book_image_url TEXT, " +
                    "book_link TEXT, " +
                    "book_memo TEXT)";

    public todayBookDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // DB 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_CATEGORIES_TABLE);
        db.execSQL(CREATE_LIKED_BOOKS_TABLE);
    }

    // DB가 업그레이드 될 경우 사용할 메소드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS UserCagegories");
            db.execSQL("DROP TABLE IF EXISTS LikedBooks");
        }
        // 새로운 테이블 생성
        onCreate(db);
    }

    /* DB CRUD 구현 메소드 */
    // UserCategories 테이블에 유저가 선택한 카테고리 추가
    public void addUserCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_name", categoryName);
        db.insert("UserCategories", null, values);
        db.close();
    }
    
    // 카테고리 업데이트 갱신을 위한 테이블 삭제 메소드
    public void deleteUserCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UserCategories", null,null);
        db.close();
    }

    // 사용자가 선택한 모든 카테고리 조회
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT category_name" +
                " FROM UserCategories", null);
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    // LikedBooks 테이블 정보 추가 메소드
    public void addLikedBook(BookItem book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("book_title", book.getTitle());
        values.put("book_author", book.getAuthor());
        values.put("book_publisher", book.getPublisher());
        values.put("book_description", book.getDescription());
        values.put("book_image_url", book.getImageUrl());
        values.put("book_link", book.getLink());
        db.insert("LikedBooks", null, values);
        db.close();
    }

    // 좋아요한 모든 책들을 조회하는 메소드. do-while문을 통해 모든 DB 레코드들을 순회한 후 리턴한다.
    public List<BookItem> getAllLikedBooks() {
        List<BookItem> likedBooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LikedBooks", null);
        if (cursor.moveToFirst()) {
            do {
                // 컬럼 인덱스 번호에 따라 추출해 Book 객체를 생성해준다.
                String title = cursor.getString(0);
                String author = cursor.getString(1);
                String publisher = cursor.getString(2);
                String description = cursor.getString(3);
                String imageUrl = cursor.getString(4);
                String link = cursor.getString(5);
                BookItem likeBook = new BookItem();
                likeBook.setTitle(title);
                likeBook.setAuthor(author);
                likeBook.setPublisher(publisher);
                likeBook.setDescription(description);
                likeBook.setImageUrl(imageUrl);
                likeBook.setLink(link);
                likedBooks.add(likeBook);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return likedBooks;
    }

    // 이미 좋아요를 누른 책인지 체크하는 메소드
    public boolean isBookAlreadyLiked(String likeBookTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM LikedBooks WHERE book_title = ?",
                new String[]{likeBookTitle});

        int bookColumnPrimaryKeyCount = 0;
        if (cursor.moveToFirst()) {
            bookColumnPrimaryKeyCount = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return bookColumnPrimaryKeyCount > 0;
    }

    // UserCategories에 데이터가 존재한 지 확인하는 메소드
    public boolean hasCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM UserCategories", null);
        int cnt = 0;
        if (cursor.moveToFirst()) {
            cnt = cursor.getInt(0); // 테이블에 값 있는지 확인.
        }

        cursor.close();
        db.close();
        return cnt > 0; // 0 = false , 그 이상은 값이 있기 때문에 true로 cnt값 그대로 반환.
    }

    // 특정 책에 대한 사용자가 작성한 메모 저장 메소드
    public void saveLikeBookMemo(String bookTitle, String memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("book_memo", memo);
        db.update(
                "LikedBooks",
                values,
                "book_title=?",
                new String[]{bookTitle});
        db.close();
    }

    // 메모를 다른 곳에서 불러오기 위한 메소드(READ)
    public String getLikeBookUserMemo(String bookTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userMemo = "";

        Cursor cursor = db.query(
                "LikedBooks",
                new String[]{"book_memo"},
                "book_title = ?",
                new String[]{bookTitle},
                null,
                null,
                null);
        int memoColumnIndex = cursor.getColumnIndex("book_memo");
        if(memoColumnIndex != -1 && cursor.moveToFirst()) {
            userMemo = cursor.getString(memoColumnIndex);
        }
        cursor.close();
        db.close();
        return userMemo;
    }

    // 좋아요를 누른 책 삭제하는 메소드
    public void deleteLikedBook(String bookTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                "LikedBooks",
                "book_title=?",
                new String[]{bookTitle}
        );
        db.close();
    }

}