package com.example.coffeegroup.Services;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeegroup.R;

import java.util.LinkedList;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeHolder> {

    private LinkedList<UserFormat> list;
    private Context context;

    public CoffeeAdapter(LinkedList<UserFormat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CoffeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coffee,parent,false);
        return new CoffeeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeHolder holder, int position) {
        UserFormat coffee = list.get(position);
        holder.tvName.setText(coffee.getName());
        holder.tvBrew.setText(coffee.getBrew_level()+" /10");
        holder.tvSugar.setText(coffee.getSugar()+" spoon(s)");
        if(coffee.getWant()==1)
            holder.cl.setBackgroundColor(Color.parseColor("#32CD32"));
        else
            holder.cl.setBackgroundColor(Color.RED);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CoffeeHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cl;
        TextView tvName, tvSugar, tvBrew;
        public CoffeeHolder(@NonNull View itemView)
        {
            super(itemView);
            cl = itemView.findViewById(R.id.relative);
            tvName = itemView.findViewById(R.id.tvName);
            tvSugar = itemView.findViewById(R.id.tvSugar);
            tvBrew = itemView.findViewById(R.id.tvBrewing);
        }
    }
}
