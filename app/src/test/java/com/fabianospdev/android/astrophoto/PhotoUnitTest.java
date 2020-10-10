package com.fabianospdev.android.astrophoto;

import com.fabianospdev.android.astrophoto.model.Photo;

import org.junit.Test;

import static org.junit.Assert.*;


public class PhotoUnitTest {

    @Test
    public void photoNumberArgs(){
        Photo p = new Photo(1,"name","camera","model","software","type",
            "dimenions","lens","2020-10-09 12:19:00", "exposure","exposurebias",
            "iso_sensitivity","diaphragm_opening","focal_distance",
            "dpi_resolution","flash_mode", "white_balance", "rotation",
            "tags", "width","height", "size", "path","geolocation", new byte[0]
        );
        Photo ps = new Photo("name","camera","model","software","type",
            "dimenions","lens","2020-10-09 12:19:00", "exposure","exposurebias",
            "iso_sensitivity","diaphragm_opening","focal_distance",
            "dpi_resolution","flash_mode", "white_balance", "rotation",
            "tags", "width","height", "size", "path","geolocation", new byte[0]
        );
    }

    @Test
    public void minImageSize(){
        Photo p = new Photo( 1,"name","camera","model","software","type",
            "dimenions","lens","2020-10-09 12:19:00", "exposure","exposurebias",
            "iso_sensitivity","diaphragm_opening","focal_distance",
            "dpi_resolution","flash_mode", "white_balance", "rotation",
            "tags", "width","height", "11", "path","geolocation", new byte[0]
        );
        int size = Integer.parseInt(p.getSize());
        int minLimit = 5;
        boolean result=false;
        if(size > minLimit){
            result = true;
        }
        assertTrue( result);
    }



}