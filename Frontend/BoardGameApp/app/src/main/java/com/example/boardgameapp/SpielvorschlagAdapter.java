package com.example.boardgameapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgameapp.Callbacks.SpielvorschlagAbstimmungCallback;
import com.example.boardgameapp.DAO.BoardgameAPI;
import com.example.boardgameapp.DTO.SpielterminDto;
import com.example.boardgameapp.DTO.SpielvorschlagDto;

import java.util.List;

public class SpielvorschlagAdapter extends RecyclerView.Adapter<SpielvorschlagAdapter.ViewHolder> {
    private List<SpielvorschlagDto> spielvorschlaegeListe;
    private BoardgameAPI boardgameAPI;
    private SpielterminDto spieltermin;
    private SpielvorschlagAbstimmungCallback abstimmungCallback;

    public SpielvorschlagAdapter(List<SpielvorschlagDto> spielvorschlaegeListe, BoardgameAPI boardgameAPI, SpielterminDto spieltermin, SpielvorschlagAbstimmungCallback callback) {
        this.spielvorschlaegeListe = spielvorschlaegeListe;
        this.boardgameAPI = boardgameAPI;
        this.spieltermin = spieltermin;
        this.abstimmungCallback = callback;
    }
        public SpielvorschlagAdapter(List<SpielvorschlagDto> spielvorschlaegeListe) {
            this.spielvorschlaegeListe = spielvorschlaegeListe;
        }
        public void setBoardgameAPI(BoardgameAPI boardgameAPI) {
            this.boardgameAPI = boardgameAPI;
        }

        public void setSpieltermin(SpielterminDto spieltermin) {
            this.spieltermin = spieltermin;
        }

        @NonNull
        @Override
        public SpielvorschlagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spielvorschlag, parent, false);
            return new ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull SpielvorschlagAdapter.ViewHolder holder, int position) {
        SpielvorschlagDto spielvorschlag = spielvorschlaegeListe.get(position);
        holder.tvSpielvorschlagBeschreibung.setText(spielvorschlag.getspielvorschlagName());

        holder.btnJa.setOnClickListener(v -> {
            boardgameAPI.CreateSpielabstimmung(spieltermin.getSpielgruppeId(), spielvorschlag.getId(), true, new SpielvorschlagAbstimmungCallback() {
                @Override
                public void onSuccess(String response) {
                    abstimmungCallback.onSuccess(response);
                }

                @Override
                public void onFailure(Exception e) {
                    abstimmungCallback.onFailure(e);
                }
            });
        });

        holder.btnNein.setOnClickListener(v -> {
            boardgameAPI.CreateSpielabstimmung(spieltermin.getSpielgruppeId(), spielvorschlag.getId(), false, new SpielvorschlagAbstimmungCallback() {
                @Override
                public void onSuccess(String response) {
                    abstimmungCallback.onSuccess(response);
                }

                @Override
                public void onFailure(Exception e) {
                    abstimmungCallback.onFailure(e);
                }
            });
        });
    }
    public void updateSpielvorschlaege(List<SpielvorschlagDto> neueSpielvorschlaege) {
        this.spielvorschlaegeListe.clear();
        this.spielvorschlaegeListe.addAll(neueSpielvorschlaege);
        notifyDataSetChanged();
    }
        @Override
        public int getItemCount() {
            return spielvorschlaegeListe.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvSpielvorschlagBeschreibung;
            public Button btnJa, btnNein;

            public ViewHolder(View view) {
                super(view);
                tvSpielvorschlagBeschreibung = view.findViewById(R.id.tvSpielvorschlagBeschreibung);
                btnJa = view.findViewById(R.id.btnJa);
                btnNein = view.findViewById(R.id.btnNein);
            }
        }

}
