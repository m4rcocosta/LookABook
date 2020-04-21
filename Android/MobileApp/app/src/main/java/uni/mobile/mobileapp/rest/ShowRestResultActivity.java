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
                .baseUrl("http://192.168.1.173:3000/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getHouses();
        getRooms();
        getWalls();
        getShelves();
        getBooks();
    }

    /*
    * HOUSE
     */
    private void getHouses(){

        Call<MyResponse<House>> call = jsonPlaceHolderApi.getHouses(1);

        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }

                List<House> houses = response.body().getData();
                String content = "\n HOUSES \n";
                for(House house: houses){
                    content += "";
                    content += "ID: " + house.getId() + "\n";
                    content += "Name: " + house.getName() + "\n";


                }
                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    /*
     * ROOM
     */
    private void getRooms(){

        Call<MyResponse<Room>> call = jsonPlaceHolderApi.getRooms(1,1);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }
                List<Room> rooms = response.body().getData();
                String content = "\n ROOMS" + "\n";
                for(Room room: rooms){
                    content += "";
                    content += "ID: " + room.getId() + "\n";
                    content += "Name: " + room.getName() + "\n";


                }
                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    /*
    * WALL
     */
    private void getWalls(){
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.getWalls(1,1,1);

        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }
                List<Wall> Walls = response.body().getData();
                String content = "\n WALLS" + "\n";
                for(Wall wall: Walls){
                    content += "";
                    content += "ID: " + wall.getId() + "\n";
                    content += "Name: " + wall.getName() + "\n";


                }
                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    /*
    * SHELF
     */
    private void getShelves(){
        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.getShelves(1,1,1,1);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }
                List<Shelf> shelves = response.body().getData();
                String content = "\n SHELVES" + "\n";
                for(Shelf shelf: shelves){
                    content += "";
                    content += "ID: " + shelf.getId() + "\n";
                    content += "Name: " + shelf.getName() + "\n";


                }
                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    /*
    * BOOK
     */
    private void getBooks(){
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.getBooks(1,1,1,1,1);

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!response.isSuccessful()){
                    textViewResult.setText( "Code: " + response.code());
                    return;
                }
                List<Book> books = response.body().getData();
                String content = "\n BookS" + "\n";
                for(Book book: books){
                    content += "";
                    content += "ID: " + book.getId() + "\n";
                    content += "Title: " + book.getTitle() + "\n";


                    }
                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

}
