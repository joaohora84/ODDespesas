package com.odsoftware.oddespesas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odsoftware.oddespesas.R;
import com.odsoftware.oddespesas.model.Receita;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder> {

    private List<Receita> receitasList;
    private NumberFormat formatoMoeda;

    public ReceitaAdapter(List<Receita> receitasList) {
        this.receitasList = receitasList;
        this.formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        this.formatoMoeda.setCurrency(Currency.getInstance("BRL"));
    }

    @NonNull
    @Override
    public ReceitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receita, parent, false);
        return new ReceitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        Receita receita = receitasList.get(position);

        holder.txtDescricao.setText(receita.getDescricao());
        holder.txtValor.setText(formatoMoeda.format(receita.getValor()));
        holder.txtData.setText(receita.getData());
        holder.txtCategoria.setText(receita.getCategoria());
    }

    @Override
    public int getItemCount() {
        return receitasList.size();
    }

    static class ReceitaViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao;
        TextView txtValor;
        TextView txtData;
        TextView txtCategoria;

        public ReceitaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_receita_descricao);
            txtValor = itemView.findViewById(R.id.txt_receita_valor);
            txtData = itemView.findViewById(R.id.txt_receita_data);
            txtCategoria = itemView.findViewById(R.id.txt_receita_categoria);
        }
    }
   
}
