package com.odsoftware.oddespesas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odsoftware.oddespesas.R;
import com.odsoftware.oddespesas.model.Despesa;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class DespesaAdapter extends RecyclerView.Adapter<DespesaAdapter.DespesaViewHolder> {
    private List<Despesa> despesasList;
    private NumberFormat formatoMoeda;

    public DespesaAdapter(List<Despesa> despesasList) {
        this.despesasList = despesasList;
        this.formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        this.formatoMoeda.setCurrency(Currency.getInstance("BRL"));
    }

    @NonNull
    @Override
    public DespesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_despesa, parent, false);
        return new DespesaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DespesaViewHolder holder, int position) {
        Despesa despesa = despesasList.get(position);

        holder.txtDescricao.setText(despesa.getDescricao());
        holder.txtValor.setText(formatoMoeda.format(despesa.getValor()));
        holder.txtData.setText(despesa.getData());
        holder.txtCategoria.setText(despesa.getCategoria());
    }

    @Override
    public int getItemCount() {
        return despesasList.size();
    }

    static class DespesaViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao;
        TextView txtValor;
        TextView txtData;
        TextView txtCategoria;

        public DespesaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_despesa_descricao);
            txtValor = itemView.findViewById(R.id.txt_despesa_valor);
            txtData = itemView.findViewById(R.id.txt_despesa_data);
            txtCategoria = itemView.findViewById(R.id.txt_despesa_categoria);
        }
    }
}
