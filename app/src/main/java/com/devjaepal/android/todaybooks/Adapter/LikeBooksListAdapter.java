package com.devjaepal.android.todaybooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devjaepal.android.todaybooks.API.BookItem;
import com.devjaepal.android.todaybooks.R;

import java.util.List;

public class LikeBooksListAdapter extends RecyclerView.Adapter<LikeBooksListAdapter.ViewHolder> {
    private Context context;
    private List<BookItem> likedBooksList;

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
        BookItem bookItem = likedBooksList.get(position);

        holder.titleTextView.setText(bookItem.getTitle());

        Glide.with(context)
                .load(bookItem.getImageUrl())
                .into(holder.bookImageView);
    }

    @Override
    public int getItemCount() {
        return likedBooksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView bookImageView;
        public TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}