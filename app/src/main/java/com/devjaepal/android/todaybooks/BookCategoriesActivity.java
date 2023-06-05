package com.devjaepal.android.todaybooks;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class BookCategoriesActivity extends AppCompatActivity {

    private ListView listView;
    private Button selectButton;
    private ArrayAdapter<String> adapter;
    private List<String> bookCategoriesList;

    // 선택한 카테고리를 Alert로 보여주는 메소드
    private void showSelectedCategoriesDialog(List<String> selectedCategories) {
        StringBuilder messageBuilder = new StringBuilder();
        for(String bookCategory : selectedCategories) {
            messageBuilder.append("- ").append(bookCategory).append("\n");
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BookCategoriesActivity.this)
                .setTitle("선택한 항목")
                .setMessage(messageBuilder.toString())
                .setPositiveButton("확인", null);

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        listView = findViewById(R.id.bookCategories);
        selectButton = findViewById(R.id.selectButton);

        // 리스트 뷰에 담을 카테고리 리스트
        String[] bookCategories = {
                "소설", "시 / 에세이", "컴퓨터 / IT",
                "국어 / 외국어", "여행", "가정 / 요리",
                "어린이", "유아", "종교", "예술 / 대중문화",
                "자연 / 과학", "사회 / 정치", "역사", "자기계발", "경제 / 경엉"};

        // 배열에 있는 카테고리 문자열 전부 추가.
        bookCategoriesList = new ArrayList<>();
        for (String bookCategory : bookCategories) {
            bookCategoriesList.add(bookCategory);
        }
        // 어댑터 설정
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                bookCategories);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 카테고리에 따른 API 처리
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                int itemCount = listView.getCount();
                List<String> selectedBookCategories  = new ArrayList<>();

                for (int i = 0; i < itemCount; i++) {
                    if (checkedItems.get(i)) {
                        selectedBookCategories.add(bookCategoriesList.get(i));
                    }
                }
                // 선택한 항목이 있을 경우 Dialog 표시
                if(!selectedBookCategories.isEmpty()) {
                    showSelectedCategoriesDialog(selectedBookCategories);
                }
            }
        });
    }
}