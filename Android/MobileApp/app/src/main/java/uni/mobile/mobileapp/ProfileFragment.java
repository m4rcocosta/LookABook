package uni.mobile.mobileapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView profilePicImageView;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Advanced"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabAdapter adapter = new TabAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        profilePicImageView = view.findViewById(R.id.imageProfileImageView);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (getUserProvider(user).equals("GOOGLE")) {
            Uri imageUri = increaseUriImageSize(user.getPhotoUrl());
            Picasso.get().load(imageUri).fit().centerInside().into(profilePicImageView);
        }
        else {
            // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
            storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
                    // ".fit().centerInside()" fits the entire image into the specified area.
                    // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
                    Picasso.get().load(uri).fit().centerInside().into(profilePicImageView);
                }
            });
        }
    }

    private Uri increaseUriImageSize(Uri uri) {

        // Variable holding the original String portion of the url that will be replaced
        String originalPieceOfUrl = "s96-c";

        // Variable holding the new String portion of the url that does the replacing, to improve image quality
        String newPieceOfUrlToAdd = "s400-c";

        // Check if the Url path is null
        if (uri != null) {

            // Convert the Url to a String and store into a variable
            String photoPath = uri.toString();

            // Replace the original part of the Url with the new part
            String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);
            Uri newUri = Uri.parse(newString);
            return newUri;
        }
        return null;
    }

    private String getUserProvider(FirebaseUser user) {
        List<? extends UserInfo> infos = user.getProviderData();
        String provider = "FIREBASE";
        for (UserInfo ui : infos) {
            if (ui.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) provider = "GOOGLE";
            else if (ui.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID)) provider = "FACEBOOK";
        }
        return provider;
    }
}
