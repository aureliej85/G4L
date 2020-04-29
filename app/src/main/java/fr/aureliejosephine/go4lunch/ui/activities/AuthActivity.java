package fr.aureliejosephine.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fr.aureliejosephine.go4lunch.R;


public class AuthActivity extends BaseActivity {


    private static final int RC_SIGN_IN = 123;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!this.isCurrentUserLogged()){
            this.emailAuth();
        }else {
            launchMainActivity();
        }

    }


    public void emailAuth() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }


    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {

            Log.i("AuthActivity", "onActivityResult: RC sign in");

            if (resultCode == RESULT_OK) {

                Log.i("AuthActivity", "onActivityResult: result OK");
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();

            } else {

                if (response == null) {
                    Log.e("Login","Login canceled by User");
                    return;
                }
            }
        }
    }


    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
