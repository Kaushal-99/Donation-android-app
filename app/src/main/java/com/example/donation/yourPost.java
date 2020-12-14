package com.example.donation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class yourPost extends AppCompatActivity {
    String user_email;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_post);
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        user_email = sharedPreferences.getString("EMAIL", "");
        RecyclerView posts=(RecyclerView)this.findViewById(R.id.requests);
        posts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        posts.setLayoutManager(new LinearLayoutManager(this));
        DB_handling db=new DB_handling(this);
        SQLiteDatabase db1=db.getReadableDatabase();
        ArrayList<String> subject=new ArrayList<>();
        ArrayList<String> requirement=new ArrayList<>();
        ArrayList<String> requirement_type=new ArrayList<>();
        ArrayList<String> postStatus=new ArrayList<>();
        ArrayList<String> donorMail=new ArrayList<>();
        ArrayList<String> postCount=new ArrayList<>();
        ArrayList<Integer> postId=new ArrayList<>();
        ArrayList<byte[]> images=new ArrayList<>();

        Cursor result =  db1.rawQuery( "SELECT post.id,post.subject,post.requirement_type,post.requirement,post.image,post_user_request.donor_email,post.isCompleted\n" +
                "FROM post\n" +
                "LEFT JOIN post_user_request ON post.id=post_user_request.post_id where post.email="+"'"+user_email+"'" +
                "ORDER BY post.id DESC", null );
        result.moveToFirst();
        while(!result.isAfterLast()){
            subject.add(result.getString(1));
            requirement_type.add(result.getString(2));
            requirement.add(result.getString(3));
            images.add(result.getBlob(4));
            donorMail.add(result.getString(5));
            postStatus.add(result.getString(6));
            postCount.add(db.getPostCount(result.getInt(0)));
            postId.add(result.getInt(0));
            result.moveToNext();
        }

        posts.setAdapter(new YourPostAdapter(subject,requirement_type,requirement,postStatus,postCount,donorMail,images,postId));
    }
    public void goBack(View view){
        onBackPressed();
    }
}