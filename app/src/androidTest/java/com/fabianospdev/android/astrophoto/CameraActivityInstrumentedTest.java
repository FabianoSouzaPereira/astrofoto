package com.fabianospdev.android.astrophoto;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CameraActivityInstrumentedTest {

    private Context context = androidx.test.core.app.ApplicationProvider.getApplicationContext();
    CameraManager manager = ( CameraManager ) context.getSystemService ( android.content.Context.CAMERA_SERVICE );
   private int numCameras ;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.fabianospdev.android.astrophoto", appContext.getPackageName());
    }


    @org.junit.Test
    public void CaseCameraManagerServiceisNotNULL ( ) {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
       // CameraManager manager = ( CameraManager ) context.getSystemService ( android.content.Context.CAMERA_SERVICE );
        assertNotNull ( manager);
    }

    @org.junit.Test
    public void CaseCharacteristicsValueIsNotNull() throws android.hardware.camera2.CameraAccessException {
      //  CameraManager manager = ( CameraManager ) context.getSystemService ( android.content.Context.CAMERA_SERVICE );
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
       CameraCharacteristics characteristics = manager.getCameraCharacteristics ( "0" );
        android.util.Log.d ( "characteristics - CameraID = ", characteristics .toString () );
       assertNotNull (  characteristics  );
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void CaseCharacteristicsValueIsNull() throws android.hardware.camera2.CameraAccessException {
      //  CameraManager manager = ( CameraManager ) context.getSystemService ( android.content.Context.CAMERA_SERVICE );
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CameraCharacteristics characteristics = manager.getCameraCharacteristics ( "5" );
        android.util.Log.d ( "characteristics - CameraID = ", characteristics .toString () );
        assertNotNull (  characteristics  );
    }

    @org.junit.Test
    public void CaseCameraInUseISTrue( ) throws android.hardware.camera2.CameraAccessException {
        CameraActivity camera = new com.fabianospdev.android.astrophoto.CameraActivity ();
        String[ ] res = manager.getCameraIdList ( );
        numCameras = res.length;
        //todo  se camera em uso mostrar exception
    }
}