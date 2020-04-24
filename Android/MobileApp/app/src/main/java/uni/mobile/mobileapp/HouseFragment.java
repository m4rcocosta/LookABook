package uni.mobile.mobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.mobile.mobileapp.guiAdapters.ListAdapter;
import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.House;
import uni.mobile.mobileapp.rest.MyResponse;
import uni.mobile.mobileapp.rest.RestLocalMethods;


public class HouseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RestLocalMethods.initRetrofit(this.getContext());
        RestLocalMethods.setUserToken("fooToken"); //TODO put user token

        Call<MyResponse<House>> callAsync = RestLocalMethods.getJsonPlaceHolderApi().getHouses(1); //TODO correct userID
        callAsync.enqueue(new Callback<MyResponse<House>>()
        {

            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response)
            {
                if (response.isSuccessful())
                {

                    List<House> houses = response.body().getData();

                    ArrayList<String> names = new ArrayList<String>();
                    //ArrayList<String> authors = new ArrayList<String>();
                    for(House h: houses){
                        names.add( h.getName() ) ;
                        //authors.add(b.getAuthors());
                    }

                    ListView lView = (ListView) view.findViewById(R.id.androidList);

                    ListAdapter lAdapter = new ListAdapter(getContext(), names, null, null,R.drawable.ic_houseicon);

                    lView.setAdapter(lAdapter);
                    Toast.makeText(getContext(), "Found " + houses.size() +" Houses", Toast.LENGTH_SHORT).show();
                    Log.d("BBBB",names.toString());
                    lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(getContext(), names.get(i), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
                else
                {
                    Log.d("BBBB","Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t)
            {
                Log.d("BBBB","Request Error :: " + t.getMessage() );
            }
        });



    }
}
