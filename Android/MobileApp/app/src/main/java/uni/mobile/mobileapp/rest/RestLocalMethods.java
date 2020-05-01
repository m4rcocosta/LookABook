package uni.mobile.mobileapp.rest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uni.mobile.mobileapp.SignInActivity;
import uni.mobile.mobileapp.SignUpActivity;
import uni.mobile.mobileapp.rest.callbacks.HouseCallback;
import uni.mobile.mobileapp.rest.callbacks.OnConnectionTimeoutListener;
import uni.mobile.mobileapp.rest.callbacks.BookCallback;
import uni.mobile.mobileapp.rest.callbacks.RoomCallback;
import uni.mobile.mobileapp.rest.callbacks.ShelfCallback;
import uni.mobile.mobileapp.rest.callbacks.UserCallback;
import uni.mobile.mobileapp.rest.callbacks.WallCallback;

public class RestLocalMethods {

    public  static Multimap<String, Integer> objectsIds = HashMultimap.create();
    private static List<User> users;
    private static List<House> houses;
    private static List<Room> rooms;
    private static List<Wall> walls;
    private static List<Shelf> shelves;
    private static List<Book> books;
    private static Gson gson= new Gson();
    private static JsonPlaceHolderApi jsonPlaceHolderApi;
    private static Context context;
    private static Integer userId;
    private static String userToken;
    private static OnConnectionTimeoutListener listener;
    public static final int FIRST_CHECK = 1;
    public static final int SECOND_CHECK = 2;
    public static final int MARCO_CHECK = 3;




