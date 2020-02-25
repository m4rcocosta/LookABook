package uni.mobile.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;

import static uni.mobile.mobileapp.TextRecognitionActivity.EXTRA_MESSAGE;

public class PhotoDecisionActivity extends AppCompatActivity {

    private String titles;
    private Bitmap bm;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_decision);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        titles = intent.getStringExtra(EXTRA_MESSAGE);

        filename= intent.getStringExtra(EXTRA_MESSAGE+"bitmapFilename");

        try {
            FileInputStream is = this.openFileInput(filename);
            bm = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView im = findViewById(R.id.imageView);
        im.setImageBitmap(bm);

    }


    public void onDislikedPhoto(View v){
        Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
        onBackPressed();

    }
    public void onLikedPhoto(View v){
        Toast.makeText(this, "Yes", Toast.LENGTH_LONG).show();

        //Intent intent = new Intent(this, SelectTitlesActivity.class);
        Intent intent = new Intent(this, SelectTitlesActivity.class);

        intent.putExtra(EXTRA_MESSAGE, titles);
        startActivity(intent);
    }
}
