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
                            registerCallback.onSuccess(new String(buffer, 2, packet.getLength() - 2, StandardCharsets.UTF_8));
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
        byte[] devIp = device.getIpAddress().getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[tdNameBytes.length + tdUIDBytes.length + devIp.length + 4];
        data[0] = (byte)0xF7;
        data[1] = (byte)tdNameBytes.length;
        data[2] = (byte)tdUIDBytes.length;
        data[3] = (byte)devIp.length;
        System.arraycopy(tdNameBytes, 0, data, 4, tdNameBytes.length);
        System.arraycopy(tdUIDBytes, 0, data, tdNameBytes.length + 4, tdUIDBytes.length);
        System.arraycopy(devIp, 0, data, tdNameBytes.length + tdUIDBytes.length + 4, devIp.length);
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
