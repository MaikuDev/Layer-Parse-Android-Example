package com.layer.layerparseexample.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.layer.layerparseexample.Layer.LayerImpl;
import com.layer.layerparseexample.Parse.ParseImpl;
import com.layer.layerparseexample.Parse.ParseLoginCallbacks;
import com.layer.layerparseexample.R;
import com.layer.sdk.exceptions.LayerException;
import com.parse.ParseException;
import com.parse.ParseUser;

/*
 * SignupActivity.java
 * Allows a user to register a new account using Parse.
 */

public class SignupActivity extends ActivityBase implements ParseLoginCallbacks{

    //Signup input fields
    private EditText mUsername, mPassword, mEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        //Register the signup button
        Button signUpButton = (Button)findViewById(R.id.signup);
        if(signUpButton != null)
            signUpButton.setOnClickListener(this);

        //Grab the input fields
        mUsername = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mEmail = (EditText)findViewById(R.id.email);
    }

    //Starts the registration process once the user taps the signup button
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.signup:
                Log.d("Activity", "New user is being registered");
                onRegisterUser();
                break;

        }
    }

    //Validates the inputs and registers the user with Parse
    private void onRegisterUser(){

        String usernameString = getTextAsString(mUsername);
        String passwordString = getTextAsString(mPassword);
        String emailString = getTextAsString(mEmail);

        //Make sure the username is valid
        if(usernameString.length() <= 3){

            showAlert("Sign Up Error", "A valid username longer than 3 characters is required to sign up.");
            Log.d("Activity","Cannot create account, username is too short");

        //Make sure the password is valid
        } else if(passwordString.length() <= 3){

            showAlert("Sign Up Error", "A valid password longer than 3 characters is required to sign up.");
            Log.d("Activity", "Cannot create account, password is too short");

        //Make sure the email address is valid
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {

            showAlert("Sign Up Error", "A valid email is required to sign up.");
            Log.d("Activity", "Cannot create account, email is invalid");

        //Register the user with Parse
        } else {

            Log.d("Activity", "Staring registration process for " + usernameString);
            ParseImpl.registerUser(usernameString, passwordString, emailString, this);

        }
    }

    //Callback for when the user is successfully registered. Authenticate the user with Layer
    public void loginSucceeded(ParseUser user) {

        Log.d("Activity", "User is registered with Parse. Starting Layer authentication.");
        LayerImpl.authenticateUser();
    }

    //Parse user registration failed for some reason
    public void loginFailed(ParseException e) {

        showAlert("Parse Error", "Encountered the following error while registering: " + e.toString());
        Log.d("Activity", "Cannot register user with Parse. Exception: " + e.toString());
    }

    //When the user finishes authenticating with Layer, show the Conversation screen
    public void onUserAuthenticated(String userID){

        //Go to the conversation view
        Log.d("Activity", "User authenticated");

        Intent intent = new Intent(SignupActivity.this, ConversationsActivity.class);
        startActivity(intent);
    }

    //Layer authentication failed for some reason
    public void onUserAuthenticatedError(LayerException e){

        showAlert("Layer Error", "Encountered the following error while authenticating: " + e.toString());
        Log.d("Activity", "Cannot authenticate Layer. Exception: " + e.toString());
    }
}
