package com.example.quanlynhasach.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quanlynhasach.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class registerFragment extends Fragment implements View.OnClickListener{
    ImageButton backButton;
    Button loginButton;
    Button registerButton;
    TextInputEditText emailC;
    TextInputEditText passwordC;
    TextInputEditText cPasswordC;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        backButton =  view.findViewById(R.id.previous);
        backButton.setOnClickListener(this);
        loginButton = view.findViewById(R.id.login);
        loginButton.setOnClickListener(this);
        registerButton = view.findViewById(R.id.register);
        registerButton.setOnClickListener(this);
        emailC =   view.findViewById(R.id.email);
        passwordC =  view.findViewById(R.id.registerPassword);
        cPasswordC =  view.findViewById(R.id.confirmPassword);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                createAccount();
                break;
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack("registerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }

    void createAccount(){
        String email = emailC.getText().toString();
        String password = passwordC.getText().toString();
        String confirmPassword = cPasswordC.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailC.setError("Email cannot be empty");
            emailC.requestFocus();
        }
        else
        if(TextUtils.isEmpty(password)){
            passwordC.setError("Password cannot be empty");
            passwordC.requestFocus();
        }
        else
        if(TextUtils.isEmpty(confirmPassword)){
            cPasswordC.setError("Password cannot be empty");
            cPasswordC.requestFocus();
        }
        else

        if(!TextUtils.equals(password,confirmPassword)){
            cPasswordC.setError("Confirmed password doesn't match");
            cPasswordC.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,confirmPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Successful",Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack("registerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    else{
                        Toast.makeText(getContext(),"Registration error: "+ task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}