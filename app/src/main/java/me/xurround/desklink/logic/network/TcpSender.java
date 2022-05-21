package me.xurround.desklink.logic.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpSender
{
    private final String address;

    private OnMessageReceived onMessageReceived;

    private BufferedReader reader;
    private DataOutputStream writer;

    private volatile boolean isRunning = false;

    private final Thread workingThread;

    public TcpSender(String ipAddress, int port)
    {
        this.address = ipAddress;
        workingThread = new Thread(() ->
        {
            isRunning = true;
            try
            {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(InetAddress.getByName(address), port));
                writer = new DataOutputStream(socket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (isRunning)
                {
                    String data = reader.readLine();
                    if (data != null && onMessageReceived != null)
                        onMessageReceived.messageReceived(data);
                }
            }
            catch (Exception e)
            {
                isRunning = false;
                e.printStackTrace();
            }
        });
    }

    public void setOnMessageReceived(OnMessageReceived onMessageReceived)
    {
        this.onMessageReceived = onMessageReceived;
    }

    public void connect()
    {
        workingThread.start();
    }

    public void disconnect()
    {
        isRunning = false;
        if (writer != null)
        {
            try
            {
                writer.flush();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void send(byte[] data)
    {
        if (!isRunning)
            return;
        new Thread(() ->
        {
            try
            {
                writer.write(data);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public interface OnMessageReceived
    {
        void messageReceived(String message);
    }
}
