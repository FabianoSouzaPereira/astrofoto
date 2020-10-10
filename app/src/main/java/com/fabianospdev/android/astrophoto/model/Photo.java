package com.fabianospdev.android.astrophoto.model;

import java.util.Date;
import java.util.EmptyStackException;
import java.util.Objects;

public class Photo {
  private int code;
  private String name = "";
  private String camera = "";
  private String model = "";
  private String software = "";
  private String type = "";
  private String dimenions = "";
  private String lens = "";
  private String schedule = "";
  private String exposure = "";
  private String exposurebias = "";
  private String iso_sensitivity = "";
  private String diaphragm_opening = "";
  private String focal_distance = "";
  private String dpi_resolution = "";
  private String flash_mode = "";
  private String white_balance = "";
  private String rotation = "";
  private String tags = "";
  private String width = "";
  private String height = "";
  private String size = "";
  private String path = "";
  private String geolocation = "";
  private byte[] image;

  public Photo(){}

  public Photo( String name, String camera, String model, String software, String type, String dimenions, String lens, String schedule, String exposure, String exposurebias, String iso_sensitivity, String diaphragm_opening, String focal_distance, String dpi_resolution, String flash_mode, String white_balance, String rotation, String tags, String width, String height, String size, String path, String geolocation, byte[] image ) {
    this.name = name;
    this.camera = camera;
    this.model = model;
    this.software = software;
    this.type = type;
    this.dimenions = dimenions;
    this.lens = lens;
    this.schedule = schedule;
    this.exposure = exposure;
    this.exposurebias = exposurebias;
    this.iso_sensitivity = iso_sensitivity;
    this.diaphragm_opening = diaphragm_opening;
    this.focal_distance = focal_distance;
    this.dpi_resolution = dpi_resolution;
    this.flash_mode = flash_mode;
    this.white_balance = white_balance;
    this.rotation = rotation;
    this.tags = tags;
    this.width = width;
    this.height = height;
    this.size = size;
    this.path = path;
    this.geolocation = geolocation;
    this.image = image;
    validateConstructor(0);
  }

  public Photo( int code, String name, String camera, String model, String software, String type, String dimenions, String lens, String schedule, String exposure, String exposurebias, String iso_sensitivity, String diaphragm_opening, String focal_distance, String dpi_resolution, String flash_mode, String white_balance, String rotation, String tags, String width, String height, String size, String path, String geolocation, byte[] image ) {
    this.code = code;
    this.name = name;
    this.camera = camera;
    this.model = model;
    this.software = software;
    this.type = type;
    this.dimenions = dimenions;
    this.lens = lens;
    this.schedule = schedule;
    this.exposure = exposure;
    this.exposurebias = exposurebias;
    this.iso_sensitivity = iso_sensitivity;
    this.diaphragm_opening = diaphragm_opening;
    this.focal_distance = focal_distance;
    this.dpi_resolution = dpi_resolution;
    this.flash_mode = flash_mode;
    this.white_balance = white_balance;
    this.rotation = rotation;
    this.tags = tags;
    this.width = width;
    this.height = height;
    this.size = size;
    this.path = path;
    this.geolocation = geolocation;
    this.image = image;
    validateConstructor(1);
  }

  /** Validate if importantes parametres are null or empty */
  private void  validateConstructor(int var){

      if ( name == null || name.equals( "" ) ) {
        throw new IllegalArgumentException( "Name Can not be empty" );
      }
      if ( schedule == null || schedule.equals( "" ) ) {
        throw new IllegalArgumentException( "Schedule Can not be empty" );
      }
      if ( image == null ) {
        throw new IllegalArgumentException( "Image Can not be empty" );
      }
    if(var == 1){
      if(code <= 0) {
        throw new IllegalArgumentException( "Code Can not be less 1" );
      }
    }

  }

  public int getCode() {
    return code;
  }

  public void setCode( int code ) {
    if(code <= 0){
      throw new IllegalArgumentException("Code Can not be less than 1");
    }
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    if(name.isEmpty()){
      name = "";
    }
    this.name = name;
  }

  public String getCamera() {
    return camera;
  }

  public void setCamera( String camera ) {
    this.camera = camera;
  }

  public String getModel() {
    return model;
  }

  public void setModel( String model ) {
    this.model = model;
  }

  public String getSoftware() {
    return software;
  }

  public void setSoftware( String software ) {
    this.software = software;
  }

  public String getType() {
    return type;
  }

  public void setType( String type ) {
    this.type = type;
  }

  public String getDimenions() {
    return dimenions;
  }

  public void setDimenions( String dimenions ) {
    this.dimenions = dimenions;
  }

  public String getLens() {
    return lens;
  }

  public void setLens( String lens ) {
    this.lens = lens;
  }

  public String getSchedule() {
    return schedule;
  }

  public void setSchedule( String schedule ) {
    //todo teste de data
    if ( !Objects.equals( schedule, "" ) ){
      this.schedule = schedule;
    }
  }

  public String getExposure() {
    return exposure;
  }

  public void setExposure( String exposure ) {
    this.exposure = exposure;
  }

  public String getExposurebias() {
    return exposurebias;
  }

  public void setExposurebias( String exposurebias ) {
    this.exposurebias = exposurebias;
  }

  public String getIso_sensitivity() {
    return iso_sensitivity;
  }

  public void setIso_sensitivity( String iso_sensitivity ) {
    this.iso_sensitivity = iso_sensitivity;
  }

  public String getDiaphragm_opening() {
    return diaphragm_opening;
  }

  public void setDiaphragm_opening( String diaphragm_opening ) {
    this.diaphragm_opening = diaphragm_opening;
  }

  public String getFocal_distance() {
    return focal_distance;
  }

  public void setFocal_distance( String focal_distance ) {
    this.focal_distance = focal_distance;
  }

  public String getDpi_resolution() {
    return dpi_resolution;
  }

  public void setDpi_resolution( String dpi_resolution ) {
    this.dpi_resolution = dpi_resolution;
  }

  public String getFlash_mode() {
    return flash_mode;
  }

  public void setFlash_mode( String flash_mode ) {
    this.flash_mode = flash_mode;
  }

  public String getWhite_balance() {
    return white_balance;
  }

  public void setWhite_balance( String white_balance ) {
    this.white_balance = white_balance;
  }

  public String getRotation() {
    return rotation;
  }

  public void setRotation( String rotation ) {
    this.rotation = rotation;
  }

  public String getTags() {
    return tags;
  }

  public void setTags( String tags ) {
    this.tags = tags;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth( String width ) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight( String height ) {
    this.height = height;
  }

  public String getSize() {
    return size;
  }

  public void setSize( String size ) {
    this.size = size;
  }

  public String getPath() {
    return path;
  }

  public void setPath( String path ) {
    this.path = path;
  }

  public String getGeolocation() {
    return geolocation;
  }

  public void setGeolocation( String geolocation ) {
    this.geolocation = geolocation;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage( byte[] image ) {
    if ( !Objects.equals(image, null )) {
      this.image = image;
    }
  }
}
