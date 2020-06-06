package com.example.lifeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.Indicateur;
import com.example.lifeconnect.model.Structure;

import java.util.ArrayList;

public class AddIndicateurActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner listIndicateur;
    Button addButton;
    Button saveButton;
    int numberNewIndicateur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_indicateur);

        Intent intent = getIntent();
        final Structure structure = (Structure) intent.getSerializableExtra("Structure");
        final Espace espace = (Espace) intent.getSerializableExtra("Espace");

        TextView title = findViewById(R.id.espaceNameIndicateur);
        title.setText("Ajout d\'un nouvel indicateur\n" + espace.getName());

        listIndicateur = findViewById(R.id.spinnerIndicateur);
        addButton = findViewById(R.id.addButton);
        saveButton = findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.indicateur_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listIndicateur.setAdapter(adapter);
        listIndicateur.setOnItemSelectedListener(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newIndicateur = listIndicateur.getSelectedItem().toString();

                TextView textViewNumberNewIndicateur = findViewById(R.id.textNumberNewIndcateur);
                String newText = textViewNumberNewIndicateur.getText().toString();

                ArrayList<Indicateur> arrayList = new ArrayList<>();
                int id = 0;
                if (espace.getListIndicateur() != null && espace.getListIndicateur().size() != 0) {
                    arrayList = espace.getListIndicateur();
                    id = arrayList.get(arrayList.size() - 1).getId() + 1;
                }
                Indicateur indicateur = null;

                switch (newIndicateur) {
                    case "Texte":
                        EditText nameNewTextView = findViewById(R.id.nameNewTextView);
                        EditText descriptionNewTextView = findViewById(R.id.descriptionNewTextView);
                        Switch isTitleNewTextView = findViewById(R.id.isTitleNewTextView);

                        indicateur = new Indicateur(id, nameNewTextView.getText().toString(), 0, descriptionNewTextView.getText().toString() + "#" + isTitleNewTextView.isChecked());

                        break;

                    case "Barre de progression":
                        EditText nameNewProgressBar = findViewById(R.id.nameNewProgressBar);
                        EditText descriptionNewProgressBar = findViewById(R.id.descriptionNewProgressBar);
                        EditText objectifNewProgressBar = findViewById(R.id.objectifNewProgressBar);

                        indicateur = new Indicateur(id, nameNewProgressBar.getText().toString(), 1, descriptionNewProgressBar.getText().toString(), objectifNewProgressBar.getText().toString());

                        break;

                    case "Heure + texte modifiable":
                        TextView newTimeView = findViewById(R.id.newTimeView);
                        EditText nameNewTimeView = findViewById(R.id.nameNewTimeView);
                        EditText descriptionNewTimeView = findViewById(R.id.descriptionNewTimeView);
                        EditText indiceNewTimeView = findViewById(R.id.indiceNewTimeView);

                        indicateur = new Indicateur(id, nameNewTimeView.getText().toString(), 2, newTimeView.getText() + "#" + descriptionNewTimeView.getText().toString() + "#" + indiceNewTimeView.getText().toString());

                        break;
                }

                if (indicateur != null) {
                    arrayList.add(indicateur);
                    numberNewIndicateur += 1;
                    if (numberNewIndicateur == 1) {
                        newText = "Vous avez ajouté " + numberNewIndicateur + " indicateur pour le moment";
                    }
                    else if (numberNewIndicateur > 1) {
                        newText = "Vous avez ajouté " + numberNewIndicateur + " indicateurs pour le moment";
                    }
                }

                textViewNumberNewIndicateur.setText(newText);
                espace.setListIndicateur(arrayList);

            }
        });

        saveButton.
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Structure newStructure = UpdateStructure(structure, espace);

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Structure", newStructure);
                        returnIntent.putExtra("Espace", espace);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ConstraintLayout layout = findViewById(R.id.addIndicateurLayout);
        ConstraintSet constraintSet = new ConstraintSet();

        removeAllView(layout);

        switch (position) {
            case 0:

                if (findViewById(R.id.newTextView) == null) {
                    TextView newTextView = new TextView(getApplicationContext());
                    newTextView.setId(R.id.newTextView);
                    newTextView.setText("");
                    newTextView.setTextSize(15);
                    newTextView.setWidth(parent.getWidth() * 2);
                    newTextView.setTextColor(Color.BLACK);
                    layout.addView(newTextView);
                }
                if (findViewById(R.id.nameNewTextView) == null) {
                    EditText nameNewTextView = new EditText(getApplicationContext());
                    nameNewTextView.setId(R.id.nameNewTextView);
                    nameNewTextView.setHint("Nom de l'indicateur...");
                    nameNewTextView.setTextSize(15);
                    nameNewTextView.setWidth(parent.getWidth() * 2);
                    nameNewTextView.setSingleLine(true);
                    layout.addView(nameNewTextView);
                }
                if (findViewById(R.id.descriptionNewTextView) == null) {
                    EditText contentNewTextView = new EditText(getApplicationContext());
                    contentNewTextView.setId(R.id.descriptionNewTextView);
                    contentNewTextView.setHint("Texte de l'indicateur...");
                    contentNewTextView.setTextSize(15);
                    contentNewTextView.setWidth(parent.getWidth() * 2);
                    contentNewTextView.setSingleLine(true);
                    contentNewTextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            TextView newTextView = findViewById(R.id.newTextView);
                            newTextView.setText(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                    layout.addView(contentNewTextView);
                }
                if (findViewById(R.id.isTitleNewTextView) == null) {
                    Switch isTitleNewTextView = new Switch(getApplicationContext());
                    isTitleNewTextView.setId(R.id.isTitleNewTextView);
                    isTitleNewTextView.setText("Est un titre :");
                    isTitleNewTextView.setChecked(false);
                    isTitleNewTextView.setTextSize(15);
                    isTitleNewTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                TextView newTextView = findViewById(R.id.newTextView);
                                newTextView.setTextSize(20);
                                newTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            }
                            if (!isChecked) {
                                TextView newTextView = findViewById(R.id.newTextView);
                                newTextView.setTextSize(15);
                                newTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            }
                        }
                    });
                    layout.addView(isTitleNewTextView);
                }

                // Begin constraint
                constraintSet.clone(layout);
                constraintSet.connect(R.id.newTextView, ConstraintSet.TOP, R.id.spinnerIndicateur, ConstraintSet.BOTTOM, 192);
                constraintSet.connect(R.id.newTextView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.newTextView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.nameNewTextView, ConstraintSet.TOP, R.id.newTextView, ConstraintSet.BOTTOM, 32);
                constraintSet.connect(R.id.nameNewTextView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.nameNewTextView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.descriptionNewTextView, ConstraintSet.TOP, R.id.nameNewTextView, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.descriptionNewTextView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.descriptionNewTextView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.isTitleNewTextView, ConstraintSet.TOP, R.id.descriptionNewTextView, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.isTitleNewTextView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.isTitleNewTextView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.applyTo(layout);

                break;

            case 1:

                if (findViewById(R.id.newProgressBar) == null) {
                    ProgressBar newProgressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                    newProgressBar.setId(R.id.newProgressBar);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(parent.getWidth() * 2, 10);
                    newProgressBar.setLayoutParams(params);
                    layout.addView(newProgressBar);
                }
                if (findViewById(R.id.nameNewProgressBar) == null) {
                    EditText nameNewProgressBar = new EditText(getApplicationContext());
                    nameNewProgressBar.setId(R.id.nameNewProgressBar);
                    nameNewProgressBar.setHint("Nom de la bar de progression...");
                    nameNewProgressBar.setTextSize(15);
                    nameNewProgressBar.setWidth(parent.getWidth() * 2);
                    nameNewProgressBar.setSingleLine(true);
                    layout.addView(nameNewProgressBar);
                }
                if (findViewById(R.id.descriptionNewProgressBar) == null) {
                    EditText descriptionNewProgressBar = new EditText(getApplicationContext());
                    descriptionNewProgressBar.setId(R.id.descriptionNewProgressBar);
                    descriptionNewProgressBar.setHint("Description de la bar de progression...");
                    descriptionNewProgressBar.setTextSize(15);
                    descriptionNewProgressBar.setWidth(parent.getWidth() * 2);
                    descriptionNewProgressBar.setSingleLine(true);
                    layout.addView(descriptionNewProgressBar);
                }
                if (findViewById(R.id.objectifNewProgressBar) == null) {
                    EditText objectifNewProgressBar = new EditText(getApplicationContext());
                    objectifNewProgressBar.setId(R.id.objectifNewProgressBar);
                    objectifNewProgressBar.setHint("Objectif à atteindre...");
                    objectifNewProgressBar.setTextSize(15);
                    objectifNewProgressBar.setWidth(parent.getWidth() * 2);
                    objectifNewProgressBar.setSingleLine(true);
                    objectifNewProgressBar.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    layout.addView(objectifNewProgressBar);
                }

                constraintSet.clone(layout);
                constraintSet.connect(R.id.newProgressBar, ConstraintSet.TOP, R.id.spinnerIndicateur, ConstraintSet.BOTTOM, 254);
                constraintSet.connect(R.id.newProgressBar, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.newProgressBar, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.nameNewProgressBar, ConstraintSet.TOP, R.id.newProgressBar, ConstraintSet.BOTTOM, 32);
                constraintSet.connect(R.id.nameNewProgressBar, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.nameNewProgressBar, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.descriptionNewProgressBar, ConstraintSet.TOP, R.id.nameNewProgressBar, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.descriptionNewProgressBar, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.descriptionNewProgressBar, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.objectifNewProgressBar, ConstraintSet.TOP, R.id.descriptionNewProgressBar, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.objectifNewProgressBar, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.objectifNewProgressBar, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.applyTo(layout);

                break;

            case 2:

                if (findViewById(R.id.newTimeView) == null) {
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            parent.getWidth(),
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );

                    TextView newTimeView = new TextView(getApplicationContext());
                    newTimeView.setId(R.id.newTimeView);
                    newTimeView.setTextSize(20);
                    newTimeView.setLayoutParams(params);
                    newTimeView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    newTimeView.setTextColor(Color.BLACK);
                    layout.addView(newTimeView);
                }
                if (findViewById(R.id.nameNewTimeView) == null) {
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            parent.getWidth(),
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );

                    EditText newEditTime = new EditText(getApplicationContext());
                    newEditTime.setId(R.id.newEditTime);
                    newEditTime.setTextSize(20);
                    newEditTime.setLayoutParams(params);
                    newEditTime.setSingleLine(true);
                    layout.addView(newEditTime);
                }
                if (findViewById(R.id.nameNewTimeView) == null) {
                    EditText nameNewTimeView = new EditText(getApplicationContext());
                    nameNewTimeView.setId(R.id.nameNewTimeView);
                    nameNewTimeView.setHint("Nom de l'indicateur...");
                    nameNewTimeView.setTextSize(15);
                    nameNewTimeView.setWidth(parent.getWidth() * 2);
                    nameNewTimeView.setSingleLine(true);
                    layout.addView(nameNewTimeView);
                }
                if (findViewById(R.id.descriptionNewTimeView) == null) {
                    EditText descriptionNewTimeView = new EditText(getApplicationContext());
                    descriptionNewTimeView.setId(R.id.descriptionNewTimeView);
                    descriptionNewTimeView.setHint("Description de l'indicateur...");
                    descriptionNewTimeView.setTextSize(15);
                    descriptionNewTimeView.setWidth(parent.getWidth() * 2);
                    descriptionNewTimeView.setSingleLine(true);
                    layout.addView(descriptionNewTimeView);
                }
                if (findViewById(R.id.timeNewTimeView) == null) {
                    Button timeNewTimeView = new Button(getApplicationContext());
                    timeNewTimeView.setId(R.id.timeNewTimeView);
                    timeNewTimeView.setText("Sélectionner l'heure");
                    timeNewTimeView.setTextSize(15);
                    timeNewTimeView.setWidth(parent.getWidth() * 2);
                    timeNewTimeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(AddIndicateurActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                    TextView newTimeView = findViewById(R.id.newTimeView);
                                    if (minutes < 10) {
                                        newTimeView.setText(hourOfDay + ":0" + minutes);
                                    }
                                    else newTimeView.setText(hourOfDay + ":" + minutes);
                                }
                            }, 0, 0, false);
                            timePickerDialog.show();
                        }
                    });
                    layout.addView(timeNewTimeView);
                }
                if (findViewById(R.id.indiceNewTimeView) == null) {
                    EditText indiceNewTimeView = new EditText(getApplicationContext());
                    indiceNewTimeView.setId(R.id.indiceNewTimeView);
                    indiceNewTimeView.setHint("Indice pour la complétion de l'indicateur...");
                    indiceNewTimeView.setTextSize(15);
                    indiceNewTimeView.setWidth(parent.getWidth() * 2);
                    indiceNewTimeView.setSingleLine(true);
                    indiceNewTimeView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            EditText newEditTime = findViewById(R.id.newEditTime);
                            newEditTime.setHint(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    layout.addView(indiceNewTimeView);
                }

                constraintSet.clone(layout);
                constraintSet.connect(R.id.newTimeView, ConstraintSet.TOP, R.id.spinnerIndicateur, ConstraintSet.BOTTOM, 192);
                constraintSet.connect(R.id.newTimeView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.newTimeView, ConstraintSet.END, R.id.newEditTime, ConstraintSet.START, 16);
                constraintSet.connect(R.id.newEditTime, ConstraintSet.TOP, R.id.spinnerIndicateur, ConstraintSet.BOTTOM, 164);
                constraintSet.connect(R.id.newEditTime, ConstraintSet.START, R.id.newTimeView, ConstraintSet.END, 16);
                constraintSet.connect(R.id.newEditTime, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.nameNewTimeView, ConstraintSet.TOP, R.id.newTimeView, ConstraintSet.BOTTOM, 128);
                constraintSet.connect(R.id.nameNewTimeView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.nameNewTimeView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.descriptionNewTimeView, ConstraintSet.TOP, R.id.nameNewTimeView, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.descriptionNewTimeView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.descriptionNewTimeView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.timeNewTimeView, ConstraintSet.TOP, R.id.descriptionNewTimeView, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.timeNewTimeView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.timeNewTimeView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.connect(R.id.indiceNewTimeView, ConstraintSet.TOP, R.id.timeNewTimeView, ConstraintSet.BOTTOM, 16);
                constraintSet.connect(R.id.indiceNewTimeView, ConstraintSet.START, R.id.addIndicateurLayout, ConstraintSet.START, 16);
                constraintSet.connect(R.id.indiceNewTimeView, ConstraintSet.END, R.id.addIndicateurLayout, ConstraintSet.END, 16);
                constraintSet.applyTo(layout);

                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void removeAllView(ConstraintLayout _layout) {

        // case 0 :
        if (findViewById(R.id.newTextView) != null) {
            _layout.removeView(findViewById(R.id.newTextView));
        }
        if (findViewById(R.id.nameNewTextView) != null) {
            _layout.removeView(findViewById(R.id.nameNewTextView));
        }
        if (findViewById(R.id.descriptionNewTextView) != null) {
            _layout.removeView(findViewById(R.id.descriptionNewTextView));
        }
        if (findViewById(R.id.isTitleNewTextView) != null) {
            _layout.removeView(findViewById(R.id.isTitleNewTextView));
        }

        // case 1 :
        if (findViewById(R.id.newProgressBar) != null) {
            _layout.removeView(findViewById(R.id.newProgressBar));
        }
        if (findViewById(R.id.nameNewProgressBar) != null) {
            _layout.removeView(findViewById(R.id.nameNewProgressBar));
        }
        if (findViewById(R.id.descriptionNewProgressBar) != null) {
            _layout.removeView(findViewById(R.id.descriptionNewProgressBar));
        }
        if (findViewById(R.id.objectifNewProgressBar) != null) {
            _layout.removeView(findViewById(R.id.objectifNewProgressBar));
        }

        // case 2 :
        if (findViewById(R.id.newTimeView) != null) {
            _layout.removeView(findViewById(R.id.newTimeView));
        }
        if (findViewById(R.id.newEditTime) != null) {
            _layout.removeView(findViewById(R.id.newEditTime));
        }
        if (findViewById(R.id.nameNewTimeView) != null) {
            _layout.removeView(findViewById(R.id.nameNewTimeView));
        }
        if (findViewById(R.id.descriptionNewTimeView) != null) {
            _layout.removeView(findViewById(R.id.descriptionNewTimeView));
        }
        if (findViewById(R.id.indiceNewTimeView) != null) {
            _layout.removeView(findViewById(R.id.indiceNewTimeView));
        }
    }

    private Structure UpdateStructure(Structure _structure, Espace _espace) {
        ArrayList<Espace> arrayList = _structure.getEspaces();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getId() == _espace.getId()) {
                arrayList.get(i).setName(_espace.getName());
                arrayList.get(i).setListIndicateur(_espace.getListIndicateur());
            }
        }
        return _structure;
    }

}
