package com.alitech.beoneseye;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BeOnesEye.db";
    public static final String USER_TABLE_NAME = "users";
    public static final String LOGINH_TABLE_NAME = "loginhistory";
    public static final String HISTORY_TABLE_NAME = "history";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_LOCATION = "location";
    public static final String LOGINH_COLUMN_ID = "id";
    public static final String LOGIN_COLUMN_DATETIME = "logindatetime";
    public static final String LOGOUT_COLUMN_DATETIME = "logoutdatetime";
    public static final String HISTORY_COLUMN_ID = "id";
    public static final String HISTORY_COLUMN_DATETIME = "datetime";
    public static final String HISTORY_COLUMN_TEXT = "textextracted";
    public static final String HISTORY_COLUMN_IMAGE_PATH = "imagePath";
    public static final String HISTORY_COLUMN_FEEDBACK = "feedback";
    public static final String HISTORY_COLUMN_ACTION = "Action_";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " +USER_TABLE_NAME+
                        " ("+USER_COLUMN_ID+" text primary key,"+USER_COLUMN_NAME+" text,"+USER_COLUMN_EMAIL+" text,"+USER_COLUMN_LOCATION+" text)"
        );
        db.execSQL(
                "create table " +LOGINH_TABLE_NAME+
                        " ("+LOGINH_COLUMN_ID+" text,"+LOGIN_COLUMN_DATETIME+" text,"+LOGOUT_COLUMN_DATETIME+" text, FOREIGN KEY ("+LOGINH_COLUMN_ID+") REFERENCES "+USER_TABLE_NAME+" ("+USER_COLUMN_ID+"))"
        );
        db.execSQL(
                "create table " +HISTORY_TABLE_NAME+
                        " ("+HISTORY_COLUMN_ID+" text,"+HISTORY_COLUMN_ACTION+" text,"+HISTORY_COLUMN_DATETIME+" text,"+HISTORY_COLUMN_TEXT+" text,"+HISTORY_COLUMN_IMAGE_PATH+" text,"+HISTORY_COLUMN_FEEDBACK+" text, FOREIGN KEY ("+HISTORY_COLUMN_ID+") REFERENCES "+USER_TABLE_NAME+" ("+USER_COLUMN_ID+"))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+LOGINH_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+HISTORY_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUSer (String id, String name, String email, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, id);
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_LOCATION, location);
        if(db.insert(USER_TABLE_NAME, null, contentValues)!=-1)return true;
        else return false;
    }
    public boolean insertLogINH (String id, String name, String loginTime, String logoutTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOGINH_COLUMN_ID, id);
        contentValues.put(LOGIN_COLUMN_DATETIME, loginTime);
        contentValues.put(LOGOUT_COLUMN_DATETIME, logoutTime);
        if(db.insert(LOGINH_TABLE_NAME, null, contentValues)!=-1) return true;
        else return false;
    }
    public boolean insertHistory (String id, String action, String textExtracted,String actionTime, String imagePath,String feebback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COLUMN_ID, id);
        contentValues.put(HISTORY_COLUMN_ACTION, action);
        contentValues.put(HISTORY_COLUMN_DATETIME, actionTime);
        contentValues.put(HISTORY_COLUMN_TEXT, textExtracted);
        contentValues.put(HISTORY_COLUMN_IMAGE_PATH, imagePath);
        contentValues.put(HISTORY_COLUMN_FEEDBACK, feebback);
        if(db.insert(HISTORY_TABLE_NAME, null, contentValues)!=-1)return true;
        else return false;
    }

    public ArrayList<History> getHistory(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+HISTORY_TABLE_NAME+" where "+HISTORY_COLUMN_ID+"='"+id+"'", null );
        res.moveToFirst();
        ArrayList<History> array_list = new ArrayList<History>();
        while(res.isAfterLast() == false){
            String a,at,te,imp,fb;
            a=res.getString(res.getColumnIndex(HISTORY_COLUMN_ACTION));
            at=res.getString(res.getColumnIndex(HISTORY_COLUMN_DATETIME));
            te=res.getString(res.getColumnIndex(HISTORY_COLUMN_TEXT));
            imp =res.getString(res.getColumnIndex(HISTORY_COLUMN_IMAGE_PATH));
            fb=res.getString(res.getColumnIndex(HISTORY_COLUMN_FEEDBACK));
            History obj=new History(id,at,te,imp,fb,a);
            array_list.add(obj);
            res.moveToNext();
            }
        return array_list;
    }

    public ArrayList<List<String>> getLogiNH(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+LOGINH_TABLE_NAME+" where "+LOGINH_COLUMN_ID+"='"+id+"'", null );
        res.moveToFirst();
        List<String> aRow = new ArrayList<String>();
        ArrayList<List<String>> array_list=new ArrayList<List<String>>();
        while(res.isAfterLast() == false){
            aRow.add(res.getString(res.getColumnIndex(LOGIN_COLUMN_DATETIME)));
            aRow.add(res.getString(res.getColumnIndex(LOGOUT_COLUMN_DATETIME)));
            array_list.add(aRow);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getuser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+USER_TABLE_NAME+" where "+USER_COLUMN_ID+"='"+id+"'", null );
        ArrayList<String> array_list = new ArrayList<String>();
        array_list.add(id);
        res.moveToFirst();
        array_list.add(res.getString(res.getColumnIndex(USER_COLUMN_NAME)));
        array_list.add(res.getString(res.getColumnIndex(USER_COLUMN_EMAIL)));
        array_list.add(res.getString(res.getColumnIndex(USER_COLUMN_LOCATION)));
        return array_list;
    }

    public boolean updateUSER (String id, String name, String email, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, id);
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_LOCATION, location);
        db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID+" = ? ", new String[] { id } );
        return true;
    }

    public Integer deleteUser (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME,
                USER_COLUMN_ID+" = ? ",
                new String[] { id });
    }

    public Integer deleteLogInH (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LOGINH_TABLE_NAME,
                LOGINH_COLUMN_ID+" = ? ",
                new String[] { id });
    }
    public Integer deleteHistory (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(HISTORY_TABLE_NAME,
                HISTORY_COLUMN_ID+" = ? ",
                new String[] { id });
    }
}