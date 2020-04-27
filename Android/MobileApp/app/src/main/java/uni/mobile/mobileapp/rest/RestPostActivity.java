    package uni.mobile.mobileapp.rest;

    import androidx.appcompat.app.AppCompatActivity;

    import android.os.Bundle;
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

    public class RestPostActivity extends AppCompatActivity {

        private JsonPlaceHolderApi jsonPlaceHolderApi ;
        private TextView postResultTextView;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rest_post);



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
                    .baseUrl("http://lookabookreal.herokuapp.com/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


             jsonPlaceHolderApi =retrofit.create(JsonPlaceHolderApi.class);
             postResultTextView = findViewById(R.id.postResult);

            createHouse();
            patchHouse();
            deleteHouse();
        }
        /*
         * HOUSE
         */
        private void createHouse(){
            House h = new House("newHouseCreatedFromMobile",null,null,null);

            Call<MyResponse<House>> call = jsonPlaceHolderApi.createHouse(1,h);

            call.enqueue(new Callback<MyResponse<House>>() {
                @Override
                public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                    if(! response.isSuccessful()){
                        postResultTextView.append("Code: "+ response.code());
                        return;
                    }
                    List<House> hres = response.body().getData();
                    postResultTextView.append("Added "+hres.get(0).getName() + "\n");
                }

                @Override
                public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                    postResultTextView.append(t.getMessage());
                }
            });
        }

        private void patchHouse(){
                final House h = new House("modifiedHouseCreatedFromMobile",null,null,null);

                Call<MyResponse<House>> call = jsonPlaceHolderApi.patchHouse(1,7,h);

                call.enqueue(new Callback<MyResponse<House>>() {
                    @Override
                    public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                        if(! response.isSuccessful()){
                            postResultTextView.append("Code: "+ response.code());
                            return;
                        }
                        List<House> hres = response.body().getData();
                        postResultTextView.append("Patched "+h.getName() +  " to "+ hres.get(0).getName() + "\n");
                    }

                    @Override
                    public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                        postResultTextView.append(t.getMessage());
                    }
                });
        }

        private void deleteHouse(){
            Call<MyResponse<House>> call = jsonPlaceHolderApi.deleteHouse(1,5);
            call.enqueue(new Callback<MyResponse<House>>() {
                @Override
                public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                    if(! response.isSuccessful()){
                        postResultTextView.append("Code: "+ response.code());
                        return;
                    }
                    List<House> hres = response.body().getData();
                    postResultTextView.append("Deleted "+ hres.get(0).getName() + "\n");
                }

                @Override
                public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                    postResultTextView.append(t.getMessage());
                }
            });
        }

        /*
         * ROOM
         */

        /*
         * WALL
         */

        /*
         * SHELF
         */

        /*
         * BOOK
         */
    }
