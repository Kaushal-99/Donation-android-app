package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DonationForm extends AppCompatActivity {
    String ngo_name,ngo_email;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_form);
        TextView NGOName;
        NGOName=(TextView)findViewById(R.id.NGO_name);
        Intent in=getIntent();
        ngo_name=in.getStringExtra("NGO Name");
        NGOName.setText(ngo_name);
        String i=in.getStringExtra("index");
        ngo_email=in.getStringExtra("ngo email");
        index=Integer.parseInt(i);
    }
    public void goBack(View view){
        onBackPressed();
    }
    public void generateDonationRequest(View view){
        String items,subject;
        EditText subject_id=(EditText)findViewById(R.id.subject);
        EditText items_id=(EditText)findViewById(R.id.items_donated);
        subject=subject_id.getText().toString();
        items=items_id.getText().toString();
        DB_handling db=new DB_handling(this);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String user_email = prefs.getString("EMAIL", "");
        //Log.i("email",user_email);
        if(db.insertUserRequest(ngo_email,ngo_name,subject,items,user_email))
            Toast.makeText(this,"Request submitted successfully",Toast. LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Error",Toast. LENGTH_SHORT).show();
        onBackPressed();
    }
}