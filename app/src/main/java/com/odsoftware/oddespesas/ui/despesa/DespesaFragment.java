package com.odsoftware.oddespesas.ui.despesa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.odsoftware.oddespesas.controller.DespesaController;
import com.odsoftware.oddespesas.databinding.FragmentDespesaBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DespesaFragment extends Fragment {

    private FragmentDespesaBinding binding;

    private DespesaController despesaController;

    private TextInputLayout descricaoLayout;
    private TextInputLayout valorLayout;
    private TextInputLayout dataLayout;
    private TextInputLayout categoriaLayout;

    private TextInputEditText descricaoEdit;
    private TextInputEditText valorEdit;
    private TextInputEditText dataEdit;
    private AutoCompleteTextView categoriaSpinner;

    private MaterialButton limparButton;
    private MaterialButton salvarButton;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDespesaBinding.inflate(inflater, container, false);

        despesaController = new DespesaController(requireContext());

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

        initViews();
        setupDatePicker();
        setupCategoriaSpinner();
        setupListeners();

    }

    private void initViews() {
        descricaoLayout = binding.layoutDescricao;
        valorLayout = binding.layoutValor;
        dataLayout = binding.layoutData;
        categoriaLayout = binding.layoutCategoria;

        descricaoEdit = binding.editDescricao;
        valorEdit = binding.editValor;
        dataEdit = binding.editData;
        categoriaSpinner = binding.spinnerCategoria;

        limparButton = binding.btnLimpar;
        salvarButton = binding.btnSalvar;
    }

    private void setupDatePicker() {
        dataEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        dataLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        dataEdit.setText(dateFormat.format(calendar.getTime()));

                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupCategoriaSpinner() {
        List<String> categorias = new ArrayList<>();
        categorias.add("Alimentação");
        categorias.add("Moradia");
        categorias.add("Transporte");
        categorias.add("Saúde");
        categorias.add("Educação");
        categorias.add("Lazer");
        categorias.add("Vestuário");
        categorias.add("Outros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categorias
        );

        categoriaSpinner.setAdapter(adapter);
    }

    private void setupListeners() {
        limparButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    salvarDespesa();
                }
            }
        });
    }

    private void limparCampos() {
        if (descricaoEdit.getText() != null) {
            descricaoEdit.getText().clear();
        }
        if (valorEdit.getText() != null) {
            valorEdit.getText().clear();
        }
        if (dataEdit.getText() != null) {
            dataEdit.getText().clear();
        }
        if (categoriaSpinner.getText() != null) {
            categoriaSpinner.getText().clear();
        }

        // Limpa possíveis erros
        descricaoLayout.setError(null);
        valorLayout.setError(null);
        dataLayout.setError(null);
        categoriaLayout.setError(null);
    }

    private boolean validarCampos() {
        boolean isValid = true;

        // Validar descrição
        if (descricaoEdit.getText() == null || descricaoEdit.getText().toString().trim().isEmpty()) {
            descricaoLayout.setError("Descrição é obrigatória");
            isValid = false;
        } else {
            descricaoLayout.setError(null);
        }

        // Validar valor
        if (valorEdit.getText() == null || valorEdit.getText().toString().trim().isEmpty()) {
            valorLayout.setError("Valor é obrigatório");
            isValid = false;
        } else {
            try {
                double valor = Double.parseDouble(valorEdit.getText().toString().replace(",", "."));
                if (valor <= 0) {
                    valorLayout.setError("Valor deve ser maior que zero");
                    isValid = false;
                } else {
                    valorLayout.setError(null);
                }
            } catch (NumberFormatException e) {
                valorLayout.setError("Valor inválido");
                isValid = false;
            }
        }

        // Validar data
        if (dataEdit.getText() == null || dataEdit.getText().toString().trim().isEmpty()) {
            dataLayout.setError("Data é obrigatória");
            isValid = false;
        } else {
            dataLayout.setError(null);
        }

        // Validar categoria
        if (categoriaSpinner.getText() == null || categoriaSpinner.getText().toString().trim().isEmpty()) {
            categoriaLayout.setError("Categoria é obrigatória");
            isValid = false;
        } else {
            categoriaLayout.setError(null);
        }

        return isValid;
    }

    private void salvarDespesa() {

        String descricao = descricaoEdit.getText() != null ? descricaoEdit.getText().toString().trim() : "";
        double valor = 0;
        try {
            valor = Double.parseDouble(valorEdit.getText().toString().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Erro" + e.getMessage());
        }
        String data = dataEdit.getText() != null ? dataEdit.getText().toString() : "";
        String categoria = categoriaSpinner.getText() != null ? categoriaSpinner.getText().toString() : "";

        boolean retorno = despesaController.salvarDespesa(
                descricao,
                valor,
                data,
                categoria
        );

        if(retorno){
            Toast.makeText(requireContext(), "Despesa salva com sucesso!", Toast.LENGTH_SHORT).show();
            limparCampos();
        }else{
            Toast.makeText(requireContext(), "Erro ao salvar despesa!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}