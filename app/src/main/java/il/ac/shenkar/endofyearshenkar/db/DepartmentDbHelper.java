package il.ac.shenkar.endofyearshenkar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;

public class DepartmentDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "ShowDepartments.db";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DepartmentContract.DepartmentEntry.TABLE_NAME + " (" +
                    DepartmentContract.DepartmentEntry._ID + " INTEGER PRIMARY KEY," +
                    DepartmentContract.DepartmentEntry.COLUMN_NAME_SERVER_ID + " INTEGER," +
                    DepartmentContract.DepartmentEntry.COLUMN_NAME_JSON_BLOB + " TEXT)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DepartmentContract.DepartmentEntry.TABLE_NAME;

    public DepartmentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        db.close();
    }

    public void insertDepartment(DepartmentJson departmentJson) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DepartmentContract.DepartmentEntry.COLUMN_NAME_SERVER_ID, departmentJson.getId());
        values.put(DepartmentContract.DepartmentEntry.COLUMN_NAME_JSON_BLOB, new Gson().toJson(departmentJson).toString());

        db.insert(DepartmentContract.DepartmentEntry.TABLE_NAME, null, values);

        db.close();
    }

    public List<DepartmentJson> getAll() {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DepartmentContract.DepartmentEntry.COLUMN_NAME_JSON_BLOB
        };

        Cursor cursor = db.query(
                DepartmentContract.DepartmentEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List departments = new ArrayList<DepartmentJson>();
        while (cursor.moveToNext()) {
            String depString = cursor.getString(cursor.getColumnIndexOrThrow(DepartmentContract.DepartmentEntry.COLUMN_NAME_JSON_BLOB));
            departments.add(new Gson().fromJson(depString, DepartmentJson.class));
        }
        cursor.close();

        db.close();

        return departments;
    }
}
