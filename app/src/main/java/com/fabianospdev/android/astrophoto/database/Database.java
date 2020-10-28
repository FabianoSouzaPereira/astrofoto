package com.fabianospdev.android.astrophoto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.fabianospdev.android.astrophoto.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

  private static final int BD_VERSION = 1;
  private static final String DB_ASTROPHOTO = "db_AstroPhoto";
  private static final String PHOTO_TABLE = "tb_photo";
  private static final String CODE_COLUMN = "codigo";
  private static final String NAME_COLUMN = "name";
  private static final String CAMERA_COLUMN = "camera";
  private static final String MODEL_COLUMN = "model";
  private static final String SOFTWARE_COLUMN = "software";
  private static final String TYPE_COLUMN = "type";
  private static final String DIMENIONS = "dimensions";
  private static final String LENS_COLUMN = "lens";
  private static final String SCHEDULE_COLUMN = "schedule";
  private static final String EXPOSE_COLUMN = "expose";
  private static final String EXPOSUREBIAS_COLUMN = "exposurebias";
  private static final String ISO_SENSITIVITY_COLUMN = "iso";
  private static final String DIAPHRAGM_OPENING_COLUMN = "diaphragm_opening";
  private static final String FOCAL_DISTANCE_COLUMN = "focal_distance";
  private static final String DPI_RESOLUTION_COLUMN = "dpi";
  private static final String FLASH_MODE_COLUMN = "flash";
  private static final String WHITE_BALANCE_COLUMN = "balance";
  private static final String ROTATION_COLUMN = "rotation";
  private static final String TAGS_COLUMN = "tags";
  private static final String WIDTH_COLUMN = "width";
  private static final String HEIGHT_COLUMN = "height";
  private static final String SIZE_COLUMN = "size";
  private static final String PATH_COLUMN = "path";
  private static final String GEOLOCATION_COLUMN = "geolocation";
  private static final String  IMAGE_COLUMN = "image";

  private static final String SQL_CREATE_PHOTO_TABLE =
      "CREATE TABLE " + PHOTO_TABLE + " (" +
          CODE_COLUMN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
          NAME_COLUMN + " TEXT NOT NULL UNIQUE," +
          CAMERA_COLUMN + " TEXT NOT NULL," +
          MODEL_COLUMN + " TEXT NOT NULL," +
          SOFTWARE_COLUMN + " TEXT NOT NULL," +
          TYPE_COLUMN + " TEXT NOT NULL," +
          DIMENIONS + " TEXT NOT NULL," +
          LENS_COLUMN + " TEXT NOT NULL," +
          SCHEDULE_COLUMN + " TEXT NOT NULL," +
          EXPOSE_COLUMN + " TEXT NOT NULL," +
          EXPOSUREBIAS_COLUMN + " TEXT NOT NULL," +
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
          GEOLOCATION_COLUMN + " TEXT NOT NULL," +
          IMAGE_COLUMN + " BLOB)";

  private static final String[] Photocol = {CODE_COLUMN, NAME_COLUMN, CAMERA_COLUMN, MODEL_COLUMN, SOFTWARE_COLUMN,
      TYPE_COLUMN, DIMENIONS, LENS_COLUMN, SCHEDULE_COLUMN,  EXPOSE_COLUMN,  EXPOSUREBIAS_COLUMN, ISO_SENSITIVITY_COLUMN,
      DIAPHRAGM_OPENING_COLUMN,  FOCAL_DISTANCE_COLUMN, DPI_RESOLUTION_COLUMN, FLASH_MODE_COLUMN, WHITE_BALANCE_COLUMN,
      ROTATION_COLUMN, TAGS_COLUMN, WIDTH_COLUMN, HEIGHT_COLUMN,  SIZE_COLUMN,  PATH_COLUMN,  GEOLOCATION_COLUMN,
      IMAGE_COLUMN };

  private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PHOTO_TABLE;

  private static final String SQL_UPDATE_ENTRIES = "UPDATE IF EXISTS "  + PHOTO_TABLE;


  public Database( @Nullable Context context ) {
    super( context, DB_ASTROPHOTO, null, BD_VERSION );
  }

  @Override
  public void onCreate( SQLiteDatabase db ) {
      db.execSQL( SQL_CREATE_PHOTO_TABLE );
  }

  @Override
  public void onUpgrade( SQLiteDatabase sqLiteDatabase, int i, int i1 ) { }

  public void addPhoto( Photo photo){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(NAME_COLUMN, photo.getName());
    values.put(CODE_COLUMN,photo.getName());
    values.put(NAME_COLUMN,photo.getImage());
    values.put(CAMERA_COLUMN,photo.getCamera());
    values.put(MODEL_COLUMN,photo.getCamera());
    values.put(SOFTWARE_COLUMN,photo.getCamera());
    values.put(TYPE_COLUMN,photo.getCamera());
    values.put(DIMENIONS,photo.getCamera());
    values.put(LENS_COLUMN,photo.getLens());
    values.put(SCHEDULE_COLUMN,photo.getSchedule());
    values.put(EXPOSE_COLUMN,photo.getExposure());
    values.put(EXPOSUREBIAS_COLUMN,photo.getExposure());
    values.put(ISO_SENSITIVITY_COLUMN,photo.getIso_sensitivity());
    values.put(DIAPHRAGM_OPENING_COLUMN,photo.getDiaphragm_opening());
    values.put(FOCAL_DISTANCE_COLUMN,photo.getFocal_distance());
    values.put(DPI_RESOLUTION_COLUMN,photo.getDpi_resolution());
    values.put(FLASH_MODE_COLUMN,photo.getFlash_mode());
    values.put(WHITE_BALANCE_COLUMN,photo.getWhite_balance());
    values.put(ROTATION_COLUMN,photo.getRotation());
    values.put(TAGS_COLUMN,photo.getTags());
    values.put(WIDTH_COLUMN,photo.getWidth());
    values.put(HEIGHT_COLUMN,photo.getHeight());
    values.put(SIZE_COLUMN,photo.getSize());
    values.put(PATH_COLUMN,photo.getPath());
    values.put(GEOLOCATION_COLUMN,photo.getGeolocation());
    values.put(IMAGE_COLUMN, photo.getImage());

    db.insert(PHOTO_TABLE, null, values);
    db.close();
  }

  public Photo selectPhoto(int codigo){
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.query(PHOTO_TABLE, Photocol,CODE_COLUMN + " = ?",
        new String[]{String.valueOf(codigo)},null,null,null,null);

    if(cursor != null){ cursor.moveToFirst();  }
    Photo photo = new Photo(
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
        cursor.getString(19),
        cursor.getString(20),

        cursor.getString(21),
        cursor.getString(22),
        cursor.getString(23),
        cursor.getBlob( 24 )
    );
    cursor.close();
    db.close();
    return photo;
  }

  public List<Photo> listAllPhotos() {
    List<Photo> photos = new ArrayList<Photo>();
    String query = "SELECT * FROM " + PHOTO_TABLE;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor c = db.rawQuery(query,null);

    if(c.moveToFirst()) {
      do {
        Photo photo = new Photo();
        photo.setCode( Integer.parseInt(c.getString(0 )) );
        photo.setName( c.getString( 1 ) );
        photo.setCamera( c.getString( 2 ) );
        photo.setModel( c.getString( 3 ) );
        photo.setSoftware( c.getString( 4 ) );
        photo.setType(c.getString(5));
        photo.setDimenions(c.getString( 6 ));
        photo.setLens( c.getString( 7 ) );
        photo.setSchedule( c.getString( 8 ) );
        photo.setExposure( c.getString( 9 ) );
        photo.setExposurebias( c.getString( 10 ) );
        photo.setIso_sensitivity( c.getString( 11 ) );
        photo.setDiaphragm_opening( c.getString( 12 ) );
        photo.setFocal_distance( c.getString( 13 ) );
        photo.setDpi_resolution( c.getString( 14 ) );
        photo.setFlash_mode( c.getString( 15 ) );
        photo.setWhite_balance( c.getString( 16 ) );
        photo.setRotation( c.getString( 17) );
        photo.setTags( c.getString( 18 ) );
        photo.setWidth( c.getString( 19 ) );
        photo.setHeight( c.getString( 20 ) );
        photo.setSize( c.getString( 21) );
        photo.setPath( c.getString( 22 ) );
        photo.setGeolocation( c.getString( 23 ) );
        photo.setImage(c.getBlob( 24 ) );


      }while (c.moveToNext());
      c.close();
      db.close();
    }
  return  photos;
  }


  public void updatePhoto(Photo photo){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put(NAME_COLUMN, photo.getName());
    values.put(CODE_COLUMN,photo.getName());
    values.put(NAME_COLUMN,photo.getImage());
    values.put(CAMERA_COLUMN,photo.getCamera());
    values.put(MODEL_COLUMN,photo.getCamera());
    values.put(SOFTWARE_COLUMN,photo.getCamera());
    values.put(TYPE_COLUMN,photo.getCamera());
    values.put(DIMENIONS,photo.getCamera());
    values.put(LENS_COLUMN,photo.getLens());
    values.put(SCHEDULE_COLUMN,photo.getSchedule());
    values.put(EXPOSE_COLUMN,photo.getExposure());
    values.put(EXPOSUREBIAS_COLUMN,photo.getExposure());
    values.put(ISO_SENSITIVITY_COLUMN,photo.getIso_sensitivity());
    values.put(DIAPHRAGM_OPENING_COLUMN,photo.getDiaphragm_opening());
    values.put(FOCAL_DISTANCE_COLUMN,photo.getFocal_distance());
    values.put(DPI_RESOLUTION_COLUMN,photo.getDpi_resolution());
    values.put(FLASH_MODE_COLUMN,photo.getFlash_mode());
    values.put(WHITE_BALANCE_COLUMN,photo.getWhite_balance());
    values.put(ROTATION_COLUMN,photo.getRotation());
    values.put(TAGS_COLUMN,photo.getTags());
    values.put(WIDTH_COLUMN,photo.getWidth());
    values.put(HEIGHT_COLUMN,photo.getHeight());
    values.put(SIZE_COLUMN,photo.getSize());
    values.put(PATH_COLUMN,photo.getPath());
    values.put(GEOLOCATION_COLUMN,photo.getGeolocation());
    values.put(IMAGE_COLUMN, photo.getImage());

    db.update(PHOTO_TABLE, values, CODE_COLUMN + " = ?", new String[]{String.valueOf(photo.getCode())});
    db.close();
  }


  public void deletePhotos(int codigo){
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete( PHOTO_TABLE, CODE_COLUMN + " =  ?", new String[]{ String.valueOf(codigo)} );
    db.close();
  }
}
