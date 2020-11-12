package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Post_Form extends AppCompatActivity {
    String ngo_name,subject,requirement_type;
    int index;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post__form);
        TextView NGOName,Subject;
        NGOName=(TextView)findViewById(R.id.NGO_name);
        Subject=(TextView)findViewById(R.id.subject) ;
        button=(Button)findViewById(R.id.post_request_submit_btn);
        Intent in=getIntent();
        ngo_name=in.getStringExtra("NGO Name");
        NGOName.setText(ngo_name);
        String i=in.getStringExtra("index");
        index=Integer.parseInt(i);
        subject=in.getStringExtra("subject");
        requirement_type=in.getStringExtra("req_type");
        Subject.setText(subject);
        if(requirement_type.equalsIgnoreCase("Donation")){
            button.setText("Donate");
        }
        else{
            button.setText("Volunteer");
        }
    }
    public void goBack(View view){
        onBackPressed();
    }
    public void generateRequest(View view){
        String items;
        EditText items_id=(EditText)findViewById(R.id.items_donated);
        items=items_id.getText().toString();
        DB_handling db=new DB_handling(this);
        SQLiteDatabase db1=db.getReadableDatabase();
        Cursor query=db1.rawQuery("Select email_id from ngo where ngo_name="+"'"+ngo_name+"'",null);
        query.moveToFirst();
        String ngo_email= query.getString(0);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String user_email = prefs.getString("EMAIL", "");
        //Log.i("email",user_email);
        if(db.insertPostUserRequest(index,ngo_name,ngo_email,subject,items,user_email))
            Toast.makeText(this,"Request submitted successfully",Toast. LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Error",Toast. LENGTH_SHORT).show();
        onBackPressed();
    }
}