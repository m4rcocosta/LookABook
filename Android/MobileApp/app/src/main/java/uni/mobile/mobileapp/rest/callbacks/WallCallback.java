package uni.mobile.mobileapp.rest.callbacks;

import androidx.annotation.NonNull;

import java.util.List;

import uni.mobile.mobileapp.rest.Wall;

public interface WallCallback {
    void onSuccess(@NonNull List<Wall> value);

    //void onError(@NonNull Throwable throwable);
}
