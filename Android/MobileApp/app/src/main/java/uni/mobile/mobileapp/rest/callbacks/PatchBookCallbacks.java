package uni.mobile.mobileapp.rest.callbacks;

import androidx.annotation.NonNull;

import java.util.List;

import uni.mobile.mobileapp.rest.Book;

public interface PatchBookCallbacks {
    void onSuccess(@NonNull List<Book> value);

    //void onError(@NonNull Throwable throwable);
}
