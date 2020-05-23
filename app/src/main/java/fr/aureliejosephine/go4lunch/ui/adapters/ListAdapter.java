package fr.aureliejosephine.go4lunch.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Booking;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.distance.DistanceApiResponse;
import fr.aureliejosephine.go4lunch.models.distance.Element;
import fr.aureliejosephine.go4lunch.models.distance.Row;
import fr.aureliejosephine.go4lunch.repositories.BookingRepository;
import fr.aureliejosephine.go4lunch.repositories.DistanceRepository;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.ui.activities.DetailsActivity;
import fr.aureliejosephine.go4lunch.ui.fragments.ListFragment;
import fr.aureliejosephine.go4lunch.viewmodel.BookingViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.DetailsViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.DistanceViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.RestaurantViewModel;

import static java.security.AccessController.getContext;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    private Context context;
    ArrayList<DetailsResult> restaurantsList;

    private RestaurantViewModel restaurantViewModel;
    private DistanceViewModel distanceViewModel;
    private DetailsViewModel detailsViewModel;
    private String dist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference restaurantRef;
    private DocumentReference wmRef;
    private Restaurant restaurant;
    private List<String> openHour;
    private DistanceRepository distanceRepository;
    private User user;
    private double lat;
    private double lgt;
    String userPosition;
    String hour;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 300;
    public static final int MAX_HEIGHT = 300;
    String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";




    public ListAdapter(Context context, ArrayList<DetailsResult> restaurantsList) {
        this.context = context;
        this.restaurantsList = restaurantsList;

        restaurantViewModel = ViewModelProviders.of((FragmentActivity) context).get(RestaurantViewModel.class);
        distanceViewModel = ViewModelProviders.of((FragmentActivity) context).get(DistanceViewModel.class);
        detailsViewModel = ViewModelProviders.of((FragmentActivity) context).get(DetailsViewModel.class);

    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, int position) {
        DetailsResult result = restaurantsList.get(position);

        holder.UpdateWithData(this.restaurantsList.get(position));



        //holder.nbOfUsersEatingHere(result);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                //intent.putExtra("result", result);
                intent.putExtra("placeId", result.getPlaceId());
                view.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }



    public class ListViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        TextView addrTv;
        TextView hoursTv;
        TextView nbUserTv;
        ImageView picIv;
        TextView distance;
        RatingBar ratingBar;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleItemList);
            addrTv = itemView.findViewById(R.id.addressItemList);
            hoursTv = itemView.findViewById(R.id.hoursDetailsTv);
            nbUserTv = itemView.findViewById(R.id.nbUserHereTv);
            picIv = itemView.findViewById(R.id.picItemList);
            distance = itemView.findViewById(R.id.distTv);
            ratingBar = itemView.findViewById(R.id.item_ratingBar);

        }



        public void UpdateWithData(DetailsResult result){
            RequestManager glide = Glide.with(itemView);

            this.titleTv.setText(result.getName());
            this.addrTv.setText(result.getVicinity());
            //this.distance.setText(dist);

            // Display Photos
            if (!(result.getPhotos() == null)){
                if (!(result.getPhotos().isEmpty())){
                    String url = BASE_URL+"?maxwidth="+MAX_WIDTH+"&maxheight="+MAX_HEIGHT+"&photoreference="+result.getPhotos().get(0).getPhotoReference()+"&key="+key ;
                    glide.load(url)
                            .apply(RequestOptions.centerCropTransform())
                            .into(picIv);
                    createRestaurantInFirestore(result, url);
                }
            }else{
                glide.load(R.drawable.logo_g4l).apply(RequestOptions.centerCropTransform()).into(picIv);
                createRestaurantInFirestore(result, null);
            }


            // Distance - VIEWMODEL
           wmRef = db.collection("users").document(getCurrentUser().getUid());
            wmRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot != null){
                            user = documentSnapshot.toObject(User.class);

                            if (user != null){
                                lat = user.getLatitude();
                                lgt = user.getLongitude();
                                userPosition = lat + "," + lgt;

                                distanceViewModel.getDistance(userPosition,"place_id:" + result.getPlaceId()).observe((FragmentActivity) context, distanceResponse -> {
                                    if (distanceResponse != null) {
                                        dist = distanceResponse.getRows().get(0).getElements().get(0).getDistance().getText();
                                        Log.e("ListAdapter", "UpdateWithData: " + dist );
                                        distance.setText(dist);
                                    }

                                });
                            } else {
                                distance.setVisibility(View.GONE);
                            }

                        }
                }
            });



            // Nb Users
            CollectionReference wmRef = db.collection("users");
            wmRef.whereEqualTo("placeName", result.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            nbUserTv.setText(Integer.toString(task.getResult().size())  );
                        }
                    }
                }
            });


            //OPEN HOUR
           if (result.getOpeningHours() != null ){
                if(result.getOpeningHours().getOpenNow()){
                    String hour = result.getOpeningHours().getOpenNow().toString();
                    hoursTv.setText("Open" );
                    Log.e("ListAdapter", "Opening Hour open: " + hour );
                } else {
                    hoursTv.setText("Closed now");
                    hoursTv.setTextColor(Color.RED);
                    Log.e("ListAdapter", "Opening Hour close: " );
                }
            }


            // Display Opening Hours
            /*if (result.getOpeningHours() != null){
                if (result.getOpeningHours().getOpenNow().toString().equals("false")){
                    displayOpeningHour("CLOSED",null);
                }else{
                    getOpeningHoursInfo(result);
                }
            }else{
                displayOpeningHour("OPENING_HOURS_NOT_KNOW",null);
            }*/


            // GET RATING
            if (result.getRating() != null){
                double googleRating = result.getRating();
                double rating = (googleRating * 3) / 5;
                ratingBar.setRating((float)rating);
                ratingBar.setVisibility(View.VISIBLE);
                Log.e("ListAdapter", "getRating " + result.getPlaceId());
            }else{
                ratingBar.setVisibility(View.GONE);
            }


        }


       private void createRestaurantInFirestore(DetailsResult result, String url){

                String urlPicture = url;
                String restaurantName = result.getName();
                String address = result.getVicinity();
                String uid = result.getId();
                String phoneNumber = result.getFormattedPhoneNumber();
                String website = result.getWebsite();
                String placeId = result.getPlaceId();
                Double lat = result.getGeometry().getLocation().getLat();
                Double lgt = result.getGeometry().getLocation().getLng();

            restaurantRef = db.collection("restaurants").document(result.getId());

            restaurantRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(!documentSnapshot.exists())
                    restaurantViewModel.CreateRestaurant(result.getId(), result.getName(), url, address, phoneNumber, website, placeId, null, lat, lgt);
                }
            });
        }




       //////////////////////////////// OPENIN HOUR DETAIL


      /*  private void getOpeningHoursInfo(DetailsResult result){
            int daysArray[] = {0,1,2,3,4,5,6};

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minOfDay = calendar.get(Calendar.MINUTE);
            if (minOfDay < 10){minOfDay = '0'+minOfDay;}
            String currentHourString = Integer.toString(hourOfDay)+Integer.toString(minOfDay);
            int currentHour = Integer.parseInt(currentHourString);

            if(result.getOpeningHours().getPeriods().size() > 0){
                for (int i=0;i < result.getOpeningHours().getPeriods().size();i++){
                    if (result.getOpeningHours().getPeriods().get(i).getOpen().getDay() == daysArray[day]){
                        String closeHour = result.getOpeningHours().getPeriods().get(i).getClose().getTime();
                        if (currentHour < Integer.parseInt(closeHour) || daysArray[day] < result.getOpeningHours().getPeriods().get(i).getClose().getDay()){
                            int timeDifference = Integer.parseInt(closeHour) - currentHour;
                            //Log.e("TAG", "RestaurantName : " + results.getName() + " | CurrentHour : " + currentHour + " | CloseHour : " + Integer.parseInt(closeHour) + " | TimeDifference : " + timeDifference);
                            if (timeDifference <= 30 && daysArray[day] == result.getOpeningHours().getPeriods().get(i).getClose().getDay()){
                                displayOpeningHour("CLOSING_SOON", closeHour);
                            }else{
                                displayOpeningHour("OPEN",result.getOpeningHours().getPeriods().get(i).getClose().getTime());
                            }
                            break;
                        }
                    }
                }
            }

        }

        @SuppressLint("StringFormatInvalid")
        private void displayOpeningHour(String type, String hour){
            switch (type){
                case "OPEN":
                    this.hoursTv.setText(itemView.getResources().getString(R.string.restaurant_open_until,formatTime(itemView.getContext(),hour)));
                    this.hoursTv.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                    this.hoursTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case "CLOSED":
                    this.hoursTv.setText("closed");
                    this.hoursTv.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
                    this.hoursTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    break;
                case "CLOSING_SOON":
                    this.hoursTv.setText(itemView.getResources().getString(R.string.restaurant_closing_soon,formatTime(itemView.getContext(),hour)));
                    this.hoursTv.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                    this.hoursTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
                case "OPENING_HOURS_NOT_KNOW":
                    this.hoursTv.setText("horaire inconnue");
                    this.hoursTv.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent));
                    this.hoursTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    break;
            }
        }

        private String formatTime(Context context, String date) {
            date = date.substring(0,2) + ":" +date.substring(2);
            try {
                Date date1 = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(date);
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    return dateFormat.format(date1);
                }else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("h.mm a", Locale.getDefault());
                    return dateFormat.format(date1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getTodayDate(){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(c.getTime());
        }*/






        @Nullable
        protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    }
}
