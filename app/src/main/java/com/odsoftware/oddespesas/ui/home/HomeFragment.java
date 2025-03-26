package com.odsoftware.oddespesas.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.card.MaterialCardView;
import com.odsoftware.oddespesas.R;
import com.odsoftware.oddespesas.adapter.DespesaAdapter;
import com.odsoftware.oddespesas.adapter.ReceitaAdapter;
import com.odsoftware.oddespesas.controller.DespesaController;
import com.odsoftware.oddespesas.controller.ReceitaController;
import com.odsoftware.oddespesas.databinding.FragmentHomeBinding;
import com.odsoftware.oddespesas.model.Despesa;
import com.odsoftware.oddespesas.model.Receita;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private DespesaController despesaController;

    private ReceitaController receitaController;

    // Textos de resumo financeiro
    private TextView txtValorReceitas;
    private TextView txtValorDespesas;
    private TextView txtValorSaldo;

    // Opções "Ver todas"
    private TextView txtVerTodasDespesas;
    private TextView txtVerTodasReceitas;

    // RecyclerViews
    private RecyclerView recyclerDespesas;
    private RecyclerView recyclerReceitas;

    // Cards
    private MaterialCardView cardResumoFinanceiro;
    private MaterialCardView cardListaDespesas;
    private MaterialCardView cardGraficoCategorias;
    private MaterialCardView cardListaReceitas;

    // Adapters
    private DespesaAdapter despesaAdapter;
    private ReceitaAdapter receitaAdapter;

    // Dados temporários para demonstração
    private List<Despesa> despesasList;
    private List<Receita> receitasList;

    // Valores do resumo
    private double totalReceitas = 0.0;
    private double totalDespesas = 0.0;
    private double saldo = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        despesaController = new DespesaController(requireContext());
        receitaController = new ReceitaController(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setupRecyclerViews();
        loadDados();
        atualizarResumoFinanceiro();
        setupListeners();
    }

    private void initViews() {
        // Textos de resumo financeiro
        txtValorReceitas = binding.txtValorReceitas;
        txtValorDespesas = binding.txtValorDespesas;
        txtValorSaldo = binding.txtValorSaldo;

        // Opções "Ver todas"
        txtVerTodasDespesas = binding.txtVerTodasDespesas;
        txtVerTodasReceitas = binding.txtVerTodasReceitas;

        // RecyclerViews
        recyclerDespesas = binding.recyclerDespesas;
        recyclerReceitas = binding.recyclerReceitas;

        // Cards
        cardResumoFinanceiro = binding.cardResumoFinanceiro;
        cardListaDespesas = binding.cardListaDespesas;
        cardGraficoCategorias = binding.cardGraficoCategorias;
        cardListaReceitas = binding.cardListaReceitas;
    }

    private void setupRecyclerViews() {
        // Configurar RecyclerView de Despesas
        recyclerDespesas.setLayoutManager(new LinearLayoutManager(requireContext()));
        despesasList = new ArrayList<>();
        despesaAdapter = new DespesaAdapter(despesasList);
        recyclerDespesas.setAdapter(despesaAdapter);

        // Configurar RecyclerView de Receitas
        recyclerReceitas.setLayoutManager(new LinearLayoutManager(requireContext()));
        receitasList = new ArrayList<>();
        receitaAdapter = new ReceitaAdapter(receitasList);
        recyclerReceitas.setAdapter(receitaAdapter);
    }

    private void loadDados() {
        // Carregar dados de demonstração
        carregarDespesasDemonstracao();
        carregarReceitasDemonstracao();

        // Notificar os adapters
        despesaAdapter.notifyDataSetChanged();
        receitaAdapter.notifyDataSetChanged();

        // Calcular totais
        calcularTotais();
    }

    private void carregarDespesasDemonstracao() {

        try {

            despesasList.clear();

            List<Despesa> despesas = despesaController.listarDespesas();

            for(Despesa d : despesas){
                despesasList.add(new Despesa(d.getDescricao(),
                        d.getValor(),
                        d.getData(),
                        d.getCategoria()));
            }

        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

    }

    private void carregarReceitasDemonstracao() {

        try {
            receitasList.clear();

            List<Receita> receitas = receitaController.listarReceitas();

            for(Receita r : receitas){

                receitasList.add(new Receita(
                        r.getDescricao(),
                        r.getValor(),
                        r.getData(),
                        r.getCategoria()
                ));

            }
        }catch (Exception e){
            Log.e("ERRO", e.getMessage());
        }


    }

    private void calcularTotais() {
        // Calcular total de despesas
        totalDespesas = 0.0;
        for (Despesa despesa : despesasList) {
            totalDespesas += despesa.getValor();
        }

        // Calcular total de receitas
        totalReceitas = 0.0;
        for (Receita receita : receitasList) {
            totalReceitas += receita.getValor();
        }

        // Calcular saldo
        saldo = totalReceitas - totalDespesas;
    }

    private void atualizarResumoFinanceiro() {
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        formatoMoeda.setCurrency(Currency.getInstance("BRL"));

        txtValorReceitas.setText(formatoMoeda.format(totalReceitas));
        txtValorDespesas.setText(formatoMoeda.format(totalDespesas));
        txtValorSaldo.setText(formatoMoeda.format(saldo));

        // Se o saldo for negativo, alterar a cor para vermelho
        if (saldo < 0) {
            txtValorSaldo.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
        } else {
            txtValorSaldo.setTextColor(getResources().getColor(R.color.purple_500, null));
        }
    }

    private void setupListeners() {
        // Configurar click listeners para "Ver todas"
        txtVerTodasDespesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para tela de listagem completa de despesas
                //Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_listaDespesasFragment);
            }
        });

        txtVerTodasReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para tela de listagem completa de receitas
                //Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_listaReceitasFragment);
            }
        });

        // Outras ações podem ser configuradas aqui, como cliques nos cards
        cardResumoFinanceiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card de resumo financeiro
                // Por exemplo, mostrar um relatório detalhado
            }
        });

        cardGraficoCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no card do gráfico
                // Por exemplo, exibir uma visualização maior do gráfico
            }
        });
    }

    private void setupPieChart() {
        PieChart pieChart = binding.graficoPizzaCategorias;

        // Configurações básicas do gráfico
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setHoleRadius(58f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Despesas");
        pieChart.setCenterTextSize(16f);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // Definir dados do gráfico
        ArrayList<PieEntry> entries = new ArrayList<>();

        List<Despesa> despesas = despesaController.listarDespesas();

        for (Despesa d : despesas){
            entries.add(new PieEntry(
                    (float) d.getValor(), d.getCategoria()
            ));
        }


        // Agregar despesas por categoria
        Map<String, Float> despesasPorCategoria = new HashMap<>();
        for (Despesa despesa : despesas) {
            String categoria = despesa.getCategoria();
            float valor = (float) despesa.getValor();

            if (despesasPorCategoria.containsKey(categoria)) {
                despesasPorCategoria.put(categoria, despesasPorCategoria.get(categoria) + valor);
            } else {
                despesasPorCategoria.put(categoria, valor);
            }
        }

        // Adicionar entradas para o gráfico
        for (Map.Entry<String, Float> entry : despesasPorCategoria.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categorias");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // Cores
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(64, 89, 128));
        colors.add(Color.rgb(149, 165, 124));
        colors.add(Color.rgb(217, 184, 162));
        colors.add(Color.rgb(191, 134, 134));
        colors.add(Color.rgb(179, 48, 80));
        colors.add(Color.rgb(255, 193, 7));
        colors.add(Color.rgb(76, 175, 80));
        colors.add(Color.rgb(33, 150, 243));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}