package com.example.donation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DB_handling extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 16;
    public static final String DATABASE_NAME = "DoNation.db";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String AADHAR = "aadhar";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
    public static final String PROFILEPIC = "profilepic";
    public static final String TYPE = "type";
    public static final String NGO_NAME = "ngo_name";
    public static final String NGOPROFILEPIC = "ngoprofilepic";
    public static final String LOCATION  = "location";
    public static final String CATEGORY= "category";
    public static final String TIME="timestamp";
    //public static final String PHONE = "phone";
    public static final String EMAIL_ID = "email_id";
    public static final String ABOUT="about";
    public static final String SUBJECT = "subject";
    public static final String DONOR_EMAIL= "donor_email";
    public static final String ITEMS = "items";
    public static final String SELECTED_IMAGE="image";
    public static final String REQUIREMENT = "requirement";
    public static final String REQUIREMENT_TYPE="requirement_type";
    public DB_handling(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public boolean insertUser(String name, String email, String phone, String aadhar, String password){
        SQLiteDatabase db1=this.getWritableDatabase();
        ContentValues values_login = new ContentValues();
        values_login.put(EMAIL, email);
        values_login.put(PASSWORD,password);
        values_login.put(TYPE,"Donor");
        // Insert the new row, returning the primary key value of the new row
        db1.insert("Login", null, values_login);
        ContentValues values_reg = new ContentValues();
        values_reg.put(EMAIL, email);
        values_reg.put(NAME,name);
        values_reg.put(PHONE,phone);
        values_reg.put(AADHAR,aadhar);
        values_reg.put(PASSWORD,password);
        // Insert the new row, returning the primary key value of the new row
        db1.insert("Register", null, values_reg);
        return true;
    }
    public boolean insertPost(String subject,String items,String user_email,String requirement_type, byte[]image){
        SQLiteDatabase db1=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, user_email);
        values.put(SUBJECT,subject);
        values.put(REQUIREMENT_TYPE,requirement_type);
        values.put(REQUIREMENT,items);
        values.put(SELECTED_IMAGE,image);
        // Insert the new row, returning the primary key value of the new row
        if(db1.insert("post", null, values)!=0){
            return true;
        }
        return false;
    }

    public boolean insertUserRequest(String ngo_email,String ngo_name,String subject,String items,String user_email){
        SQLiteDatabase db1=this.getWritableDatabase();
        ContentValues values_user_req = new ContentValues();
        values_user_req.put("ngo_email",ngo_email);
        values_user_req.put(SUBJECT, subject);
        values_user_req.put(DONOR_EMAIL,user_email);
        values_user_req.put(NGO_NAME,ngo_name);
        values_user_req.put(ITEMS,items);
        values_user_req.put(STATUS,"pending");
        Cursor res=db1.rawQuery("Select datetime('now','localtime') as time",null);
        res.moveToFirst();
        values_user_req.put(TIME,res.getString(0));
        if(db1.insert("user_request", null, values_user_req)!=0){
            return true;
        }
        else
            return false;
    }
    public boolean insertPostUserRequest(int index,String ngo_name,String ngo_email,String subject,String items,String user_email){
        SQLiteDatabase db1=this.getWritableDatabase();
        ContentValues values_user_req = new ContentValues();
        values_user_req.put("post_id",index);
        values_user_req.put(NGO_NAME,ngo_name);
        values_user_req.put("ngo_email",ngo_email);
        values_user_req.put(SUBJECT, subject);
        values_user_req.put(ITEMS,items);
        values_user_req.put(DONOR_EMAIL,user_email);
        values_user_req.put(STATUS,"pending");
        Cursor res=db1.rawQuery("Select datetime('now','localtime') as time",null);
        res.moveToFirst();
        values_user_req.put(TIME,res.getString(0));
        if(db1.insert("post_user_request", null, values_user_req)!=0){
            return true;
        }
        else
            return false;
    }
    public static void createRegisterTable(SQLiteDatabase db){
        String Table_name = "Register";
        String query="CREATE TABLE "
                + Table_name + "(" + EMAIL + " TEXT PRIMARY KEY ,"
                + NAME + " TEXT, " +
                PHONE+" TEXT," +
                AADHAR+" TEXT," +
                PASSWORD+" TEXT,"+
                 ADDRESS+" TEXT,"+
                PROFILEPIC+" BLOB);";
        db.execSQL(query);
    }
    public static void createLoginTable(SQLiteDatabase db){
        String Table_name = "Login";
        String query="CREATE TABLE "
                + Table_name + "(" + EMAIL + " TEXT PRIMARY KEY ,"
                + PASSWORD+" TEXT,"
                + TYPE+" TEXT );";
        db.execSQL(query);
    }
    public static void createPostTable(SQLiteDatabase db){
        String Table_name = "post";
        String query="CREATE TABLE "
                + Table_name + "( id " + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EMAIL+" TEXT,"
                + SUBJECT+" TEXT,"
                + REQUIREMENT_TYPE+" TEXT,"
                + REQUIREMENT+" TEXT,"
                + "isCompleted"+" TEXT DEFAULT 'pending',"
                + SELECTED_IMAGE+" BLOB );";
        db.execSQL(query);
    }


    public static void createUserRequestTable(SQLiteDatabase db){
        String Table_name = "user_request";
        String query="CREATE TABLE "
                + Table_name + "( req_id " + " INTEGER PRIMARY KEY    ,"
                + "ngo_email"+ " TEXT,"
                + SUBJECT +" TEXT,"
                + DONOR_EMAIL+" TEXT,"
                + NGO_NAME+" TEXT,"
                + ITEMS+" TEXT,"
                + STATUS+" TEXT,"
                + TIME+" TEXT );";
        db.execSQL(query);
    }
    public static void createPostUserRequestTable(SQLiteDatabase db){
        String Table_name = "post_user_request";
        String query="CREATE TABLE "
                + Table_name + "( req_id " + " INTEGER PRIMARY KEY AUTOINCREMENT   ,"
                + "post_id"+ " INTEGER,"
                + NGO_NAME+" TEXT,"
                + "ngo_email"+" TEXT,"
                + SUBJECT +" TEXT,"
                + ITEMS+" TEXT,"
                + DONOR_EMAIL+" TEXT,"
                +STATUS +" TEXT,"
                + TIME+" TEXT );";
        db.execSQL(query);
    }
    public void updatePostUserRequest(int post_id,String status){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("UPDATE post_user_request SET status="+"'"+status+"'"+" WHERE post_id="+post_id );

    }
    public void updateUserRequest(int req_id,String status){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("UPDATE user_request SET status="+"'"+status+"'"+" WHERE req_id="+req_id);
    }

    public void onCreate(SQLiteDatabase db) {
        createRegisterTable(db);
        createLoginTable(db);
        createPostTable(db);
        createPostUserRequestTable(db);

        createUserRequestTable(db);
        String query="CREATE TABLE "
                + "ngo" + "("+"id"+" INTEGER,"
                +EMAIL_ID  + " TEXT PRIMARY KEY,"
                + NGO_NAME +" TEXT,"
                + LOCATION+" TEXT,"
                + CATEGORY+" TEXT,"
                + ABOUT+" TEXT,"
                + PHONE+" TEXT,"
                +PASSWORD+" TEXT,"
                +NGOPROFILEPIC+" BLOB);";
        db.execSQL(query);
        Log.i("ngo","database created");
        String[] NGOName={"Center For Social Security Action & Research (CSSAR)", "Mazi Sainik Shikshan Ani Swasthya Kalyan Sanstha",
                "Annamrita Foundation","JK MAASS Foundation","Mobile Creches for Working Mothers' Children","National Society for Equal Opportunities for the Handicapped",
                "Navjyoti India Foundation","Pahal Jan Sahyog Vikas Sansthan","Saath (Initiatives for Equity in Development)","Salaam Bombay Foundation"};

        String[] NGOType={"education","education","children","differently abled","children","differently abled","education","health","children","children"};

        String[] NGOLocation={"Delhi","Maharashtra","Maharashtra","Tamil Nadu","Delhi","Maharashtra","Delhi","Madhya Pradesh","Gujarat","Maharashtra"};

        String[] NGOAbout={"Centre for Social Security and Research aims at strengthening urban poor communities, especially the slum dwellers to participate in the development process for claiming their rights. Students who used to drop out of the schools because of unsatisfactory teaching methods or lack of finances to afford a tutor are regularly attending school these days owing to the remedial classes conducted by CSSAR.","The Mazi Sainik Shishan Ani Swasthya Kalyan Sanstha (MSSASKS) is a charitable Trustestablished by ex servicemen (ESMs). The NGO believes that education is a vital tool to amalgamate the underprivileged tribal into the mainstream, find a vocation, obtain a job,become self-sufficient, keep away from the evils of the society.","ISKCON Food Relief Foundation (IFRF) believes in providing children with the right nutrition to support their education through their 'Annamrita' program. The programme is based on the belief that, You Are What You Eat. An assured nutritious meal a day brings thousands of children to school.","JK MAASS Foundation is a social, health, development and empowerment agency servicing differently abled people from underprivileged communities for the past 16 years. The Foundation is dedicated to the rehabilitation of the differently abled and works for their upliftment in the urban slums and rural areas of Madurai district. It was started with the aim of providing services like physiotherapy, speech therapy and special education to children affected by Cerebral Palsy and counselling to their parents.","Mobile Creches for Working Mothers aims to build a just and caring world which enables young children of marginalized and mobile population to develop into competent and confident individuals.", "As one of its core activities, NASEOH runs a Vocational Training Center for imparting vocational training to the differently-abled. The key objective of the programme is to ensure employment for the differently-abled and providing them with economic independence. Apart from trades like Pottery, Welding, Gardening.","Navjyoti India Foundation is a not-for-profit organisation which challenges the social inequalities and empowers the vulnerable by making them independent to help themselves. Navjyoti began with an aim to improve, correct de-addiction and rehabilitation of the drug addicts as a crime prevention measure.","Pahal is providing nutritional support to T.B patients from its project implementation area. It is involved in T.B awareness generation, case detection and treatment adherence of DOTs for the T.B patients","SAATH runs Balghars that provide pre-school education and nutrition support to underprivileged children from the slums of Ahmedabad. The curriculum has been specially designed with the help of expert child psychologists in collaboration with teachers to improve and strengthen the quality of pre-school education components.","Salaam Bombay Foundation believes that children grow as their horizons grow. Started to empower children to overcome the obstacles of the environment they live in."};

        String[] NGOEmail={"cssar@gmail.com","mssasks@gmail.com","af@gmail.com","jk@gmail.com","wm@gmail.com","nsfoep@gmail.com","nif@gmail.com","pjsvs@gmail.com","saath@gmail.com","salaambombay@gmail.com"};
        int i;
        Log.i(NGOName[0],NGOLocation[0]);
       /* SQLiteDatabase db1=this.getWritableDatabase();*/
        for( i=1;i<=10;i++){
            ContentValues values_ngo = new ContentValues();
            values_ngo.put("id",i);
            values_ngo.put(EMAIL_ID,NGOEmail[i-1]);
            values_ngo.put(NGO_NAME,NGOName[i-1]);
            values_ngo.put(LOCATION,NGOLocation[i-1]);
            values_ngo.put(CATEGORY,NGOType[i-1]);
            values_ngo.put(ABOUT,NGOAbout[i-1]);
            values_ngo.put(PHONE,"1234");
            values_ngo.put(PASSWORD,"123");
            long x=db.insert("ngo", null, values_ngo);
            ContentValues values_ngo_login=new ContentValues();
            values_ngo_login.put(EMAIL,NGOEmail[i-1]);
            values_ngo_login.put(PASSWORD,"123");
            values_ngo_login.put(TYPE,"ngo");
            db.insert("Login", null, values_ngo_login);
        }


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Login'");
        db.execSQL("DROP TABLE IF EXISTS 'Register'");
        db.execSQL("DROP TABLE IF EXISTS 'user_request'");
        db.execSQL("DROP TABLE IF EXISTS 'post'");
        db.execSQL("DROP TABLE IF EXISTS 'post_user_request'");
        db.execSQL("DROP TABLE IF EXISTS 'ngo'");
        onCreate(db);
    }

    public boolean updateUserAddress(String email,String address){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("UPDATE Register SET ADDRESS="+"'"+address+"'"+" WHERE EMAIL ="+"'"+email+"'");
        return true;
    }
    public boolean updateUserPhoto(String email,byte[] image){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PROFILEPIC, image);
        db.update("Register", cv, "EMAIL=" + "'"+email+"'", null);
        return true;
       // db.execSQL("UPDATE Register SET PROFILEPIC="+image+"WHERE EMAIL ="+"'"+email+"'");

    }


    public boolean updateNGOAddress(String email,String address){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("UPDATE ngo SET LOCATION="+"'"+address+"'"+" WHERE EMAIL_ID ="+"'"+email+"'");
        return true;
    }
    public boolean updateNgoPhoto(String email,byte[] image){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NGOPROFILEPIC, image);
        db.update("ngo", cv, "EMAIL_ID=" + "'"+email+"'", null);
        return true;
        // db.execSQL("UPDATE Register SET PROFILEPIC="+image+"WHERE EMAIL ="+"'"+email+"'");

    }

    public String getPostCount(int postId){
        //DB_handling db=new DB_handling(this);
        SQLiteDatabase db1=this.getReadableDatabase();
        Cursor c=db1.rawQuery("SELECT COUNT(post_id)\n" +
                "FROM post_user_request WHERE post_id="+postId,null);
        c.moveToFirst();
        return String.valueOf(c.getInt(0));

    }

    public boolean updatePostStatus(int postId,String status){
        SQLiteDatabase db=getWritableDatabase();
        try {

            db.execSQL("UPDATE post SET isCompleted="+"'"+status+"'"+" WHERE id ="+postId);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }


    public boolean getPostIsCompleted(int postId){
        Cursor c;

            //DB_handling db=new DB_handling(null);
            SQLiteDatabase db1 = this.getReadableDatabase();
            c = db1.rawQuery("SELECT isCompleted\n" +
                    "FROM post WHERE id=" + postId, null);
            c.moveToFirst();

            return c.getString(0).equals("Completed");



    }




}
