package com.devjaepal.android.todaybooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.devjaepal.android.todaybooks.BookRecommendActivity;

// BroadcastReceiver를 상속받은 클래스이다.
// 이 클래스를 통해 매일 작업을 트리거한다.
public class DailyTaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 매일 작업을 실행해서 새로운 추천 도서를 갱신함.
        ((BookRecommendActivity) context).refreshBookDisplay();
    }
}
