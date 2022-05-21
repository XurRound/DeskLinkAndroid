package me.xurround.desklink.logic.network;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import me.xurround.desklink.interfaces.DeviceDiscoveredListener;
import me.xurround.desklink.models.Device;

public class ServiceDiscovery
{
    private DatagramSocket broadcastSocket;
    private DatagramSocket listenerSocket;

    private static final int PORT = 15507;

    private volatile boolean discoverActive = false;

    private Thread senderThread;
    private Thread receiverThread;

    private final DeviceDiscoveredListener deviceDiscoveredListener;

    public ServiceDiscovery(DeviceDiscoveredListener deviceDiscoveredListener)
    {
        this.deviceDiscoveredListener = deviceDiscoveredListener;
        try
        {
            broadcastSocket = new DatagramSocket();
            broadcastSocket.setBroadcast(true);
            listenerSocket = new DatagramSocket(PORT + 1);
            listenerSocket.setSoTimeout(3000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        discoverActive = true;
        receiverThread = new Thread(() ->
        {
            while (discoverActive)
            {
                byte[] buffer = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                try
                {
                    listenerSocket.receive(incomingPacket);
                    String text = new String(buffer, 0, incomingPacket.getLength());
                    String[] data = text.split("\\|");
                    if (data.length == 4 && Objects.equals(data[0], "DLS"))
                    {
                        if (deviceDiscoveredListener != null)
                            deviceDiscoveredListener.onDeviceDiscovered(new Device(data[1], data[3], data[2]));
                    }
                }
                catch (SocketTimeoutException ignored)
                {

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        receiverThread.start();
        senderThread = new Thread(() ->
        {
            while (discoverActive)
            {
                sendBroadcast("DeskLink|1.0");
                try
                {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        senderThread.start();
    }

    public void stop()
    {
        discoverActive = false;
        senderThread.interrupt();
        receiverThread.interrupt();
    }

    private List<InetAddress> getBroadcastAddresses()
    {
        List<InetAddress> result = new ArrayList<>();
        try
        {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements())
            {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback())
                    continue;
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
                {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null)
                        continue;
                    result.add(broadcast);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private void sendBroadcast(String messageStr)
    {
        try
        {
            byte[] sendData = messageStr.getBytes();
            for (InetAddress address : getBroadcastAddresses())
            {
                broadcastSocket.send(new DatagramPacket(sendData, sendData.length, new InetSocketAddress(address, PORT)));
                Log.d("SENDER", "Sending to: " + address.getHostName());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
