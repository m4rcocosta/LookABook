package uni.mobile.mobileapp.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    //relative Url needs to be inserted here
    @GET("users/{userId}}/houses")
    Call<List<House>> getHouses(@Path("userId") int userId);

    //relative Url needs to be inserted here
    @GET("users/{userId}/houses/{houseId}}/rooms")
    Call<List<Room>> getRooms(@Path("userId") int userId,@Path("houseId") int houseId);


}
