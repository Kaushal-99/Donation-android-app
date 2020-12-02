package com.example.donation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public void checkUser(View view){
        String email,password;
        EditText email_id=(EditText)findViewById(R.id.email);
        EditText password_id=(EditText)findViewById(R.id.password);
        email=email_id.getText().toString();
        password=password_id.getText().toString();
        if(isEmailValid(email)){
            DB_handling dbh=new DB_handling(this);
            SQLiteDatabase db = dbh.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM Login WHERE email='"
                            + email + "'" +
                            "AND password='"+password+"'"
                    , null);
            if (c.moveToFirst()) {
                String loginEmail = c.getString(0);
                Log.i("email",loginEmail);
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("EMAIL", email);
                editor.commit();
                if(c.getString(2).equalsIgnoreCase("donor")){
                    Intent user_page=new Intent(this,user_homepage.class);
                    startActivity(user_page);
                }
                else if(c.getString(2).equalsIgnoreCase("ngo")){
                    Intent ngo_page=new Intent(this,NGO_homepage.class);
                    startActivity(ngo_page);
                }


        }
            else{
                Toast.makeText(this,"Invalid email or password",Toast. LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Invalid email",Toast. LENGTH_SHORT).show();
        }
    }

}
