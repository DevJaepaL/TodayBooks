package com.devjaepal.android.todaybooks.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devjaepal.android.todaybooks.API.BookItem;
import com.devjaepal.android.todaybooks.DB.todayBookDB;
import com.devjaepal.android.todaybooks.R;

import java.util.List;

public class LikeBooksListAdapter extends RecyclerView.Adapter<LikeBooksListAdapter.ViewHolder> {
    private Context context;
    private List<BookItem> likedBooksList;
    private BookItemListener bookItemListener;

    public void setBookItemListener(BookItemListener listener) {
        this.bookItemListener = listener;
    }

    public LikeBooksListAdapter(Context context, List<BookItem> likedBooksList) {
        this.context = context;
        this.likedBooksList = likedBooksList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int currentPositon = holder.getAdapterPosition();
        BookItem bookItem = likedBooksList.get(currentPositon);

        holder.titleTextView.setText(bookItem.getTitle());
        holder.userBookMemoTextView.setText(getLikeBookUserMemo(bookItem.getTitle()));

        Glide.with(context)
                .load(bookItem.getImageUrl())
                .into(holder.bookImageView);

        // 좋아하는 책을 길게(롱 클릭) 눌렀을 경우 나오는 기능들을 호출하는 메소드
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (bookItemListener != null) {
                    // 3 가지의 선택항목이 나오는 다이얼로그 생성(메모, 삭제)
                    showBookSettingDialog(currentPositon, bookItem);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return likedBooksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView bookImageView;
        public TextView titleTextView;
        public TextView userBookMemoTextView; // 메모 표시할 텍스트 뷰

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            userBookMemoTextView = itemView.findViewById(R.id.userBookMemoTextView);
        }
    }

    // 롱 클릭 시 해당 책을 삭제,메모,별점 부여하는 기능을 위한 인터페이스
    public interface BookItemListener {
        void onBookDelete(int position, BookItem bookItem);

        void onBookMemo(int position, BookItem bookItem);
        void showLikeBooksDetailDialog(BookItem bookItem);
    }

    private String getLikeBookUserMemo(String bookTitle) {
        todayBookDB db = new todayBookDB(context);
        return db.getLikeBookUserMemo(bookTitle);
    }

    private void showBookSettingDialog(int position, BookItem bookItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("좋아하는 책 설정")
                .setItems(new CharSequence[]{"메모 하기", "책 상세보기" , "좋아하는 목록에서 지우기"},
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 메모 기능
                                if(bookItemListener != null) {
                                    bookItemListener.onBookMemo(position, bookItem);
                                }
                                break;
                            case 1: // 책 상세보기 기능
                                if(bookItemListener != null) {
                                    bookItemListener.showLikeBooksDetailDialog(bookItem);
                                }
                                break;
                            case 2: // 목록에서 삭제 기능
                                if(bookItemListener != null) {
                                    bookItemListener.onBookDelete(position, bookItem);
                                }
                                break;
                        }
                    }
                }).setNegativeButton("취 소", null)
                    .show();
    }
}