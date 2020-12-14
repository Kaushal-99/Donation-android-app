package com.example.donation;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class Requests_NGO extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View requests_ngo_obj = inflater.inflate(R.layout.requests_ngo, container, false);
        RecyclerView requestList=(RecyclerView)requests_ngo_obj.findViewById(R.id.requests_ngo);
        requestList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        requestList.setLayoutManager(new LinearLayoutManager(getContext()));
        DB_handling db=new DB_handling(getContext());
        SQLiteDatabase db1=db.getReadableDatabase();
        Log.i("NGO","Enyer ngo request page");
        ArrayList<String> donor_names=new ArrayList<>();
        ArrayList<String> subject=new ArrayList<>();
        ArrayList<String> items=new ArrayList<>();
        ArrayList<String> post=new ArrayList<>();
        ArrayList<String> user_req=new ArrayList<>();
        ArrayList<String> donor_email=new ArrayList<>();
        SharedPreferences sharedPreferences;
        sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String user_email = sharedPreferences.getString("EMAIL", "");
        String user_email_to_compare='"'+user_email+'"';
        Log.i("email",user_email);
        Cursor res =  db1.rawQuery( "select r.name,u.subject,u.items,u.req_id,u.donor_email from user_request u inner join Register r where r.email=u.donor_email and u.status='pending' and ngo_email="+user_email_to_compare, null );
        if(res!=null)
            res.moveToFirst();
        while(res.isAfterLast() == false){
            subject.add(res.getString(1));
            donor_names.add(res.getString(0));
            items.add(res.getString(2));
            post.add(" ");
            user_req.add(res.getString(3));
            donor_email.add(res.getString(4));
            res.moveToNext();
        }
        Cursor result =  db1.rawQuery( "select r.name,u.subject,u.items,u.post_id,u.donor_email from post_user_request u inner join Register r where r.email=u.donor_email and u.status='pending' and ngo_email="+user_email_to_compare, null );
        if(result!=null)
            result.moveToFirst();
        while(result.isAfterLast() == false){
            subject.add(result.getString(1));
            donor_names.add(result.getString(0));
            items.add(result.getString(2));
            post.add(String.valueOf(result.getInt(3)));
            user_req.add(" ");
            donor_email.add(result.getString(4));
            result.moveToNext();
        }
        RequestNGOAdapter madapter=new RequestNGOAdapter(donor_names,subject,items,post,user_req,donor_email);
        requestList.setAdapter(madapter);
        return requests_ngo_obj;
    }
}
