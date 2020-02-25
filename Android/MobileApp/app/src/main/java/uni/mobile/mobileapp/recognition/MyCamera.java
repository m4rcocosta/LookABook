package uni.mobile.mobileapp.recognition;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import uni.mobile.mobileapp.R;

class MyCamera {
    private static final String TAG = "DBG";
    private final SurfaceView mCameraView;
    private CameraSource mCameraSource;
    private TextRecognizer textRecognizer;
    private TextView mTextView;
    private TextView numberView;

    private SurfaceHolder sHholder;

    private String FILENAME = "trBitmap.png";

    private Boolean isReady=false;
    private Context ctx;
    private TextRecognitionActivity act;
    // private CheckBoxThread checkBoxThread;
    private Bitmap bm;
    private Detector.Processor<TextBlock> proc;

    public MyCamera(Context context, TextRecognitionActivity activity) {
        ctx = context;
        act = activity;
        // this.checkBoxThread = checkBoxThread;
        mCameraView = act.findViewById(R.id.surfaceView);
        mTextView = act.findViewById(R.id.text_view);
        numberView = act.findViewById(R.id.numberView);


        //Set the TextRecognizer's Processor.
        proc=new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            /**
             * Detect all the text from camera using TextBlock and the values into a stringBuilder
             * which will then be set to the textView. IT IS DONE IN LIVE
             * */


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


                                //Draw Rectangle

                                //The Color of the Rectangle to Draw on top of Text
                                Paint rectPaint = new Paint();
                                rectPaint.setColor(Color.RED);
                                rectPaint.setStyle(Paint.Style.STROKE);
                                rectPaint.setStrokeWidth(4.0f);

                                //Va in errore

                                    /*
                                    SurfaceHolder holder = mCameraView.getHolder();
                                    Canvas canvas = holder.lockCanvas();
                                    if (canvas == null) {
                                        Log.e(TAG, "Cannot draw onto the canvas as it's null");
                                    } else {

                                        RectF rect = new RectF(item.getBoundingBox());
                                        //rect = translateRect(rect);
                                        rectPaint.setColor(Color.BLACK);


                                        //Finally Draw Rectangle/boundingBox around word
                                        canvas.drawRect(rect, rectPaint);
                                        Log.i("CANVAS","Writing on canvas");
                                        holder.unlockCanvasAndPost(canvas);
                                    }
*/

                            }

                            //Use String Read
                            String stringRead = stringBuilder.toString();
                            mTextView.setText(stringRead);
                            Log.i("FROM LIVE", stringRead);
                            String[] arr = stringRead.split("\n");
                            int counter=0;
                            for(int i=0; i<arr.length;i++){if(arr[i].length()>5)counter++;}
                            numberView.setText( Integer.toString(counter));
                            if(counter>0)numberView.setTextColor(Color.GREEN);
                            else numberView.setTextColor(Color.RED);

                        }
                    });
                }
            }

        };

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

                            int requestPermissionID = 101;
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

            textRecognizer.setProcessor(proc);
            isReady=true;
        }
    }


    public void stopCamera() {
        mCameraSource.stop();
        textRecognizer.release();
        isReady=false;
    }

    //Will do detection just on one frame
    private void frameDetect(Bitmap bm) throws IOException {

        this.stopCamera();
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


                    /*final String[] split = stringRead.split("\n");
                    for (String potentialTitle : split) {
                        if (potentialTitle.length() > 5)
                            checkBoxThread.setTitles(potentialTitle);

                    }*/


                    act.onSnapped(stringRead,FILENAME);

                }
            });

        }

    }

    //On call will take a picture and start detection on it
    public void snap() {
        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                //Creating bitmap from tap on surfaceview
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
                Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
                bm=bitmap;

                //Creating thread to store the bitmap for the next activity
                Thread bmToFile = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Write file

                        try {
                            FileOutputStream stream = act.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);

                            //Cleanup
                            stream.close();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        bm.recycle();

                    }
                });
                bmToFile.start();

                try {
                    frameDetect(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public Boolean getReady() {
        return isReady;
    }
}
