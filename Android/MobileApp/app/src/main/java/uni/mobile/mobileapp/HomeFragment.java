package uni.mobile.mobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import uni.mobile.mobileapp.recognition.TextRecognitionActivity;
import uni.mobile.mobileapp.rest.MyHolder;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.RestTreeLocalMethods;
import uni.mobile.mobileapp.rest.atv.model.TreeNode;
import uni.mobile.mobileapp.rest.atv.view.AndroidTreeView;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uni.mobile.mobileapp.R;
import uni.mobile.mobileapp.rest.JsonPlaceHolderApi;
import uni.mobile.mobileapp.rest.MyHolder;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.RestTreeLocalMethods;
import uni.mobile.mobileapp.rest.atv.model.TreeNode;
import uni.mobile.mobileapp.rest.atv.view.AndroidTreeView;

public class HomeFragment extends Fragment {

    private Button startTextRecognitionButton;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private Boolean isFABOpen =false;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;

    private TreeNode parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //TODO start recognition
//        startTextRecognitionButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Permission
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED) {
//                    // Requesting the permission
//                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_CODE);
//                }
//                else {
//                    Intent intent = new Intent(getActivity(), TextRecognitionActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });



        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view.findViewById(android.R.id.content), "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) view.findViewById(R.id.fab4);


        //RestLocalMethods.initRetrofit(this.getActivity());

        /* Building the tree*/
        //Root
        TreeNode root = TreeNode.root();

        //Parent
        MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Choose a shelf");
        parent = new TreeNode(nodeItem).setViewHolder(new MyHolder(getActivity(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));
        root.addChild(parent);
        Log.d("hf","HF::"+RestLocalMethods.getMyUserId()+RestLocalMethods.getJsonPlaceHolderApi());
        RestLocalMethods.setContext(getActivity());
        RestTreeLocalMethods.printAllObjectsFromUser(RestLocalMethods.getMyUserId(), parent);

        //Add AndroidTreeView into view.
        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        ((FrameLayout) view.findViewById(R.id.shelfChooserLayout)).addView(tView.getView());


    }

    public void onStart () {

        super.onStart();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TextRecognitionActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }






    private void showFABMenu(){
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_180));
        fab4.animate().translationY(-getResources().getDimension(R.dimen.standard_240));

    }

    private void closeFABMenu(){
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        fab4.animate().translationY(0);

    }
}



