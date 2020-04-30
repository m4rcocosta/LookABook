package uni.mobile.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.util.Log;

public class CheckNetwork {

    private Context context;

    private Boolean isNetworkConnected;
    // You need to pass the context when creating the class
    public CheckNetwork(Context context) {
        this.context = context;
    }

    // Network Check
    public void registerNetworkCallback()
    {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                                                                   @Override
                                                                   public void onAvailable(Network network) {
                                                                       isNetworkConnected = true; // Global Static Variable
                                                                   }
                                                                   @Override
                                                                   public void onLost(Network network) {
                                                                       isNetworkConnected = false; // Global Static Variable
                                                                       Log.d("checknetwork","connection lost");
                                                                       connectionLost();
                                                                   }
                                                               }

            );
            isNetworkConnected = false;
        }catch (Exception e){
            isNetworkConnected = false;
            connectionLost();
        }
    }

    private void connectionLost(){
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);


    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Boolean getNetworkConnected() {
        return isNetworkConnected;
    }

    public void setNetworkConnected(Boolean networkConnected) {
        isNetworkConnected = networkConnected;
    }
}
