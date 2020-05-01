package uni.mobile.mobileapp.rest.callbacks;

import androidx.annotation.NonNull;

import java.util.List;

import uni.mobile.mobileapp.rest.Shelf;
import uni.mobile.mobileapp.rest.User;

public interface UserCallback {
    void onSuccess( User value);

    //void onError(@NonNull Throwable throwable);
}
