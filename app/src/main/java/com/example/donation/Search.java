package com.example.donation;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Search extends Fragment{
    SearchView searchView;
    ListView ngolist;
    String [] ngo_list=new String[10];
    ArrayAdapter<String> ngo_adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View search=inflater.inflate(R.layout.search,container,false);

        searchView=search.findViewById(R.id.searchNGO);
        ngolist=search.findViewById(R.id.ngolist);
        DB_handling db=new DB_handling(getContext());
        SQLiteDatabase db1 = db.getReadableDatabase();
        Cursor res =  db1.rawQuery( "select * from ngo", null );
        res.moveToFirst();
        int i=0;
        while(res.isAfterLast() == false){
            String s=res.getString(2);
            ngo_list[i]=s;
            i++;
            res.moveToNext();
        }
        ngo_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,ngo_list);
        ngolist.setAdapter(ngo_adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ngo_adapter.getFilter().filter(s);
                return false;
            }
        });
        ngolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList =(String) (ngolist.getItemAtPosition(i));
                Intent in=new Intent(getContext(),NGOData.class);
                in.putExtra("NGO Name",selectedFromList);
                in.putExtra("index",String.valueOf(i));

                startActivity(in);
            }
        });
        return search;
    }


}
