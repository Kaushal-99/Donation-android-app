package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void userRegistration(View view){
        SharedPreferences sharedpreferences;
        EditText name_id=(EditText)findViewById(R.id.name);
        EditText email_id=(EditText)findViewById(R.id.email);
        EditText phone_id=(EditText)findViewById(R.id.phone);
        EditText aadhar_id=(EditText)findViewById(R.id.aadhar);
        EditText password_id=(EditText)findViewById(R.id.password);
        EditText confirm_pass_id=(EditText)findViewById(R.id.confirm_password);
        Intent log=new Intent(MainActivity.this,LoginActivity.class);
        String name,email,phone,aadhar,password,confirm_pass;
        name=name_id.getText().toString();
        email=email_id.getText().toString();
        phone=phone_id.getText().toString();
        aadhar=aadhar_id.getText().toString();
        password=password_id.getText().toString();
        confirm_pass=confirm_pass_id.getText().toString();


        DB_handling db=new DB_handling(this);
        SQLiteDatabase dbi = db.getReadableDatabase();
        Cursor c = dbi.rawQuery("SELECT * FROM Login WHERE email='"
                        + email + "'"
                , null);
        if (c.moveToFirst()) {
           Toast.makeText(this,"Email id exists",Toast. LENGTH_SHORT).show();
        }
        else{
            if(password.equals(confirm_pass)){

                if(db.insertUser(name,email,phone,aadhar,password));
                Toast.makeText(this,"Registered successfully", Toast.LENGTH_SHORT).show();
                Log.i("data inserted","done");
                startActivity(log);
            }
            else{
                Log.i("error","error");
                Log.i(password,confirm_pass);
            }
        }


    }
    public void goToLogin(View view){
        Intent log=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(log);
    }

}