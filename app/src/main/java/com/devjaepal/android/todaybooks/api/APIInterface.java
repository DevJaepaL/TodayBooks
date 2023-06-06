package com.devjaepal.android.todaybooks.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/* 레트로핏 객체를 통해 호출할 추상 메소드 */
public interface APIInterface {
    @GET("search/{type}")
    Call<String> getBookSearchResult(
            @Header("X-Naver-Client-Id") String ClientId,
            @Header("X-Naver-Client-Secret") String secretPwd,
            @Path("type") String type,
            @Query("query") String query
    );
}
