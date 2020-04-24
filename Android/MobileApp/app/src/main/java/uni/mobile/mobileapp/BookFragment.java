package uni.mobile.mobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView t= view.findViewById(R.id.bookTxt);
        RestLocalMethods.initRetrofit(this.getContext());

       /* Wall w = RestLocalMethods.createWall(1,6,1,new Wall("stranaHouse") );
        //t.setText(h.getName());
        User back=RestLocalMethods.createUser(new User("mardzxcfaio","","","asdxzcsad@asasdd.com","zzxczxz<x<zx"));
        //t.setText( back.getEmail() );*/

        RestLocalMethods.changeToken("fooToken");

        List<Book> books;

        Call<MyResponse<Book>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getAllBooks(1); //TODO correct userID

        callAsync.enqueue(new Callback<MyResponse<Book>>()
        {

            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response)
            {
                if (response.isSuccessful())
                {
                    books = response.body().getData();

                    //API response
                    System.out.println(apiResponse);
                }
                else
                {
                    System.out.println("Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t)
            {
                System.out.println("Network Error :: " + t.getLocalizedMessage());
            }
        });



        if(books==null) {
            Log.d("BBBB","NO BOOKS!");
            return;
        }

        String titles[] = (String []) books.stream().map(b -> b.getTitle()).toArray();
        String authors[] = (String []) books.stream().map( b -> b.getAuthors()).toArray();

        ListView lView = (ListView) view.findViewById(R.id.androidList);

        ListAdapter lAdapter = new ListAdapter(this.getContext(), titles, authors, null,R.drawable.ic_book_red);

        lView.setAdapter(lAdapter);
        Toast.makeText(getContext(), "Found " + books.size() +" books", Toast.LENGTH_SHORT).show();
        Log.d("BBBB",titles.toString());
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getContext(), titles[i], Toast.LENGTH_SHORT).show();

            }
        });


    }
}
