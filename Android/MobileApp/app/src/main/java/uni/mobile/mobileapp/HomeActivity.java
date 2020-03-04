package uni.mobile.mobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private static final int STORAGE_PERMISSION_CODE = 101;
    private int bottomNavigationSelectedItem, navigationSelectedItem;
    private Toolbar toolbar;
    private DrawerLayout dl;
    private TextView titleToolbar;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private ImageView profilePic;
    private TextView navTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        // Permission
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(HomeActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
        }

        titleToolbar = findViewById(R.id.toolbar_title);
        dl = findViewById(R.id.activity_home);
        t = new ActionBarDrawerToggle(this, dl, toolbar, R.string.Open, R.string.Close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                titleToolbar.setText("Menu");
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                titleToolbar.setText("MobileApp");
                invalidateOptionsMenu();
            }
        };

        t.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nv = findViewById(R.id.navigation);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_profile:
                        if (item.getItemId() == navigationSelectedItem) {
                            dl.closeDrawers();
                            break;
                        }
                        navigationSelectedItem = R.id.nav_profile;
                        bottomNavigationSelectedItem = R.id.navigation_profile;
                        openFragment(new ProfileFragment());
                        dl.closeDrawers();
                        return true;
                    case R.id.nav_settings:
                        if (item.getItemId() == navigationSelectedItem) {
                            dl.closeDrawers();
                            break;
                        }
                        navigationSelectedItem = R.id.nav_settings;
                        bottomNavigationSelectedItem = R.id.navigation_settings;
                        openFragment(new SettingsFragment(bottomNavigationView));
                        dl.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        Toast.makeText(HomeActivity.this, "Logout",Toast.LENGTH_SHORT).show();
                        return true;
                }

                return false;
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (item.getItemId() == bottomNavigationSelectedItem) break;
                        bottomNavigationSelectedItem = R.id.navigation_home;
                        navigationSelectedItem = R.id.navigation_home;
                        openFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_profile:
                        if (item.getItemId() == bottomNavigationSelectedItem) break;
                        bottomNavigationSelectedItem = R.id.navigation_profile;
                        openFragment(new ProfileFragment());
                        return true;
                    case R.id.navigation_settings:
                        if (item.getItemId() == bottomNavigationSelectedItem) break;
                        bottomNavigationSelectedItem = R.id.navigation_settings;
                        openFragment(new SettingsFragment(bottomNavigationView));
                        return true;
                }
                return false;
            }
        });

        profilePic = nv.getHeaderView(0).findViewById(R.id.nav_profile_pic);
        navTitle = nv.getHeaderView(0).findViewById(R.id.nav_header_textView);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        Uri imageUri = user != null ? user.getPhotoUrl() : null;

        if (imageUri != null) {
            imageUri = increaseUriImageSize(imageUri);
            Picasso.get().load(imageUri).fit().centerInside().into(profilePic);
        }
        else {
            // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
            storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerInside().into(profilePic);
                }
            });
        }
        navTitle.setText(user.getDisplayName());


        //loading the default fragment
        bottomNavigationSelectedItem = R.id.navigation_home;
        navigationSelectedItem = R.id.navigation_home;
        openFragment(new HomeFragment());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(HomeActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        t.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        t.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
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

}
