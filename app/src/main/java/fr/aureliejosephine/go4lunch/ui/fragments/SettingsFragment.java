package fr.aureliejosephine.go4lunch.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.api.UserHelper;

public class SettingsFragment extends BaseFragment {

    private TextInputEditText name;
    private TextInputEditText email;
    private TextView saveParams;
    private TextView deleteAccount;
    @Nullable
    private ImageView workmatePic;
    private static final int RC_SIGN_IN = 123;




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
        deleteAccount = getView().findViewById(R.id.deleteAccountTv);

        updateUserSettings();
        updateUserParams();
        deleteUserParams();

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

            //Get email & username from Firebase
            String uEmail = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

            //Update views with data
            name.setText(username);
            email.setText(uEmail);
        }
    }

    private void updateUserParams(){

        saveParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = name.getText().toString();
                String emailUser = email.getText().toString();

                UserHelper.updateUsernameAndEmail(username,emailUser, getCurrentUser().getUid()).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void updateTask) {
                                Log.e("SETTINGS_ACTIVITY", "saveSettings: DONE");
                                Toast.makeText(getContext(), "update ok", Toast.LENGTH_SHORT).show();
                            }
                        });;
            }
        });

    }

    private void deleteUserParams(){

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserHelper.deleteUser(getCurrentUser().getUid()).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void updateTask) {
                                Log.e("SETTINGS_ACTIVITY", "deleteAccount: DONE");
                                Toast.makeText(getContext(), "delete ok", Toast.LENGTH_SHORT).show();
                            }
                        });;

                redirectAfterSignOut();
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
