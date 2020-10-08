package com.fabianosdev.astrofoto.model;

import java.util.Date;

public class Photo {
    private int code;
    private String name = "";
    private String camera = "";
    private String lens = "";
    private String schedule= String.valueOf(new Date().getTime());
    private String expose = "";
    private String iso_sensitivity = "";
    private String diaphragm_opening = "";
    private String focal_distance = "";
    private String dpi_resolution = "";
    private String flash_mode = "";
    private String white_balance  = "";
    private String rotation = "";
    private String tags = "";
    private String width = "";
    private String height = "";
    private String size = "";
    private String path = "";
    private String geolocation = "0-0";
    private byte[] image; //usa-se byte[] pq blob no android não existe

    public Photo(){}

    public Photo(String name, String camera, String lens, String schedule, String expose, String iso_sensitivity, String diaphragm_opening, String focal_distance, String dpi_resolution, String flash_mode, String white_balance, String rotation, String tags, String width, String height, String size, String path, String geolocation, byte[] image) {
        this.name = name;
        this.camera = camera;
        this.lens = lens;
        this.schedule = schedule;
        this.expose = expose;
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
    }

    public Photo(int code, String name, String camera, String lens, String schedule, String expose, String iso_sensitivity, String diaphragm_opening, String focal_distance, String dpi_resolution, String flash_mode, String white_balance, String rotation, String tags, String width, String height, String size, String path, String geolocation, byte[] image) {
        this.code = code;
        this.name = name;
        this.camera = camera;
        this.lens = lens;
        this.schedule = schedule;
        this.expose = expose;
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
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getExpose() {
        return expose;
    }

    public void setExpose(String expose) {
        this.expose = expose;
    }

    public String getIso_sensitivity() {
        return iso_sensitivity;
    }

    public void setIso_sensitivity(String iso_sensitivity) {
        this.iso_sensitivity = iso_sensitivity;
    }

    public String getDiaphragm_opening() {
        return diaphragm_opening;
    }

    public void setDiaphragm_opening(String diaphragm_opening) {
        this.diaphragm_opening = diaphragm_opening;
    }

    public String getFocal_distance() {
        return focal_distance;
    }

    public void setFocal_distance(String focal_distance) {
        this.focal_distance = focal_distance;
    }

    public String getDpi_resolution() {
        return dpi_resolution;
    }

    public void setDpi_resolution(String dpi_resolution) {
        this.dpi_resolution = dpi_resolution;
    }

    public String getFlash_mode() {
        return flash_mode;
    }

    public void setFlash_mode(String flash_mode) {
        this.flash_mode = flash_mode;
    }

    public String getWhite_balance() {
        return white_balance;
    }

    public void setWhite_balance(String white_balance) {
        this.white_balance = white_balance;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
