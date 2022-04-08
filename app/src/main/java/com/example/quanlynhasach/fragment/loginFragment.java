package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.example.quanlynhasach.fragment.registerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginFragment extends Fragment implements View.OnClickListener{
    Button registerButton;
    Button loginButton;
    TextInputEditText email;
    TextInputEditText password;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //registerButton = view.findViewById(R.id.registerButton);
        //registerButton.setOnClickListener(this);
        email = view.findViewById(R.id.emailLogin);
        password = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton:
                authentication();
                break;
            //case R.id.registerButton:
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new registerFragment()).addToBackStack("registerFragment").commit();
        }
    }

    void authentication(){
        String emailC = email.getText().toString();
        String passwordC = password.getText().toString();
        if(TextUtils.isEmpty(emailC)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }
        else
        if(TextUtils.isEmpty(passwordC)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(emailC,passwordC).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Successful",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    else {
                        Toast.makeText(getContext(),"Authentication error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}