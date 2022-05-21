package me.xurround.desklink.models;

public class KnownDevice extends Device
{
    private String description = "";

    public KnownDevice(String id, String name, String lastSeenIP)
    {
        super(id, name, lastSeenIP);
    }

    public KnownDevice(Device device, String description)
    {
        this(device.getId(), device.getName(), device.getIpAddress());
        setDescription(description);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
