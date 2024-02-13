package com.example.boardgameapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.boardgameapp.DTO.Essensrichtung;

import java.util.List;

public class EssensrichtungAdapter extends ArrayAdapter<Essensrichtung> {
    private Context context;
    private List<Essensrichtung> essensrichtungen;

    public EssensrichtungAdapter(@NonNull Context context, int resource, @NonNull List<Essensrichtung> objects) {
        super(context, resource, objects);
        this.context = context;
        this.essensrichtungen = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView text = convertView.findViewById(android.R.id.text1);
        text.setText(essensrichtungen.get(position).getArt());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView text = convertView.findViewById(android.R.id.text1);
        text.setText(essensrichtungen.get(position).getArt());
        return convertView;
    }
}

