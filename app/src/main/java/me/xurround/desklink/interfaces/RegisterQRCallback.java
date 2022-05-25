package me.xurround.desklink.interfaces;

import me.xurround.desklink.models.Device;

public interface RegisterQRCallback
{
    void onSuccess(Device device);
    void onFailure();
}
