package com.example.app;
import com.example.app.model.*;
import com.example.app.adapter.*;
import com.example.app.network.*;
import com.example.app.view.*;
import com.example.app.ml.*;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register_page extends AppCompatActivity {
    Button btn;
    EditText user,pass,email,phone;

FirebaseAuth firebaseAuth;
    Spinner spinner;
    String[] Country={"Select country","India","UK","US","canada","China"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        user=findViewById(R.id.editTextText);
        pass=findViewById(R.id.editTextText3);
        email=findViewById(R.id.editTextText2);
        phone=findViewById(R.id.editTextText4);
        btn=findViewById(R.id.button2);
        firebaseAuth=FirebaseAuth.getInstance();
        spinner=findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter=new ArrayAdapter(register_page.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,Country);
        spinner.setAdapter(arrayAdapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString();

                String firstName = user.getText().toString();
                String phoneNumber = phone.getText().toString();
                String password = pass.getText().toString();
                firebaseAuth = FirebaseAuth.getInstance();
                if (firstName.isEmpty()) {
                    user.setError("Please enter user name");
                    user.requestFocus();
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    phone.setError("Please enter phone no");
                    phone.requestFocus();
                    return;
                }
                if (mEmail.isEmpty()) {
                    email.setError("Please enter email");
                    email.requestFocus();

                }
                if (password.isEmpty()) {
                    pass.setError("Please enter password");
                    pass.requestFocus();
                }


                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(register_page.this, "Register Successfully...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(register_page.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }
}