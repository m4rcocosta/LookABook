package uni.mobile.mobileapp.rest;


import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uni.mobile.mobileapp.R;
import uni.mobile.mobileapp.rest.atv.model.TreeNode;

public class RestTreeLocalMethods {

    public  static Multimap<String, Integer> objectsIds = HashMultimap.create();
    private static Context ctx;
    /*
     * ALL
     */

    public static void printAllObjectsFromUser(final Integer userId,final TreeNode root){
        ctx=RestLocalMethods.getContext();
        RestTreeLocalMethods.getHouses(userId,root, true);

    }


    /*
    User
     */
    //public static void postUser

    /*
     * HOUSE
     */



    public  static void getHouses(Integer userId,final TreeNode parent, final Boolean recursiveSearch  ){
        //if(userId==null)
        //userId=RestLocalMethods.getMyUserId(); //TODO use the real one
        JsonPlaceHolderApi jsonPlaceHolderApi=RestLocalMethods.getJsonPlaceHolderApi();

        Call<MyResponse<House>> call = jsonPlaceHolderApi.getHouses(userId);

        call.enqueue(new Callback<MyResponse<House>>() {
            @Override
            public void onResponse(Call<MyResponse<House>> call, Response<MyResponse<House>> response) {
                if(!RestLocalMethods.isResponseSuccessfull(response))return;

                List<House> houses = response.body().getData();
                for(House house: houses){
                    objectsIds.put("houses",house.getId());
                    //Child
                    MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_houseicon, house.getName() );
                    TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(ctx, false, R.layout.child, 25));
                    //Add child.
                    parent.addChildren(child);


                    if(recursiveSearch){
                        getRooms(child,house.getId(),true);
                    }

                }

            }

            @Override
            public void onFailure(Call<MyResponse<House>> call, Throwable t) {
                RestLocalMethods.isConnected(t);
            }
        });
    }


    /*
     * ROOM
     */
    /*
     * WALL
     */

    public static  void getRooms(final TreeNode parent, final Integer houseId   , final Boolean recursiveSearch ){
        // Call<MyResponse<Room>> call = jsonPlaceHolderApi.getRooms(RestLocalMethods.getMyUserId(), houseId);
        Call<MyResponse<Room>> call = RestLocalMethods.getJsonPlaceHolderApi().getRooms(RestLocalMethods.getMyUserId(),houseId);

        call.enqueue(new Callback<MyResponse<Room>>() {
            @Override
            public void onResponse(Call<MyResponse<Room>> call, Response<MyResponse<Room>> response) {
                if(!RestLocalMethods.isResponseSuccessfull(response))return;

                List<Room> rooms = response.body().getData();
                for (Room room : rooms) {
                    objectsIds.put("rooms", room.getId());
                    //Child
                    MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_roomicon, room.getName() );
                    TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(ctx, false, R.layout.child, 45));
                    //Add child.
                    child.setSelected(false);
                    parent.addChildren(child);


                    if(recursiveSearch){
                        getWalls(child,houseId,room.getId(),true);
                    }

                }

            }

            @Override
            public void onFailure(Call<MyResponse<Room>> call, Throwable t) {
                RestLocalMethods.isConnected(t);
            }
        });

    }


    public  static void getWalls(final TreeNode parent,  final Integer houseId, final Integer roomId   , final Boolean recursiveSearch ){
        //Call<MyResponse<Wall>> call = jsonPlaceHolderApi.getWalls(RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId());
        Call<MyResponse<Wall>> call = RestLocalMethods.getJsonPlaceHolderApi().getWalls(RestLocalMethods.getMyUserId(),houseId,  roomId);

        call.enqueue(new Callback<MyResponse<Wall>>() {
            @Override
            public void onResponse(Call<MyResponse<Wall>> call, Response<MyResponse<Wall>> response) {
                if(!RestLocalMethods.isResponseSuccessfull(response))return;

                List<Wall> walls = response.body().getData();


                for(Wall wall: walls){
                    objectsIds.put("walls",wall.getId());

                    //Child
                    MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_wallicon2, wall.getName() );
                    TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(ctx, false, R.layout.child, 65));
                    //Add child.
                    parent.addChildren(child);






                    if(recursiveSearch){
                        getShelves(child,houseId,roomId,wall.getId(),true);

                    }


                }

            }

            @Override
            public void onFailure(Call<MyResponse<Wall>> call, Throwable t) {
                RestLocalMethods.isConnected(t);
            }
        });
    }

    /*
     * SHELF
     */
    public  static void getShelves(final TreeNode parent,final Integer houseId,final Integer roomId,final Integer wallId  ,Boolean recursiveSearch ){
        //Call<MyResponse<Shelf>> call = jsonPlaceHolderApi.getShelves(RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId());
        Call<MyResponse<Shelf>> call = RestLocalMethods.getJsonPlaceHolderApi().getShelves(RestLocalMethods.getMyUserId(),houseId,  roomId,  wallId);

        call.enqueue(new Callback<MyResponse<Shelf>>() {
            @Override
            public void onResponse(Call<MyResponse<Shelf>> call, Response<MyResponse<Shelf>> response) {
                if(!response.isSuccessful()){

                }
                List<Shelf> shelves = response.body().getData();


                for(Shelf shelf: shelves){
                    objectsIds.put("shelves",shelf.getId());

                    //Child
                    MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_shelficon, shelf.getName() );
                    TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(ctx, false, R.layout.child, 85));
                    //Add child.
                    parent.addChildren(child);





                    //if(recursiveSearch)
                    // getBooks(textViewResult,jsonPlaceHolderApi,houseId,roomId,wallId,shelf.getId(),true);

                }

            }

            @Override
            public void onFailure(Call<MyResponse<Shelf>> call, Throwable t) {
                RestLocalMethods.isConnected(t);
            }
        });
    }


    /*
     * BOOK
     */
    public  static void getBooks( final TreeNode parent,final Context ctx ,final TextView textViewResult,final  JsonPlaceHolderApi jsonPlaceHolderApi , final Integer houseId,final  Integer roomId,final  Integer wallId, final Integer shelfId  ,Boolean recursiveSearch ){
        //Call<MyResponse<Book>> call = jsonPlaceHolderApi.getBooks(RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId(),RestLocalMethods.getMyUserId());
        Call<MyResponse<Book>> call = jsonPlaceHolderApi.getBooks(RestLocalMethods.getMyUserId(), houseId,  roomId,  wallId,  shelfId );

        call.enqueue(new Callback<MyResponse<Book>>() {
            @Override
            public void onResponse(Call<MyResponse<Book>> call, Response<MyResponse<Book>> response) {
                if(!response.isSuccessful()){
                    return;
                }
                List<Book> books = response.body().getData();



                for(Book book: books){
                    objectsIds.put("books",book.getId());
                    //Child
                    MyHolder.IconTreeItem childItem = new MyHolder.IconTreeItem(R.drawable.ic_folder, book.getTitle() );
                    TreeNode child = new TreeNode(childItem).setViewHolder(new MyHolder(ctx, false, R.layout.child, RestLocalMethods.getMyUserId() ));
                    //Add child.
                    parent.addChildren(child);






                }

            }

            @Override
            public void onFailure(Call<MyResponse<Book>> call, Throwable t) {
                RestLocalMethods.isConnected(t);
            }
        });
    }



}
