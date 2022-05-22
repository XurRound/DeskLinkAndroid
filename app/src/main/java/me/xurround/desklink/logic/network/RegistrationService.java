package me.xurround.desklink.logic.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import me.xurround.desklink.interfaces.RegisterCallback;
import me.xurround.desklink.models.Device;

public class RegistrationService
{
    private DatagramSocket socket;

    private volatile boolean isRunning;

    public RegistrationService(int port, RegisterCallback registerCallback)
    {
        isRunning = true;
        try
        {
            socket = new DatagramSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        new Thread(() ->
        {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (isRunning)
            {
                try
                {
                    socket.receive(packet);
                    if (buffer[0] == 0x77)
                    {
                        if (buffer[1] == (byte)0x01)
                            registerCallback.onSuccess();
                        else
                            registerCallback.onFailure();
                    }
                    isRunning = false;
                    socket.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void tryRegister(Device device, String tdName, String tdUID)
    {
        byte[] tdNameBytes = tdName.getBytes(StandardCharsets.UTF_8);
        byte[] tdUIDBytes = tdUID.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[tdNameBytes.length + tdUIDBytes.length + 3];
        data[0] = (byte)0xF7;
        data[1] = (byte)tdNameBytes.length;
        data[2] = (byte)tdUIDBytes.length;
        System.arraycopy(tdNameBytes, 0, data, 3, tdNameBytes.length);
        System.arraycopy(tdUIDBytes, 0, data, tdNameBytes.length + 3, tdUIDBytes.length);
        new Thread(() ->
        {
            try
            {
                socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(device.getIpAddress()), 15500));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }
}
