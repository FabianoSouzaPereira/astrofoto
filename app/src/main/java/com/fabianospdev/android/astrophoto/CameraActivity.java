package com.fabianospdev.android.astrophoto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.core.app.ActivityCompat;

import com.fabianospdev.android.astrophoto.database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static androidx.camera.core.TorchState.OFF;
import static com.fabianospdev.android.astrophoto.MainActivity.PERMISSION_CODE;
import static com.fabianospdev.android.astrophoto.MainActivity.Permission_All;
import static com.fabianospdev.android.astrophoto.MainActivity.Permissions;

public class CameraActivity extends AppCompatActivity implements CameraXConfig.Provider {
    public final String TAG = "Log -> ";
    //check orientation of output image
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray ( );
    private static boolean FILESAVE = true;
    private static final int ERROR_CAMERA_DEVICE = 4;
    private static final int ERROR_CAMERA_DISABLED = 3;
    private static final int ERROR_CAMERA_IN_USE = 1;
    private static final int ERROR_CAMERA_SERVICE = 4;
    private static final int ERROR_MAX_CAMERAS_IN_USE = 2;

    static {
        ORIENTATIONS.append ( Surface.ROTATION_0 , 90 );
        ORIENTATIONS.append ( Surface.ROTATION_90 , 0 );
        ORIENTATIONS.append ( Surface.ROTATION_180 , 270 );
        ORIENTATIONS.append ( Surface.ROTATION_270 , 180 );
    }

    // CAMERA METADATA
    public static final Boolean BLACK_LEVEL_LOCK = false;

    public static final Integer COLOR_CORRECTION_ABERRATION_MODE = OFF;
    public static final Integer COLOR_CORRECTION_ABERRATION_MODE_FAST = 0x00000001;
    public static final Integer COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY = 0x00000002;
    public static final Integer COLOR_CORRECTION_ABERRATION_MODE_OFF = 0x00000000;

    public static final int COLOR_CORRECTION_MODE_FAST = 0x00000001;
    public static final int COLOR_CORRECTION_MODE_HIGH_QUALITY = 0x00000002;
    public static final int COLOR_CORRECTION_MODE_TRANSFORM_MATRIX = 0x00000000;

    public static final int CONTROL_AE_ANTIBANDING_MODE_50HZ = 0x00000001;
    public static final int CONTROL_AE_ANTIBANDING_MODE_60HZ = 0x00000002;
    public static final int CONTROL_AE_ANTIBANDING_MODE_AUTO = 0x00000003;
    public static final int CONTROL_AE_ANTIBANDING_MODE_OFF = 0x00000000;
    public static final int CONTROL_AE_MODE_OFF = 0x00000000;
    public static final int CONTROL_AE_MODE_ON = 0x00000001;
    public static final int CONTROL_AE_MODE_ON_ALWAYS_FLASH = 0x00000003;
    public static final int CONTROL_AE_MODE_ON_AUTO_FLASH = 0x00000002;
    public static final int CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE = 0x00000004;
    public static final int CONTROL_AE_MODE_ON_EXTERNAL_FLASH = 0x00000005;
    public static final int CONTROL_AE_PRECAPTURE_TRIGGER_CANCEL = 0x00000002;
    public static final int CONTROL_AE_PRECAPTURE_TRIGGER_IDLE = 0x00000000;
    public static final int CONTROL_AE_PRECAPTURE_TRIGGER_START = 0x00000001;
    public static final int CONTROL_AE_STATE_CONVERGED = 0x00000002;
    public static final int CONTROL_AE_STATE_FLASH_REQUIRED = 0x00000004;
    public static final int CONTROL_AE_STATE_INACTIVE = 0x00000000;
    public static final int CONTROL_AE_STATE_LOCKED = 0x00000003;
    public static final int CONTROL_AE_STATE_PRECAPTURE = 0x00000005;
    public static final int CONTROL_AE_STATE_SEARCHING = 0x00000001;

    public static final int CONTROL_AF_MODE_AUTO = 0x00000001;
    public static final int CONTROL_AF_MODE_CONTINUOUS_PICTURE = 0x00000004;
    public static final int CONTROL_AF_MODE_CONTINUOUS_VIDEO = 0x00000003;
    public static final int CONTROL_AF_MODE_EDOF = 0x00000005;
    public static final int CONTROL_AF_MODE_MACRO = 0x00000002;
    public static final int CONTROL_AF_MODE_OFF = 0x00000000;
    public static final int CONTROL_AF_SCENE_CHANGE_DETECTED = 0x00000001;
    public static final int CONTROL_AF_SCENE_CHANGE_NOT_DETECTED = 0x00000000;
    public static final int CONTROL_AF_STATE_ACTIVE_SCAN = 0x00000003;
    public static final int CONTROL_AF_STATE_FOCUSED_LOCKED = 0x00000004;
    public static final int CONTROL_AF_STATE_INACTIVE = 0x00000000;
    public static final int CONTROL_AF_STATE_NOT_FOCUSED_LOCKED = 0x00000005;
    public static final int CONTROL_AF_STATE_PASSIVE_FOCUSED = 0x00000002;
    public static final int CONTROL_AF_STATE_PASSIVE_SCAN = 0x00000001;
    public static final int CONTROL_AF_STATE_PASSIVE_UNFOCUSED = 0x00000006;
    public static final int CONTROL_AF_TRIGGER_CANCEL = 0x00000002;
    public static final int CONTROL_AF_TRIGGER_IDLE = 0x00000000;
    public static final int CONTROL_AF_TRIGGER_START = 0x00000001;

