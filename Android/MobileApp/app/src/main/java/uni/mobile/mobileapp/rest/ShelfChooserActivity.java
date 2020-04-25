package uni.mobile.mobileapp.rest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import uni.mobile.mobileapp.rest.atv.model.TreeNode;
import uni.mobile.mobileapp.rest.atv.view.AndroidTreeView;

public class ShelfChooserActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_chooser);

        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RestLocalMethods.initRetrofit(getApplicationContext(),RestLocalMethods.getUserToken()); //Todo remove it for user one

        /* Building the tree*/
        //Root
        TreeNode root = TreeNode.root();

        //Parent
        MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Choose a shelf");
        TreeNode parent = new TreeNode(nodeItem).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));
        root.addChild(parent);

        JsonPlaceHolderApi jsonPlaceHolderApi = RestLocalMethods.getJsonPlaceHolderApi();

        RestTreeLocalMethods.printAllObjectsFromUser(parent,getApplicationContext(),null,jsonPlaceHolderApi);


        //Add AndroidTreeView into view.
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
        ((LinearLayout) findViewById(R.id.shelfChoserLayout)).addView(tView.getView());

    }
}
