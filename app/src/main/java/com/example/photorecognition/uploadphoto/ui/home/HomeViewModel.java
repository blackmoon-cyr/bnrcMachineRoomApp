package com.example.photorecognition.uploadphoto.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("请不要让图像中的设备倾斜");
    }

    public LiveData<String> getText() {
        return mText;
    }
}