package me.xurround.desklink.logic.network;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import me.xurround.desklink.interfaces.ConnectCallbackListener;
import me.xurround.desklink.logic.Helpers;

public class NetworkClient
{
    private final String address;
    private final int port;

    private volatile boolean isRunning = false;

    private DatagramSocket socket;

    private Thread workingThread;

    private ConnectCallbackListener connectCallback;

    public NetworkClient(String ipAddress, int port)
    {
        this.address = ipAddress;
        this.port = port;
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

    public void connect(String uid, ConnectCallbackListener connectCallback)
    {
        isRunning = true;
        workingThread.start();
        this.connectCallback = connectCallback;
        byte[] authData = new byte[17];
        authData[0] = 0x07;
        System.arraycopy(Helpers.hexStringToByteArray(uid), 0, authData, 1, 16);
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
        isRunning = false;
        workingThread.interrupt();
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
