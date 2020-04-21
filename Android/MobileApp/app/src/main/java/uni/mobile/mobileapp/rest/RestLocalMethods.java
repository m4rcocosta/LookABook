package uni.mobile.mobileapp.rest;

import android.widget.TextView;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestLocalMethods {

    public  static Multimap<String, Integer> objectsIds = HashMultimap.create();


    /*
     * ALL
     */
    public static void getAllObjectsFromUser(final TextView textViewResult , JsonPlaceHolderApi jsonPlaceHolderApi){
        getHouses(textViewResult, jsonPlaceHolderApi, true);
    }

    /*
     * HOUSE
     */



    public  static void getHouses(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi, final Boolean recursiveSearch  ){

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
                    objectsIds.put("houses",house.getId());
                    if(recursiveSearch){
                        getRooms(textViewResult,jsonPlaceHolderApi,house.getId(),true);
                    }
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
    /*
     * WALL
     */

    public static  void getRooms(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer houseId   , final Boolean recursiveSearch ){
        // Call<MyResponse<Room>> call = jsonPlaceHolderApi.getRooms(1, houseId);
        Call<MyResponse<Room>> call = jsonPlaceHolderApi.getRooms(1,houseId);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Room> rooms = response.body().getData();
                String content = "\n ROOMS" + "\n";
                for (Room room : rooms) {
                    objectsIds.put("rooms", room.getId());
                    if(recursiveSearch){
                        getWalls(textViewResult,jsonPlaceHolderApi,houseId,room.getId(),true);
                    }
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
    public  static void getWalls(final TextView textViewResult, final JsonPlaceHolderApi jsonPlaceHolderApi, final Integer houseId,final Integer roomId   , final Boolean recursiveSearch ){
        //Call<MyResponse<Wall>> call = jsonPlaceHolderApi.getWalls(1,1,1);
        Call<MyResponse<Wall>> call = jsonPlaceHolderApi.getWalls(1,houseId,  roomId);

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
                    objectsIds.put("walls",wall.getId());
                    if(recursiveSearch){
                        getShelves(textViewResult,jsonPlaceHolderApi,houseId,roomId,wall.getId(),true);

                    }
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
    public  static void getShelves( final TextView textViewResult,final JsonPlaceHolderApi jsonPlaceHolderApi ,final Integer houseId,final Integer roomId,final Integer wallId  ,Boolean recursiveSearch ){
        //Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.getShelves(1,1,1,1);
        Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.getShelves(1,houseId,  roomId,  wallId);

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
                    objectsIds.put("shelves",shelf.getId());
                    getBooks(textViewResult,jsonPlaceHolderApi,houseId,roomId,wallId,shelf.getId(),true);

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
    public  static void getBooks( final TextView textViewResult,final  JsonPlaceHolderApi jsonPlaceHolderApi , final Integer houseId,final  Integer roomId,final  Integer wallId, final Integer shelfId  ,Boolean recursiveSearch ){
        //Call<MyResponse<Book>> call = jsonPlaceHolderApi.getBooks(1,1,1,1,1);
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.getBooks(1, houseId,  roomId,  wallId,  shelfId );

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
                    objectsIds.put("books",book.getId());
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
