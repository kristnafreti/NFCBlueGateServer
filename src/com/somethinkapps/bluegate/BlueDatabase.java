/**
 * 
 */
package com.somethinkapps.bluegate;
/**
 * 
 */
//http://code.google.com/p/j2memaprouteprovider/source/browse/#svn%2Ftrunk%2FJ2MEMapRouteAndroidEx%2Fsrc%2Forg%2Fci%2Fgeo%2Froute
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;
import java.util.ArrayList;

import java.util.List;

public class BlueDatabase {
 private static final String DATABASE_NAME = "bluegate";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_NAME = "daftar_anggota";
  private static final String TABLE_NAME1 = "daftar_masuk";
  private Context context;
  private SQLiteDatabase db;

  private SQLiteStatement insertStmt;
  private static final String INSERT = "insert into " + TABLE_NAME + "(name) values (?)";
  private static final String INSERT1 = "insert into "+ TABLE_NAME1 + "(name) values (?)";

  public BlueDatabase(Context context) {
     this.context = context;
     OpenHelper openHelper = new OpenHelper(this.context);
     this.db = openHelper.getWritableDatabase();
     this.insertStmt = this.db.compileStatement(INSERT);
     this.insertStmt = this.db.compileStatement(INSERT1);
  }

  public long insert(String name) {
     this.insertStmt.bindString(1, name);
     return this.insertStmt.executeInsert();
  }
  public void deleteAll() {
     this.db.delete(TABLE_NAME, null, null);
     this.db.delete(TABLE_NAME1, null, null);
  }
  public List<String> selectID() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME, new String[] { "id_smartphone" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }

  public List<String> selectID1() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "id_smartphone" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
 
  public List<String> selectEntryTime() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "EntryTime" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public List<String> selectEntryDate() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "EntryDate" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public List<String> selectEntryH() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "EntryH" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public List<String> selectEntryM() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "EntryM" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public List<String> selectEntryS() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "EntryS" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public List<String> selectEntryDayofyear() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "EntryDayofyear" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public List<String> selectElapsed() {
	     List<String> list = new ArrayList<String>();
	     Cursor cursor = this.db.query(TABLE_NAME1, new String[] { "ElapsedTime" },
	       null, null, null, null, null);
	     if (cursor.moveToFirst()) {
	        do {
	           list.add(cursor.getString(0));
	        } while (cursor.moveToNext());
	     }
	     if (cursor != null && !cursor.isClosed()) {
	        cursor.close();
	     }
	     return list;
	  }
  public long insertTitle(int num, String id, String name, String id_member)
	{
	ContentValues initialValues = new ContentValues();
	initialValues.put("no", num);
	initialValues.put("id_smartphone", id);
	initialValues.put("nama", name);
	initialValues.put("id_member", id_member);
	return db.insert(TABLE_NAME, null, initialValues);
	}
  public long update(int rowID, String ext, String exd, int xh, int xm, int xs, int xdoy, String elapsed)
  {
	  ContentValues args = new ContentValues();
	   args.put("exitTime", ext);
	   args.put("exitDate", exd);
	   args.put("exitH", xh);
	   args.put("exitM", xm);
	   args.put("exitS", xs);
	   args.put("exitDayofyear", xdoy);
	   args.put("ElapsedTime", elapsed);
	   return db.update(TABLE_NAME1, args, "no" + "=" + rowID, null);
  }
  public long insertTitle1(int num, String id, int jml, String time, String date, int h, int m, int s, int doy)
 	{
 	ContentValues initialValues = new ContentValues();
 	initialValues.put("no", num);
 	initialValues.put("id_smartphone", id);
 	initialValues.put("jumlah_masuk", jml);
 	initialValues.put("entryTime", time);
 	initialValues.put("entryDate", date);	
 	initialValues.put("entryH", h);
 	initialValues.put("entryM", m);
 	initialValues.put("entryS", s);
 	initialValues.put("entryDayofyear", doy);
 	initialValues.put("ElapsedTime", " ");
 	return db.insert(TABLE_NAME1, null, initialValues);
 	}
 
  private static class OpenHelper extends SQLiteOpenHelper {

     OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
     }

     @Override
     public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(no integer not null, id_smartphone text not null, id_member text, nama text)");
        db.execSQL("CREATE TABLE " + TABLE_NAME1 + "(no integer, id_smartphone text not null, jumlah_masuk int, " +
        		"entryTime text, entryH integer,entryM integer,entryS integer, entryDate text, entryDayofyear integer," +
        		"exitTime text, exitH integer,exitM integer,exitS integer, exitDate text, exitDayofyear integer, ElapsedTime text)");
     }
  
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Example", "Upgrading database, this will drop tables and recreate.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
     }
  }
}