    public static Boolean initRetrofit(Context ctx,String token){
        if(userToken==null)
            userToken=token;
        return initRetrofit(ctx);
    }
    //To build jsonPlaceHolderApi object handling REST API
    public static  Boolean initRetrofit(Context ctx) {
        context=ctx;


        if(userToken==null){
            Toast.makeText(ctx,"Token not found",Toast.LENGTH_SHORT).show();
            userToken="";
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        listener=new OnConnectionTimeoutListener() {
            @Override
            public void onConnectionTimeout() {
                Intent intent = new Intent(context, SignUpActivity.class);
                context.startActivity(intent);
            }
        };
        httpClient.addInterceptor(new NetworkConnectionInterceptor(context,userToken) );
//                .addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                return onOnIntercept(chain);
//            }
//        });

//        String railsHostBaseUrl="http://192.168.1.157:3000/api/v1/"; //DEVELOPMENT
        String railsHostBaseUrl="http://lookabookreal.herokuapp.com/api/v1/"; //PRODUCTION
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


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        return true;
    }




    /*
     * HELPERS
     */
// To stringify from object to json
    public static String jsonize(Object obj){
        return gson.toJson(obj);
    }

    /*
     * ALL
     */


    /*
     * USER
     */


    public static User getUserByEmail(String email, FirebaseUser userToBeCreated, int type_of_check,  @Nullable UserCallback callback){
        users = null;
        Call<MyResponse<User>> call = jsonPlaceHolderApi.getUserByEmail(email);
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {

                User railsUser;
                if (!isResponseSuccessfull(response)){
                    railsUser = null;
                }
                else {
                    users = response.body().getData();
                    if(users!=null) {
                        railsUser = users.get(0);
                        RestLocalMethods.setMyUserId(railsUser.getId());
                    }
                    else{
                        railsUser = null;
                        return;
                    }
                }
                if (type_of_check == FIRST_CHECK) {
                    if (railsUser != null) {
                        Toast.makeText(context, "User with email address " + users.get(0).getEmail() + " exists!", Toast.LENGTH_SHORT).show();
                        RestLocalMethods.setUserToken(railsUser.getAuth_token());
                        RestLocalMethods.setMyUserId(railsUser.getId());
                    }
                    else{   // You need to create a new user in rails (Synch in this thread)
                        Toast.makeText(context,"User with email address " + email + " doesn't exists!" ,Toast.LENGTH_SHORT).show();
                        //SignInActivity.createUserOnBackend(userToBeCreated);

                        String idToken = userToBeCreated.getUid();
                        Log.i("User Token: ", idToken);
                        // Send token to your backend via HTTPS
                        RestLocalMethods.setUserToken(idToken);
                                 User newRailsUser=new User(userToBeCreated.getDisplayName(),
                                 "",userToBeCreated.getPhoneNumber(),userToBeCreated.getEmail(), idToken);

                        Call<MyResponse<User>> call2 = jsonPlaceHolderApi.createUser(newRailsUser);
                        try
                        {
                            Response<MyResponse<User>> response2 = call2.execute();
                            railsUser= response2.body().getData().get(0);

                            //API response
                            Log.i("user","Created: " + railsUser.toString());
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }

                    }
                    if(callback!=null) callback.onSuccess(railsUser);
                }
                else if (type_of_check == SECOND_CHECK){
                    checkGetUserByMail( users.get(0));
                }

                else{

                }
            }



            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {
                isConnected(t);
            }
        });
        if (users != null) return users.get(0);
        else return null;
    }

    public static User getUserByToken(final JsonPlaceHolderApi jsonPlaceHolderApi){
        Call<MyResponse<User>> call = jsonPlaceHolderApi.getUserByToken();
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!isResponseSuccessfull(response)) return;;

                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {
                isConnected(t);
            }
        });
        return users.get(0);

    }

    public static User createUser( User user){
        users = null;
//        RequestBody rb= RequestBody.create(MediaType.parse("application/json"),jsonize(user));
//        Log.d("rb",jsonize(user));
        Call<MyResponse<User>> call = jsonPlaceHolderApi.createUser(user);

        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!isResponseSuccessfull(response)) return;;

                users = response.body().getData();
                User newRailsUser = users.get(0);
                if(newRailsUser!=null) {
                    Toast.makeText(context, "User with email address " + newRailsUser.getEmail() + " created on backend!", Toast.LENGTH_SHORT).show();
                    RestLocalMethods.setMyUserId(newRailsUser.getId());
                    RestLocalMethods.setUserToken(newRailsUser.getAuth_token());
                }
                else {
                    Toast.makeText(context, "User not  created on backend (fail)!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {            }
        });
        if (users != null) return users.get(0);
        else return null;
    }


    private static void checkCreate(User newRailsUser, String mail) {
        if(newRailsUser!=null) {
            Toast.makeText(context, "User with email address " + newRailsUser.getEmail() + " created on backend!", Toast.LENGTH_SHORT).show();
            RestLocalMethods.setMyUserId(newRailsUser.getId());
            RestLocalMethods.setUserToken(newRailsUser.getAuth_token());
        }
        else {

        }

    }

    private static void checkGetUserByMail(User oldRailsUser){

        if(oldRailsUser!=null) {
            RestLocalMethods.setMyUserId(oldRailsUser.getId());
            Toast.makeText(context, "User found on rails", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Problem with api result in creation of user", Toast.LENGTH_SHORT).show();
        }
    }



    public static User patchUser(  int userId,  User user){
        Call<MyResponse<User>> call = jsonPlaceHolderApi.patchUser(userId,user);

        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!isResponseSuccessfull(response)) return;;


                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {isConnected(t);}
        });
        return users.get(0);
    }

    public static User deleteUser(int userId){
        Call<MyResponse<User>> call = jsonPlaceHolderApi.deleteUser(userId);
        call.enqueue(new Callback<MyResponse<User>>() {
            @Override
            public void onResponse(Call<MyResponse<User>> call, Response<MyResponse<User>> response) {
                if(!isResponseSuccessfull(response)) return;;


                users = response.body().getData();
            }

            @Override
            public void onFailure(Call<MyResponse<User>> call, Throwable t) {
                isConnected(t);
            }
        });
        return users.get(0);
    }

    /*
     * HOUSE
     */


    // GET (index)
    public  static List<House> getHouses(final TextView textViewResult,  final Integer userId, final Boolean recursiveSearch  ){
        houses=null;
        Call<MyResponse<House>> call = jsonPlaceHolderApi.getHouses(userId);
        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!isResponseSuccessfull(response)) return;;


                houses = response.body().getData();

                for(House house: houses){
                    objectsIds.put("houses",house.getId());
                    if(recursiveSearch){
                        getRooms(textViewResult,userId,house.getId(),true);
                    }

                }
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                isConnected(t);
                return ;
            }

        });
        if (houses != null) return houses;
        else return null;
    }

    //POST
    public static House createHouse(final Integer userId, House house, @Nullable HouseCallback callback) {
        houses = null;
        Call<MyResponse<House>> call = jsonPlaceHolderApi.createHouse(userId,house);

        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!isResponseSuccessfull(response)) return;;

                houses = response.body().getData();
                if (callback != null) callback.onSuccess(houses);
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
        if (houses != null) return houses.get(0);
        else return null;
    }
    //PATCH
    public static void patchHouse(final Integer userId, final Integer houseId, House patchedHouse, @Nullable HouseCallback callback) {

        Call<MyResponse<House>> call = jsonPlaceHolderApi.patchHouse(userId, houseId, patchedHouse);

        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!isResponseSuccessfull(response)) return;;

                List<House> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }
    //DELETE
    public static void deleteHouse(final Integer userId, final Integer houseId, @Nullable HouseCallback callback) {
        Call<MyResponse<House>> call = jsonPlaceHolderApi.deleteHouse(userId, houseId);
        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!isResponseSuccessfull(response)) return;

                List<House> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                Toast.makeText(context,"API response failed: " + t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }


    /*
     * ROOM
     */


    //GET
    public static List<Room> getRooms(final TextView textViewResult, final Integer userId  ,final Integer houseId, final Boolean recursiveSearch) {

        rooms=null;
        Call<MyResponse<Room>> call = jsonPlaceHolderApi.getRooms(userId,houseId);
        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(!isResponseSuccessfull(response)) return;;

                rooms = response.body().getData();

                for (Room room : rooms) {
                    Log.d("myrest", "room:"+room.getId() );
                    if(recursiveSearch){
                        getWalls(textViewResult, userId, houseId, room.getId(), true);
                    }


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                Log.d("myrest", "room:" + t.getMessage() );
            }
        });
        if (rooms != null) return rooms;
        else return null;

    }

    //POST
    public static Room createRoom(final Integer userId ,final Integer houseId, Room room, @Nullable RoomCallback callback) {
        rooms = null;
        Call<MyResponse<Room>> call = jsonPlaceHolderApi.createRoom(userId,houseId,room);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(!isResponseSuccessfull(response)) return;;

                rooms = response.body().getData();
                if (callback != null) callback.onSuccess(rooms);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
        if (rooms != null) return rooms.get(0);
        else return null;
    }
    //PATCH
    public static void patchRoom(final Integer userId, final Integer houseId, final Integer roomId, Room patchedRoom, @Nullable RoomCallback callback){

        Call<MyResponse<Room>> call = jsonPlaceHolderApi.patchRoom(userId, houseId, roomId, patchedRoom);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if (!isResponseSuccessfull(response)) return;;

                List<Room> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                Toast.makeText(context,"API response failed: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    //DELETE
    public static void deleteRoom(final Integer userId, final Integer houseId, final Integer roomId, @Nullable RoomCallback callback) {
        Call<MyResponse<Room>> call = jsonPlaceHolderApi.deleteRoom(userId,houseId,roomId);
        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(!isResponseSuccessfull(response)) return;

                List<Room> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }

    /*
     * WALLS
     */

    //GET
    public  static List<Wall> getWalls(final TextView textViewResult, final Integer userId, final Integer houseId, final Integer roomId, final Boolean recursiveSearch) {
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.getWalls(userId,houseId,  roomId);

        walls = null;
        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!isResponseSuccessfull(response)) return;;

                walls = response.body().getData();
                String content = "\n WALLS" + "\n";

                for(Wall wall: walls){
                    objectsIds.put("walls",wall.getId());

                    Log.d("myrest" , "wall "+ wall.getName() );

                    if(recursiveSearch){
                        getShelves(textViewResult, userId, houseId, roomId, wall.getId(), true);

                    }


                }


            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Log.d("myrest" , "wall Code:"+ t.getMessage());
                isConnected(t);
            }

        });
        return walls;
    }

    //POST  (not Trump one)
    public static Wall createWall(final Integer userId , final Integer houseId, Integer roomId, Wall wall, @Nullable WallCallback callback){
        walls = null;
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.createWall(userId,houseId,roomId,wall);

        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!isResponseSuccessfull(response)) return;;

                walls = response.body().getData();
                if (callback != null) callback.onSuccess(walls);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();
            }
        });
        if(walls!= null) return walls.get(0);
        else return null;
    }

    //PATCH
    public static void patchWall(final Integer userId, final Integer houseId, Integer roomId, Integer wallId, Wall patchedWall, @Nullable WallCallback callback){

        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.patchWall(userId, houseId, roomId, wallId, patchedWall);

        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if (!isResponseSuccessfull(response)) return;;

                List<Wall> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Toast.makeText(context,"API response failed: " + t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }
    //DELETE
    public static void deleteWall(final Integer userId, final Integer houseId, final Integer roomId, final Integer wallId, @Nullable WallCallback callback){
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.deleteWall(userId, houseId, roomId, wallId);
        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!isResponseSuccessfull(response)) return;;

                List<Wall> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }

    /*
     * SHELF
     */


