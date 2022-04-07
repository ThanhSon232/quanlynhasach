package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.fragment.bookDetailFragment;
import com.example.quanlynhasach.model.bookModel;
import com.squareup.picasso.Picasso;

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
        Picasso.get().load(books.get(position).getHinhAnh()).into(holder.image);
        holder.whole_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name",books.get(holder.getAdapterPosition()).getTenSach());
                bundle.putString("type",books.get(holder.getAdapterPosition()).getTheLoai());
                bundle.putString("author",books.get(holder.getAdapterPosition()).getTacGia());
                bundle.putInt("quantity",books.get(holder.getAdapterPosition()).getSoLuongConLai());
                bundle.putInt("price",books.get(holder.getAdapterPosition()).getDonGia());
                bundle.putString("image",books.get(holder.getAdapterPosition()).getHinhAnh());
                bundle.putString("id",books.get(holder.getAdapterPosition()).getMaSach());
                bookDetailFragment bookDetailFragment = new bookDetailFragment();
                bookDetailFragment.setArguments(bundle);
                ((AppCompatActivity)context).findViewById(R.id.appbar).setVisibility(View.GONE);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, bookDetailFragment).addToBackStack("bookDetailFragment")
                        .commit();




            }
        });
    }

    class bookViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView type;
        TextView author;
        TextView price;
        TextView quantity;
        ImageView image;
        RelativeLayout whole_book;
        public bookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            author = itemView.findViewById(R.id.author);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            image = itemView.findViewById(R.id.picture);
            whole_book = itemView.findViewById(R.id.whole_book);
        }
    }
}
