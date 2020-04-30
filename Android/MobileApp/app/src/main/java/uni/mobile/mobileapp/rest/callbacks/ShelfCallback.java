package uni.mobile.mobileapp.rest.callbacks;

import androidx.annotation.NonNull;

import java.util.List;

import uni.mobile.mobileapp.rest.Shelf;

public interface ShelfCallback {
    void onSuccess(@NonNull List<Shelf> value);

    //void onError(@NonNull Throwable throwable);
}
