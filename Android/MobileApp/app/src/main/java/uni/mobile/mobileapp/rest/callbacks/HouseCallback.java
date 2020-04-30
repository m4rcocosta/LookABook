package uni.mobile.mobileapp.rest.callbacks;

import androidx.annotation.NonNull;

import java.util.List;

import uni.mobile.mobileapp.rest.House;

public interface HouseCallback {
    void onSuccess(@NonNull List<House> value);

    //void onError(@NonNull Throwable throwable);
}
