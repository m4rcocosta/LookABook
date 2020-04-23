package uni.mobile.mobileapp.rest;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.common.ServerStatsFieldEnums;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RestLocalMethods {

    public  static Multimap<String, Integer> objectsIds = HashMultimap.create();
    private static List<User> users;
    private static List<House> houses;
    private static List<Room> rooms;
    private static List<Wall> walls;
    private static List<Shelf> shelves;
    private static List<Book> books;
    private static Gson gson= new Gson();


    public static JsonPlaceHolderApi initRetrofit() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("TOKEN", "")
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        String railsHostBaseUrl="http://192.168.1.157:3000/api/v1/";
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .disableInnerClassSerialization()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                //.excludeFieldsWithoutExposeAnnotation()
                .setVersion(1.0)
                .create();

        OkHttpClient client = httpClient
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(railsHostBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();


        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        return jsonPlaceHolderApi;
    }


    /*
    * Stringifier
     */
    public static String jsonize(Object obj){
        return gson.toJson(obj);
    }

    /*
     * ALL
     */
    public static void getAllObjectsFromUser(final TextView textViewResult ,final JsonPlaceHolderApi jsonPlaceHolderApi,Integer userId){
        //Todo remove this
        getHouses(textViewResult, jsonPlaceHolderApi, userId,true);
    }

    /*
     * USER
     */


    public static User getUserByEmail(final JsonPlaceHolderApi jsonPlaceHolderApi,  String email){
        users = null;
        Call<MyResponse<User>> call = jsonPlaceHolderApi.getUserByEmail(email);
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {}
        });
        if (users != null) return users.get(0);
        else return null;
    }

    public static User getUserByToken(final JsonPlaceHolderApi jsonPlaceHolderApi){
        Call<MyResponse<User>> call = jsonPlaceHolderApi.getUserByToken();
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {}
        });
        return users.get(0);

    }

    public static User createUser( final JsonPlaceHolderApi jsonPlaceHolderApi,  User user){
        users = null;
//        RequestBody rb= RequestBody.create(MediaType.parse("application/json"),jsonize(user));
//        Log.d("rb",jsonize(user));
        Call<MyResponse<User>> call = jsonPlaceHolderApi.createUser(user);

        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {            }
        });
        if (users != null) return users.get(0);
        else return null;
    }

    public static User patchUser(final JsonPlaceHolderApi jsonPlaceHolderApi,  int userId,  User user){
        Call<MyResponse<User>> call = jsonPlaceHolderApi.patchUser(userId,user);

        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {}
        });
        return users.get(0);
    }

    public static User deleteUser(final JsonPlaceHolderApi jsonPlaceHolderApi,  int userId){
        Call<MyResponse<User>> call = jsonPlaceHolderApi.deleteUser(userId);
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {}
        });
        return users.get(0);
    }

    /*
     * HOUSE
     */


    // GET (index)
    public  static List<House> getHouses(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId, final Boolean recursiveSearch  ){
        houses=null;
        Call<MyResponse<House>> call = jsonPlaceHolderApi.getHouses(userId);
        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!response.isSuccessful()){
                    return;
                }

                houses = response.body().getData();

                for(House house: houses){
                    objectsIds.put("houses",house.getId());
                    if(recursiveSearch){
                        getRooms(textViewResult,jsonPlaceHolderApi,userId,house.getId(),true);
                    }

                }
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {

                return ;
            }

        });
        if(houses!=null) return houses;
        else return null;
    }

    //POST
    private void createHouse( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId, House house){

        Call<MyResponse<House>> call = jsonPlaceHolderApi.createHouse(userId,house);

        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<House> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                //TODO print error
            }
        });
    }
    //PATCH
    private void patchHouse( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId,final Integer houseId,House patchedHouse){

        Call<MyResponse<House>> call = jsonPlaceHolderApi.patchHouse(userId,houseId,patchedHouse);

        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<House> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                //TODO print error
            }
        });
    }
    //DELETE
    private void deleteHouse( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId,final Integer houseId){
        Call<MyResponse<House>> call = jsonPlaceHolderApi.deleteHouse(userId,houseId);
        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<House> hres = response.body().getData();
                //TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                //TODO print error
            }
        });
    }


    /*
     * ROOM
     */


    //GET
    public static List<Room> getRooms(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId  ,final Integer houseId   , final Boolean recursiveSearch ){

        rooms=null;
        Call<MyResponse<Room>> call = jsonPlaceHolderApi.getRooms(userId,houseId);
        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if (!response.isSuccessful()) {
                    Log.d("myrest" , "room Code:"+String.valueOf(response.code()));
                    return;
                }
                rooms = response.body().getData();

                for (Room room : rooms) {
                    Log.d("myrest", "room:"+room.getId() );
                    if(recursiveSearch){
                        getWalls(textViewResult,jsonPlaceHolderApi,userId,houseId,room.getId(),true);
                    }


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                Log.d("myrest", "room:"+t.getMessage() );
            }
        });
        if(rooms!=null) return rooms;
        else return null;

    }

    //POST
    private void createRoom( final JsonPlaceHolderApi jsonPlaceHolderApi,  final Integer userId ,final Integer houseId, Room room){

        Call<MyResponse<Room>> call = jsonPlaceHolderApi.createRoom(userId,houseId,room);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Room> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                //TODO print error
            }
        });
    }
    //PATCH
    private void patchRoom( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId,final Integer houseId,final Integer roomId,Room patchedRoom){

        Call<MyResponse<Room>> call = jsonPlaceHolderApi.patchRoom(userId,houseId,roomId,patchedRoom);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Room> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                //TODO print error
            }
        });
    }
    //DELETE
    private void deleteRoom( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId, final Integer houseId, final Integer roomId){
        Call<MyResponse<Room>> call = jsonPlaceHolderApi.deleteRoom(userId,houseId,roomId);
        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Room> hres = response.body().getData();
                //TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                //TODO print error
            }
        });
    }

    /*
     * WALLS
     */

    //GET
    public  static List<Wall> getWalls(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi,
                                       final Integer userId,final Integer houseId,final Integer roomId ,final Boolean recursiveSearch ){
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.getWalls(userId,houseId,  roomId);

        walls=null;
        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!response.isSuccessful()){
                    Log.d("myrest" , "wall Code:"+ response.code() );
                    return;
                }
                walls = response.body().getData();
                String content = "\n WALLS" + "\n";

                for(Wall wall: walls){
                    objectsIds.put("walls",wall.getId());

                    Log.d("myrest" , "wall "+ wall.getName() );

                    if(recursiveSearch){
                        getShelves(textViewResult,jsonPlaceHolderApi,userId,houseId,roomId,wall.getId(),true);

                    }


                }


            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Log.d("myrest" , "wall Code:"+ t.getMessage());
            }
        });
        return walls;
    }

    //POST
    private void createWall( final JsonPlaceHolderApi jsonPlaceHolderApi,  final Integer userId ,final Integer houseId,
                             Integer roomId, Wall wall){

        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.createWall(userId,houseId,roomId,wall);

        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Wall> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                //TODO print error
            }
        });
    }

    //PATCH
    private void patchWall( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId,final Integer houseId,
                            Integer wallId, Integer roomId,Wall patchedWall){

        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.patchWall(userId,houseId,roomId,wallId,patchedWall);

        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Wall> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                //TODO print error
            }
        });
    }
    //DELETE
    private void deleteWall( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId, final Integer houseId, final Integer roomId, final Integer wallId){
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.deleteWall(userId,houseId,roomId,wallId);
        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Wall> hres = response.body().getData();
                //TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                //TODO print error
            }
        });
    }

    /*
     * SHELF
     */


    //GET

    public  static List<Shelf> getShelves(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi,
                                          Integer userId, final Integer houseId, final Integer roomId , final Integer wallId   , final Boolean recursiveSearch ){
        shelves=null;
        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.getShelves(userId,houseId, roomId, wallId);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!response.isSuccessful()){
                    Log.d("myrest", "shelf Code: " + response.code() );
                    return;
                }
                shelves = response.body().getData();

                for(Shelf shelf: shelves){
                    objectsIds.put("shelves",shelf.getId());

                    Log.d("myrest", "shelf Code: " + shelf.getName());

                    if(recursiveSearch){
                        getShelves(textViewResult,jsonPlaceHolderApi,houseId,roomId,wallId,shelf.getId(),true);

                    }


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                Log.d("myrest", "shelf Code: " + t.getMessage());
            }
        });
        if(shelves!=null) return shelves;
        else return null;
    }

    //POST
    private void createShelf( final JsonPlaceHolderApi jsonPlaceHolderApi,  final Integer userId ,Integer houseId,
                              Integer roomId, Integer wallId, Shelf shelf){

        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.createShelf(userId,houseId, roomId, wallId, shelf);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Shelf> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                //TODO print error
            }
        });
    }

    //PATCH
    private void patchShelf( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId,final Integer houseId,
                             Integer roomId,Integer wallId,Integer shelfId, Shelf patchedShelf){

        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.patchShelf(userId, houseId, roomId, shelfId, wallId, patchedShelf);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Shelf> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                //TODO print error
            }
        });
    }
    //DELETE
    private void deleteShelf( final JsonPlaceHolderApi jsonPlaceHolderApi, Integer userId, final Integer houseId, Integer roomId, Integer wallId ,Integer shelfId){
        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.deleteShelf(userId,houseId,roomId, wallId, shelfId);
        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Shelf> hres = response.body().getData();
                //TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                //TODO print error
            }
        });
    }


    /*
     * BOOK
     */

    //GET
    public  static List<Book> getBooks(final TextView textViewResult, final  JsonPlaceHolderApi jsonPlaceHolderApi ,
                                       final Integer userId, final Integer houseId, final  Integer roomId, final  Integer wallId, final Integer shelfId ,
                                       Boolean recursiveSearch ){
        books=null;
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.getBooks(userId, houseId,  roomId,  wallId, shelfId );

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!response.isSuccessful()){
                    Log.d("myrest", "book Code: " + response.code());
                    return;
                }
                books = response.body().getData();

                for(Book book: books){
                    objectsIds.put("books",book.getId());
                    Log.d("myrest", "book : " + book.getTitle() );


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                Log.d("myrest", "book Code: " + t.getMessage());
            }
        });
        if(books!=null) return books;
        else return null;
    }

    //GET
    public  static List<Book> getAllBooks(final TextView textViewResult, final  JsonPlaceHolderApi jsonPlaceHolderApi ,
                                          final Integer userId){
        books=null;
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.getAllBooks(userId );

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!response.isSuccessful()){
                    Log.d("myrest", "book Code: " + response.code());
                    return;
                }
                books = response.body().getData();

                for(Book book: books){
                    objectsIds.put("books",book.getId());
                    Log.d("myrest", "book : " + book.getTitle() );


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                Log.d("myrest", "book Code: " + t.getMessage());
            }
        });
        if(books!= null) return books;
        else return null;
    }

    //POST
    private void createBook( final JsonPlaceHolderApi jsonPlaceHolderApi,  final Integer userId ,final Integer houseId,
                             final Integer roomId, final Integer wallId,  final Integer shelfId ,Book book){

        Call<MyResponse<Book>> call = jsonPlaceHolderApi.createBook(userId,houseId, roomId, wallId, shelfId,book);

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Book> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                //TODO print error
            }
        });
    }

    //PATCH
    private void patchBook( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId,final Integer houseId,
                            final Integer roomId,final Integer wallId, final Integer shelfId,final Integer bookId, Book patchedBook){

        Call<MyResponse<Book>> call = jsonPlaceHolderApi.patchBook(userId, houseId, roomId, wallId, shelfId,bookId, patchedBook);

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(! response.isSuccessful()){
                    return;
                }
                List<Book> hres = response.body().getData();
                //TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                //TODO print error
            }
        });
    }

    //DELETE
    private void deleteBook( final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer userId, final Integer houseId, final Integer roomId,
                             final Integer wallId ,final Integer shelfId, final Integer bookId){
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.deleteBook(userId,houseId,roomId, wallId,shelfId ,bookId);
        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Book> hres = response.body().getData();
                //TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                //TODO print error
            }
        });
    }




}



