package uni.mobile.mobileapp.rest.callbacks;

import androidx.annotation.NonNull;

import java.util.List;

import uni.mobile.mobileapp.rest.Room;

public interface RoomCallback {
    void onSuccess(@NonNull List<Room> value);

    //void onError(@NonNull Throwable throwable);
}
