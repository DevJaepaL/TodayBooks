package com.devjaepal.android.todaybooks.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

public class LikeBooksListActivity extends AppCompatActivity {
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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_books_list);

        /* Navigation Bar 파트 */
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == TAB_HOME) {
                    startActivity(new Intent(LikeBooksListActivity.this, MainActivity.class));
                    return true;
                } else if (itemId == TAB_LIKE_BOOKS) {
                    return true;
                } else if (itemId == USER_SETTING) {
                    // 마이페이지로 이동하는 코드 작성
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        // 설정 완료 되면 DB로부터 값을 가져온다.
        getLikedBooksFromDB();
    }

    private void getLikedBooksFromDB() {
        todayBookDB db = new todayBookDB(this);
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
}