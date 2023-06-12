package com.devjaepal.android.todaybooks.API;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BookSearchResponse {
    @SerializedName("total")
    private int total;

    @SerializedName("start")
    private int start;

    @SerializedName("display")
    private int display;

    @SerializedName("items")
    private List<BookItem> items;

    public List<BookItem> getItems() {
        return items;
    }
}