package me.xurround.desklink.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xurround.desklink.models.KnownDevice;

public class MainViewModel extends ViewModel
{
    private final MutableLiveData<List<KnownDevice>> knownDevices;

    public MainViewModel()
    {
        knownDevices = new MutableLiveData<>();

        List<KnownDevice> kds = new ArrayList<>();
        kds.add(new KnownDevice("Computer #1", "FDkn", "192.168.0.7", true, "Main PC"));
        kds.add(new KnownDevice("Computer #2", "sfag", "192.168.1.123", true, "Work PC"));
        kds.add(new KnownDevice("Computer #3", "zvbb", "192.168.23.123", true, "Presentation PC"));
        kds.add(new KnownDevice("Computer #4", "shsj", "192.168.4.71", true, "Idk PC"));
        kds.add(new KnownDevice("Computer #5", "FDkn", "192.168.0.7", true, "Something else PC"));
        kds.add(new KnownDevice("Computer #6", "sfag", "192.168.1.123", true, "Mom's PC"));
        kds.add(new KnownDevice("Computer #7", "zvbb", "192.168.23.123", true, "Father's PC"));
        kds.add(new KnownDevice("Computer #8", "shsj", "192.168.4.71", true, "Brother's PC"));

        knownDevices.setValue(kds);
    }

    public MutableLiveData<List<KnownDevice>> getKnownDevicesMD()
    {
        return knownDevices;
    }
}
