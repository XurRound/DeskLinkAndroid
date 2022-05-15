package me.xurround.desklink.models;

public class Device
{
    private final String name;
    private final String btName;
    private final String lastSeenIP;
    private final boolean available;

    public Device(String name, String btName, String lastSeenIP, boolean available)
    {
        this.name = name;
        this.btName = btName;
        this.lastSeenIP = lastSeenIP;
        this.available = available;
    }

    public String getName()
    {
        return name;
    }

    public String getBtName()
    {
        return btName;
    }

    public String getLastSeenIP()
    {
        return lastSeenIP;
    }

    public boolean isAvailable()
    {
        return available;
    }
}
