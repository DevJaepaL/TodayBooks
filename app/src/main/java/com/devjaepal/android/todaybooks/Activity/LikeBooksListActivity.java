package com.devjaepal.android.todaybooks.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devjaepal.android.todaybooks.API.BookItem;
import com.devjaepal.android.todaybooks.Adapter.LikeBooksListAdapter;
import com.devjaepal.android.todaybooks.DB.todayBookDB;
import com.devjaepal.android.todaybooks.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class LikeBooksListActivity extends AppCompatActivity implements LikeBooksListAdapter.BookItemListener {
    // 하단 네비게이션 바 멤버 번수
    private BottomNavigationView bottomNavigationView;
    public static final int TAB_HOME = R.id.tab_home;
    public static final int TAB_LIKE_BOOKS = R.id.tab_likeBooks;
    public static final int USER_SETTING = R.id.userSetting;

    private RecyclerView recyclerView;
    private TextView noLikeTextView;
    private LikeBooksListAdapter adapter;
    private List<BookItem> likedBooksList;
    private todayBookDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new todayBookDB(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        likedBooksList = new ArrayList<>();
        setContentView(R.layout.activity_like_books_list);

        /* Navigation Bar 파트 */
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(TAB_LIKE_BOOKS);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == TAB_HOME) {
                    item.setChecked(true);
                    startActivity(new Intent(LikeBooksListActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == TAB_LIKE_BOOKS) {
                    item.setChecked(true);
                    return true;
                } else if (itemId == USER_SETTING) {
                    return true;
                }
                return false;
            }
        });

        /* 좋아하는 책들의 목록을 표시해주는 부분이다.
        *  RecyclerView, ViewHolder등을 활용해서 CardView로 보여준다. */
        recyclerView = findViewById(R.id.booksRecyclerView);
        noLikeTextView = findViewById(R.id.noLikesTextView);
        likedBooksList = new ArrayList<>();
        adapter = new LikeBooksListAdapter(this, likedBooksList);
        adapter.setBookItemListener(this); // 롱 클릭 이벤트 지정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // 액티비티가 다시 화면에 보여질 때마다 좋아하는 책들이 담긴 테이블을 새로 불러온다.
    @Override
    protected void onResume() {
        super.onResume();
        getLikedBooksFromDB();
    }

    private void getLikedBooksFromDB() {
        if(db == null) {
            db = new todayBookDB(this);
        }
        List<BookItem> likedBooks = db.getAllLikedBooks();
        db.close();

        likedBooksList.clear();
        likedBooksList.addAll(likedBooks);
        adapter.notifyDataSetChanged();

        // 좋아하는 책이 없을 경우 표시할 텍스트를 조정
        if (likedBooksList.isEmpty()) {
            noLikeTextView.setVisibility(View.VISIBLE);
        } else {
            noLikeTextView.setVisibility(View.GONE);
        }
    }

    // 좋아하는 책을 목록에서 삭제하는 메소드
    @Override
    public void onBookDelete(int position, BookItem bookItem) {
        // DB에서 책 삭제
        db.deleteLikedBook(bookItem.getTitle());
        // 목록에서 아이템 제거
        likedBooksList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, likedBooksList.size());

        // 좋아하는 책이 없을 경우 표시할 텍스트를 조정
        if (likedBooksList.isEmpty()) {
            noLikeTextView.setVisibility(View.VISIBLE);
        } else {
            noLikeTextView.setVisibility(View.GONE);
        }
        Toast.makeText(this, "책이 목록에서 삭제 됐어요 !", Toast.LENGTH_SHORT).show();
    }

    // 좋아하는 책에 메모하는 메소드
    @Override
    public void onBookMemo(int position, BookItem bookItem) {
        // 메모 항목 클릭시 메모를 작성할 수 있는 Dialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모 남기기");
        builder.setMessage("메모를 입력해주세요.");
        // 다이얼로그에 입력 필드를 추가해준다.
        final EditText memoUserInputText = new EditText(this);
        builder.setView(memoUserInputText);

        // 작성 완료 버튼 클릭 시 처리하는 로직 구현
        builder.setPositiveButton("작성 완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userBookMemo = memoUserInputText.getText().toString().trim();
                db.saveLikeBookMemo(bookItem.getTitle(), userBookMemo); // 작성한 메모를 DB에 저장한다.
                adapter.notifyItemChanged(position);
            }
        });
        builder.setNegativeButton("취 소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // 다이얼로그를 표시한다.
        builder.show();
    }

    @Override
    public void showLikeBooksDetailDialog(BookItem bookItem) {
        // 다이얼로그 생성 및 레이아웃 지정
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_book_detail);
        // 다이얼로그 내부에 있는 아이디 가져옴.
        TextView bookTitleTextView = dialog.findViewById(R.id.dialogBookTitle);
        TextView bookAuthorTextView = dialog.findViewById(R.id.dialogBookAuthor);
        TextView bookPublisherTextView = dialog.findViewById(R.id.dialogBookPublisher);
        TextView bookDescriptionTextView = dialog.findViewById(R.id.dialogBookDescription);
        TextView bookLinkTextView = dialog.findViewById(R.id.dialogBookLink);

        // 파싱 데이터들을 다이얼로그 내부에 지정함.
        String bookTitle = bookItem.getTitle();
        String bookAuthor = bookItem.getAuthor();
        String bookPublisher = bookItem.getPublisher();
        String bookDescription = bookItem.getDescription();
        String bookLink = bookItem.getLink();

        bookTitleTextView.setText(bookTitle);
        bookAuthorTextView.setText("저자 : " + bookAuthor);
        bookPublisherTextView.setText("출판사 : " + bookPublisher);
        bookDescriptionTextView.setText(bookDescription);
        bookLinkTextView.setText(bookLink);
        dialog.show();
    }
}