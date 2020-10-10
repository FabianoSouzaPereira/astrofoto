package com.fabianospdev.android.astrophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.internal.PreviewConfigProvider;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.lifecycle.LifecycleOwner;

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
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
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

import com.fabianospdev.android.astrophoto.database.Database;
import com.fabianospdev.android.astrophoto.model.Photo;
import com.google.common.util.concurrent.ListenableFuture;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.fabianospdev.android.astrophoto.MainActivity.PERMISSION_CODE;
import static com.fabianospdev.android.astrophoto.MainActivity.Permission_All;
import static com.fabianospdev.android.astrophoto.MainActivity.Permissions;

public class CameraActivity extends AppCompatActivity implements CameraXConfig.Provider {

  private static boolean FILESAVE = true;
  private Button mBtnCapture;
  private TextureView textureView;
  //check orientation of output image
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

  static {
    ORIENTATIONS.append( Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }

  private String cameraId;
  private CameraDevice cameraDevice;
  private CameraCaptureSession cameraCaptureSessions;
  private CaptureRequest.Builder captureRequestBuilder;
  private Size imageDimension;
  private ImageReader imageReader;

  //Save to FILE
  private File file;
  private boolean mFlashSupported;
  private Handler mBackgroundHandler;
  private HandlerThread mBackgroundThread;

  //Save DB
  private final Integer CODE = null;
  private final String NAME = null;
  private final String CAMERA = null;
  private final String MODEL = null;
  private final String SOFTWARE = null;
  private final String TYPE = null;
  private final String DIMENIONS = null;
  private final String LENS = null;
  private final String SCHEDULE = null;
  private final String EXPOSURE = null;
  private final String EXPOSUREBIAS = null;
  private final String ISO_SENSITIVITY = null;
  private final String DIAPHRAGM_OPENING = null;
  private final String FOCAL_DISTANCE = null;
  private final String DPI_RESOLUTION = null;
  private final String FLASH_MODE = null;
  private final String WHITE_BALANCE = null;
  private final String ROTATION = null;
  private final String TAGS = null;
  private final String WIDTH = null;
  private final String HEIGHT = null;
  private final String SIZE = null;
  private final String PATH = null;
  private final String GEOLOCATION = null;
  private final byte[] IMAGE = null;


  CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
      cameraDevice = camera;
      createCameraPreview();
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
      cameraDevice.close();
    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int i) {
      cameraDevice.close();
      cameraDevice = null;
    }
  };

  public CameraActivity() {
  }

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_camera );

    textureView = findViewById(R.id.texture_View);
    assert textureView != null;
    textureView.setSurfaceTextureListener(textureListener);

    mBtnCapture = findViewById(R.id.btnCapture);
    mBtnCapture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        takePicture();
      }
    });
  }


  protected void takePicture() {
    if (cameraDevice == null)
      return;
    CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
      Size[] jpegSizes = null;

      if (characteristics != null) {
        jpegSizes = characteristics.get( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP ).getOutputSizes( ImageFormat.JPEG );

        //Capture image width custom size
        int width = 640;
        int height = 480;
        if ( jpegSizes != null && jpegSizes.length > 0 ) {
          width = jpegSizes[ 0 ].getWidth();
          height = jpegSizes[ 0 ].getHeight();
        }
        ImageReader reader = ImageReader.newInstance( width, height, ImageFormat.JPEG, 1 );

        List<Surface> outputSurface = new ArrayList<>();
        outputSurface.add( reader.getSurface() );
        outputSurface.add( new Surface( textureView.getSurfaceTexture() ) );

        CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest( CameraDevice.TEMPLATE_STILL_CAPTURE );
        captureBuilder.addTarget( reader.getSurface() );
        captureBuilder.set( CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO );

        //Check orientation base on device
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set( CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get( rotation ) );

        if(!(FILESAVE)){
          //TODO  fazer metodo para salvar no banco. Manter também metodo para salvar em arquivo
          Database db = new Database( this );
          if(db != null){
            db.addPhoto( new Photo( NAME, CAMERA, MODEL, SOFTWARE, TYPE, DIMENIONS, LENS,SCHEDULE,
                EXPOSURE, EXPOSUREBIAS,ISO_SENSITIVITY, DIAPHRAGM_OPENING, FOCAL_DISTANCE, DPI_RESOLUTION,
                FLASH_MODE, WHITE_BALANCE, ROTATION, TAGS, WIDTH, HEIGHT, SIZE, PATH, GEOLOCATION, IMAGE ) );
          }
        }

        if ( FILESAVE ) {
          file = new File( Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + UUID.randomUUID().toString() + ".jpg" );
          ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable( ImageReader imageReader ) {
              Image image = null;
              try {
                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[ 0 ].getBuffer();
                byte[] bytes = new byte[ buffer.capacity() ];
                buffer.get( bytes );
                save( bytes );
              } catch ( FileNotFoundException e ) {
                e.printStackTrace();
              } catch ( IOException e ) {
                e.printStackTrace();
              } finally {
                if ( image != null ) {
                  image.close();
                }
              }
            }

            private void save( byte[] bytes ) throws IOException {
              OutputStream outputStream = null;
              try {
                outputStream = new FileOutputStream( file );
                outputStream.write( bytes );
              } finally {
                if ( outputStream != null ) {
                  outputStream.close();
                }
              }
            }
          };

          reader.setOnImageAvailableListener( readerListener, mBackgroundHandler );
          CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted( @NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result ) {
              super.onCaptureCompleted( session, request, result );
              Toast.makeText( CameraActivity.this, "Saves " + file, Toast.LENGTH_SHORT ).show();
              createCameraPreview();
            }
          };

          cameraDevice.createCaptureSession( outputSurface, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured( @NonNull CameraCaptureSession cameraCaptureSession ) {
              try {
                cameraCaptureSession.capture( captureBuilder.build(), captureListener, mBackgroundHandler );
              } catch ( CameraAccessException e ) {
                e.printStackTrace();
              }
            }

            @Override
            public void onConfigureFailed( @NonNull CameraCaptureSession cameraCaptureSession ) {

            }
          }, mBackgroundHandler );
        }
      }
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void createCameraPreview() {
    try {
      SurfaceTexture texture = textureView.getSurfaceTexture();
      assert texture != null;
      texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
      Surface surface = new Surface(texture);
      captureRequestBuilder = cameraDevice.createCaptureRequest( CameraDevice.TEMPLATE_PREVIEW );
      captureRequestBuilder.addTarget( surface );
      cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
          if (cameraDevice == null)
            return;
          cameraCaptureSessions = cameraCaptureSession;
          updatePreview();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
          Toast.makeText(CameraActivity.this, "Changed ", Toast.LENGTH_SHORT).show();
        }
      }, null);

    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void updatePreview() {
    if (cameraDevice == null)
      Toast.makeText(CameraActivity.this, "Error ", Toast.LENGTH_SHORT).show();
    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
    try {
      cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void openCamera() {
    CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

    try {
      cameraId = manager.getCameraIdList()[0];
      CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
      StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
      assert map != null;
      imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        for(String permission: Permissions){
          if(ActivityCompat.checkSelfPermission(this , permission ) != PackageManager.PERMISSION_GRANTED){
            return;
          }
        }
      }
      manager.openCamera(cameraId, stateCallBack, null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  TextureView.SurfaceTextureListener textureListener = new SurfaceTextureListener() {
    @Override
    public void onSurfaceTextureAvailable( @NonNull SurfaceTexture surfaceTexture, int i, int i1 ) {
      openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged( @NonNull SurfaceTexture surfaceTexture, int i, int i1 ) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed( @NonNull SurfaceTexture surfaceTexture ) {
      return false;
    }

    @Override
    public void onSurfaceTextureUpdated( @NonNull SurfaceTexture surfaceTexture ) {

    }
  };

  @Override
  protected void onStart() {
    super.onStart();

  }

  @Override
  protected void onResume() {
    super.onResume();
    startBackgroundThread();
    if(textureView.isAvailable())
      openCamera();
    else
      textureView.setSurfaceTextureListener( textureListener );

    if (!hasPhonePermissions( this, Permissions )) {
      ActivityCompat.requestPermissions( this,Permissions,Permission_All );
    }
  }

  private void startBackgroundThread() {
    mBackgroundThread = new HandlerThread( "Camera Background" );
    mBackgroundThread.start();
    mBackgroundHandler = new Handler( mBackgroundThread.getLooper() );
  }

  @SuppressLint("InlinedApi")
  private static boolean hasPhonePermissions(Context context, String... permissions) {
    if(context != null && permissions != null){
      for(String permission: permissions){
        if(ActivityCompat.checkSelfPermission( context, permission ) != PackageManager.PERMISSION_GRANTED){
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if(requestCode == PERMISSION_CODE){
      if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        Toast.makeText( CameraActivity.this, "Permission allowed" , Toast.LENGTH_SHORT).show();
      }else{
        Toast.makeText( CameraActivity.this, "Permission denied" , Toast.LENGTH_SHORT).show();
      }
    }
  }


  @Override
  protected void onPause() {
    stopBackgroundTread();
    super.onPause();
  }

  private void stopBackgroundTread() {
    mBackgroundThread.quitSafely();
    try{
      mBackgroundThread.join();
      mBackgroundThread = null;
      mBackgroundHandler = null;
    } catch ( InterruptedException e ) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @NonNull
  @Override
  public CameraXConfig getCameraXConfig() {
    return Camera2Config.defaultConfig();
  }
}