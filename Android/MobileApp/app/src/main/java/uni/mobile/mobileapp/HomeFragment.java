package uni.mobile.mobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import uni.mobile.mobileapp.recognition.TextRecognitionActivity;

public class HomeFragment extends Fragment {

    private Button startTextRecognitionButton;
    private static final int CAMERA_PERMISSION_CODE = 100;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        startTextRecognitionButton = view.findViewById(R.id.startTextRecognitionButton);
        startTextRecognitionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Permission
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED) {
                    // Requesting the permission
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_CODE);
                }
                else {
                    Intent intent = new Intent(getActivity(), TextRecognitionActivity.class);
                    startActivity(intent);
                }
            }
        });
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
}
