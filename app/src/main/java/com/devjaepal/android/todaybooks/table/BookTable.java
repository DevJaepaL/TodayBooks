package com.devjaepal.android.todaybooks.table;

/* Book 테이블 */
public class BookTable {
    public static final String TABLE_NAME = "book"; // 테이블 명
    public static final String COLUMN_BOOK_ID = "book_id"; // 책 고유 아이디(PK)
    public static final String COLUMN_TITLE = "title"; // 책 제목
    public static final String COLUMN_AUTHOR = "author"; // 저자
    public static final String COLUMN_DISCOUNT = "discount"; // 책 가격
    public static final String COLUMN_PUBLISHER = "publisher"; // 출판사
    public static final String COLUMN_PUBDATE = "pubdate"; // 출판일자
    public static final String COLUMN_CATEGORY = "category"; // 책 카테고리
    public static final String COLUMN_DESCRIPT = "descript"; // 책 내용

    // 테이블 생성 쿼리 문
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_AUTHOR + " TEXT,"
            + COLUMN_DISCOUNT + " REAL,"
            + COLUMN_PUBLISHER + " TEXT,"
            + COLUMN_PUBDATE + " DATE,"
            + COLUMN_CATEGORY + " TEXT,"
            + COLUMN_DESCRIPT + " TEXT"
            + ")";

    // 생성자 위한 멤버 변수
    private int bookId;
    private String title;
    private String author;
    private double discount;
    private String publisher;
    private String pubdate;
    private String category;
    private String descript;

    public BookTable(int bookId, String title, String author, double discount, String publisher,
                     String pubdate, String category, String descript) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.discount = discount;
        this.publisher = publisher;
        this.pubdate = pubdate;
        this.category = category;
        this.descript = descript;
    }

    // Getter & Setter 메소드
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    // CREATE_TABLE_QUERY getter 메서드
    public static String getCreateTableQuery() {
        return CREATE_TABLE_QUERY;
    }
}
