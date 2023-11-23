package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static String KEY_ID = "id";
    private static final String KEY_TASK = "task";
    private static final String KEY_STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TASK + " TEXT,"
            + KEY_STATUS + " TEXT)";

    private SQLiteDatabase db;

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDataBase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TASK, task.getTask());
        cv.put(KEY_STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(KEY_TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(KEY_STATUS)));
                        taskList.add(task);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, status);
        if (db != null) {
            db.update(TODO_TABLE, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});
        }
    }


    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TASK, task);
        db.update(TODO_TABLE, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});

    }
        public void deleteTask ( int id){
            db.delete(TODO_TABLE, KEY_ID + "=?", new String[]{String.valueOf(id)});
        }

}


