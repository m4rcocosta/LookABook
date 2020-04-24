package uni.mobile.mobileapp.rest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    /*
     * USER
     * */
    @GET("get-user-by-email")
    Call<MyResponse<User>> getUserByEmail(@Query("email") String email);

    @GET("get-user-by-token")
    Call<MyResponse<User>> getUserByToken();


    @POST("users")
    Call<MyResponse<User>> createUser( @Body User user);


    @PATCH("users/{userId}")
    Call<MyResponse<User>> patchUser(@Path("userId") int userId,  @Body User user);

    @DELETE("users/{userId}/houses/{houseId}")
    Call<MyResponse<User>> deleteUser(@Path("userId") int userId);


    /*
     * HOUSE
     * */
    @GET("users/{userId}/houses")
    Call<MyResponse<House>> getHouses(@Path("userId") int userId);

    @POST("users/{userId}/houses")
    Call<MyResponse<House>> createHouse(@Path("userId") int userId, @Body House house);

    @PATCH("users/{userId}/houses/{houseId}")
    Call<MyResponse<House>> patchHouse(@Path("userId") int userId, @Path("houseId") int houseId, @Body House house);

    @DELETE("users/{userId}/houses/{houseId}")
    Call<MyResponse<House>> deleteHouse(@Path("userId") int userId, @Path("houseId") int houseId);

    /*
     * ROOM
     * */
    @GET("users/{userId}/houses/{houseId}/rooms")
    Call<MyResponse<Room>> getRooms(@Path("userId") int userId,@Path("houseId") int houseId);

    @POST("users/{userId}/houses/{houseId}/rooms")
    Call<MyResponse<Room>> createRoom(@Path("userId") int userId, @Path("houseId") int houseId, @Body Room room);

    @PATCH("users/{userId}/houses/{houseId}/rooms/{roomId}")
    Call<MyResponse<Room>> patchRoom(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Body Room room);

    @DELETE("users/{userId}/houses/{houseId}/rooms/{roomId}")
    Call<MyResponse<Room>> deleteRoom(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId);


    /*
     * WALL
     * */
    @GET("users/{userId}/houses/{houseId}/rooms/{roomId}/walls")
    Call<MyResponse<Wall>> getWalls(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId);

    @POST("users/{userId}/houses/{houseId}/rooms/{roomId}/walls")
    Call<MyResponse<Wall>> createWall(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Body Wall wall);

    @PATCH("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}")
    Call<MyResponse<Wall>> patchWall(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId, @Body Wall wall);

    @DELETE("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}")
    Call<MyResponse<Wall>> deleteWall(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId );



    /*
     * SHELF
     * */
    @GET("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves")
    Call<MyResponse<Shelf>> getShelves(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId, @Path("wallId") int wallId);

    @POST("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves")
    Call<MyResponse<Shelf>> createShelf(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId , @Body Shelf shelf);

    @PATCH("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}")
    Call<MyResponse<Shelf>> patchShelf(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId, @Path("wallId") int shelfId, @Body Shelf shelf);

    @DELETE("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}")
    Call<MyResponse<Shelf>> deleteShelf(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId, @Path("wallId") int shelfId);


    /*
     * BOOK
     * */
    @GET("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}/books")
    Call<MyResponse<Book>> getBooks(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId,@Path("wallId") int wallId,@Path("shelfId") int shelfId);

    @GET("allBooks")
    Call<MyResponse<Book>> getAllBooks(@Path("userId") int userId);

    @POST("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}/books")
    Call<MyResponse<Book>> createBook(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId , @Path("shelfId") int shelfId, @Body Book book);

    @PATCH("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}/books/{bookId}")
    Call<MyResponse<Book>> patchBook(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId, @Path("shelfId") int shelfId,
                                      @Path("shelfId") int bookId, @Body Book book);

    @DELETE("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}/books/{bookId}")
    Call<MyResponse<Book>> deleteBook(@Path("userId") int userId, @Path("houseId") int houseId, @Path("roomId") int roomId , @Path("wallId") int wallId, @Path("shelfId") int shelfId,
                                       @Path("shelfId") int bookId);


    /*
     * GOOGLE Books API methods
     *
     */
    @GET("users/{userId}/houses/{houseId}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}/search-on-google")
    Call<MyResponse<Book>> getSearchOnGoogle(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId,@Path("wallId") int wallId,@Path("shelfId") int shelfId);


}
