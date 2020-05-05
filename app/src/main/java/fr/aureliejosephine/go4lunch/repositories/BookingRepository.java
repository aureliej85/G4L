package fr.aureliejosephine.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import fr.aureliejosephine.go4lunch.models.Booking;

public class BookingRepository {

    private static final String COLLECTION_NAME = "booking";
    private CollectionReference bookingCollection;
    private Booking booking;
    private static volatile BookingRepository INSTANCE;

    public static BookingRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BookingRepository();
        }
        return INSTANCE;
    }

    public BookingRepository(){
        this.bookingCollection = getBookingCollection();
    }

    // --- COLLECTION REFERENCE ---

    private CollectionReference getBookingCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private Query getBookingGroupCollection(){
        return FirebaseFirestore.getInstance().collectionGroup(COLLECTION_NAME);
    }


    // --- CREATE ---

    public Task<DocumentReference> createBooking(String rName, String rUrlPic, String uIdr, String uUsername, String uPic, String rDate){
        Booking bookingToCreate = new Booking(rName, rUrlPic, uIdr, uUsername, uPic, rDate);
        return bookingCollection.add(bookingToCreate);
    }


    // --- GET ---

    public Task<QuerySnapshot> getBookingRestaurantName(String rName){
        return bookingCollection.whereEqualTo("rName", rName).get();
    }


    // --- UPDATE ---

    public Task<Void> updateUrlPicRestaurant(String rName, String rUrlPic){
        Booking booking = new Booking();
        booking.setrUrlPic(rUrlPic);
        return bookingCollection.document("rName").update("rUrlPic", rUrlPic);
    }


    // --- DELETE ---

    public Task<Void> deleteBooking(String id) {
        return bookingCollection.document(id).delete();
    }


}