//GET

    public  static List<Shelf> getShelves(final TextView textViewResult, Integer userId, final Integer houseId, final Integer roomId , final Integer wallId, final Boolean recursiveSearch ){
        shelves = null;
        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.getShelves(userId,houseId, roomId, wallId);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!isResponseSuccessfull(response)) return;;

                shelves = response.body().getData();

                for(Shelf shelf: shelves){
                    objectsIds.put("shelves",shelf.getId());

                    Log.d("myrest", "shelf Code: " + shelf.getName());

                    if(recursiveSearch){
                        getShelves(textViewResult, houseId, roomId, wallId, shelf.getId(), true);

                    }


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                Log.d("myrest", "shelf Code: " + t.getMessage());
            }
        });
        if (shelves != null) return shelves;
        else return null;
    }

    //POST
    public static Shelf createShelf(final Integer userId ,Integer houseId, Integer roomId, Integer wallId, Shelf shelf, @Nullable ShelfCallback callback){

        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.createShelf(userId,houseId, roomId, wallId, shelf);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!isResponseSuccessfull(response)) return;;

                shelves = response.body().getData();
                if (callback != null) callback.onSuccess(shelves);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
        if (shelves!=null) return shelves.get(0);
        else return null;
    }

    //PATCH
    public static void patchShelf(final Integer userId, final Integer houseId, Integer roomId, Integer wallId, Integer shelfId, Shelf patchedShelf, @Nullable ShelfCallback callback){

        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.patchShelf(userId, houseId, roomId, wallId, shelfId, patchedShelf);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!isResponseSuccessfull(response)) return;;

                List<Shelf> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                Toast.makeText(context,"API response failed: " + t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }
    //DELETE
    public static void deleteShelf(Integer userId, final Integer houseId, Integer roomId, Integer wallId, Integer shelfId, @Nullable ShelfCallback callback){
        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.deleteShelf(userId,houseId,roomId, wallId, shelfId);
        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!isResponseSuccessfull(response)) return;;

                List<Shelf> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }
            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

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
                if(!isResponseSuccessfull(response)) return;;

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
    public  static List<Book> scanAllBooks(Activity act, Button btn){
        books=null;
        btn.setClickable(false);
        Snackbar.make(act.findViewById(android.R.id.content), "Search on Google started ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.scanAllBooks(userId );

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!isResponseSuccessfull(response)) {
                    btn.setClickable(true);
                    return;

                }

                books = response.body().getData();
                btn.setClickable(true);
                Snackbar.make(act.findViewById(android.R.id.content), "Scan completed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //TODO update books on book fragment
            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                Log.d("myrest", "book Code: " + t.getMessage());
                isConnected(t);
                btn.setClickable(true);
            }
        });
        if(books!= null) return books;
        else return null;
    }

    //GET
    public  static List<Book> getAllBooks(){
        books=null;


        Call<MyResponse<Book>> call = jsonPlaceHolderApi.getAllBooks(userId );

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!isResponseSuccessfull(response)) return;;

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
    public static Book createBook(final Integer userId, final Integer houseId, final Integer roomId, final Integer wallId, final Integer shelfId, Book book, @Nullable BookCallback callback){

        books=null;
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.createBook(userId,houseId, roomId, wallId, shelfId,book);

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!isResponseSuccessfull(response)) return;;

                books = response.body().getData();
                if (callback != null) callback.onSuccess(books);
