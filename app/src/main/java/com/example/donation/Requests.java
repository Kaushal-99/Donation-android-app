package com.example.donation;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import android.content.SharedPreferences;
public class Requests extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.requests,container,false);
        RecyclerView requestList=(RecyclerView)view.findViewById(R.id.requests);
        requestList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        requestList.setLayoutManager(new LinearLayoutManager(getContext()));
        DB_handling db=new DB_handling(getContext());
        SQLiteDatabase db1=db.getReadableDatabase();
        ArrayList<String> ngo_names=new ArrayList<>();
        ArrayList<String> titles=new ArrayList<>();
        ArrayList<String> status=new ArrayList<>();
        ArrayList<String> items_list=new ArrayList<>();
        ArrayList<String[]> allval=new ArrayList<String[]>();
        ArrayList<Integer> postId=new ArrayList<>();
        SharedPreferences sharedPreferences;
        sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String user_email = sharedPreferences.getString("EMAIL", "");
        String user_email_to_compare='"'+user_email+'"';
        Cursor res =  db1.rawQuery( "select * from user_request where donor_email="+user_email_to_compare, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            postId.add(-1);
            titles.add(res.getString(2));
            ngo_names.add(res.getString(4));
            status.add(res.getString(6));
            items_list.add(res.getString(5));
            allval.add(new String[]{res.getString(2), res.getString(4), res.getString(6), res.getString(5),res.getString(7)});
            res.moveToNext();
        }
        Cursor result =  db1.rawQuery( "select * from post_user_request where donor_email="+user_email_to_compare, null );
        result.moveToFirst();
        while(result.isAfterLast() == false){
            postId.add(result.getInt(1));
            titles.add(result.getString(4));
            ngo_names.add(result.getString(2));
            status.add(result.getString(7));
            items_list.add(result.getString(5));
            allval.add(new String[]{result.getString(4), result.getString(2), result.getString(7), result.getString(5),result.getString(1),result.getString(8)});
            result.moveToNext();
        }
        Collections.sort(allval, new DateComparator());
        Collections.reverse(allval);
        requestList.setAdapter(new RequestAdapter(ngo_names,titles,status,items_list,postId,allval));
        return view;
    }
}
class DateComparator implements Comparator<String []>{
    @Override
    public int compare(String[] o1, String[] o2) {
        return o1[o1.length-1].compareTo(o2[o2.length-1]);
    }
}
