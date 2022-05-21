package me.xurround.desklink.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.logic.AppSettings;
import me.xurround.desklink.models.KnownDevice;

public class MainViewModel extends AndroidViewModel
{
    private final MutableLiveData<List<KnownDevice>> knownDevices;

    public MainViewModel(Application application)
    {
        super(application);

        knownDevices = new MutableLiveData<>();
        knownDevices.setValue(AppSettings.getInstance(getApplication().getApplicationContext()).loadKnownDevices());
    }

    public void setKnownDevices(List<KnownDevice> devices)
    {
        knownDevices.postValue(devices);
    }

    public MutableLiveData<List<KnownDevice>> getKnownDevicesMD()
    {
        return knownDevices;
    }
}
