package com.devjaepal.android.todaybooks;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devjaepal.android.todaybooks.api.APIClient;
import com.devjaepal.android.todaybooks.api.APIInterface;
import com.devjaepal.android.todaybooks.api.BookItem;
import com.devjaepal.android.todaybooks.api.BookSearchResponse;
import com.devjaepal.android.todaybooks.api.CategoryKewordUtility;
import com.devjaepal.android.todaybooks.db.todayBookDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class BookRecommendActivity extends AppCompatActivity {

    private static final String MY_CLIENT_ID = "v8_2r1fSN3Zq0pmCwv_E";
    private static final String MY_CLIENT_SECRET = "FWD5ZbhFXF";
    private BookItem selectedBook; // 책 정보를 저장할 멤버 변수
    // SharedPreferences를 사용하기 위한 멤버 변수
    private static final String RECOMMENDED_BOOK_KEY = "recommended_book";
    private SharedPreferences sharedPreferences;
    // DailyTaskReceiver로부터 전달받을 BroadcastReciver 객체임.
    private BroadcastReceiver refreshReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recommend);
        // 이전 액티비티로부터 선택한 카테고리들을 DB 로부터 가져온다.
        List<String> selectedUserCategory = getSelectedCategoriesFromDB();
        // sharedPreferences 객체 생성
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.contains(RECOMMENDED_BOOK_KEY)) {
            selectedBook = getDailyRecommendedBook();
            displayBookInformation(selectedBook);
            // 최초 실행일 경우 아래 코드가 실행된 후 SharedPreference에 저장한다.
        } else {
            // 유저가 선택한 카테고리 중 하나를 랜덤으로 선택한다.
            Random random = new Random();
            String selectedCategory = selectedUserCategory.get(random.nextInt(selectedUserCategory.size()));
            // API 요청 수행 메소드
            requestBookRecommendation(selectedCategory);
        }

        // BroadcastReceiver 등록 후, REFRESH_BOOK_DISPLAY 액션 수신.
        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshBookDisplay();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                refreshReceiver,
                new IntentFilter("REFRESH_BOOK_DISPLAY"));

        /* 이하는 버튼 이벤트 처리 부분. */
        TextView detailTextBtn = findViewById(R.id.detailTextBtn);
        detailTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookDetailDialog(selectedBook);
            }
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBookDisplay();
            }
        });
        // 자정에 책을 새로 받도록 예약.
        scheduleDailyTask();
    }

    /*  수신자가 제대로 등록 및 해제 되도록 onDestroy 메소드 내부에서
    *   액티비티가 꺼지며 같이 지워준다.*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // BroadcastReceiver 등록 해제
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(refreshReceiver);
    }

    // 선택된 책 정보를 SharedPreferences에 저장한다.
    private void saveDailyRecommendedBook(BookItem bookItem) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bookItem);
        editor.putString(RECOMMENDED_BOOK_KEY, json);
        editor.apply();
    }

    // 저장된 책 정보를 불러오는 메소드
    private BookItem getDailyRecommendedBook() {
        String json = sharedPreferences.getString(RECOMMENDED_BOOK_KEY, null);
        if(json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, BookItem.class);
        }
        return null;
    }
    // DB에 저장된 카테고리들을 가져오는 메소드.
    private List<String> getSelectedCategoriesFromDB() {
        todayBookDB db = new todayBookDB(this);
        List<String> selectedCategories = db.getAllCategories();
        db.close();
        return selectedCategories;
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
                100);               // 키워드로 검색한 책 50개 중, 하나 랜덤으로 찾아서 검색.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
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
        if (bookSearchResponse != null && bookSearchResponse.getItems() != null &&
                !bookSearchResponse.getItems().isEmpty()) {
            List<BookItem> bookItems = bookSearchResponse.getItems();
            // 추천된 책 중 하나를 랜덤하게 선택한다.
            Random random = new Random();
            selectedBook = bookItems.get(random.nextInt(bookItems.size()));
            displayBookInformation(selectedBook);
            saveDailyRecommendedBook(selectedBook);
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

        bookTitleTextView.setText("\"" + getAPIBookTitle + "\"");
    }

    // 파싱한 데이터들을 상세정보 화면에 넘겨 보여주는 메소드
    private void showBookDetailDialog(BookItem bookItem) {
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

    // 새로고침 버튼 눌렀을 때 다른 책을 추천해주는 메소드
    protected void refreshBookDisplay() {
        // 이전에 선택된 카테고리들을 가져온다.
        List<String> selectedUserCategory = getSelectedCategoriesFromDB();
        // 유저가 선택한 카테고리 중 하나를 랜덤으로 선택한다.
        Random random = new Random();
        String selectedCategory = selectedUserCategory.get(random.nextInt(selectedUserCategory.size()));
        // API 요청 수행 메소드
        requestBookRecommendation(selectedCategory);
    }

    /* 매일 작업을 예약하는 메소드
    *  이 메소드를 통해 하루마다 추천할 책을 바꿔준다. */
    private void scheduleDailyTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 작업이 실행될 시간을 자정으로 지정한다.
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 0시
        calendar.set(Calendar.MINUTE, 0); // 0분
        calendar.set(Calendar.SECOND, 0); // 0초

        // 작업 처리할 클래스 지정(DailyTaskReceiver)
        Intent intent = new Intent(this, DailyTaskReceiver.class);
        // getBroadcast 메소드로 리시버 실행할 PendingIntent 가져오기
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE);

        /* 작업을 예약하는 코드.
        *  WAKEUP을 통해 맞춰둔 시간에 작업이 실행이 되어 시스템을 깨운다. */
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }
}