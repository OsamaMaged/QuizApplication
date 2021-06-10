package com.example.quiz.viewmodel;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.quiz.views.MainActivity;
import com.example.quiz.views.adminActivity;
import com.example.quiz.views.userActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.example.quiz.model.AppRepository;

import static android.content.ContentValues.TAG;

public class authViewModel extends AndroidViewModel {
    private  AppRepository  appRepository;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
   // FirebaseAuth fAuth;
    public authViewModel( @NonNull Application application) {
        super(application);
     //   fAuth=FirebaseAuth.getInstance();
        appRepository  =new AppRepository(application);
    userMutableLiveData =appRepository.getUserMutableLiveData();

    }

    public void login( String e, String p,String UI)
    {appRepository.login(e, p,UI);}

    public int isSuccessful() {
        int successful;
        Log.d(TAG, "Calling isSuccessful");
        successful =appRepository.isSuccess();
        Log.d(TAG, "isSuccessful :"+successful);
        return successful;
    }
public void Admin_user()
{

}
    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
