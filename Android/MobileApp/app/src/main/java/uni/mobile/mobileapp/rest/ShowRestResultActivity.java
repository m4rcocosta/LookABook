package uni.mobile.mobileapp.rest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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
                                                  .header("TOKEN", "fooToken")
                                                  .header("Accept", "application/json")
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = httpClient
                        .addInterceptor(loggingInterceptor)
                        .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lookabook.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        RestLocalMethods.getAllObjectsFromUser(textViewResult,jsonPlaceHolderApi,1);
        //RestLocalMethods.getHouses(textViewResult,jsonPlaceHolderApi);
        Log.i("ids",  RestLocalMethods.objectsIds.get("rooms").toString() );
        //RestLocalMethods.getRooms(textViewResult,jsonPlaceHolderApi, RestLocalMethods.objectsIds.get("rooms"));
        //RestLocalMethods.getWalls(textViewResult,jsonPlaceHolderApi);
        //RestLocalMethods.getShelves(textViewResult,jsonPlaceHolderApi);
        //RestLocalMethods.getBooks(textViewResult,jsonPlaceHolderApi);
    }


}