    public static final int CONTROL_AWB_MODE_AUTO = 0x00000001;
    public static final int CONTROL_AWB_MODE_CLOUDY_DAYLIGHT = 0x00000006;
    public static final int CONTROL_AWB_MODE_DAYLIGHT = 0x00000005;
    public static final int CONTROL_AWB_MODE_FLUORESCENT = 0x00000003;
    public static final int CONTROL_AWB_MODE_INCANDESCENT = 0x00000002;
    public static final int CONTROL_AWB_MODE_OFF = 0x00000000;
    public static final int CONTROL_AWB_MODE_SHADE = 0x00000008;
    public static final int CONTROL_AWB_MODE_TWILIGHT = 0x00000007;
    public static final int CONTROL_AWB_MODE_WARM_FLUORESCENT = 0x00000004;
    public static final int CONTROL_AWB_STATE_CONVERGED = 0x00000002;
    public static final int CONTROL_AWB_STATE_INACTIVE = 0x00000000;
    public static final int CONTROL_AWB_STATE_LOCKED = 0x00000003;
    public static final int CONTROL_AWB_STATE_SEARCHING = 0x00000001;

    public static final int CONTROL_CAPTURE_INTENT_CUSTOM = 0x00000000;
    public static final int CONTROL_CAPTURE_INTENT_MANUAL = 0x00000006;
    public static final int CONTROL_CAPTURE_INTENT_MOTION_TRACKING = 0x00000007;
    public static final int CONTROL_CAPTURE_INTENT_PREVIEW = 0x00000001;
    public static final int CONTROL_CAPTURE_INTENT_STILL_CAPTURE = 0x00000002;
    public static final int CONTROL_CAPTURE_INTENT_VIDEO_RECORD = 0x00000003;
    public static final int CONTROL_CAPTURE_INTENT_VIDEO_SNAPSHOT = 0x00000004;
    public static final int CONTROL_CAPTURE_INTENT_ZERO_SHUTTER_LAG = 0x00000005;

    public static final int CONTROL_EFFECT_MODE_AQUA = 0x00000008;
    public static final int CONTROL_EFFECT_MODE_BLACKBOARD = 0x00000007;
    public static final int CONTROL_EFFECT_MODE_MONO = 0x00000001;
    public static final int CONTROL_EFFECT_MODE_NEGATIVE = 0x00000002;
    public static final int CONTROL_EFFECT_MODE_OFF = 0x000000000;
    public static final int CONTROL_EFFECT_MODE_POSTERIZE = 0x00000005;
    public static final int CONTROL_EFFECT_MODE_SEPIA = 0x00000004;
    public static final int CONTROL_EFFECT_MODE_SOLARIZE = 0x00000003;
    public static final int CONTROL_EFFECT_MODE_WHITEBOARD = 0x00000006;

    public static final int CONTROL_EXTENDED_SCENE_MODE_BOKEH_CONTINUOUS = 0x00000002;
    public static final int CONTROL_EXTENDED_SCENE_MODE_BOKEH_STILL_CAPTURE = 0x00000001;
    public static final int CONTROL_EXTENDED_SCENE_MODE_DISABLED = 0x00000000;

    public static final int CONTROL_MODE_AUTO = 0x00000001;
    public static final int CONTROL_MODE_OFF = 0x00000000;
    public static final int CONTROL_MODE_OFF_KEEP_STATE = 0x00000003;
    public static final int CONTROL_MODE_USE_EXTENDED_SCENE_MODE = 0x00000004;
    public static final int CONTROL_MODE_USE_SCENE_MODE = 0x00000002;

    public static final int CONTROL_SCENE_MODE_ACTION = 0x00000002;
    public static final int CONTROL_SCENE_MODE_BARCODE = 0x00000010;
    public static final int CONTROL_SCENE_MODE_BEACH = 0x00000008;
    public static final int CONTROL_SCENE_MODE_CANDLELIGHT = 0x0000000f;
    public static final int CONTROL_SCENE_MODE_DISABLED = 0x00000000;
    public static final int CONTROL_SCENE_MODE_FACE_PRIORITY = 0x00000001;
    public static final int CONTROL_SCENE_MODE_FIREWORKS = 0x0000000c;
    public static final int CONTROL_SCENE_MODE_HDR = 0x00000012;
    public static final int CONTROL_SCENE_MODE_HIGH_SPEED_VIDEO = 0x00000011;
    public static final int CONTROL_SCENE_MODE_LANDSCAPE = 0x00000004;
    public static final int CONTROL_SCENE_MODE_NIGHT = 0x00000005;
    public static final int CONTROL_SCENE_MODE_NIGHT_PORTRAIT = 0x00000006;
    public static final int CONTROL_SCENE_MODE_PARTY = 0x0000000e;
    public static final int CONTROL_SCENE_MODE_PORTRAIT = 0x00000003;
    public static final int CONTROL_SCENE_MODE_SNOW = 0x00000009;
    public static final int CONTROL_SCENE_MODE_SPORTS = 0x0000000d;
    public static final int CONTROL_SCENE_MODE_STEADYPHOTO = 0x0000000b;
    public static final int CONTROL_SCENE_MODE_SUNSET = 0x0000000a;
    public static final int CONTROL_SCENE_MODE_THEATRE = 0x00000007;
    public static final int CONTROL_VIDEO_STABILIZATION_MODE_OFF = 0x00000000;
    public static final int CONTROL_VIDEO_STABILIZATION_MODE_ON = 0x00000001;

    public static final int DISTORTION_CORRECTION_MODE_FAST = 0x00000001;
    public static final int DISTORTION_CORRECTION_MODE_HIGH_QUALITY = 0x00000002;
    public static final int DISTORTION_CORRECTION_MODE_OFF = 0x00000000;

