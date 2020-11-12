package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static android.graphics.PorterDuff.*;

public class NGOData extends AppCompatActivity {
    String ngo_name,ngo_email;int index;
    String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_data);
        Intent in=getIntent();
        ngo_name=in.getStringExtra("NGO Name");

        String i=in.getStringExtra("index");

        index=Integer.parseInt(i)+1;
       // Log.i("hello",ngo_name);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.black), Mode.SRC_ATOP);
        TextView NGO_name,mobile,email,about_us;
        NGO_name=(TextView) findViewById(R.id.NGO_name);
        about_us=(TextView)findViewById(R.id.about_us) ;
        mobile=(TextView) findViewById(R.id.NGO_mobile_no);
        email=(TextView) findViewById(R.id.NGO_email_id);
        NGO_name.setText(ngo_name);
        DB_handling db1=new DB_handling(this);
        SQLiteDatabase db = db1.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ngo where id="+index, null );
        res.moveToFirst();
        mobile.setText(res.getString(6));
        email.setText(res.getString(1));
        about_us.setText(res.getString(5));
        ngo_email=res.getString(1);
    }
    public void goBack(View view){
        onBackPressed();
    }
    public void raiseDonation(View view){
        Intent form=new Intent(NGOData.this,DonationForm.class);
        form.putExtra("NGO Name",ngo_name);
        form.putExtra("index",String.valueOf(index));
        form.putExtra("ngo email",ngo_email);
        startActivity(form);
    }
}