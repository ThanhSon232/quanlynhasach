package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlynhasach.R;
import com.squareup.picasso.Picasso;

public class bookDetailFragment extends Fragment {
    ImageButton previous;
    ImageView detailPicture;
    TextView detailName;
    TextView detailPrice;
    TextView detailQuantity;
    TextView detailAuthor;
    TextView detailType;
    Bundle bundle = new Bundle();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_book_detail, container, false);
        detailPicture = view.findViewById(R.id.picture);
        detailName = view.findViewById(R.id.detailName);
        detailType = view.findViewById(R.id.detailType);
        detailAuthor = view.findViewById(R.id.detailAuthor);
        detailQuantity = view.findViewById(R.id.detailQuantity);
        detailPrice = view.findViewById(R.id.detailPrice);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.appbar).setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack("bookDetailFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        bundle = this.getArguments();
        getData();
        return view;
    }

    void getData(){
        Picasso.get().load(bundle.getString("image")).into(detailPicture);
        detailName.setText(bundle.getString("name"));
        detailType.setText(bundle.getString("type"));
        detailQuantity.setText(bundle.getInt("quantity") + "");
        detailPrice.setText(bundle.getInt("price") + "");
        detailAuthor.setText(bundle.getString("author"));
    }


}