//TODO print changes
            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
        if(books!=null) return books.get(0);
        else return null;
    }

    //PATCH
    public static void patchBook(final Integer userId,final Integer houseId, final Integer roomId,final Integer wallId, final Integer shelfId,final Integer bookId, Book patchedBook, @Nullable BookCallback callback) {

        Call<MyResponse<Book>> call = jsonPlaceHolderApi.patchBook(userId, houseId, roomId, wallId, shelfId,bookId, patchedBook);

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!isResponseSuccessfull(response)) return;;

                List<Book> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                Toast.makeText(context,"API response failed: "+t.getMessage() ,Toast.LENGTH_LONG).show();
            }
        });
    }

    //DELETE
    public static void deleteBook(final Integer userId, final Integer houseId, final Integer roomId, final Integer wallId, final Integer shelfId, final Integer bookId, @Nullable BookCallback callback){
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.deleteBook(userId,houseId,roomId, wallId,shelfId ,bookId);
        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!isResponseSuccessfull(response)) return;

                List<Book> hres = response.body().getData();
                if (callback != null) callback.onSuccess(hres);
//TODO print changes
            }
            @Override
            public  void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                Toast.makeText(context,"API response failed: " + t.getMessage() ,Toast.LENGTH_LONG).show();

            }
        });
    }

    public static Boolean isResponseSuccessfull(Response response){
        if(! response.isSuccessful()){
            Toast.makeText(context,"API response unsuccessful" ,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static void isConnected(Throwable t){
        String message="Failed Api";
        if(t instanceof SocketTimeoutException){
            message = "Socket Time out. Please try again.";
        }
        if(t instanceof NoConnectivityException) {
            message=t.getMessage();
            if(context!=null) {
                Intent intent = new Intent(context, SignInActivity.class);
                context.startActivity(intent);
            }
        }
        if(context!=null) {

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static JsonPlaceHolderApi getJsonPlaceHolderApi() {
        return jsonPlaceHolderApi;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken(String userToken) {
        RestLocalMethods.userToken = userToken;
        initRetrofit(context);
    }

    public static Integer getMyUserId() {
        return userId;
    }

    public static void setMyUserId(Integer myUserId) {
        RestLocalMethods.userId = myUserId;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        RestLocalMethods.context = context;
    }

    private static okhttp3.Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            okhttp3.Response response = chain.proceed(chain.request());
            String content = response.toString();
            Log.d("retrofit",  " - " + content);
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
        }
        catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            if(listener != null)
                listener.onConnectionTimeout();
        }

        return chain.proceed(chain.request());
    }
}



