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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static Pattern aadhaarPattern = Pattern.compile("^[2-9]{1}[0-9]{11}$");
    public static boolean isValidAadharNum(String name)
    {
        Matcher matcher = aadhaarPattern.matcher(name);
        return matcher.find();
    }
    static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public static boolean isValidPassword(String password)
    {

        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";


        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

    public static boolean isValidatePhoneNumber(String phoneNumber) {
        // validate phone numbers of format "1234567890"
        if (phoneNumber.matches("\\d{10}"))
            return true;
            // validating phone number with -, . or spaces
        else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
            // validating phone number with extension length from 3 to 5
        else if (phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
            // validating phone number where area code is in braces ()
        else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
            // Validation for India numbers
        else if (phoneNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
            return true;
        else if (phoneNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}"))
            return true;

        else if (phoneNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
            return true;
            // return false if nothing matches the input
        else
            return false;

    }

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
        if((email.equals("")) || (phone.equals("")) || (aadhar.equals("")) || (password.equals("")) || (confirm_pass.equals(""))){
            Toast.makeText(this,"One or More Empty Field!",Toast. LENGTH_SHORT).show();
        }
        else if(!(isEmailValid(email))){
            Toast.makeText(this,"Email Id Not Valid",Toast. LENGTH_SHORT).show();
        }
        else if (c.moveToFirst()) {
           Toast.makeText(this,"Email id exists",Toast. LENGTH_SHORT).show();
        }

        else if(!isValidAadharNum(aadhar)){
            Toast.makeText(this,"Aadhar Number Not Valid",Toast. LENGTH_SHORT).show();
        }
        else if(!isValidatePhoneNumber(phone)){
            Toast.makeText(this,"Phone Number Not Valid",Toast. LENGTH_SHORT).show();
        }

        else if(!isValidPassword(password)){
            Toast.makeText(this,"Password must contain 1 uppercase,1 lowercase,1 digit , 1 special character and length should be 8-20 characters",Toast. LENGTH_LONG).show();
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