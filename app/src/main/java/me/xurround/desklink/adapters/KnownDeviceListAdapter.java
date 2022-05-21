package me.xurround.desklink.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.xurround.desklink.R;
import me.xurround.desklink.interfaces.ConnectClickListener;
import me.xurround.desklink.models.KnownDevice;

public class KnownDeviceListAdapter extends RecyclerView.Adapter<KnownDeviceListAdapter.ViewHolder>
{
    private final List<KnownDevice> devices;

    private final ConnectClickListener connectClickListener;

    public KnownDeviceListAdapter(List<KnownDevice> devices, ConnectClickListener connectClickListener)
    {
        this.devices = devices;
        this.connectClickListener = connectClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.known_device_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        KnownDevice device = devices.get(position);
        holder.nameTV.setText(device.getName());
        holder.ipTV.setText(device.getIpAddress());
        holder.descTV.setText(device.getDescription());
        holder.connectBtn.setOnClickListener(v ->
        {
            if (connectClickListener != null)
                connectClickListener.connect(device);
        });
    }

    @Override
    public int getItemCount()
    {
        return devices.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView nameTV;
        private final TextView ipTV;
        private final TextView descTV;
        private final Button connectBtn;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameTV = itemView.findViewById(R.id.kd_device_name);
            ipTV = itemView.findViewById(R.id.kd_device_ip);
            descTV = itemView.findViewById(R.id.kd_device_desc);
            connectBtn = itemView.findViewById(R.id.kd_connect_btn);
        }
    }
}
