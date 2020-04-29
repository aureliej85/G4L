package fr.aureliejosephine.go4lunch.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.viewmodel.UserViewModel;

public class SettingsFragment extends BaseFragment {

    private TextInputEditText name;
    private TextInputEditText email;
    private TextView saveParams;
    private TextView deleteAccount;
    @Nullable
    private ImageView workmatePic;
    private static final int RC_SIGN_IN = 123;
    private User user;
    private UserViewModel userViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());




        //FOR DATA
        // 2 - Identify each Http Request
        private static final int SIGN_OUT_TASK = 10;
        private static final int DELETE_USER_TASK = 20;


            // --------------------
            // ACTIONS
            // --------------------



        // --------------------
        // UI
        // --------------------



        // 3 - Create OnCompleteListener called after tasks ended




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getView().findViewById(R.id.nameEt);
        email = getView().findViewById(R.id.emailEt);
        workmatePic = getView().findViewById(R.id.workmateIv);
        saveParams = getView().findViewById(R.id.saveParamsTv);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        updateUserSettings();
        updateUserParams();

    }


    private void updateUserSettings(){

        if (this.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(workmatePic);
            }

            //Get email & username from Firestore
            /*userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);

                    String uName = user.getUsername();
                    String uEmail = user.getEmail();

                    name.setText(uName);
                    email.setText(uEmail);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("SettingsFragment", "onFailure: " + e.toString());
                }
            });*/

            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w("SettingsFragment", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        //Log.d(TAG, "Current data: " + snapshot.getData());
                        user = documentSnapshot.toObject(User.class);

                        String uName = user.getUsername();
                        String uEmail = user.getEmail();

                        name.setText(uName);
                        email.setText(uEmail);
                    } else {
                        Log.d("SettingsFragment", "Current data: null");
                    }


                }
            });


        }
    }

    private void updateUserParams(){

        saveParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = name.getText().toString();
                String emailUser = email.getText().toString();

                userViewModel.UpdateUser(username, emailUser, getCurrentUser().getUid());
                /*UserRepository.updateUsernameAndEmail(username,emailUser, getCurrentUser().getUid()).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void updateTask) {
                                Log.e("SETTINGS_ACTIVITY", "saveSettings: DONE");
                                Toast.makeText(getContext(), "update ok", Toast.LENGTH_SHORT).show();
                            }
                        });;*/
            }
        });

    }




    private void redirectAfterSignOut(){
        /*Intent in = new Intent(this, AuthActivity.class);
        startActivity(in);
        finish();*/
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.FacebookBuilder().build(), //GOOGLE
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build())) //EMAIL)) // FACEBOOK)) // SUPPORT GOOGLE))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo_g4l)
                        .build(),
                RC_SIGN_IN);
        Log.i("AuthActivity", "email Auth");
    }







}
