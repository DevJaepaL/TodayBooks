package com.devjaepal.android.todaybooks;
import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.devjaepal.android.todaybooks.api.APIClient;
import com.devjaepal.android.todaybooks.api.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devjaepal.android.todaybooks.api.BookItem;
import com.devjaepal.android.todaybooks.api.BookSearchResponse;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

public class BookRecommendActivity extends AppCompatActivity {

    private static final String MY_CLIENT_ID = "v8_2r1fSN3Zq0pmCwv_E";
    private static final String MY_CLIENT_SECRET = "FWD5ZbhFXF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recommend);

        // 유저가 선택한 카테고리 중 하나를 랜덤으로 선택한다.
        List<String> selectedUserCategory = getIntent().getStringArrayListExtra("selectedCategories");
        Random random = new Random();
        String selectedCategory = selectedUserCategory.get(random.nextInt(selectedUserCategory.size()));

        // API 요청 수행 메소드
        requestBookRecommendation(selectedCategory);
//        testAPI();
    }

    private void requestBookRecommendation(String category) {
        String keword = getCategoriesKeyword(category); // 카테고리에 따른 키워드 랜덤으로 찾기.
        APIInterface apiInterface = APIClient.getInstance().create(APIInterface.class);
        Call<String> call = apiInterface.getBookSearchResult(
                // 요청 API 값들
                MY_CLIENT_ID,       // 내 API 아이디
                MY_CLIENT_SECRET,   // SECRET ID
                "book.json",        // JSON 타입으로 API 요청.
                keword,             // 카테고리 검색이 미지원, 따라서 내 임의대로 카테고리에 따른 검색 키워드 지정해야함.
                "sim",              // 검색어 정확도를 기준으로 찾는다. -> date로 바꾸면 최신일자 책 검색.
                50);               // 책 100개 중, 하나 랜덤으로 찾아서 검색.

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
            BookItem selectedBook = bookItems.get(random.nextInt(bookItems.size()));
            displayBookInformation(selectedBook);
        }
    }

    // API에서 파싱한 데이터들을 화면에 보여주기 위한 메소드
    private void displayBookInformation(BookItem bookItem) {
        // Glide를 통해 이미지 뷰에 네이버 이미지 URL 사용해서 이미지 삽입.
        ImageView bookThumbnailView = findViewById(R.id.NaverBookImage);
        String imageURL = bookItem.getImageUrl();
        Glide.with(this)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bookThumbnailView);
    }


    private String getCategoriesKeyword(String category) {
        String categoryKeword = "";

        switch (category) {
            case "소설":
                categoryKeword = "고전 문학";
                break;
            case "시/에세이":
                categoryKeword = "고전 문학";
                break;
            case "컴퓨터/IT":
                categoryKeword = "고전 문학";
                break;
            case "만화":
                categoryKeword = "귀멸의 칼날";
                break;
            case "정치":
                categoryKeword = "한국 정치";
                break;
            case "국어/외국어":
                categoryKeword = "영어 독학";
                break;
            case "여행":

                break;
            case "가정/요리":

                break;
            case "어린이":

                break;
            case "유아":

                break;
            case "종교":

                break;
            case "예술/대중문화":

                break;
            case "자연/과학":

                break;
            case "사회 / 정치":

                break;
            case "역사":

                break;
            case "자기계발":

                break;
            case "경제 / 경영":

                break;
        }

        return categoryKeword;
    }
}