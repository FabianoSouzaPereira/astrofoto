package com.fabianospdev.android.astrophoto;

/*
  TEMPLATE_MANUAL  - A basic template for direct application control of capture parameters. All automatic control is disabled (auto-exposure, auto-white balance, auto-focus),
  and post-processing parameters are set to preview quality. The manual capture parameters (exposure, sensitivity, and so on) are set to reasonable defaults,
  but should be overriden by the application depending on the intended use case. This template is guaranteed to be supported on camera devices that support
  the CameraMetadata#REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR capability.

 AUDIO_RESTRICTION_NONE  -  API 30  No vibration or sound muting for this camera device. This is the default mode for all camera devices.
 AUDIO_RESTRICTION_VIBRATION  -  API 30  Mute vibration from ringtones, alarms or notifications while this camera device is in use.
 AUDIO_RESTRICTION_VIBRATION_SOUND  -   API 30  Mute vibration and sound from ringtones, alarms or notifications while this camera device is in use.

  TEMPLATE_ZERO_SHUTTER_LAG  -  Create a request suitable for zero shutter lag still capture. This means means maximizing image quality without compromising preview frame rate.
  AE/AWB/AF should be on auto mode. This is intended for application-operated ZSL. For device-operated ZSL, use CaptureRequest#CONTROL_ENABLE_ZSL if available.
  This template is guaranteed to be supported on camera devices that support the CameraMetadata#REQUEST_AVAILABLE_CAPABILITIES_PRIVATE_REPROCESSING
  capability or the CameraMetadata#REQUEST_AVAILABLE_CAPABILITIES_YUV_REPROCESSING capability.
 */


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import android.hardware.camera2.CameraDevice;


public class SettingsActivity extends AppCompatActivity {
  public static int AUDIO_RESTRICTION_NONE = 0;
  public static int AUDIO_RESTRICTION_VIBRATION  = 1;
  public static int AUDIO_RESTRICTION_VIBRATION_SOUND  = 3;
  public static int TEMPLATE_STILL_CAPTURE = 2;
  public static int TEMPLATE_ZERO_SHUTTER_LAG = 5;
  public static int TEMPLATE_MANUAL = 6;
  public static int TEMPLATE_PREVIEW;
  public static int TEMPLATE_RECORD;
  public static int TEMPLATE_VIDEO_SNAPSHOT;
  public int cameraId = -1;
  private CameraDevice cameraDevice;
  private CameraDevice.StateCallback cameraStateCallback;
  public static  int audioRestrition = 0;


  @Override
  protected void onCreate( android.os.Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( com.fabianospdev.android.astrophoto.R.layout.settings_activity );
    getSupportFragmentManager().beginTransaction().replace( com.fabianospdev.android.astrophoto.R.id.settings, new com.fabianospdev.android.astrophoto.SettingsActivity.SettingsFragment() ).commit();
    androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();

    if ( actionBar != null ) {
      actionBar.setDisplayHomeAsUpEnabled( true );
    }

    if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R ) {
      audioRestrition =  AudioRestition();
    }

          /* void	setCameraAudioRestriction(int mode)
          Set audio restriction mode when this CameraDevice is being used.*/


  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  /**  Get currently applied global camera audio restriction mode.  API 30 */
  @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.R)
  public int AudioRestition(){
    int AR = 0;
    try {
      AR =  cameraDevice.getCameraAudioRestriction();
    } catch ( android.hardware.camera2.CameraAccessException e ) {
      e.printStackTrace();
    }
    return AR;
  }

  public static class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences( Bundle savedInstanceState, String rootKey ) {
      setPreferencesFromResource( R.xml.root_preferences, rootKey );
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}