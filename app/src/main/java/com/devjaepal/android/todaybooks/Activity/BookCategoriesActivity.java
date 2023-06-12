package com.devjaepal.android.todaybooks.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.devjaepal.android.todaybooks.DB.todayBookDB;
import com.devjaepal.android.todaybooks.R;

import java.util.ArrayList;
import java.util.List;


public class BookCategoriesActivity extends AppCompatActivity {

    private ListView listView;
    private Button selectButton;
    private ArrayAdapter<String> adapter;
    private List<String> bookCategoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        listView = findViewById(R.id.bookCategories);
        selectButton = findViewById(R.id.selectButton);

        // 리스트 뷰에 담을 카테고리 리스트
        String[] bookCategories = {
                "소설", "시/에세이", "컴퓨터/IT",
                "만화", "잡지", "국어/외국어", "여행", "가정/요리",
                "어린이", "유아", "종교", "예술/대중문화",
                "자연/과학", "사회 / 정치", "역사", "자기계발",
                "경제 / 경영"};

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

        selectButton.setOnClickListener(v -> {
            // 선택된 카테고리에 따른 API 처리
            SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
            int itemCount = listView.getCount();
            List<String> selectedBookCategories = new ArrayList<>();

            for (int i = 0; i < itemCount; i++) {
                if (checkedItems.get(i)) {
                    selectedBookCategories.add(bookCategoriesList.get(i));
                }
            }
            // 선택한 항목이 있을 경우 Dialog 표시
            if (!selectedBookCategories.isEmpty()) {
                saveSelectedCategories(selectedBookCategories); // DB에 카테고리들을 저장한다.
                showSelectedCategoriesDialog(selectedBookCategories);
            } else {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BookCategoriesActivity.this)
                        .setTitle("알림")
                        .setMessage("선택한 카테고리가 없어요 !")
                        .setPositiveButton("확인", null);

                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        });
    }

    // 선택한 카테고리를 Alert로 보여주는 메소드
    private void showSelectedCategoriesDialog(List<String> selectedCategories) {
        StringBuilder messageBuilder = new StringBuilder();
        for (String bookCategory : selectedCategories) {
            messageBuilder.append("· ").append(bookCategory).append("\n");
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BookCategoriesActivity.this)
                .setTitle("선택한 항목")
                .setMessage(messageBuilder.toString())
                .setPositiveButton("확 인", ((dialog, which) -> {
                    // 확인 버튼 눌렀을 때 다음 액티비티로 넘어간다.
                    openBookRecommendActivity(selectedCategories);
                }));

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    // 선택한 카테고리들을 DB에 저장하는 메소드
    private void saveSelectedCategories(List<String> selectedCategories) {
        todayBookDB db = new todayBookDB(this);
        for(String category : selectedCategories) {
            db.addUserCategory(category);
        }
        db.close();
    }

    // Intent를 사용해서 사용자가 선택한 주제들을 다른 액티비티로 전달하는 메소드
    private void openBookRecommendActivity(List<String> selectedCategories) {
        // BookCategories 액티비티 -> BookRecommend 액티비티로 정보 전달하는 Intent.
        Intent intent = new Intent(BookCategoriesActivity.this, BookRecommendActivity.class);
        intent.putStringArrayListExtra("selectedCategories", new ArrayList<>(selectedCategories));
        startActivity(intent);
    }
}