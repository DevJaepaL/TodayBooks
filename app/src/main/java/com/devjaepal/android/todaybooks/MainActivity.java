package com.devjaepal.android.todaybooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 버튼 누르면 카테고리 선택으로 넘어감.
    public void appStart(View view) {
        Intent intent = new Intent(this, BookCategoriesActivity.class);
        startActivity(intent);
    }
}