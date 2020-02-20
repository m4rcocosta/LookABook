package com.example.textrecognition;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCamera {

    private static final String TAG = "DBG";
    private final SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private TextRecognizer textRecognizer;
    private TextView mTextView;

    private Context ctx;
    private Activity act;
    private CheckBoxThread checkBoxThread;
    private Bitmap bm;

    public MyCamera(Context context,Activity activity,CheckBoxThread checkBoxThread){
        ctx = context;
        act = activity;
        this.checkBoxThread = checkBoxThread;
        mCameraView= act.findViewById(R.id.surfaceView);
        mTextView = act.findViewById(R.id.text_view);

    }

    protected void startCameraSource() {

        //Create the TextRecognizer
        textRecognizer = new TextRecognizer.Builder(ctx).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(ctx, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(ctx,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            int requestPermissionID=101;
                            ActivityCompat.requestPermissions(act,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */

                String currentPhotoPath;


                private File createImageFile() throws IOException {
                    // Create an image file name
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );

                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = image.getAbsolutePath();
                    return image;
                }

                static final int REQUEST_TAKE_PHOTO = 1;

                private void dispatchTakePictureIntent() {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(ctx.getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(ctx,
                                    "com.example.android.fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            //ctx.startActivityForResult();
                        }
                    }
                }







                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }

                                String stringRead=stringBuilder.toString();
                                mTextView.setText(stringRead);
                                Log.i(TAG,stringRead);



                            }
                        });
                    }
                }

            });
        }
    }


    public void stopCamera(){
        mCameraSource.stop();
        textRecognizer.release();

    }


    private void frameDetect(Bitmap bm) throws IOException {
                    /*dispatchTakePictureIntent();

                    ImageReader reader=new ImageReader();
                    Image image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
*/
        // Recognize text in the image
        TextRecognizer ocrFrame = new TextRecognizer.Builder(ctx).build();
        Frame frame = new Frame.Builder().setBitmap(bm).build();
        if (ocrFrame.isOperational()) {
            Log.e(TAG, "Textrecognizer is operational");
        }

        final SparseArray<TextBlock> items = textRecognizer.detect(frame);
        if (items.size() != 0) {
            mTextView.post(new Runnable() {
                @Override
                public void run() {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
                    }

                    String stringRead = stringBuilder.toString();
                    mTextView.setText(stringRead);
                    Log.i("FROM PICTURE", stringRead);


                    final String[] split = stringRead.split("\n");
                    for (String potentialTitle : split) {
                        if (potentialTitle.length() > 5)
                            checkBoxThread.setTitles(potentialTitle);

                    }

                }
            });

        }
    }

    public void snap(){
        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
                Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
                try {
                    frameDetect(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