    public static final int EDGE_MODE_FAST = 0x00000001;
    public static final int EDGE_MODE_HIGH_QUALITY = 0x00000002;
    public static final int EDGE_MODE_OFF = 0x00000000;
    public static final int EDGE_MODE_ZERO_SHUTTER_LAG = 0x00000003;

    public static Integer FLASH_STATE = OFF;
    public static final int FLASH_MODE_OFF = 0x00000000;
    public static final int FLASH_MODE_SINGLE = 0x00000001;
    public static final int FLASH_MODE_TORCH = 0x00000002;
    public static final int FLASH_STATE_CHARGING = 0x00000001;
    public static final int FLASH_STATE_FIRED = 0x00000003;
    public static final int FLASH_STATE_PARTIAL = 0x000000040;
    public static final int FLASH_STATE_READY = 0x00000002;
    public static final int FLASH_STATE_UNAVAILABLE = 0x00000000;

    public static final int HOT_PIXEL_MODE_FAST = 0x00000001;
    public static final int HOT_PIXEL_MODE_HIGH_QUALITY = 0x00000002;
    public static final int HOT_PIXEL_MODE_OFF = 0x00000000;

    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_3 = 0x00000003;
    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL = 0x00000004;
    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_FULL = 0x00000001;
    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY = 0x00000002;
    public static final int INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED = 0x00000000;

    public static final int LENS_FACING_BACK = 0x00000001;
    public static final int LENS_FACING_EXTERNAL = 0x00000002;
    public static final int LENS_FACING_FRONT = 0x00000000;
    public static final int LENS_INFO_FOCUS_DISTANCE_CALIBRATION_APPROXIMATE = 0x00000001;
    public static final int LENS_INFO_FOCUS_DISTANCE_CALIBRATION_CALIBRATED = 0x00000002;
    public static final int LENS_INFO_FOCUS_DISTANCE_CALIBRATION_UNCALIBRATED = 0x00000000;
    public static final int LENS_OPTICAL_STABILIZATION_MODE_OFF = 0x00000000;
    public static final int LENS_OPTICAL_STABILIZATION_MODE_ON = 0x00000001;
    public static final int LENS_POSE_REFERENCE_GYROSCOPE = 0x00000001;
    public static final int LENS_POSE_REFERENCE_PRIMARY_CAMERA = 0x00000000;
    public static final int LENS_POSE_REFERENCE_UNDEFINED = 0x00000002;
    public static final int LENS_STATE_MOVING = 0x00000001;
    public static final int LENS_STATE_STATIONARY = 0x00000000;

    public static final int LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE_APPROXIMATE = 0x00000000;
    public static final int LOGICAL_MULTI_CAMERA_SENSOR_SYNC_TYPE_CALIBRATED = 0x00000001;
    public static final int NOISE_REDUCTION_MODE_FAST = 0x00000001;
    public static final int NOISE_REDUCTION_MODE_HIGH_QUALITY = 0x00000002;
    public static final int NOISE_REDUCTION_MODE_MINIMAL = 0x00000003;
    public static final int NOISE_REDUCTION_MODE_OFF = 0x00000000;
    public static final int NOISE_REDUCTION_MODE_ZERO_SHUTTER_LAG = 0x00000004;

    public static final int REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE = 0x00000000;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_BURST_CAPTURE = 0x00000006;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_CONSTRAINED_HIGH_SPEED_VIDEO = 0x00000009;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT = 0x00000008;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA = 0x0000000b;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_MANUAL_POST_PROCESSING = 0x00000002;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR = 0x00000001;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_MONOCHROME = 0x0000000c;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_MOTION_TRACKING = 0x0000000a;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_OFFLINE_PROCESSING = 0x0000000f;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_PRIVATE_REPROCESSING = 0x00000004;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_RAW = 0x00000003;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_READ_SENSOR_SETTINGS = 0x00000005;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_SECURE_IMAGE_DATA = 0x0000000d;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_SYSTEM_CAMERA = 0x0000000e;
    public static final int REQUEST_AVAILABLE_CAPABILITIES_YUV_REPROCESSING = 0x00000007;

    public static final int SCALER_CROPPING_TYPE_CENTER_ONLY = 0x00000000;
    public static final int SCALER_CROPPING_TYPE_FREEFORM = 0x00000001;

    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_BGGR = 0x00000003;
    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_GBRG = 0x00000002;
    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_GRBG = 0x00000001;
    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_MONO = 0x00000005;
    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_NIR = 0x00000006;
    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_RGB = 0x00000004;
    public static final int SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_RGGB = 0x00000000;
    public static final int SENSOR_INFO_TIMESTAMP_SOURCE_REALTIME = 0x00000001;
    public static final int SENSOR_INFO_TIMESTAMP_SOURCE_UNKNOWN = 0x00000000;

    public static final int SENSOR_REFERENCE_ILLUMINANT1_CLOUDY_WEATHER = 0x0000000a;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_COOL_WHITE_FLUORESCENT = 0x0000000e;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_D50 = 0x00000017;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_D55 = 0x00000014;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_D65 = 0x00000015;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_D75 = 0x00000016;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_DAYLIGHT = 0x00000001;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_DAYLIGHT_FLUORESCENT = 0x0000000c;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_DAY_WHITE_FLUORESCENT = 0x0000000d;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_FINE_WEATHER = 0x00000009;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_FLASH = 0x00000004;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_FLUORESCENT = 0x00000002;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_ISO_STUDIO_TUNGSTEN = 0x00000018;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_SHADE = 0x0000000b;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_STANDARD_A = 0x00000011;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_STANDARD_B = 0x00000012;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_STANDARD_C = 0x00000013;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_TUNGSTEN = 0x00000003;
    public static final int SENSOR_REFERENCE_ILLUMINANT1_WHITE_FLUORESCENT = 0x0000000f;

