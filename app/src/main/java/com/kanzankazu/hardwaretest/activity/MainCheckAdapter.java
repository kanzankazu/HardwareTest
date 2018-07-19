package com.kanzankazu.hardwaretest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.model.ui.CheckModel;
import com.kanzankazu.hardwaretest.util.DeviceDetailUtil;

import java.util.ArrayList;
import java.util.List;

class MainCheckAdapter extends RecyclerView.Adapter<MainCheckAdapter.ViewHolder> {
    private final Context context;
    private CheckHookInteface parent;
    private List<CheckModel> models;
    private int currentPosition;
    private int state;

    public MainCheckAdapter(Context context, Activity parent, List<CheckModel> models) {
        this.models = models;
        this.context = context;
        try {
            this.parent = (CheckHookInteface) parent;
        } catch (Exception e) {
            this.parent = null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvrvCheckfvbi;
        private final ProgressBar pbrvCheckfvbi;
        private final ImageView ivrvCheckfvbi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvrvCheckfvbi = (TextView) itemView.findViewById(R.id.tvrvCheck);
            pbrvCheckfvbi = (ProgressBar) itemView.findViewById(R.id.pbrvCheck);
            ivrvCheckfvbi = (ImageView) itemView.findViewById(R.id.ivrvCheck);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_checkstatus, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckModel model = models.get(position);
        holder.tvrvCheckfvbi.setText(model.getModul());
        currentPosition = position;

        if (model.getStatus() == CheckModel.UNCHECKING) {
            holder.ivrvCheckfvbi.setVisibility(View.GONE);
            holder.pbrvCheckfvbi.setVisibility(View.INVISIBLE);
        } else if (model.getStatus() == CheckModel.CHECKING) {
            holder.ivrvCheckfvbi.setVisibility(View.GONE);
            holder.pbrvCheckfvbi.setVisibility(View.VISIBLE);
        } else if (model.getStatus() == CheckModel.CHECK_DONE) {
            holder.ivrvCheckfvbi.setVisibility(View.VISIBLE);
            if (DeviceDetailUtil.isKitkatBelow()) {
                holder.ivrvCheckfvbi.setImageResource(R.drawable.ic_check);
            } else {
                holder.ivrvCheckfvbi.setImageDrawable(context.getDrawable(R.drawable.ic_check));
            }
            holder.pbrvCheckfvbi.setVisibility(View.GONE);
        } else if (model.getStatus() == CheckModel.CHECK_ERROR) {
            holder.ivrvCheckfvbi.setVisibility(View.VISIBLE);
            if (DeviceDetailUtil.isKitkatBelow()) {
                holder.ivrvCheckfvbi.setImageResource(R.drawable.ic_error);
            } else {
                holder.ivrvCheckfvbi.setImageDrawable(context.getDrawable(R.drawable.ic_error));
            }
            holder.pbrvCheckfvbi.setVisibility(View.GONE);
        }
    }

    /*public void updateModel(int id, @Nullable String content, String status) {
        CheckModel model = models.get(id);
        if (content != null) {
            model.setContent(content);
        }
        model.setStatus(status);
    }*/

    public void updateModelAt(int id, int status) {
        CheckModel model = null;
        int counter = 0;
        for (CheckModel jink : models) {
            counter++;
            if (jink.getId() == id) {
                model = jink;
                break;
            }
        }
        if (model != null) {
            model.setStatus(status);
        }
    }

    public void addModel(CheckModel model) {
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
