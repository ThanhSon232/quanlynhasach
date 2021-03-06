package com.example.quanlynhasach.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.model.bookModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class bookInNoteAdapter extends RecyclerView.Adapter<bookInNoteAdapter.bookViewHolder>{
    ArrayList<bookModel> books;
    Context context;

    public ArrayList<bookModel> getBooks() {
        return books;
    }

    public bookInNoteAdapter(ArrayList<bookModel> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public bookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_received_note,parent,false);
        return new bookInNoteAdapter.bookViewHolder(view);
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
        holder.quantity.setText(books.get(position).getSoLuongNhap().toString());
        Picasso.get().load(books.get(position).getHinhAnh()).into(holder.image);
        holder.whole_book.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                books.remove(books.get(holder.getAdapterPosition()));
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("B???n c?? mu???n x??a?").setPositiveButton("C??", dialogClickListener)
                        .setNegativeButton("Kh??ng", dialogClickListener).show();
                return true;
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nh???p s??? l?????ng m???i");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        books.get(holder.getAdapterPosition()).setSoLuongNhap(Integer.valueOf(input.getText().toString()));
                        holder.quantity.setText(books.get(holder.getAdapterPosition()).getSoLuongNhap().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
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
        Button update;
        public bookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            author = itemView.findViewById(R.id.author);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            image = itemView.findViewById(R.id.picture);
            whole_book = itemView.findViewById(R.id.whole_book);
            update = itemView.findViewById(R.id.update);
        }
    }
}
