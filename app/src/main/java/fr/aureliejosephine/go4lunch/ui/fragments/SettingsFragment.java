package fr.aureliejosephine.go4lunch.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.api.UserHelper;

public class SettingsFragment extends Fragment {

    private TextInputEditText name;
    private TextInputEditText email;
    private TextView saveParams;
    @Nullable
    private ImageView workmatePic;


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

        configureUserSettings();
        updateUserParams();

    }


    private void configureUserSettings(){

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
                Toast.makeText(getContext(), "I just clicked my textview!", Toast.LENGTH_LONG).show();

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

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }


}
