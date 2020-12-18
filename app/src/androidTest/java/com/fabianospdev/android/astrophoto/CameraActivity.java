package com.fabianospdev.android.astrophoto;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith( AndroidJUnit4.class )
public class CameraActivity {

    @Rule
    public ActivityTestRule < MainActivity > mActivityTestRule = new ActivityTestRule <> ( MainActivity.class );

    @Rule
    public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant ( "android.permission.ACCESS_FINE_LOCATION" , "android.permission.READ_SMS" , "android.permission.ACCESS_COARSE_LOCATION" , "android.permission.CAMERA" , "android.permission.READ_EXTERNAL_STORAGE" , "android.permission.WRITE_EXTERNAL_STORAGE" , "android.permission.READ_PHONE_STATE" );

    @Test
    public void cameraActivity ( ) {
    }
}
