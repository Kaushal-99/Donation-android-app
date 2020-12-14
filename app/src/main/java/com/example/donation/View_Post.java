package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class View_Post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view__post);
        TextView ngo_name,subject_tv,requirements;
        ImageView image;
        ngo_name=(TextView)findViewById(R.id.ngo_name);
        subject_tv=(TextView)findViewById(R.id.title);
        requirements=(TextView)findViewById(R.id.requirement_type);
        image=findViewById(R.id.post_image);
        DB_handling db=new DB_handling(this);
        SQLiteDatabase db1=db.getReadableDatabase();
        Intent intent=getIntent();
        String post_id=String.valueOf(Integer.parseInt(intent.getStringExtra("post id")));
        Cursor res =  db1.rawQuery( "select p.subject,p.image,p.requirement,n.ngo_name from post p inner join ngo n where p.email=n.email_id and p.id="+post_id, null );
        res.moveToFirst();
        ngo_name.setText(res.getString(3));
        subject_tv.setText(res.getString(0));
        requirements.setText(res.getString(2));
        Bitmap bmp = BitmapFactory.decodeByteArray(res.getBlob(1), 0, res.getBlob(1).length);
        image.setImageBitmap(bmp);
    }
    public void goBack(View view){
        onBackPressed();
    }
}