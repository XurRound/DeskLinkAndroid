package me.xurround.desklink.models;

public class KnownDevice extends Device
{
    private String description = "";

    public KnownDevice(String name, String btName, String lastSeenIP, boolean available)
    {
        super(name, btName, lastSeenIP, available);
    }

    public KnownDevice(String name, String btName, String lastSeenIP, boolean available, String description)
    {
        this(name, btName, lastSeenIP, available);
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
