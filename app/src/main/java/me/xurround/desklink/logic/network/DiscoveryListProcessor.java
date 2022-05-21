package me.xurround.desklink.logic.network;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.xurround.desklink.models.Device;

public class DiscoveryListProcessor
{
    private final HashMap<Device, Long> devices;

    private ScheduledExecutorService checkService;

    public DiscoveryListProcessor(UpdateDiscoveredDevicesListener uddListener)
    {
        devices = new HashMap<>();
        checkService = Executors.newSingleThreadScheduledExecutor();
        checkService.scheduleAtFixedRate(() ->
        {
            for (Device dev : devices.keySet())
            {
                if (System.currentTimeMillis() - devices.get(dev) > 4000)
                    devices.remove(dev);
                if (uddListener != null)
                    uddListener.update(devices.keySet());
            }

        }, 1, 1, TimeUnit.SECONDS);
    }

    public void handleNewDevice(Device device)
    {
        devices.put(device, System.currentTimeMillis());
    }

    public interface UpdateDiscoveredDevicesListener
    {
        void update(Set<Device> devices);
    }

    public void dispose()
    {
        checkService.shutdownNow();
    }
}
