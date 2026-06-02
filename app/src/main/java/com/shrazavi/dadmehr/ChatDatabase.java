package com.shrazavi.dadmehr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class ChatDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "chat_db";
    public static final int VERSION = 1;
    public static String resultaccount;
    //=================list===================
    public static final String TABLE_NAME_LIST = "tbl_chat_list";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_IMGPROFILE = "imgprofile";
    public static final String QUERYLIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LIST + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USERNAME + " TEXT, " +
            COL_IMGPROFILE + " TEXT);";
    //================chat====================
    public static final String TABLE_CHAT = "tbl_chat";
    public static final String COL_FROM = "fromchat";
    public static final String COL_TO = "tochat";
    public static final String COL_ROOM = "room";
    public static final String COL_TIME = "time";
    public static final String COL_DATE = "date";
    public static final String COL_PIC = "pic";
    public static final String COL_VID = "vid";
    public static final String COL_VOISE = "voise";
    public static final String COL_READ = "read";
    public static final String COL_SAVE = "save";
    public static final String COL_TEXT = "text";
    public static final String QUERYCHAT = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_FROM + " TEXT, " +
            COL_TO + " TEXT, " +
            COL_ROOM + " TEXT, " +
            COL_TIME + " TEXT, " +
            COL_DATE + " INTEGER, " +
            COL_PIC + " TEXT, " +
            COL_VID + " TEXT, " +
            COL_VOISE + " TEXT, " +
            COL_READ + " INTEGER, " +
            COL_SAVE + " INTEGER, " +
            COL_TEXT + " TEXT);";
    //================Account====================
    public static final String TABLE_ACCOUNT = "tbl_account";
    public static final String COL_PRICE = "price";
    public static final String COL_STATUS = "stutus";
    public static final String QUERYACCOUNT = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCOUNT + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_PRICE + " TEXT, " +
            COL_TIME + " TEXT, " +
            COL_DATE + " INTEGER, " +
            COL_STATUS + " TEXT);";
    //================CallLog====================
    public static final String TABLE_CALL = "tbl_call";
    public static final String COL_CALLERID = "callerid";
    public static final String COL_TYPE = "type";
    public static final String QUERYCALL = "CREATE TABLE IF NOT EXISTS " + TABLE_CALL + "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USERNAME + " TEXT, " +
            COL_CALLERID + " INTEGER, " +
            COL_TIME + " TEXT, " +
            COL_DATE + " INTEGER, " +
            COL_STATUS + " TEXT, " +
            COL_TYPE + " TEXT, " +
            COL_IMGPROFILE + " TEXT);";


    Context context;

    public ChatDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(QUERYLIST);
            db.execSQL(QUERYACCOUNT);
            db.execSQL(QUERYCHAT);
            db.execSQL(QUERYCALL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addChatlist(String username, String imgprofile) {
        ContentValues cvlist = new ContentValues();
        cvlist.put(COL_USERNAME, username);
        cvlist.put(COL_IMGPROFILE, imgprofile);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String exist = "";
        Cursor cursor = getAllRecords();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            final String user = cursor.getString(1);
            if (username.equals(user)) {
                exist = "yes";
            }
        }
        if (exist.equals("")) {
            long result = sqLiteDatabase.insert(TABLE_NAME_LIST, null, cvlist);
//            if (result > 0) {
//                Toast.makeText(G.context, "id is:" + result, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(G.context, "faild", Toast.LENGTH_SHORT).show();
//            }
        } else {
//            ContentValues cvuserup = new ContentValues();
//            cvup.put(COL_READ, 1);
//            SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
//            long result = sqLiteDatabasechat.update(TABLE_CHAT, cvup, COL_READ + "=" + 0, null);
            Log.e("Exist", exist);
        }
    }

    public void upuser(String userid, String imgprofile) {
        ContentValues cvupuser = new ContentValues();
        cvupuser.put(COL_IMGPROFILE, imgprofile);


        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();

        long result = sqLiteDatabasechat.update(TABLE_NAME_LIST, cvupuser, COL_USERNAME + "= '" + userid +"'", null);
//        if (result > 0) {
//            Toast.makeText(G.context, "id is:" + result, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(G.context, "faild"+result, Toast.LENGTH_SHORT).show();
//        }

    }
    public void upaccount(String status, String id) {
        ContentValues cvaccount = new ContentValues();
        cvaccount.put(COL_STATUS, status);


        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
        long result = sqLiteDatabasechat.update(TABLE_ACCOUNT, cvaccount, COL_ID + "=" + id, null);


    }
    public void addChat(String from, String to, String room, String time, long date, String pic, String vid, String voise, int read, int save, String text) {
        ContentValues cvchat = new ContentValues();
        Log.e("database", from+" , "+room+" , "+time+" , "+date+" , "+pic+" , "+vid+" , "+voise+" , "+read+" , "+save+" , "+text+"");
        cvchat.put(COL_FROM, from);
        cvchat.put(COL_TO, to);
        cvchat.put(COL_ROOM, room);
        cvchat.put(COL_TIME, time);

        cvchat.put(COL_DATE, date);
        cvchat.put(COL_PIC, pic);
        cvchat.put(COL_VID, vid);
        cvchat.put(COL_VOISE, voise);
        cvchat.put(COL_READ, read);
        cvchat.put(COL_SAVE, save);
        cvchat.put(COL_TEXT, text);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.insert(TABLE_CHAT, null, cvchat);
//        if (result > 0) {
//            Toast.makeText(G.context, "id is:" + result, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(G.context, "faild", Toast.LENGTH_SHORT).show();
//        }
    }
    public void upcall(String imgprofile, String username, int callerid) {
        ContentValues cvupcall = new ContentValues();
        cvupcall.put(COL_USERNAME, username);
        cvupcall.put(COL_IMGPROFILE, imgprofile);


        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
        long result = sqLiteDatabasechat.update(TABLE_CALL, cvupcall, COL_CALLERID + "=" + callerid, null);


    }

    public void addCallLog(String username, int callerid, String time, long date, String status, String type, String imgprofile) {
        ContentValues cvcall = new ContentValues();
        cvcall.put(COL_USERNAME, username);
        cvcall.put(COL_CALLERID, callerid);
        cvcall.put(COL_TIME, time);
        cvcall.put(COL_DATE, date);
        cvcall.put(COL_STATUS, status);
        cvcall.put(COL_TYPE, type);
        cvcall.put(COL_IMGPROFILE, imgprofile);





        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.insert(TABLE_CALL, null, cvcall);
//        resultaccount=result+"";
        if (result > 0) {
            Toast.makeText(G.context, "id is:" + result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(G.context, "faild", Toast.LENGTH_SHORT).show();
        }
    }
    public void addTransaction(String price, String time, long date, String status) {
        ContentValues cvtrans = new ContentValues();

        cvtrans.put(COL_PRICE, price);
        cvtrans.put(COL_TIME, time);
        cvtrans.put(COL_DATE, date);
        cvtrans.put(COL_STATUS, status);



        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.insert(TABLE_ACCOUNT, null, cvtrans);
        resultaccount=result+"";
        if (result > 0) {
            Toast.makeText(G.context, "id is:" + result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(G.context, "faild", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getAllRecords() {
        String query = "SELECT * FROM " + TABLE_NAME_LIST;
        SQLiteDatabase sqLiteDatabaselist = this.getWritableDatabase();
        return sqLiteDatabaselist.rawQuery(query, null);
    }

    public Cursor getAllChat() {
        String query = "SELECT * FROM " + TABLE_CHAT;
        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
        return sqLiteDatabasechat.rawQuery(query, null);
    }
    public Cursor getCalllog() {
        String query = "SELECT * FROM " + TABLE_CALL;
        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
        return sqLiteDatabasechat.rawQuery(query, null);
    }
//    public Cursor getAllTransaction() {
//        String query = "SELECT * FROM " + TABLE_ACCOUNT;
//        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
//        return sqLiteDatabasechat.rawQuery(query, null);
//    }
    public void updateread() {

        ContentValues cvup = new ContentValues();
        cvup.put(COL_READ, 1);


        SQLiteDatabase sqLiteDatabasechat = this.getWritableDatabase();
        long result = sqLiteDatabasechat.update(TABLE_CHAT, cvup, COL_READ + "=" + 0, null);


    }
}
