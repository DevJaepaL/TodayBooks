package com.devjaepal.android.todaybooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.devjaepal.android.todaybooks.BookRecommendActivity;

// BroadcastReceiver를 상속받은 클래스이다.
// 이 클래스를 통해 매일 작업을 트리거한다.
public class DailyTaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 해당 액티비티로 메세지를 보낸다.
        Intent broadcastIntent = new Intent("REFRESH_BOOK_DISPLAY");
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
