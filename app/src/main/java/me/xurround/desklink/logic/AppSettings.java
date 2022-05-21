package me.xurround.desklink.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import me.xurround.desklink.models.KnownDevice;

public class AppSettings
{
    private static AppSettings instance;

    private static final String APP_SETTINGS = "DLC_SETTINGS";

    private final SharedPreferences sharedPreferences;

    private final Gson gson;

    private AppSettings(Context context)
    {
        sharedPreferences = context.getSharedPreferences(AppSettings.APP_SETTINGS, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveKnownDevices(List<KnownDevice> devices)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KNOWN_DEVICES", gson.toJson(devices));
        editor.apply();
    }

    public List<KnownDevice> loadKnownDevices()
    {
        String jKnownDevices = sharedPreferences.getString("KNOWN_DEVICES", "[]");
        return gson.fromJson(jKnownDevices, new TypeToken<List<KnownDevice>>(){}.getType());
    }

    public static AppSettings getInstance(Context context)
    {
        if (instance == null)
            instance = new AppSettings(context);
        return instance;
    }
}
