package me.xurround.desklink.logic.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import me.xurround.desklink.interfaces.ConnectCallback;
import me.xurround.desklink.logic.Helpers;

public class NetworkClient
{
    private final String address;
    private final int port;

    private volatile boolean isRunning = false;

    private DatagramSocket socket;

    private final Thread workingThread;

    private ConnectCallback connectCallback;

    private final String deviceUID;

    public NetworkClient(String ipAddress, int port, String uid)
    {
        this.address = ipAddress;
        this.port = port;
        this.deviceUID = uid;
        try
        {
            socket = new DatagramSocket();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        workingThread = new Thread(() ->
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
                        if (buffer[1] == (byte)0xFF)
                            connectCallback.onSuccess();
                        else
                            connectCallback.onFailure();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void connect(ConnectCallback connectCallback)
    {
        isRunning = true;
        workingThread.start();
        this.connectCallback = connectCallback;
        byte[] authData = new byte[17];
        authData[0] = 0x07;
        System.arraycopy(Helpers.hexStringToByteArray(deviceUID), 0, authData, 1, 16);
        new Thread(() ->
        {
            try
            {
                Thread.sleep(500);
                send(authData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public void disconnect()
    {
        byte[] quitData = new byte[17];
        quitData[0] = 0x5A;
        System.arraycopy(Helpers.hexStringToByteArray(deviceUID), 0, quitData, 1, 16);
        new Thread(() ->
        {
            try
            {
                DatagramSocket qSock = new DatagramSocket();
                qSock.send(new DatagramPacket(quitData, quitData.length, InetAddress.getByName(address), port));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
        isRunning = false;
        workingThread.interrupt();
        if (!socket.isClosed())
            socket.close();
    }

    public void send(byte[] data)
    {
        if (!isRunning)
            return;
        new Thread(() ->
        {
            try
            {
                socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(address), port));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }
}
