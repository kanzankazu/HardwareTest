package com.kanzankazu.hardwaretest.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.database.room.table.Hardware;
import com.kanzankazu.hardwaretest.ui.adapter.CheckHookInteface;
import com.kanzankazu.hardwaretest.util.DeviceDetailUtil;

import java.util.List;

public class MainResultAdapter extends RecyclerView.Adapter<MainResultAdapter.ViewHolder> {
    private final Context context;
    private CheckHookInteface parent;
    private List<Hardware> models;
    private int currentPosition;
    private int state;

    public MainResultAdapter(Context context, Activity parent, List<Hardware> models) {
        this.models = models;
        this.context = context;
        try {
            this.parent = (CheckHookInteface) parent;
        } catch (Exception e) {
            this.parent = null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivrvHardwareStatusfvbi;
        private final TextView tvrvHardwareNamefvbi;
        private final TextView tvrvHardwareDescfvbi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivrvHardwareStatusfvbi = (ImageView) itemView.findViewById(R.id.ivrvHardwareStatus);
            tvrvHardwareNamefvbi = (TextView) itemView.findViewById(R.id.tvrvHardwareName);
            tvrvHardwareDescfvbi = (TextView) itemView.findViewById(R.id.tvrvHardwareDesc);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_result1, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hardware model = models.get(position);
        holder.tvrvHardwareNamefvbi.setText(model.getNameHardware());
        currentPosition = position;

        if (model.getStatusHardware().equals(MainActivity.STATUS_DEVICE_ADA)) {
            holder.tvrvHardwareDescfvbi.setText(model.getDescHardware());
            holder.tvrvHardwareDescfvbi.setVisibility(View.VISIBLE);
            if (DeviceDetailUtil.isKitkatBelow()) {
                holder.ivrvHardwareStatusfvbi.setImageResource(R.drawable.ic_check);
            } else {
                holder.ivrvHardwareStatusfvbi.setImageDrawable(context.getDrawable(R.drawable.ic_check));
            }
        } else if (model.getStatusHardware().equals(MainActivity.STATUS_DEVICE_TIDAK_ADA)) {
            holder.tvrvHardwareDescfvbi.setVisibility(View.VISIBLE);
            if (DeviceDetailUtil.isKitkatBelow()) {
                holder.ivrvHardwareStatusfvbi.setImageResource(R.drawable.ic_error);
            } else {
                holder.ivrvHardwareStatusfvbi.setImageDrawable(context.getDrawable(R.drawable.ic_error));
            }
            holder.tvrvHardwareDescfvbi.setVisibility(View.INVISIBLE);
        } else {
            if (DeviceDetailUtil.isKitkatBelow()) {
                holder.ivrvHardwareStatusfvbi.setImageResource(R.drawable.ic_check);
            } else {
                holder.ivrvHardwareStatusfvbi.setImageDrawable(context.getDrawable(R.drawable.ic_check));
            }
            holder.tvrvHardwareDescfvbi.setText(model.getStatusHardware() + "\n" + model.getDescHardware());
        }
    }

    /*public void updateModel(int idHardware, @Nullable String content, String status) {
        Hardware model = models.get(idHardware);
        if (content != null) {
            model.setContent(content);
        }
        model.setStatus(status);
    }*/

    public void updateModelAt(int id, String status) {
        Hardware model = null;
        int counter = 0;
        for (Hardware jink : models) {
            counter++;
            if (jink.getIdHardware() == id) {
                model = jink;
                break;
            }
        }
        if (model != null) {
            model.setStatusHardware(status);
        }
    }

    public void addModel(Hardware model) {
        models.add(model);
    }

    public int getPosition() {
        return currentPosition;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
