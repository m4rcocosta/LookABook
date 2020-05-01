package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.sip.SipSession;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.MyHolder;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.RestTreeLocalMethods;
import uni.mobile.mobileapp.rest.atv.model.TreeNode;
import uni.mobile.mobileapp.rest.atv.view.AndroidTreeView;
import uni.mobile.mobileapp.rest.callbacks.HouseCallback;
import uni.mobile.mobileapp.rest.callbacks.TreePrintAllCallback;

public class HomeFragment extends Fragment {

    private Button startTextRecognitionButton;
    private static final int CAMERA_PERMISSION_CODE = 100;

    private Boolean isFABOpen =false;
    public FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;

    private AndroidTreeView tView;

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



        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        FragmentManager manager = getFragmentManager();
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigation);

        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) view.findViewById(R.id.fab4);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContainer,new HouseFragment(),"house");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContainer,new RoomFragment(bottomNavigationView),"room");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContainer,new WallFragment(bottomNavigationView),"wall");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentContainer,new ShelfFragment(bottomNavigationView),"shelf");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        //RestLocalMethods.initRetrofit(this.getActivity());

        /* Building the tree*/
        //Root
        TreeNode root = TreeNode.root();

        //Parent
        MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Choose a shelf");
        parent = new TreeNode(nodeItem, null).setViewHolder(new MyHolder(getActivity(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));
        root.addChild(parent);
        Log.d("hf","HF::"+RestLocalMethods.getMyUserId()+RestLocalMethods.getJsonPlaceHolderApi());

        //Add AndroidTreeView into view.

        TreePrintAllCallback refresherCallback = new TreePrintAllCallback() {
            @Override
            public void onSuccessRefresh() {

                RestLocalMethods.setContext(getActivity());
                RestTreeLocalMethods.printAllObjectsFromUser(RestLocalMethods.getMyUserId(), parent);


            }
        };
        tView = new AndroidTreeView(getActivity(), root, null);
      tView.setSelectionModeEnabled(true);
//        tView.expandAll();
    //    tView.selectAll(true);
//        tView.setUse2dScroll(true);
      //  tView.setUseAutoToggle(false);
        ((FrameLayout) view.findViewById(R.id.shelfChooserLayout)).addView(tView.getView());



    }

    public void onStart () {

        super.onStart();
        RestLocalMethods.setContext(getActivity());
        RestTreeLocalMethods.printAllObjectsFromUser(RestLocalMethods.getMyUserId(), parent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tView.onActivityResult(requestCode, resultCode, data);
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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



