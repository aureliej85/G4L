package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import fr.aureliejosephine.go4lunch.repositories.BookingRepository;

public class BookingViewModel extends AndroidViewModel {

    private BookingRepository bookingRepository;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        this.bookingRepository = new BookingRepository();
    }


    public void createBooking(String rName, String rUrlPic, String uIdr, String uUsername, String rDate){
        bookingRepository.createBooking(rName, rUrlPic, uIdr, uUsername, rDate).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("BookingViewModel", "onFailure: createBooking" + e.toString());
            }
        });
    }


    public void updateUrlPicRestaurant(String rName, String rUrlPic){
        bookingRepository.updateUrlPicRestaurant(rName, rUrlPic).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("BookingViewModel", "onFailure: updateUrlPicRestaurant " + e.toString());
            }
        });
    }
}
