package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.bookModel;

import java.util.ArrayList;

public class bookAdapter extends RecyclerView.Adapter<bookAdapter.bookViewHolder> {
    ArrayList<bookModel> books;
    Context context;

    public bookAdapter(ArrayList<bookModel> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public bookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book,parent,false);
        return new bookViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public void onBindViewHolder(@NonNull bookViewHolder holder, int position) {
        if(books.isEmpty()) return;
        holder.title.setText(books.get(position).getTenSach());
        holder.type.setText(books.get(position).getTheLoai());
        holder.author.setText(books.get(position).getTacGia());
        holder.price.setText(books.get(position).getDonGia().toString());
        holder.quantity.setText(books.get(position).getSoLuongConLai().toString());
        Picasso
    }

    class bookViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView type;
        TextView author;
        TextView price;
        TextView quantity;
        ImageView image;
        public bookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            author = itemView.findViewById(R.id.author);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            image = itemView.findViewById(R.id.avatar);
        }
    }
}
