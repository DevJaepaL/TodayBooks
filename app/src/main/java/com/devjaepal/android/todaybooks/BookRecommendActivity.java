package com.devjaepal.android.todaybooks;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.devjaepal.android.todaybooks.api.APIClient;
import com.devjaepal.android.todaybooks.api.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRecommendActivity extends AppCompatActivity {

    private static final String MY_CLIENT_ID = "v8_2r1fSN3Zq0pmCwv_E";
    private static final String MY_CLIENT_SECRET = "FWD5ZbhFXF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_recommend);
        testAPI();
    }

    private void testAPI() {
        APIInterface apiInterface = APIClient.getInstance().create(APIInterface.class);
        Call<String> call = apiInterface.getBookSearchResult(
                MY_CLIENT_ID,
                MY_CLIENT_SECRET,
                "book.json",
                "니세코이");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    Log.e(TAG, "성공 : " + result);
                } else {
                    Log.e(TAG,"실패 + " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "에러 : " + t.getMessage());
            }
        });
    }
}