    public static final int SENSOR_TEST_PATTERN_MODE_COLOR_BARS = 0x00000002;
    public static final int SENSOR_TEST_PATTERN_MODE_COLOR_BARS_FADE_TO_GRAY = 0x00000003;
    public static final int SENSOR_TEST_PATTERN_MODE_CUSTOM1 = 0x00000100;
    public static final int SENSOR_TEST_PATTERN_MODE_OFF = 0x00000000;
    public static final int SENSOR_TEST_PATTERN_MODE_PN9 = 0x00000004;
    public static final int SENSOR_TEST_PATTERN_MODE_SOLID_COLOR = 0x00000001;

    public static final int SHADING_MODE_FAST = 0x00000001;
    public static final int SHADING_MODE_HIGH_QUALITY = 0x00000002;
    public static final int SHADING_MODE_OFF = 0x00000000;
    public static final int STATISTICS_FACE_DETECT_MODE_FULL = 0x00000002;
    public static final int STATISTICS_FACE_DETECT_MODE_OFF = 0x00000000;
    public static final int STATISTICS_FACE_DETECT_MODE_SIMPLE = 0x00000001;
    public static final int STATISTICS_LENS_SHADING_MAP_MODE_OFF = 0x00000000;
    public static final int STATISTICS_LENS_SHADING_MAP_MODE_ON = 0x00000001;
    public static final int STATISTICS_OIS_DATA_MODE_OFF = 0x00000000;
    public static final int STATISTICS_OIS_DATA_MODE_ON = 0x00000001;
    public static final int STATISTICS_SCENE_FLICKER_50HZ = 0x00000001;
    public static final int STATISTICS_SCENE_FLICKER_60HZ = 0x00000002;
    public static final int STATISTICS_SCENE_FLICKER_NONE = 0x00000000;
    public static final int SYNC_MAX_LATENCY_PER_FRAME_CONTROL = 0x00000000;
    public static final int SYNC_MAX_LATENCY_UNKNOWN = 0xffffffff;
    public static final int TONEMAP_MODE_CONTRAST_CURVE = 0x00000000;
    public static final int TONEMAP_MODE_FAST = 0x00000001;
    public static final int TONEMAP_MODE_GAMMA_VALUE = 0x00000003;
    public static final int TONEMAP_MODE_HIGH_QUALITY = 0x00000002;
    public static final int TONEMAP_MODE_PRESET_CURVE = 0x00000004;
    public static final int TONEMAP_PRESET_CURVE_REC709 = 0x000000010;
    public static final int ONEMAP_PRESET_CURVE_SRGB = 0x00000000;

    // DB
    private boolean POSTENABLE = false;
    private boolean EDITENABLE = false;
    private boolean DELETEENABLE = false;
    private int CODE = -1;
    private String NAME = null;
    private String CAMERA = null;
    private String MODEL = null;
    private String SOFTWARE = null;
    private String TYPE = null;
    private String DIMENIONS = null;
    private String LENS = null;
    private String SCHEDULE = null;
    private String EXPOSURE = null;
    private String EXPOSUREBIAS = null;
    private String ISO_SENSITIVITY = null;
    private String DIAPHRAGM_OPENING = null;
    private String FOCAL_DISTANCE = null;
    private String DPI_RESOLUTION = null;
    private String FLASH_MODE = null;
    private String WHITE_BALANCE = null;
    private String ROTATION = null;
    private String TAGS = null;
    private String WIDTH = null;
    private String HEIGHT = null;
    private String SIZE = null;
    private String PATH = null;
    private String GEOLOCATION = null;
    private String BRIGHTNESS = null;
    private byte[] IMAGE = null;
    private Button mBtnCapture;
    private TextureView textureView;
    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    TextureView.SurfaceTextureListener textureListener = new SurfaceTextureListener ( ) {
        @Override
        public void onSurfaceTextureAvailable ( @NonNull SurfaceTexture surfaceTexture , int i , int i1 ) {
            openCamera ( );
        }

        @Override
        public void onSurfaceTextureSizeChanged ( @NonNull SurfaceTexture surfaceTexture , int i , int i1 ) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed ( @NonNull SurfaceTexture surfaceTexture ) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated ( @NonNull SurfaceTexture surfaceTexture ) {

        }
    };
    private ImageReader imageReader;

    // SAVE TO FILE
    private File file;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback ( ) {
        @Override
        public void onOpened ( @NonNull CameraDevice camera ) {
            cameraDevice = camera;
            createCameraPreview ( );
        }

        @Override
        public void onDisconnected ( @NonNull CameraDevice cameraDevice ) {
            cameraDevice.close ( );
        }

        @Override
        public void onError ( @NonNull CameraDevice cameraDevice , int i ) {
            cameraDevice.close ( );
            cameraDevice = null;
            switch ( i ) {
                case 1:
                    Toast.makeText ( getApplicationContext ( ) , " Camera device is in use already" , android.widget.Toast.LENGTH_LONG ).show ( );
                    break;
                case 2:
                    Toast.makeText ( getApplicationContext ( ) , " There are too many other open camera devices" , android.widget.Toast.LENGTH_LONG ).show ( );
                    break;
                case 3:
                    Toast.makeText ( getApplicationContext ( ) , "Camera is desabled" , android.widget.Toast.LENGTH_LONG ).show ( );
                    break;
                case 4:
                    Toast.makeText ( getApplicationContext ( ) , "Camera device has encountered a fatal error" , android.widget.Toast.LENGTH_LONG ).show ( );
                    break;
                case 5:
                    Toast.makeText ( getApplicationContext ( ) , "Service has encountered a fatal error" , android.widget.Toast.LENGTH_LONG ).show ( );
                    break;
                default:
                    throw new IllegalStateException ( "Unexpected value: " + i );
            }

        }

        @Override
        public void onClosed ( @androidx.annotation.NonNull android.hardware.camera2.CameraDevice camera ) {
            super.onClosed ( camera );
        }
    };
    private HandlerThread mBackgroundThread;

