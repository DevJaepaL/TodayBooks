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

    public void startBookCategoriesActivity(View view) {
        Intent intent = new Intent(this, BookCategoriesActivity.class);
        startActivity(intent);
    }
}