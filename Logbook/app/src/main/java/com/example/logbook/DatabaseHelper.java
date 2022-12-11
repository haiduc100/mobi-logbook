package com.example.logbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "database";
  private static final String TABLE = "image";

  public static final String ID = "id";
  public static final String IMAGE = "img";

  private SQLiteDatabase database;

  private static final String IMAGE_TABLE_CREATE = String.format(
    "CREATE TABLE %s (" +
    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
    "   %s TEXT)",
    TABLE,
    ID,
    IMAGE
  );

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
    database = getWritableDatabase();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(IMAGE_TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE_CREATE);

    Log.v(
      this.getClass().getName(),
      DATABASE_NAME + " database upgrade to version " + i1 + " - old data lost"
    );
    onCreate(db);
  }

  public int getIdImage(String imgUrl) {
    String MY_QUERY = "SELECT * FROM " + TABLE + " WHERE img = ?";
    Cursor cursor = database.rawQuery(
      MY_QUERY,
      new String[] { String.valueOf(imgUrl) }
    );

    List<entities> results = new ArrayList<entities>();

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      int id = cursor.getInt(0);
      String url = cursor.getString(1);

      entities entities = new entities(id, url);
      results.add(entities);
      cursor.moveToNext();
    }
    return results.get(0).getId();
  }

  public int getSize() {
    Cursor cursor = database.query(
      TABLE,
      new String[] { ID, IMAGE },
      null,
      null,
      null,
      null,
      IMAGE
    );

    List<entities> results = new ArrayList<entities>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      int id = cursor.getInt(0);
      String url = cursor.getString(1);
      entities entities = new entities(id, url);
      results.add(entities);
      cursor.moveToNext();
    }
    return results.size();
  }

  public String getImageById(String idImg) {
    String MY_QUERY = "SELECT * FROM " + TABLE + " WHERE id=?";
    Cursor cursor = database.rawQuery(MY_QUERY, new String[] { idImg });

    List<entities> results = new ArrayList<entities>();

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      int id = cursor.getInt(0);
      String url = cursor.getString(1);

      entities entities = new entities(id, url);
      results.add(entities);

      cursor.moveToNext();
    }
    return results.get(0).getImg();
  }

  public long insertImage(String image) {
    ContentValues rowValues = new ContentValues();

    rowValues.put(IMAGE, image);

    return database.insertOrThrow(TABLE, null, rowValues);
  }
}
