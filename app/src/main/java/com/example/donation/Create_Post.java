package com.example.donation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Create_Post extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner ngo_request;
    EditText subject_id;
    EditText items_id;
    byte []image;
    private static final int SELECT_PHOTO = 100;
    String requirement_type;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button b1,b2;
        View create_post = inflater.inflate(R.layout.create_post, container, false);
        subject_id=(EditText)create_post.findViewById(R.id.subject_of_post);
        items_id=(EditText)create_post.findViewById(R.id.items);
        ngo_request=(Spinner)create_post.findViewById(R.id.spinner_ngo_request_type);
        ngo_request.setOnItemSelectedListener(this);
        b1=(Button)create_post.findViewById(R.id.post_btn);
        b2=(Button)create_post.findViewById(R.id.takeImage);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this) ;
        return create_post;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        requirement_type=(String) parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    image=outputStream.toByteArray();
                }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takeImage:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.post_btn:
                String items,subject;
                subject=subject_id.getText().toString();
                items=items_id.getText().toString();
                SharedPreferences prefs;
                prefs= getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String user_email = prefs.getString("EMAIL", "");
                if(requirement_type.equalsIgnoreCase("")){
                    Toast.makeText(getContext(),"Choose a Requirement Type", Toast.LENGTH_SHORT).show();
                }
                else if(subject.equalsIgnoreCase("Choose Requirement Type")){
                    Toast.makeText(getContext(),"Choose a Subject", Toast.LENGTH_SHORT).show();
                }
                else if(items.equalsIgnoreCase("")){
                    Toast.makeText(getContext(),"Choose items", Toast.LENGTH_SHORT).show();
                }
                else if(image==null){
                    Toast.makeText(getContext(),"Choose a Image", Toast.LENGTH_SHORT).show();
                }
                else{
                    DB_handling db=new DB_handling(getContext());
                    if(db.insertPost(subject,items,user_email,requirement_type,image)){

                        subject_id.setText("");
                        items_id.setText("");
                        image=null;
                        Toast.makeText(getContext(),"Post created ",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(),"Post not created ",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
