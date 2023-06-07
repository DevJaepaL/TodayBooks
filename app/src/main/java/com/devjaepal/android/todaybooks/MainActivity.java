package com.devjaepal.android.todaybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.devjaepal.android.todaybooks.db.todayBookDB;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todayBookDB db = new todayBookDB(this);
        boolean hasUserCategories = db.hasCategories();
        db.close();

        if (hasUserCategories) {
            // 카테고리가 존재하는 경우 BookRecommendActivity로 이동
            Intent intent = new Intent(this, BookRecommendActivity.class);
            startActivity(intent);
            finish(); // MainAcitivy를 종료해서 사용자가 뒤로가기 버튼을 누르면 앱을 종료하게 한다.
        }
    }

    // 버튼 누르면 카테고리 선택으로 넘어감.
    public void appStart(View view) {
        Intent intent = new Intent(this, BookCategoriesActivity.class);
        startActivity(intent);
    }
}