    public CameraActivity ( ) {
    }

    @SuppressLint( "InlinedApi" )
    private static boolean hasPhonePermissions ( Context context , String... permissions ) {
        if ( context != null && permissions != null ) {
            for ( String permission : permissions ) {
                if ( ActivityCompat.checkSelfPermission ( context , permission ) != PackageManager.PERMISSION_GRANTED ) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_camera );

        textureView = findViewById ( R.id.texture_View );
        assert textureView != null;
        textureView.setSurfaceTextureListener ( textureListener );

        mBtnCapture = findViewById ( R.id.btnCapture );
        mBtnCapture.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                takePicture ( );
            }
        } );


    }

    protected void takePicture ( ) {
        if ( cameraDevice == null ) {
            return;
        }
        CameraManager manager = ( CameraManager ) getSystemService ( Context.CAMERA_SERVICE );
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics ( cameraDevice.getId ( ) );
            Size[] jpegSizes = null;

            if ( characteristics != null ) {
                jpegSizes = characteristics.get ( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP ).getOutputSizes ( ImageFormat.JPEG );

                //Capture image width custom size
                int width = 640;
                int height = 480;
                if ( jpegSizes != null && jpegSizes.length > 0 ) {
                    width = jpegSizes[ 0 ].getWidth ( );
                    height = jpegSizes[ 0 ].getHeight ( );
                }
                ImageReader reader = ImageReader.newInstance ( width , height , ImageFormat.JPEG , 1 );
                List < Surface > outputSurface = new ArrayList <> ( );
                outputSurface.add ( reader.getSurface ( ) );
                outputSurface.add ( new Surface ( textureView.getSurfaceTexture ( ) ) );

                /*TEMPLATE_STILL_CAPTURE  -  Create a request suitable for still image capture. Specifically, this means prioritizing image quality over frame rate.*/
                CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest ( CameraDevice.TEMPLATE_STILL_CAPTURE );
                captureBuilder.addTarget ( reader.getSurface ( ) );
                captureBuilder.set ( CaptureRequest.CONTROL_MODE , CameraMetadata.CONTROL_MODE_AUTO );

                //Check orientation base on device
                int rotation = getWindowManager ( ).getDefaultDisplay ( ).getRotation ( );
                captureBuilder.set ( CaptureRequest.JPEG_ORIENTATION , ORIENTATIONS.get ( rotation ) );

                if ( FILESAVE ) {
                    Log.d ( TAG , "FILESAVE" );
                    file = new File ( Environment.getExternalStorageDirectory ( ) + "/DCIM/astro_" + UUID.randomUUID ( ).toString ( ) + ".jpg" );
                    ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener ( ) {
                        @Override
                        public void onImageAvailable ( ImageReader imageReader ) {
                            Image image = null;
                            try {
                                image = reader.acquireLatestImage ( );
                                ByteBuffer buffer = image.getPlanes ( )[ 0 ].getBuffer ( );
                                byte[] bytes = new byte[ buffer.capacity ( ) ];
                                buffer.get ( bytes );
                                save ( bytes );
                            } catch ( FileNotFoundException e ) {
                                e.printStackTrace ( );
                            } catch ( IOException e ) {
                                e.printStackTrace ( );
                            } finally {
                                if ( image != null ) {
                                    image.close ( );
                                }
                            }
                        }

                        private void save ( byte[] bytes ) throws IOException {
                            OutputStream outputStream = null;
                            try {
                                outputStream = new FileOutputStream ( file );
                                outputStream.write ( bytes );
                            } finally {
                                if ( outputStream != null ) {
                                    outputStream.close ( );
                                }
                            }
                        }
                    };

                    if ( POSTENABLE ) {
                        //TODO  fazer metodo para salvar no banco. Manter também metodo para salvar em arquivo
                        Log.d ( TAG , "FILESAVE" );
                        android.util.Log.d ( TAG , "takePicture:  FILE" );
                        Database db = new Database ( this );
                        db.addPhoto ( new com.fabianospdev.android.astrophoto.model.Photo ( NAME , CAMERA , MODEL , SOFTWARE , TYPE , DIMENIONS , LENS , SCHEDULE , EXPOSURE , EXPOSUREBIAS , ISO_SENSITIVITY , DIAPHRAGM_OPENING , FOCAL_DISTANCE , DPI_RESOLUTION , FLASH_MODE , WHITE_BALANCE , ROTATION , TAGS , WIDTH , HEIGHT , SIZE , PATH , GEOLOCATION , IMAGE ) );
                    }

                    reader.setOnImageAvailableListener ( readerListener , mBackgroundHandler );
                    CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback ( ) {
                        @Override
                        public void onCaptureCompleted ( @androidx.annotation.NonNull android.hardware.camera2.CameraCaptureSession session , @androidx.annotation.NonNull android.hardware.camera2.CaptureRequest request , @androidx.annotation.NonNull android.hardware.camera2.TotalCaptureResult result ) {
                            super.onCaptureCompleted ( session , request , result );

                            /* All instances of CameraMetadata are immutable. The list of keys with getKeys() never changes, nor do the values returned by any key with
                            #get throughout the lifetime of the object. */

                            Integer CONTROL_AE_EXPOSURE_COMPENSATION = result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION );
                            Float LENS_APERTURE = result.get ( android.hardware.camera2.CaptureResult.LENS_APERTURE );
                            Float LENS = result.get ( android.hardware.camera2.CaptureResult.LENS_APERTURE );
                            if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ) {
                                CAMERA = result.get ( android.hardware.camera2.CaptureResult.LOGICAL_MULTI_CAMERA_ACTIVE_PHYSICAL_ID );
                            }
                            Integer MODEL = result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_MODE );
                            result.get ( android.hardware.camera2.CaptureResult.NOISE_REDUCTION_MODE );
                            BRIGHTNESS = result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION ).toString ( );  // Adjustment to auto-exposure (AE) target image brightness.
                            ISO_SENSITIVITY = result.get ( android.hardware.camera2.CaptureResult.SENSOR_SENSITIVITY ).toString ( );
                            result.get ( android.hardware.camera2.CaptureResult.BLACK_LEVEL_LOCK );                                                                             // Whether black-level compensation is locked to its current values, or is free to vary.

                            result.get ( android.hardware.camera2.CaptureResult.COLOR_CORRECTION_ABERRATION_MODE );                                   //  Mode of operation for the chromatic aberration correction algorithm.
                            WHITE_BALANCE = result.get ( android.hardware.camera2.CaptureResult.COLOR_CORRECTION_GAINS ).toString ( );    // Gains applying to Bayer raw color channels for white-balance.
                            result.get ( android.hardware.camera2.CaptureResult.COLOR_CORRECTION_MODE );                                                           // The mode control selects how the image data is converted from the sensor's native color into linear sRGB color.
                            result.get ( android.hardware.camera2.CaptureResult.COLOR_CORRECTION_TRANSFORM );                                               // A color transform matrix to use to transform from sensor RGB color space to output linear sRGB color space.

                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE );

                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_ANTIBANDING_MODE );                        // The desired setting for the camera device's auto-exposure algorithm's antibanding compensation.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_LOCK );                                                     // Whether auto-exposure (AE) is currently locked to its latest calculated values.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_PRECAPTURE_TRIGGER );
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_REGIONS );                                              //  List of metering areas to use for auto-exposure adjustment.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_STATE );                                                   // Current state of the auto-exposure (AE) algorithm.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_TARGET_FPS_RANGE );                        // Range over which the auto-exposure routine can adjust the capture frame rate to maintain good exposure.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION );            // Adjustment to auto-exposure (AE) target image brightness.

                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AF_MODE );                                                    // Whether auto-focus (AF) is currently enabled, and what mode it is set to.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AF_REGIONS );                                                // List of metering areas to use for auto-focus.
                            if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P ) {
                                result.get ( android.hardware.camera2.CaptureResult.CONTROL_AF_SCENE_CHANGE );                               // Whether a significant scene change is detected within the currently-set AF region(s).
                                result.get ( android.hardware.camera2.CaptureResult.DISTORTION_CORRECTION_MODE );                         //
                                result.get ( android.hardware.camera2.CaptureResult.LENS_DISTORTION );                                                    //
                            }
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AF_STATE );                                                  // Current state of auto-focus (AF) algorithm.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AF_TRIGGER );                                             // Whether the camera device will trigger autofocus for this request.

                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AWB_LOCK );                                               // Whether auto-white balance (AWB) is currently locked to its latest calculated values.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AWB_MODE );                                            // Whether auto-white balance (AWB) is currently setting the color transform fields, and what its illumination target is.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AWB_LOCK );                                             // Whether auto-white balance (AWB) is currently locked to its latest calculated values.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AWB_REGIONS );
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_AWB_STATE );

                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_CAPTURE_INTENT );                               // Information to the camera device 3A (auto-exposure, auto-focus, auto-white balance) routines about the purpose of this capture, to help the camera device to decide optimal 3A strategy.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_EFFECT_MODE );                                    // A special color effect to apply.
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_ENABLE_ZSL );                                         //Information to the camera device 3A (auto-exposure, auto-focus, auto-white balance) routines about the purpose of this capture, to help the camera device to decide optimal 3A strategy.
                            if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R ) {
                                result.get ( android.hardware.camera2.CaptureResult.CONTROL_EXTENDED_SCENE_MODE );             // Whether extended scene mode is enabled for a particular capture request.
                                result.get ( android.hardware.camera2.CaptureResult.CONTROL_ZOOM_RATIO );                                 // The desired zoom ratio  Instead of using CaptureRequest#SCALER_CROP_REGION for zoom, the application can now choose to use this tag to specify the desired zoom level.
                            }
                            result.get ( android.hardware.camera2.CaptureResult.CONTROL_POST_RAW_SENSITIVITY_BOOST );
                            result.get ( android.hardware.camera2.CaptureResult.EDGE_MODE );                                                           // Operation mode for edge enhancement.
                            FLASH_MODE = result.get ( android.hardware.camera2.CaptureResult.FLASH_MODE ).toString ( );        // The desired mode for for the camera device's flash control.
                            FLASH_STATE = result.get ( android.hardware.camera2.CaptureResult.FLASH_STATE );                          // Current state of the flash unit.
                            result.get ( android.hardware.camera2.CaptureResult.HOT_PIXEL_MODE );                                               // Operational mode for hot pixel correction.

                            GEOLOCATION = result.get ( android.hardware.camera2.CaptureResult.JPEG_GPS_LOCATION ).toString ( ); //A location object to use when generating image GPS metadata.
                            ROTATION = result.get ( android.hardware.camera2.CaptureResult.JPEG_ORIENTATION ).toString ( );  // The orientation for a JPEG image.
                            result.get ( android.hardware.camera2.CaptureResult.JPEG_QUALITY );                                                      //Compression quality of the final JPEG image.
                            result.get ( android.hardware.camera2.CaptureResult.JPEG_THUMBNAIL_QUALITY );                             // Compression quality of JPEG thumbnail.
                            result.get ( android.hardware.camera2.CaptureResult.JPEG_THUMBNAIL_SIZE );                                    // Resolution of embedded JPEG thumbnail.

                            result.get ( android.hardware.camera2.CaptureResult.SCALER_CROP_REGION );

                            result.get ( android.hardware.camera2.CaptureResult.LENS_FILTER_DENSITY );                                     // The desired setting for the lens neutral density filter(s).
                            result.get ( android.hardware.camera2.CaptureResult.LENS_FOCAL_LENGTH );                                      // The desired lens focal length; used for optical zoom.
                            FOCAL_DISTANCE = result.get ( android.hardware.camera2.CaptureResult.LENS_FOCUS_DISTANCE ).toString ( );  // Desired distance to plane of sharpest focus, measured from frontmost surface of the lens.
                            result.get ( android.hardware.camera2.CaptureResult.LENS_FOCUS_RANGE );                                       // The range of scene distances that are in sharp focus (depth of field).
                            result.get ( android.hardware.camera2.CaptureResult.LENS_INTRINSIC_CALIBRATION );                     // The parameters for this camera device's intrinsic calibration.
                            result.get ( android.hardware.camera2.CaptureResult.LENS_OPTICAL_STABILIZATION_MODE );      // Sets whether the camera device uses optical image stabilization (OIS) when capturing images.
                            result.get ( android.hardware.camera2.CaptureResult.LENS_POSE_ROTATION );                                 // The orientation of the camera relative to the sensor coordinate system.
                            result.get ( android.hardware.camera2.CaptureResult.LENS_POSE_TRANSLATION );                          // Position of the camera optical center.
                            result.get ( android.hardware.camera2.CaptureResult.LENS_STATE );                                                   // Current lens status.
                            result.get ( android.hardware.camera2.CaptureResult.REPROCESS_EFFECTIVE_EXPOSURE_FACTOR ); //The amount of exposure time increase factor applied to the original output frame by the application processing before sending for reprocessing.
                            result.get ( android.hardware.camera2.CaptureResult.LENS_RADIAL_DISTORTION );                         // This field was deprecated in API level 28.  This field was inconsistently defined in terms of its normalization. Use CameraCharacteristics#LENS_DISTORTION instead.

                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_DYNAMIC_BLACK_LEVEL );             //A per-frame dynamic black level offset for each of the color filter arrangement (CFA) mosaic channels.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_DYNAMIC_WHITE_LEVEL );              //Maximum raw value output by sensor for this frame.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_SENSITIVITY );
                            EXPOSURE = result.get ( android.hardware.camera2.CaptureResult.SENSOR_EXPOSURE_TIME ).toString ( );  // ESSE AQUI É O FOCO  - Duration each pixel is exposed to light.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_FRAME_DURATION );                      // Duration from start of frame exposure to start of next frame exposure.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_GREEN_SPLIT );                               // The worst-case divergence between Bayer green channels.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_NEUTRAL_COLOR_POINT );          // The estimated camera neutral color in the native sensor colorspace at the time of capture.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_NOISE_PROFILE );                           // Noise model coefficients for each CFA mosaic channel.
                            result.get ( android.hardware.camera2.CaptureResult.SENSOR_ROLLING_SHUTTER_SKEW );         // Duration between the start of exposure for the first row of the image sensor, and the start of exposure for one past the last row of the image sensor.
                            android.util.Range EXPOSURE_TIME_RANGE = characteristics.get ( CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE );
                            Long FRAME_DURATION = characteristics.get ( CameraCharacteristics.SENSOR_INFO_MAX_FRAME_DURATION );

                            int[] MANUAL_SENSOR = characteristics.get ( android.hardware.camera2.CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES );
                            int MS;
                            for ( int i = 0 ; MANUAL_SENSOR.length > i ; i++ ) {
                                MS = MANUAL_SENSOR[ i ];
                                Log.d ( TAG , "MANUAL_SENSOR  = " + MS );
                            }
                            Log.d ( TAG , "CAMERA = " + CAMERA );
                            Log.d ( TAG , "SENSOR_SENSITIVITY  = " + ISO_SENSITIVITY );
                            Log.d ( TAG , "CONTROL_AE_EXPOSURE_COMPENSATION = " + CONTROL_AE_EXPOSURE_COMPENSATION );
                            Log.d ( TAG , "LENS = " + LENS );
                            Log.d ( TAG , "LENS_APERTURE = " + LENS_APERTURE );
                            Log.d ( TAG , "Model = " + MODEL );
                            Log.d ( TAG , "FLASH_MODEL  = " + FLASH_MODE );
                            Log.d ( TAG , "FLASH_STATE  = " + FLASH_STATE );
                            Log.d ( TAG , "EXPOSURE  = " + EXPOSURE );
                            Log.d ( TAG , "FOCAL_DISTANCE = " + FOCAL_DISTANCE );
                            Log.d ( TAG , "EXPOSURE_TIME_RANGE = " + EXPOSURE_TIME_RANGE ); //know the lower and upper values supported by your phone
                            Log.d ( TAG , "FRAME_DURATION  = " + FRAME_DURATION );


                            android.widget.Toast.makeText ( CameraActivity.this , "Saves " + file , android.widget.Toast.LENGTH_SHORT ).show ( );
                            createCameraPreview ( );
                        }
                    };


                    /** A configured capture session for a CameraDevice, used for capturing images from the camera or reprocessing images captured from the camera in the
                     * same session previously.  */
                    cameraDevice.createCaptureSession ( outputSurface , new CameraCaptureSession.StateCallback ( ) {
                        @Override
                        public void onConfigured ( @NonNull CameraCaptureSession cameraCaptureSession ) {
                            try {
                                cameraCaptureSession.capture ( captureBuilder.build ( ) , captureListener , mBackgroundHandler );
                            } catch ( CameraAccessException e ) {
                                e.printStackTrace ( );
                            }
                        }

                        @Override
                        public void onConfigureFailed ( @NonNull CameraCaptureSession cameraCaptureSession ) {

                        }
                    } , mBackgroundHandler );
                }
            }
        } catch ( CameraAccessException e ) {
            e.printStackTrace ( );
        }
    }

    private void createCameraPreview ( ) {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture ( );
            assert texture != null;
            texture.setDefaultBufferSize ( imageDimension.getWidth ( ) , imageDimension.getHeight ( ) );
            Surface surface = new Surface ( texture );
            captureRequestBuilder = cameraDevice.createCaptureRequest ( CameraDevice.TEMPLATE_PREVIEW );
            captureRequestBuilder.addTarget ( surface );
            cameraDevice.createCaptureSession ( Arrays.asList ( surface ) , new CameraCaptureSession.StateCallback ( ) {
                @Override
                public void onConfigured ( @NonNull CameraCaptureSession cameraCaptureSession ) {
                    if ( cameraDevice == null ) {
                        return;
                    }
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview ( );
                }

                @Override
                public void onConfigureFailed ( @NonNull CameraCaptureSession cameraCaptureSession ) {
                    Toast.makeText ( CameraActivity.this , "Changed " , Toast.LENGTH_SHORT ).show ( );
                }
            } , null );

        } catch ( CameraAccessException e ) {
            e.printStackTrace ( );
        }
    }

    private void updatePreview ( ) {
        if ( cameraDevice == null ) {
            Toast.makeText ( CameraActivity.this , "Error " , Toast.LENGTH_SHORT ).show ( );
        }
        captureRequestBuilder.set ( CaptureRequest.CONTROL_MODE , CaptureRequest.CONTROL_MODE_AUTO );
        try {
            cameraCaptureSessions.setRepeatingRequest ( captureRequestBuilder.build ( ) , null , mBackgroundHandler );
        } catch ( CameraAccessException e ) {
            e.printStackTrace ( );
        }
    }

    private void openCamera ( ) {
        CameraManager manager = ( CameraManager ) getSystemService ( Context.CAMERA_SERVICE );

        try {
            cameraId = manager.getCameraIdList ( )[ 0 ];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics ( cameraId );
            StreamConfigurationMap map = characteristics.get ( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP );
            assert map != null;
            imageDimension = map.getOutputSizes ( SurfaceTexture.class )[ 0 ];

            if ( ActivityCompat.checkSelfPermission ( this , Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                for ( String permission : Permissions ) {
                    if ( ActivityCompat.checkSelfPermission ( this , permission ) != PackageManager.PERMISSION_GRANTED ) {
                        return;
                    }
                }
            }
            manager.openCamera ( cameraId , stateCallBack , null );
        } catch ( CameraAccessException e ) {
            e.printStackTrace ( );
        }
    }

    @Override
    protected void onStart ( ) {
        super.onStart ( );

    }

    @Override
    protected void onResume ( ) {
        super.onResume ( );
        startBackgroundThread ( );
        if ( textureView.isAvailable ( ) ) {
            openCamera ( );
        } else {
            textureView.setSurfaceTextureListener ( textureListener );
        }

        if ( !hasPhonePermissions ( this , Permissions ) ) {
            ActivityCompat.requestPermissions ( this , Permissions , Permission_All );
        }
    }

    private void startBackgroundThread ( ) {
        mBackgroundThread = new HandlerThread ( "Camera Background" );
        mBackgroundThread.start ( );
        mBackgroundHandler = new Handler ( mBackgroundThread.getLooper ( ) );
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults ) {
        if ( requestCode == PERMISSION_CODE ) {
            if ( grantResults.length > 0 && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText ( CameraActivity.this , "Permission allowed" , Toast.LENGTH_SHORT ).show ( );
            } else {
                Toast.makeText ( CameraActivity.this , "Permission denied" , Toast.LENGTH_SHORT ).show ( );
            }
        }
    }


    @Override
    protected void onPause ( ) {
        stopBackgroundTread ( );
        super.onPause ( );
    }

    private void stopBackgroundTread ( ) {
        mBackgroundThread.quitSafely ( );
        try {
            mBackgroundThread.join ( );
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch ( InterruptedException e ) {
            e.printStackTrace ( );
        }
    }

    @Override
    protected void onStop ( ) {
        super.onStop ( );
    }

    @Override
    public void onBackPressed ( ) {
        super.onBackPressed ( );
    }

    @Override
    protected void onRestart ( ) {
        super.onRestart ( );
    }

    @Override
    protected void onDestroy ( ) {
        super.onDestroy ( );
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig ( ) {
        return Camera2Config.defaultConfig ( );
    }
}