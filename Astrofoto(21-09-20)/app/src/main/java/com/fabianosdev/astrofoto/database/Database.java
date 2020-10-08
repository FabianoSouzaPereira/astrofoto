package com.fabianosdev.astrofoto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fabianosdev.astrofoto.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_ASTROFOTO = "db_AstroFoto";
    private static final String PICTURE_TABLE = "tb_picture";
    private static final String CODE_COLUMN = "code";
    private static final String NAME_COLUMN = "name";
    private static final String CAMERA_COLUMN = "camera";
    private static final String LENS_COLUMN = "lens";
    private static final String SCHEDULE_COLUMN = "schedule";
    private static final String EXPOSURE_COLUMN = "exposure";
    private static final String ISO_SENSITIVITY_COLUMN  = "iso_sensitivity";
    private static final String DIAPHRAGM_OPENING_COLUMN = "diaphragm_opening";
    private static final String FOCAL_DISTANCE_COLUMN = "focal_distance";
    private static final String DPI_RESOLUTION_COLUMN  = "dpi_resolution";
    private static final String FLASH_MODE_COLUMN  = "flash_mode";
    private static final String WHITE_BALANCE_COLUMN  = "white_balance";
    private static final String ROTATION_COLUMN  = "rotation";
    private static final String TAGS_COLUMN  = "tags";
    private static final String WIDTH_COLUMN = "width";
    private static final String HEIGHT_COLUMN = "height";
    private static final String SIZE_COLUMN = "size";
    private static final String PATH_COLUMN = "path";
    private static final String GEOLOCATION_COLUMN  = "geo_location";
    private static final String IMAGE_COLUMN = "image";

    private static final String SQL_CREATE_PICTURE_TABLE =
            "CREATE TABLE " + PICTURE_TABLE +  " (" +
                    CODE_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    NAME_COLUMN + " TEXT NOT NULL," +
                    CAMERA_COLUMN + " TEXT NOT NULL," +
                    LENS_COLUMN + " TEXT NOT NULL," +
                    SCHEDULE_COLUMN + " TEXT NOT NULL," +
                    EXPOSURE_COLUMN + " TEXT NOT NULL," +
                    ISO_SENSITIVITY_COLUMN + " TEXT NOT NULL," +
                    DIAPHRAGM_OPENING_COLUMN + " TEXT NOT NULL," +
                    FOCAL_DISTANCE_COLUMN + " TEXT NOT NULL," +
                    DPI_RESOLUTION_COLUMN + " TEXT NOT NULL," +
                    FLASH_MODE_COLUMN + " TEXT NOT NULL," +
                    WHITE_BALANCE_COLUMN + " TEXT NOT NULL," +
                    ROTATION_COLUMN + " TEXT NOT NULL," +
                    TAGS_COLUMN + " TEXT NOT NULL," +
                    WIDTH_COLUMN + " TEXT NOT NULL," +
                    HEIGHT_COLUMN + " TEXT NOT NULL," +
                    SIZE_COLUMN + " TEXT NOT NULL," +
                    PATH_COLUMN + " TEXT NOT NULL," +
                    GEOLOCATION_COLUMN  + " TEXT NOT NULL," +
                    IMAGE_COLUMN  + " BLOB NOT NULL)";

    private static final String[] Picturecol = { CODE_COLUMN, NAME_COLUMN,CAMERA_COLUMN,LENS_COLUMN,
            SCHEDULE_COLUMN, EXPOSURE_COLUMN, ISO_SENSITIVITY_COLUMN, DIAPHRAGM_OPENING_COLUMN, FOCAL_DISTANCE_COLUMN,
            DPI_RESOLUTION_COLUMN, FLASH_MODE_COLUMN, WHITE_BALANCE_COLUMN, ROTATION_COLUMN, TAGS_COLUMN, WIDTH_COLUMN,
            HEIGHT_COLUMN, SIZE_COLUMN, PATH_COLUMN, GEOLOCATION_COLUMN, IMAGE_COLUMN };

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PICTURE_TABLE;
    private static final String SQL_UPDATE_ENTRIES = "UPDATE "  + PICTURE_TABLE;

    public Database(@Nullable Context context) {
        super(context, DB_ASTROFOTO, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PICTURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDBPicture(Photo picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, picture.getName());
        values.put(CAMERA_COLUMN, picture.getCamera());
        values.put(LENS_COLUMN , picture.getLens());
        values.put(SCHEDULE_COLUMN , picture.getSchedule());
        values.put(EXPOSURE_COLUMN, picture.getExpose());
        values.put(ISO_SENSITIVITY_COLUMN, picture.getIso_sensitivity());
        values.put(DIAPHRAGM_OPENING_COLUMN, picture.getDiaphragm_opening());
        values.put(FOCAL_DISTANCE_COLUMN, picture.getFocal_distance());
        values.put(DPI_RESOLUTION_COLUMN, picture.getDpi_resolution());
        values.put(FLASH_MODE_COLUMN, picture.getFlash_mode());
        values.put(WHITE_BALANCE_COLUMN, picture.getWhite_balance());
        values.put(ROTATION_COLUMN, picture.getRotation());
        values.put(TAGS_COLUMN, picture.getTags());
        values.put(WIDTH_COLUMN, picture.getWidth());
        values.put(HEIGHT_COLUMN, picture.getHeight());
        values.put(SIZE_COLUMN, picture.getSize());
        values.put(PATH_COLUMN, picture.getPath());
        values.put(GEOLOCATION_COLUMN, picture.getGeolocation());
        values.put(IMAGE_COLUMN, picture.getImage());
        db.insert(PICTURE_TABLE, null, values);
        db.close();
    }

    public Photo selectPicture(int codigo){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PICTURE_TABLE, Picturecol,CODE_COLUMN + " = ?",
                new String[]{String.valueOf(codigo)},null,null,null,null);

        if(cursor != null){ cursor.moveToFirst();  }

        Photo picture = new Photo(
            Integer.parseInt(cursor.getString(0)),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getString(5),
            cursor.getString(6),
            cursor.getString(7),
            cursor.getString(8),
            cursor.getString(9),
            cursor.getString(10),
            cursor.getString(11),
            cursor.getString(12),
            cursor.getString(13),
            cursor.getString(14),
            cursor.getString(15),
            cursor.getString(16),
            cursor.getString(17),
            cursor.getString(18),
            cursor.getBlob(19)
        );
        cursor.close();
        db.close();
        return picture;
    }

    public List<Photo> listAllPictures(){
        List<Photo> mPictures = new ArrayList<Photo>();
        String query = "SELECT * FROM " + PICTURE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do {
                Photo picture = new Photo();
                picture.setCode(Integer.parseInt(c.getString(0)));
                picture.setName(c.getString(1));
                picture.setCamera(c.getString(2));
                picture.setLens(c.getString(3));
                picture.setSchedule(c.getString(4));
                picture.setExpose(c.getString(5));
                picture.setIso_sensitivity(c.getString(6));
                picture.setDiaphragm_opening(c.getString(7));
                picture.setFocal_distance(c.getString(8));
                picture.setDpi_resolution(c.getString(9));
                picture.setFlash_mode(c.getString(10));
                picture.setWhite_balance(c.getString(11));
                picture.setRotation(c.getString(12));
                picture.setTags(c.getString(13));
                picture.setWidth(c.getString(14));
                picture.setHeight(c.getString(15));
                picture.setSize(c.getString(16));
                picture.setPath(c.getString(17));
                picture.setGeolocation(c.getString(18));
                picture.setImage(c.getBlob(19));
                mPictures.add(picture);
            } while (c.moveToNext());
            c.close();
            db.close();
        }
        return mPictures;
    }

    public void UpdatePicture(Photo picture){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, picture.getName());
        values.put(CAMERA_COLUMN, picture.getCamera());
        values.put(LENS_COLUMN , picture.getLens());
        values.put(SCHEDULE_COLUMN , picture.getSchedule());
        values.put(EXPOSURE_COLUMN, picture.getExpose());
        values.put(ISO_SENSITIVITY_COLUMN, picture.getIso_sensitivity());
        values.put(DIAPHRAGM_OPENING_COLUMN, picture.getDiaphragm_opening());
        values.put(FOCAL_DISTANCE_COLUMN, picture.getFocal_distance());
        values.put(DPI_RESOLUTION_COLUMN, picture.getDpi_resolution());
        values.put(FLASH_MODE_COLUMN, picture.getFlash_mode());
        values.put(WHITE_BALANCE_COLUMN, picture.getWhite_balance());
        values.put(ROTATION_COLUMN, picture.getRotation());
        values.put(TAGS_COLUMN, picture.getTags());
        values.put(WIDTH_COLUMN, picture.getWidth());
        values.put(HEIGHT_COLUMN, picture.getHeight());
        values.put(SIZE_COLUMN, picture.getSize());
        values.put(PATH_COLUMN, picture.getPath());
        values.put(GEOLOCATION_COLUMN, picture.getGeolocation());
        values.put(IMAGE_COLUMN, picture.getImage());
        values.put(IMAGE_COLUMN, picture.getImage());
        db.update(PICTURE_TABLE,values,CODE_COLUMN + " = ?", new String[]{ String.valueOf(picture.getCode())});
        db.close();
    }

    public void deletePicture(int codigo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PICTURE_TABLE,CODE_COLUMN + " = ?", new String[]{ String.valueOf(codigo)});
        db.close();
    }

}
