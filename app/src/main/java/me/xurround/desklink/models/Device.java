package me.xurround.desklink.models;

import java.util.Objects;

public class Device
{
    private final String id;
    private final String name;
    private final String ipAddress;

    public Device(String id, String name, String ipAddress)
    {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getName()
    {
        return name;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public String getId()
    {
        return id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return  Objects.equals(id, device.id) && Objects.equals(name, device.name) && Objects.equals(ipAddress, device.ipAddress);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, ipAddress);
    }
}
