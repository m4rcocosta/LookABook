package uni.mobile.mobileapp.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkConnectionInterceptor implements Interceptor {

    private Context mContext;
    private String userToken;
    public NetworkConnectionInterceptor(Context context,String userToken) {
        mContext = context;
        this.userToken=userToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isConnected()) {
            throw new NoConnectivityException();
            // Throwing our custom exception 'NoConnectivityException'
        }
        Request original = chain.request();

        Request request = original.newBuilder()
                .header("TOKEN", userToken)
                .header("Accept", "application/json")
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}