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
import me.xurround.desklink.interfaces.AddDeviceListener;
import me.xurround.desklink.models.Device;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>
{
    private final List<Device> devices;

    private final AddDeviceListener addDeviceListener;

    public DeviceListAdapter(List<Device> devices, AddDeviceListener addDeviceListener)
    {
        this.devices = devices;
        this.addDeviceListener = addDeviceListener;
    }

    @NonNull
    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_entry, parent, false);
        return new DeviceListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListAdapter.ViewHolder holder, int position)
    {
        Device device = devices.get(position);
        holder.nameTV.setText(device.getName());
        holder.ipTV.setText(device.getIpAddress());
        holder.addBtn.setOnClickListener(v ->
        {
            if (addDeviceListener != null)
                addDeviceListener.add(device);
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
        private final Button addBtn;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nameTV = itemView.findViewById(R.id.d_device_name);
            ipTV = itemView.findViewById(R.id.d_device_ip);
            addBtn = itemView.findViewById(R.id.d_connect_btn);
        }
    }
}
