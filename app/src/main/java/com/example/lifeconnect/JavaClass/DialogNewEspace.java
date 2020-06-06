package com.example.lifeconnect.JavaClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lifeconnect.R;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.LocalStructures;
import com.example.lifeconnect.model.Structure;
import com.example.lifeconnect.ui.listEspaces.ListEspacesFragment;

import java.sql.Struct;
import java.util.ArrayList;

public class DialogNewEspace extends DialogFragment {

    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;
    private EditText editText;
    private Button cancelButton, confirmButton;

    private View view;
    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;

    private static final String FILENAME = "StructureDesEspaces.json";
    private static final JsonOperation jsonOp = new JsonOperation();

    private LocalStructures localStructures;
    private Structure structure;
    private ListEspacesFragment listEspacesFragment;

    public DialogNewEspace(LocalStructures localStructures, Structure structure, ListEspacesFragment listEspacesFragment) {
        this.localStructures = localStructures;
        this.structure = structure;
        this.listEspacesFragment = listEspacesFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {

        dialogBuilder = new AlertDialog.Builder(getActivity());

        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_espace, null);

        checkBox1 = view.findViewById(R.id.lundiCheckBox);
        checkBox2 = view.findViewById(R.id.mardiCheckBox);
        checkBox3 = view.findViewById(R.id.mercrediCheckBox);
        checkBox4 = view.findViewById(R.id.jeudiCheckBox);
        checkBox5 = view.findViewById(R.id.vendrediCheckBox);
        checkBox6 = view.findViewById(R.id.samediCheckBox);
        checkBox7 = view.findViewById(R.id.dimancheCheckBox);
        editText = view.findViewById(R.id.titleEditText);
        cancelButton = view.findViewById(R.id.cancelDialogButton);
        confirmButton = view.findViewById(R.id.confirmDialogButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        int id = 0;
                        if (structure.getEspaces() != null && structure.getEspaces().size() != 0) {
                            ArrayList<Espace> espaces = structure.getEspaces();
                            id = espaces.get(espaces.size() - 1).getId() + 1;
                        }

                        ArrayList<Integer> listDayOn = new ArrayList<>();
                        if (checkBox1.isChecked()) {
                            listDayOn.add(1);
                        }
                        if (checkBox2.isChecked()) {
                            listDayOn.add(2);
                        }
                        if (checkBox3.isChecked()) {
                            listDayOn.add(3);
                        }
                        if (checkBox4.isChecked()) {
                            listDayOn.add(4);
                        }
                        if (checkBox5.isChecked()) {
                            listDayOn.add(5);
                        }
                        if (checkBox6.isChecked()) {
                            listDayOn.add(6);
                        }
                        if (checkBox7.isChecked()) {
                            listDayOn.add(7);
                        }
                        if (listDayOn.size() == 0) {
                            Toast toast = Toast.makeText(listEspacesFragment.getContext(),"Aucune journée sélectionnée", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Espace espace = new Espace(id, editText.getText().toString(), null, listDayOn);
                        structure.getEspaces().add(espace);

                        boolean addStructure = true;
                        if (localStructures.getStructures().size() == 0) {
                            localStructures.getStructures().add(structure);
                            addStructure = false;
                        }
                        else {
                            for (Structure structure_local : localStructures.getStructures()) {
                                if (structure_local.getUser() == structure.getUser()) {
                                    structure_local.setEspaces(structure.getEspaces());
                                    addStructure = false;
                                }
                            }
                        }

                        if (addStructure) localStructures.getStructures().add(structure);


                        jsonOp.SaveJson(listEspacesFragment.getContext(), FILENAME, localStructures);

                        listEspacesFragment.refreshLinearLayout();
                        dialog.cancel();
            }
        });

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;


    }
}
