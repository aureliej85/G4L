package fr.aureliejosephine.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AuthActivity extends AppCompatActivity{



    //FOR DESIGN
    private Button button;

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_auth);
        checkIfUserIsLogged();
        emailAuth();

    }



    // --------------------
    // ACTIONS
    // --------------------

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

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i("AuthActivity", "onActivityResult: RC sign in");

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
                Log.i("AuthActivity", "onActivityResult: result OK");
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if (response == null) {
                    Log.e("Login","Login canceled by User");
                    return;
                }
                /*if (response.getError() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login","Unknown Error");
                    return;
                }*/
            }
        }
    }





    // --------------------
    // NAVIGATION
    // --------------------


    private void checkIfUserIsLogged(){

        if(getCurrentUser() != null){
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
            finish();
        }
    }



    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    //protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }


}
