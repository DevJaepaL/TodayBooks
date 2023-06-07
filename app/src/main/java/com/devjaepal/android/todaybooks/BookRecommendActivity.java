package com.devjaepal.android.todaybooks;
import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devjaepal.android.todaybooks.api.APIClient;
import com.devjaepal.android.todaybooks.api.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devjaepal.android.todaybooks.api.BookItem;
import com.devjaepal.android.todaybooks.api.BookSearchResponse;
import com.devjaepal.android.todaybooks.api.CategoryKewordUtility;
import com.devjaepal.android.todaybooks.db.todayBookDB;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

public class BookRecommendActivity extends AppCompatActivity {

    private static final String MY_CLIENT_ID = "v8_2r1fSN3Zq0pmCwv_E";
    private static final String MY_CLIENT_SECRET = "FWD5ZbhFXF";

    private BookItem selectedBook; // 책 정보를 저장할 멤버 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recommend);

        // 이전 액티비티로부터 선택한 카테고리들을 가져온다.
        List<String> selectedUserCategory = getSelectedCategoriesFromDB();

        TextView detailTextBtn = findViewById(R.id.detailTextBtn);
        detailTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(selectedBook);
            }
        });

        // 유저가 선택한 카테고리 중 하나를 랜덤으로 선택한다.
        Random random = new Random();
        String selectedCategory = selectedUserCategory.get(random.nextInt(selectedUserCategory.size()));

        // API 요청 수행 메소드
        requestBookRecommendation(selectedCategory);
    }

    // 책 정보 요청해서 받아온 후, 랜덤으로 책을 추천해주는 메소드.
    private void requestBookRecommendation(String category) {
        String keword = CategoryKewordUtility.getCategoriesKeyword(category); // 카테고리에 따른 키워드 랜덤으로 찾기.
        APIInterface apiInterface = APIClient.getInstance().create(APIInterface.class);
        Call<String> call = apiInterface.getBookSearchResult(
                // 요청 API 값들
                MY_CLIENT_ID,       // 내 API 아이디
                MY_CLIENT_SECRET,   // SECRET ID
                "book.json",        // JSON 타입으로 API 요청.
                keword,             // 카테고리 검색이 미지원, 따라서 내 임의대로 카테고리에 따른 검색 키워드 지정해야함.
                "sim",              // 검색어 정확도를 기준으로 찾는다. -> date로 바꾸면 최신일자 책 검색.
                50);               // 키워드로 검색한 책 50개 중, 하나 랜덤으로 찾아서 검색.

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    // 응답 결과를 파싱해서 추천된 책을 이미지 뷰에 띄워야 함.
                    parseResponse(result);
                    Log.e(TAG, "성공 : " + result);
                } else {
                    Log.e(TAG, "실패 : " + response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "에러 : " + t.getMessage());
            }
        });
    }

    // 이 메소드에서 응답 결과를 알맞는 정보들을 파싱해서 책을 추천한다.
    private void parseResponse(String response) {
        Gson gson = new Gson();
        BookSearchResponse bookSearchResponse = gson.fromJson(response, BookSearchResponse.class);
        if(bookSearchResponse != null && bookSearchResponse.getItems() != null &&
            !bookSearchResponse.getItems().isEmpty()) {
            List<BookItem> bookItems = bookSearchResponse.getItems();
            // 추천된 책 중 하나를 랜덤하게 선택한다.
            Random random = new Random();
            selectedBook = bookItems.get(random.nextInt(bookItems.size()));
            displayBookInformation(selectedBook);
        }
    }

    // API에서 파싱한 이미지 URL을 화면에 보여주기 위한 메소드
    private void displayBookInformation(BookItem bookItem) {
        // Glide를 통해 이미지 뷰에 네이버 이미지 URL 사용해서 이미지 삽입.
        ImageView bookThumbnailView = findViewById(R.id.NaverBookImage);
        TextView bookTitleTextView = findViewById(R.id.bookTitleText);

        String getAPIBookTitle = bookItem.getTitle();
        String getAPIimageURL = bookItem.getImageUrl();

        Glide.with(this)
                .load(getAPIimageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bookThumbnailView);

        bookTitleTextView.setText("\""+ getAPIBookTitle + "\"");
    }

    // 파싱한 데이터들을 상세정보 화면에 넘겨 보여주는 메소드
    private void showDetail(BookItem bookItem) {
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

        bookTitleTextView.setText(bookTitle + "\n");
        bookAuthorTextView.setText("저자 : " + bookAuthor);
        bookPublisherTextView.setText("출판사 : " + bookPublisher);
        bookDescriptionTextView.setText(bookDescription);
        bookLinkTextView.setText("도서 구매 링크 : \n" + bookLink);

        bookLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookLink(bookLink);
            }
        });

        dialog.show();
    }

    // 링크 클릭시 브라우저로 연결해주는 메소드
    private void openBookLink(String bookLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookLink));
        startActivity(intent);
    }

    // DB에 저장된 카테고리들을 가져오는 메소드.
    private List<String> getSelectedCategoriesFromDB() {
        todayBookDB db = new todayBookDB(this);
        List<String> selectedCategories = db.getAllCategories();
        db.close();
        return selectedCategories;
    }
}