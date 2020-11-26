package com.example.donation;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Feed extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View feed=inflater.inflate(R.layout.feed,container,false);
        RecyclerView posts=(RecyclerView)feed.findViewById(R.id.requests);
        posts.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        posts.setLayoutManager(new LinearLayoutManager(getContext()));
        DB_handling db=new DB_handling(getContext());
        SQLiteDatabase db1=db.getReadableDatabase();
        ArrayList<String> ngo_names=new ArrayList<>();
        ArrayList<String> subject=new ArrayList<>();
        ArrayList<String> requirement=new ArrayList<>();
        ArrayList<String> requirement_type=new ArrayList<>();
        ArrayList<Integer> postId=new ArrayList<>();
        ArrayList<byte[]> images=new ArrayList<>();
        Cursor res =  db1.rawQuery( "select * from post where isCompleted!='Completed' ", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Cursor result =  db1.rawQuery( "select ngo_name from ngo where email_id="+"'"+res.getString(1)+"'", null );
            result.moveToFirst();
            ngo_names.add(result.getString(0));
            subject.add(res.getString(2));
            requirement_type.add(res.getString(3));
            requirement.add(res.getString(4));
            images.add(res.getBlob(6));
            postId.add(res.getInt(0));
            res.moveToNext();
        }
        posts.setAdapter(new PostAdapter(ngo_names,subject,requirement_type,requirement,images,postId));
        return feed;
    }
}
