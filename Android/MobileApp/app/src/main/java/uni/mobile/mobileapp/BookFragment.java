package uni.mobile.mobileapp;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.mobile.mobileapp.guiAdapters.ListAdapter;
import uni.mobile.mobileapp.rest.Book;
import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.JsonPlaceHolderApi;
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.User;
import uni.mobile.mobileapp.rest.Wall;


public class BookFragment extends Fragment {

    private FloatingActionButton addBookButton;
    private MaterialCardView cardView;
    private ImageView googleImage;
    private TextView googleTitle;
    private TextView googleAuthors;
    private TextView googleDesc;
    private MaterialButton ignoreButton;
    private MaterialButton acceptButton;
    private ListView lView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //car related
        cardView = view.findViewById(R.id.card);
        googleImage=view.findViewById(R.id.googleImage);
        googleTitle =view.findViewById(R.id.googleTitle );
        googleAuthors =view.findViewById(R.id.googleAuthors);
        googleDesc=view.findViewById(R.id.googleDesc);
        ignoreButton=view.findViewById(R.id.ignoreButton);
        acceptButton=view.findViewById(R.id.acceptButton);

        ignoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                lView.setClickable(true);

            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO update rails with a patch
                cardView.setVisibility(View.GONE);
                lView.setClickable(true);

            }
        });


        //fab related
        addBookButton = view.findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog_add_book, null);
                final EditText bookNameEditText = alertLayout.findViewById(R.id.bookName);
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Create new book")
                        .setMessage("Insert the book name")
                        .setView(alertLayout) // this is set the view from XML inside AlertDialog
                        .setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String bookName = bookNameEditText.getText().toString();
                                Toast.makeText(getContext(), "Book " + bookName + " created!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        //List related

        lView = view.findViewById(R.id.bookList);

        RestLocalMethods.setContext(getContext());

       /* Wall w = RestLocalMethods.createWall(1,6,1,new Wall("stranaHouse") );
        //t.setText(h.getName());
        User back=RestLocalMethods.createUser(new User("mardzxcfaio","","","asdxzcsad@asasdd.com","zzxczxz<x<zx"));
        //t.setText( back.getEmail() );*/

        Call<MyResponse<Book>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllBooks(RestLocalMethods.getMyUserId());
        callAsync.enqueue(new Callback<MyResponse<Book>>()
        {

            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response)
            {
                if (response.isSuccessful())
                {

                    List<Book> books = response.body().getData();

                    ArrayList<String> titles = new ArrayList<String>();
                    ArrayList<String> authors = new ArrayList<String>();
                    for(Book b: books){
                        titles.add( b.getTitle() ) ;
                        authors.add(b.getAuthors());
                    }


                    ListAdapter lAdapter = new ListAdapter(getContext(), titles, authors, null,R.drawable.ic_book_red);

                    lView.setAdapter(lAdapter);
                    if(getContext()==null)  //too late now to print
                        return;
                    Toast.makeText(getContext(), "Found " + books.size() +" books", Toast.LENGTH_SHORT).show();
                    Log.d("BBBB",titles.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //googleImage
                            if(books.get(i).getGoogleData()!=null) {
                                if(books.get(i).getGoogleData().getVolumeInfo().getImageLinks().getThumbnail()!=null) {
                                    new DownloadImageTask(  googleImage)
                                            .execute(books.get(i).getGoogleData().getVolumeInfo().getImageLinks().getThumbnail());
                                }
                                Toast.makeText(getContext(), "Google "+titles.get(i), Toast.LENGTH_SHORT).show();
                                googleTitle.setText(books.get(i).getGoogleData().getVolumeInfo().getTitle());
                                googleAuthors.setText(books.get(i).getGoogleData().getVolumeInfo().getAuthors().toString() );
                                googleDesc.setText(books.get(i).getGoogleData().getVolumeInfo().getDescription());
                                lView.setClickable(false);
                                cardView.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getContext(), "No Google info for "+titles.get(i), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
                else
                {
                    Log.d("BBBB","Request Error :: " + response.errorBody());
                    Toast.makeText(getContext(), "Google api error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t)
            {
                Log.d("BBBB","Request Error :: " + t.getMessage() );
            }
        });



    }

    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
