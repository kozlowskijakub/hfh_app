package messanger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import messanger.Message;
import tracer.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;


public class ChatDatabaseHandler extends SQLiteOpenHelper {
    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "storedMessages";

    //tables
    private static final String TABLE_OUT_MSG = "outMsg";
    private static final String TABLE_IN_MSG = "inMsg";

//    private static final String IN_MSG_IDmsg = "id";
//    private static final String IN_MSG_CONTENT = "name";
//    private static final String IN_MSG_TIME = "distance";
//
//    private static final String OUT_MSG_ID = "id";
//    private static final String OUT_MSG_CONTENT = "name";
//    private static final String OUT_MSG_TIME = "distance";

    public static ChatDatabaseHandler database = null;


    private static final String CREATE_TABLE_IN_MSG = "CREATE TABLE " + TABLE_IN_MSG + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "content TEXT," +
            "time long)";
    private static final String CREATE_TABLE_OUT_MSG = "CREATE TABLE " + TABLE_OUT_MSG + " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "content TEXT," +
            "time long)";

    private ChatDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ChatDatabaseHandler getInstance(Context context) {

        if (database == null) {
            database = new ChatDatabaseHandler(context);
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_IN_MSG);
        db.execSQL(CREATE_TABLE_OUT_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IN_MSG);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUT_MSG);
        onCreate(db);
    }

    public void createOutMsg(Message msg) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("content", msg.content);
        values.put("time", msg.time);

        long x = database.insert(TABLE_OUT_MSG, null, values);
        Log.i("createOutMsg", String.valueOf(x));



    }

    public void createInMsg(Message msg) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("content", msg.content);
        values.put("time", msg.time);

        database.insert(TABLE_OUT_MSG, null, values);



    }

    public List<Message> getInMsgs() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<Message> inMsgs = new ArrayList<Message>();
        Message msg;
        String query = "SELECT * FROM " + TABLE_IN_MSG;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                msg = new Message();
                msg.content = cursor.getString(cursor.getColumnIndex("content"));
                msg.time = cursor.getLong(cursor.getColumnIndex("time"));
                inMsgs.add(msg);
            } while (cursor.moveToNext());
        }
        return inMsgs;
    }

    public List<Message> getOutMsgs() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<Message> inMsgs = new ArrayList<Message>();
        Message msg;
        String query = "SELECT * FROM " + TABLE_OUT_MSG;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                msg = new Message();
                msg.content = cursor.getString(cursor.getColumnIndex("content"));
                msg.time = cursor.getLong(cursor.getColumnIndex("time"));
                inMsgs.add(msg);
            } while (cursor.moveToNext());
        }
        return inMsgs;
    }

//    public long createOutMsg() {
//
//        return 0;
//    }
}
