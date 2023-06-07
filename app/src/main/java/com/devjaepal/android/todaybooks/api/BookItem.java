package com.devjaepal.android.todaybooks.api;

import com.google.gson.annotations.SerializedName;

// 파싱해서 가져올 책에 대한 데이터 정보 담을 클래스
public class BookItem {
        @SerializedName("image")
        private String image;

        @SerializedName("title")
        private String title;

        @SerializedName("link")
        private String link;

        @SerializedName("author")
        private String author;

        @SerializedName("publisher")
        private String publisher;

        @SerializedName("description")
        private String description;


    public String getImageUrl() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getAuthor() {
            return author;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDescription() {
            return description;
        }

    public void setImageUrl(String imageUrl) {
        this.image = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
