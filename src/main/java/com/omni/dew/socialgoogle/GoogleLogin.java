package com.omni.dew.socialgoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.gson.Gson;
import com.omni.dew.basemodule.BaseLogin;
import com.omni.dew.basemodule.LoginResponse;

public class GoogleLogin implements BaseLogin {

    private GoogleSignInClient googleApiClient;
    private GoogleSignInOptions gso;
    Activity activity;
    int RC_SIGN_IN = 20598;
    LoginResponse loginResponse;

    @Override
    public void init( Activity activity, LoginResponse loginResponse){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = GoogleSignIn.getClient(activity, gso);
        this.activity = activity;
        this.loginResponse = loginResponse;
    }

    @Override
    public int login(){
        Intent signInIntent = googleApiClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        return RC_SIGN_IN;
    }

    @Override
    public void onResponse(Intent data) {
        try {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException.class);
            loginResponse.sendResponse(new Gson().toJson(account));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLogin() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        return  (acct != null);
    }
}
