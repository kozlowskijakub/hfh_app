package tracer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import tracer.logicObjects.POI;
import tracer.logicObjects.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/4/13
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    //database
    private static final String DATABASE_NAME = "recordedTracks";

    //tables
    private static final String TABLE_TRACK = "track";
    private static final String TABLE_POINT = "point";

    // Track
    private static final String TRACK_ID = "id";
    private static final String TRACK_Name = "name";
    private static final String TRACK_Distance = "distance";
    private static final String TRACK_Time = "time";
    private static final String TRACK_MaxAltitude = "altitude";

    // Point
    private static final String POINT_ID = "id";
    private static final String POINT_ORDER = "order_id";
    private static final String POINT_LATITUDE = "latitude";
    private static final String POINT_LONGITUDE = "longitude";
    private static final String POINT_ALTITUDE = "altitude";
    private static final String POINT_TIME = "time";
    private static final String POINT_ACCURACY = "accuracy";


//    INTEGER: This SQLite data type generally contain INT, INTEGER, TINYINT, SMALLINT, MEDIUMINT, BIGINT, UNSIGNED BIG INT, INT2, INT8.
//
//    TEXT: This SQLite data type generally contain CHARACTER(20), VARCHAR(255), VARYING CHARACTER(255), NCHAR(55), NATIVE CHARACTER(70), NVARCHAR(100), TEXT, CLOB.
//
//    REAL: This SQLite data type generally contain REAL, DOUBLE, DOUBLE PRECISION, FLOAT.
//
//    NONE: This SQLite data type generally contain BLOB, NO DATA TYPE SPECIFIED.
//
//    NUMERIC: This SQLite data type generally contain NUMERIC, DECIMAL(10,5), BOOLEAN, DATE, DATETIME.


    private static final String CREATE_TABLE_TRACK = "CREATE TABLE track " +
            "(id INTEGER PRIMARY AUTOINCREMENT KEY NOT NULL," +
            "name TEXT," +
            "distance REAL, " +
            "time LONG, " +
            "altitude REAL)";
    private static final String CREATE_TABLE_POINT = "CREATE TABLE point " +
            "(id INTEGER PRIMARY AUTOINCREMENT KEY NOT NULL," +
            "order_id INTEGER," +
            "latitude REAL," +
            "longitude REAL, " +
            "altitude REAL, " +
            "time LONG, " +
            "accuracy REAL," +
            "id_track INTEGER NOT NULL," +
            "FOREIGN KEY (id_track) REFERENCES track(id))";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRACK);
        db.execSQL(CREATE_TABLE_POINT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DRIO TABLE IF EXISTS " + TABLE_POINT);
        db.execSQL("DRIO TABLE IF EXISTS " + TABLE_TRACK);
        onCreate(db);
    }

    // create new track
    public long createTrack(Track track, long[] id) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put("order_id", track.order_id);
        values.put("amplitude", track.amplitude);
        values.put("distance", track.distance);
        values.put("name", track.name);
        values.put("time", track.time);

        long track_id = database.insert(TABLE_TRACK, null, values);

        return track_id;
    }


    public Track getTrack(long track_id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String select = "SELECT * FROM track WHERE id =" + track_id;

        Cursor cursor = database.rawQuery(select, null);
        if (cursor != null)
            cursor.moveToFirst();

        Track track = new Track();
        track.name = (cursor.getString(1));
        track.distance = (cursor.getDouble(2));
        track.time = (cursor.getLong(3));
        track.amplitude = (cursor.getDouble(4));

        return track;
    }

    public List<Track> getAllTracks() {
        List<Track> tracks = new ArrayList<Track>();
        String select = "SELECT * FROM track";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(select, null);


        if (cursor.moveToFirst()) {
            do {
                Track track = new Track();
                track.name = (cursor.getString(1));
                track.distance = (cursor.getDouble(2));
                track.time = (cursor.getLong(3));
                track.amplitude = (cursor.getDouble(4));
                tracks.add(track);
            } while (cursor.moveToFirst());
        }
        return tracks;
    }

    public void deleteTrack(long track_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_TRACK, "id=" + track_id, null);
    }

    public long createPOI(POI poi, long track_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("order_id", poi.order_id);
        values.put("latitude", poi.getLatitude());
        values.put("longitude", poi.getLongitude());
        values.put("altitude", poi.getAltitude());
        values.put("time", poi.getTime());
        values.put("accuracy", poi.getAccuracy());
        values.put("id_track", track_id);

        long POI_id = database.insert(TABLE_POINT, null, values);
        return POI_id;
    }

//    public List<POI> getPOIs(long track_id) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        List<POI> pois = new ArrayList<POI>();
//        String select = "SELECT * FROM point where id_track=" + track_id;
//        Cursor cursor = database.rawQuery(select, null);
//
//
//        if (cursor.moveToFirst()) {
//            do {
//                POI poi = new POI();
//
//                poi. = (cursor.getString(1));
//                track.distance = (cursor.getDouble(2));
//                track.time = (cursor.getLong(3));
//                track.amplitude = (cursor.getDouble(4));
//                tracks.add(track);
//            } while (cursor.moveToFirst());
//        }
//        return tracks;
//    }
//
//    "CREATE TABLE point "+
//            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
//            "order_id INTEGER,"+
//            "latitude REAL,"+
//            "longitude REAL, "+
//            "altitude REAL, "+
//            "time LONG, "+
//            "accuracy REAL,"+
//            "id_track INTEGER NOT NULL,"+
//            "FOREIGN KEY (id_track) REFERENCES track(id))";

    // create new Point
//    public long createPoint(POI track, long[] id) {
//
//    }
    //    "CREATE TABLE track " +
//            "(id INTEGER PRIMARY KEY NOT NULL," +
//            "name TEXT," +
//            "distance REAL, " +
//            "time LONG, " +
//            "altitude REAL)";

}
