package uni.mobile.mobileapp.rest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("users/{userId}}/houses")
    Call<MyResponse<House>> getHouses(@Path("userId") int userId);

    @GET("users/{userId}/houses/{houseId}}/rooms")
    Call<MyResponse<Room>> getRooms(@Path("userId") int userId,@Path("houseId") int houseId);

    @GET("users/{userId}/houses/{houseId}}/rooms/{roomId}/walls")
    Call<MyResponse<Wall>> getWalls(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId);

    @GET("users/{userId}/houses/{houseId}}/rooms/{roomId}/walls/{wallId}/shelves")
    Call<MyResponse<Shelf>> getShelves(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId, @Path("wallId") int wallId);

    @GET("users/{userId}/houses/{houseId}}/rooms/{roomId}/walls/{wallId}/shelves/{shelfId}/books")
    Call<MyResponse<Book>> getBooks(@Path("userId") int userId,@Path("houseId") int houseId,@Path("roomId") int roomId,@Path("wallId") int wallId,@Path("shelfId") int shelfId);


}
