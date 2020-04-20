package uni.mobile.mobileapp.rest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uni.mobile.mobileapp.R;

public class ShowRestResultActivity extends AppCompatActivity {

    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rest_result2);

        textViewResult = findViewById(R.id.text_view_result);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public okhttp3.Response intercept(Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .header("TOKEN", "Ropw93ZqJZQYVRnmi5BQxiZQ")
                                                  .header("Accept", "application/json")
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });

                OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.157:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getHouses();
        getRooms();
    }

    private void getHouses(){

        Call<List<House>> call = jsonPlaceHolderApi.getHouses(1);

        call.enqueue(new Callback<List<House>>() {
            @Override
            public void onResponse(Call<List<House>> call, Response<List<House>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }

                List<House> houses = response.body();

                for(House house: houses){
                    String content = "";
                    content += "ID: " + house.getId() + "\n";
                    content += "Name: " + house.getName() + "\n";


                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<House>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getRooms(){

        Call<List<Room>> call = jsonPlaceHolderApi.getRooms(1,1);

        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }
                List<Room> rooms = response.body();
                String content = "\nROOMS" + "\n";
                for(Room room: rooms){
                    content += "";
                    content += "ID: " + room.getId() + "\n";
                    content += "Name: " + room.getName() + "\n";


                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

}
