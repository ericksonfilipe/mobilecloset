package br.edu.ufcg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * A camera view
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private Camera.PictureCallback mPictureCallback;

    private List<ImageListenerIF> listeners;
    private byte[] imageData;

    public CameraView(Context context) {
        super(context);
        if (mCamera == null) {
            init();
        }
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mCamera == null) {
            init();
        }
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (mCamera == null) {
            init();
        }
    }

    /**
     * Inicializa os atributos
     */
    public void init() {
        listeners = new ArrayList<ImageListenerIF>();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPictureCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] imageData, Camera c) {
                System.out.println(" ----------- onPictureTaken + ");
                if (imageData != null) {

                    mCamera.startPreview();
                    System.out.println(" ----------- onPictureTaken + "
                            + imageData.length);
                    CameraView.this.imageData = imageData;
                    CameraView.this.notifyListeners();

                }
            }
        };

    }

    /**
     * Tira a fotografia pelo Hardware da sub-camera
     */
    public void takePicture() {
    	
    	// if not initialized, initialize
    	if (mCamera == null) init();

        mCamera.takePicture(null, mPictureCallback, mPictureCallback);
    }

    /**
     * Callback called when the surface is created
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        System.out.println(" ----------- PREVIEW SURFACE CREATED");
        mCamera = Camera.open();

        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            Log.e("Erro", "Failed to Set Camera Holder");
        }
    }

    /**
     * Callback chamado quando o surface é destruido
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        System.out.println(" ----------- PREVIEW SURFACE DESTRYED");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
    
    protected void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;
        try
        {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        }
        catch (Exception e1)
        {
        }
    }

    /**
     * Callback chamado quando o surface é modificado
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        System.out.println(" ----------- PREVIEW SURFACE CHANGED");
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setAntibanding(Parameters.ANTIBANDING_60HZ);
        parameters.setColorEffect(Parameters.EFFECT_NONE);
        parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        parameters.setRotation(90);
        parameters.setPictureFormat(ImageFormat.JPEG);
        //parameters.setSceneMode(Parameters.SCENE_MODE_PORTRAIT);
        parameters.setWhiteBalance(Parameters.WHITE_BALANCE_FLUORESCENT);
        setDisplayOrientation(mCamera, 90);
        
        
        
        List<Size> sizes = parameters.getSupportedPreviewSizes();
        int largura = 0;
        int altura = 0;
        for (Size it:sizes) {
        	if (it.width >= largura  && it.height >= altura) {
        		largura = it.width;
        		altura = it.height;
        	}
        }
        System.out.println("PRINT!!!  largura="+largura+"   altura="+altura);
        parameters.setPreviewSize(largura, altura);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    /**
     * Releases the camera
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

    }

    /**
     * Freezes the camera
     */
    public void freezeCamera() {
        if (mCamera != null)
            mCamera.stopPreview();
    }

    /**
     * Unfreezes the camera
     */
    public void unfreezeCamera() {
        if (mCamera != null)
            mCamera.startPreview();
    }

    /**
     * Adds a image listener
     * @param listener a image listener
     */
    public void addImageListener(ImageListenerIF listener) {
        listeners.add(listener);
    }

    /**
     * Removes a image listener
     * @param listener the listener to be deleted
     */
    public void removeImageListener(ImageListenerIF listener) {
    	listeners.remove(listener);
    }

    /**
     * Notifies listeners
     */
    public void notifyListeners() {
        for (ImageListenerIF listener : listeners) {
            listener.takeImage(imageData);
        }
    }
}