package com.example.quanlynhasach.fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.example.quanlynhasach.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.example.quanlynhasach.model.*;

import java.util.UUID;

public class addNewBook extends Fragment implements View.OnClickListener {
    Toolbar appbar;
    ImageButton previous;
    ImageButton check;
    ImageView avatar;
    Bundle bundle;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText name;
    EditText type;
    EditText author;
    EditText price;
    EditText quantity;
    String sUrl;
    bookModel model;
    EditText bookID;
    Button random;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_book, container, false);
       appbar = getActivity().findViewById(R.id.appbar);
       appbar.setVisibility(View.GONE);
       previous = view.findViewById(R.id.previous);
       previous.setOnClickListener(this);
       check = view.findViewById(R.id.edit_ok);
       check.setOnClickListener(this);
       avatar = view.findViewById(R.id.avatar);
       bundle = this.getArguments();
       storage = FirebaseStorage.getInstance();
       storageReference = storage.getReference();
       name = view.findViewById(R.id.name);
       type = view.findViewById(R.id.type);
       author = view.findViewById(R.id.author);
       price = view.findViewById(R.id.price);
       quantity = view.findViewById(R.id.quantity);
       bookID = view.findViewById(R.id.bookID);
       random = view.findViewById(R.id.random);
       random.setOnClickListener(this);
       database = FirebaseDatabase.getInstance("https://quanlynhasach-c1a4c-default-rtdb.asia-southeast1.firebasedatabase.app/");
       setAvatar();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack("addNewBook", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                appbar.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_ok:
                String idC = bookID.getText().toString();
                String nameC = name.getText().toString();
                String typeC = type.getText().toString();
                String authorC = author.getText().toString();
                String priceC = price.getText().toString();
                String quantityC = quantity.getText().toString();
                if(TextUtils.isEmpty(nameC)) {
                    name.setError("Name cannot be empty");
                    name.requestFocus();
                }
                else if(TextUtils.isEmpty(idC)) {
                    bookID.setError("Name cannot be empty");
                    bookID.requestFocus();
                }
                else if(TextUtils.isEmpty(typeC)) {
                    type.setError("Type cannot be empty");
                    type.requestFocus();
                } else if(TextUtils.isEmpty(authorC)) {
                    author.setError("Author cannot be empty");
                    author.requestFocus();
                } else if(TextUtils.isEmpty(priceC)) {
                price.setError("Price cannot be empty");
                price.requestFocus();
            } else if(TextUtils.isEmpty(quantityC)) {
                quantity.setError("Quantity cannot be empty");
                quantity.requestFocus();
            }
                else {
                    uploadPicture();
                    model = new bookModel(idC,nameC,typeC,authorC,Integer.valueOf(priceC),Integer.valueOf(quantityC),sUrl);
                    myRef = database.getReference().child("books/");
                    myRef.child(model.getMaSach()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack("addNewBook", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            appbar.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
            case R.id.random:
                bookID.setText(UUID.randomUUID().toString());
                break;
        }
    }

    void setAvatar(){
        Uri selectedImage = bundle.getParcelable("data");
        System.out.println(selectedImage);
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContext().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        avatar.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    void uploadPicture(){
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading image...");
        pd.show();
        String randomKey = UUID.randomUUID().toString();
//        Uri file = Uri.fromFile(bundle.getParcelable("data"));
        StorageReference riversRef = storageReference.child("images/"+randomKey);

        riversRef.putFile(bundle.getParcelable("data")).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri downloadUrl = uri;
                        sUrl = downloadUrl.toString();
                        myRef = database.getReference().child("books/");
                        myRef.child(model.getMaSach()+"/hinhAnh").setValue(sUrl);
                    }
                } ).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                } );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(),"Failed to upload",Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00*snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                if((int)progressPercent == 100){
                    pd.dismiss();
                }
                pd.setMessage("Percentage: "+(int)progressPercent+"%");
            }
        });
    }
}