package com.example.lifeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.model.Data;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.Indicateur;
import com.example.lifeconnect.model.DataPerUser;
import com.example.lifeconnect.model.LocalDatas;
import com.example.lifeconnect.model.Structure;
import com.example.lifeconnect.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class EspaceDetailsActivity extends AppCompatActivity {

    private static final String FILENAME_DATA = "DonneesLocales.json";
    private JsonOperation jsonOperation = new JsonOperation();

    private ConstraintLayout constraintLayout;
    private ArrayList<View> views = new ArrayList<>();
    private View lastView;

    private Structure structure;
    private LocalDatas localDatas;
    private DataPerUser dataPerUser;
    private Espace espace;
    private Date date;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_details);

        constraintLayout = findViewById(R.id.detailsEspaceLayout);
        constraintLayout.setPadding(16, 16, 16, 16);

        Intent intent = getIntent();
        structure = (Structure) intent.getSerializableExtra("Structure");
        espace = (Espace) intent.getSerializableExtra("Espace");
        date = (Date) intent.getSerializableExtra("Date");
        user = (User) intent.getSerializableExtra("User");
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        Object returnData = jsonOperation.getObjectFromJson(getApplicationContext(), FILENAME_DATA, 1);
        if (returnData instanceof LocalDatas) {
            localDatas = (LocalDatas) returnData;
        }

        if (localDatas != null) {
            if (localDatas.getDataPerUsers() != null) {
                for (DataPerUser dataPerUser_json: localDatas.getDataPerUsers()) {
                    if (dataPerUser_json.getUser() == user.getId()) dataPerUser = dataPerUser_json;
                }
            }
        }
        if (dataPerUser != null) {
            Espace tmpEspace = findData();
            if (tmpEspace == null) {
                espace = findStructure();
            } else {
                espace = tmpEspace;
            }
        } else {
            espace = findStructure();
        }

        CreateEspaceDesign();

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButtonDetails);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataPerUser == null) {
                    ArrayList<Espace> espaces = new ArrayList<>();
                    espaces.add(espace);

                    ArrayList<Data> newDatas = new ArrayList<>();
                    newDatas.add(new Data(date, espaces));

                    dataPerUser = new DataPerUser(newDatas, user.getId());
                } else {
                    Data currentData = null;
                    Boolean isEspaceUpdate = false;
                    for (int i = 0; i < dataPerUser.getDatas().size(); i++) {
                        if (isDateEqual(dataPerUser.getDatas().get(i).getDate(), date)) {
                            currentData = dataPerUser.getDatas().get(i);
                            for (int j = 0; j < dataPerUser.getDatas().get(i).getEspaces().size(); j++) {
                                if (dataPerUser.getDatas().get(i).getEspaces().get(j).getId() == espace.getId()) {
                                    dataPerUser.getDatas().get(i).getEspaces().get(j).setListIndicateur(espace.getListIndicateur());
                                    isEspaceUpdate = true;
                                }
                            }
                            if (!isEspaceUpdate) {
                                dataPerUser.getDatas().get(i).getEspaces().add(espace);
                            }
                        }
                    }

                    if (currentData == null) {
                        ArrayList<Espace> espaces = new ArrayList<>();
                        espaces.add(espace);
                        currentData = new Data(date, espaces);
                        dataPerUser.getDatas().add(currentData);
                    }
                }

                Boolean addDataForUser = true;
                if (localDatas == null) localDatas = new LocalDatas(new ArrayList<DataPerUser>());
                else if (localDatas.getDataPerUsers() != null && localDatas.getDataPerUsers().size() != 0) {
                    for (DataPerUser dataPerUser_local: localDatas.getDataPerUsers()) {
                        if (dataPerUser_local.getUser() == dataPerUser.getUser()) {
                            dataPerUser_local.setDatas(dataPerUser.getDatas());
                            addDataForUser = false;
                        }
                    }
                }
                if (addDataForUser) localDatas.getDataPerUsers().add(dataPerUser);

                jsonOperation.SaveJson(getApplicationContext(), FILENAME_DATA, localDatas);

                Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.espace_details_activity_update_success), Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }

    public void CreateEspaceDesign() {

        if (espace.getListIndicateur() == null) {
            return;
        }

        for (int i = 0; i < espace.getListIndicateur().size(); i++) {
            Indicateur currentIndicateur = espace.getListIndicateur().get(i);

            switch (currentIndicateur.getType()) {
                case 0:
                    DesignTextView(currentIndicateur);
                    break;
                case 1:
                    DesignProgressBar(currentIndicateur);
                    break;
            }

            views.add(lastView);
        }

    }

    public Espace findStructure() {
        for (int i = 0; i < structure.getEspaces().size(); i++) {
            if (structure.getEspaces().get(i).getId() == espace.getId()) {
                return structure.getEspaces().get(i);
            }
        }
        return espace;
    }

    public Espace findData() {
        for (int i = 0; i < dataPerUser.getDatas().size(); i++) {
            if (isDateEqual(dataPerUser.getDatas().get(i).getDate(), date)) {
                for (int j = 0; j < dataPerUser.getDatas().get(i).getEspaces().size(); j++) {
                    Espace currentEspace = dataPerUser.getDatas().get(i).getEspaces().get(j);
                    if (currentEspace.getId() == espace.getId()) {
                        return currentEspace;
                    }
                }
            }
        }
        return null;
    }

    public boolean isDateEqual(Date firstDate, Date secondDate) {
        Boolean Year = false;
        Boolean Month = false;
        Boolean Day = false;

        if (firstDate.getYear() == secondDate.getYear()) {
            Year = true;
        }
        if (firstDate.getMonth() == secondDate.getMonth()) {
            Month = true;
        }
        if (firstDate.getDate() == secondDate.getDate()) {
            Day = true;
        }

        if (Year && Month && Day) {
            return true;
        }
        return false;

    }

    public CardView newCardView() {
        CardView cardView = new CardView(getApplicationContext());
        cardView.setId(View.generateViewId());
        cardView.setPadding(16, 16, 16, 16);
        cardView.setElevation(16);
        cardView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));

        return cardView;
    }

    public ConstraintLayout newConstraintLayout() {
        ConstraintLayout newConstraintLayout = new ConstraintLayout(getApplicationContext());
        newConstraintLayout.setPadding(16, 16, 16, 16);
        newConstraintLayout.setId(View.generateViewId());

        return newConstraintLayout;
    }

    public void SetCardViewConstraint(CardView cardView) {
        ConstraintSet constraintSetCV = new ConstraintSet();
        constraintSetCV.clone(constraintLayout);
        if (lastView != null) {
            constraintSetCV.connect(cardView.getId(), ConstraintSet.TOP, lastView.getId(), ConstraintSet.BOTTOM, 16);
        } else {
            constraintSetCV.connect(cardView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16);
        }
        constraintSetCV.connect(cardView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 32);
        constraintSetCV.connect(cardView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 32);
        constraintSetCV.applyTo(constraintLayout);
    }

    public void DesignTextView(Indicateur currentIndicateur) {
        String[] separated = currentIndicateur.getDescription().split("#");
        String description = separated[0];
        Boolean isTitle = Boolean.valueOf(separated[1]);

        TextView textView = new TextView(getApplicationContext());
        textView.setText(description);
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        textView.setId(View.generateViewId());

        if (!isTitle) {
            CardView cardView = newCardView();
            ConstraintLayout newConstraintLayout = newConstraintLayout();

            newConstraintLayout.addView(textView);

            ConstraintSet constraintSetTV = new ConstraintSet();
            constraintSetTV.clone(newConstraintLayout);
            constraintSetTV.connect(textView.getId(), ConstraintSet.TOP, newConstraintLayout.getId(), ConstraintSet.TOP, 16);
            constraintSetTV.connect(textView.getId(), ConstraintSet.START, newConstraintLayout.getId(), ConstraintSet.START, 0);
            constraintSetTV.connect(textView.getId(), ConstraintSet.END, newConstraintLayout.getId(), ConstraintSet.END, 0);
            constraintSetTV.applyTo(newConstraintLayout);

            cardView.addView(newConstraintLayout);
            constraintLayout.addView(cardView);

            SetCardViewConstraint(cardView);

            lastView = cardView;
        } else {

            textView.setTextSize(20);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));

            constraintLayout.addView(textView);

            ConstraintSet constraintSetTV = new ConstraintSet();
            constraintSetTV.clone(constraintLayout);
            if (lastView != null) {
                constraintSetTV.connect(textView.getId(), ConstraintSet.TOP, lastView.getId(), ConstraintSet.BOTTOM, 16);
                constraintSetTV.connect(textView.getId(), ConstraintSet.START, lastView.getId(), ConstraintSet.START, 0);
                constraintSetTV.connect(textView.getId(), ConstraintSet.END, lastView.getId(), ConstraintSet.END, 0);
            } else {
                constraintSetTV.connect(textView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16);
                constraintSetTV.connect(textView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
                constraintSetTV.connect(textView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
            }
            constraintSetTV.applyTo(constraintLayout);

            lastView = textView;
        }
    }

    public void DesignProgressBar(final Indicateur currentIndicateur) {
        String description = currentIndicateur.getDescription();
        final String objectif = currentIndicateur.getObjectif();

        CardView cardView = newCardView();
        ConstraintLayout newConstraintLayout = newConstraintLayout();

        // Create Progress Bar
        final ProgressBar progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgress(0);

        if (currentIndicateur.getValue() != null) {
            float progress = (Float.valueOf(currentIndicateur.getValue()) * 100) / Float.valueOf(objectif);
            progressBar.setProgress(Math.round(progress));
        }

        progressBar.setId(View.generateViewId());
        progressBar.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));

        newConstraintLayout.addView(progressBar);

        ConstraintSet constraintSetPB = new ConstraintSet();
        constraintSetPB.clone(newConstraintLayout);
        constraintSetPB.connect(progressBar.getId(), ConstraintSet.TOP, newConstraintLayout.getId(), ConstraintSet.TOP, 64);
        constraintSetPB.connect(progressBar.getId(), ConstraintSet.START, newConstraintLayout.getId(), ConstraintSet.START, 0);
        constraintSetPB.connect(progressBar.getId(), ConstraintSet.END, newConstraintLayout.getId(), ConstraintSet.END, 0);
        constraintSetPB.applyTo(newConstraintLayout);

        // Create Description TextView
        TextView textView = new TextView(getApplicationContext());
        textView.setText(description);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setId(View.generateViewId());

        newConstraintLayout.addView(textView);

        constraintSetPB.clone(newConstraintLayout);
        constraintSetPB.connect(textView.getId(), ConstraintSet.BOTTOM, progressBar.getId(), ConstraintSet.TOP, 0);
        constraintSetPB.connect(textView.getId(), ConstraintSet.START, progressBar.getId(), ConstraintSet.START, 0);
        constraintSetPB.connect(textView.getId(), ConstraintSet.END, progressBar.getId(), ConstraintSet.END, 0);
        constraintSetPB.applyTo(newConstraintLayout);

        // Create Value TextView
        TextView textView1 = new TextView(getApplicationContext());
        textView1.setText("Réalisé :");
        textView1.setTextSize(15);
        textView1.setId(View.generateViewId());

        newConstraintLayout.addView(textView1);

        constraintSetPB.clone(newConstraintLayout);
        constraintSetPB.connect(textView1.getId(), ConstraintSet.TOP, progressBar.getId(), ConstraintSet.BOTTOM, 0);
        constraintSetPB.connect(textView1.getId(), ConstraintSet.START, progressBar.getId(), ConstraintSet.START, 0);
        constraintSetPB.applyTo(newConstraintLayout);

        // Create Value EditText
        EditText editText = new EditText(getApplicationContext());
        editText.setHint("Réalisé : ");
        editText.setSingleLine(true);
        editText.setTextSize(15);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setId(View.generateViewId());

        if (currentIndicateur.getValue() != null) {
            editText.setText(currentIndicateur.getValue());
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float result = 0;
                if (count == 0) {
                    if (start == 0) {
                        progressBar.setProgress(0);
                        result = 0;
                    } else if (s.charAt(s.length() - 1) != '.') {
                        progressBar.setProgress(
                                Math.round((Float.valueOf(s.toString()) * 100) / Float.valueOf(objectif))
                        );
                        result = Float.valueOf(s.toString());
                    }
                } else if (s.charAt(start - before) == '.') {
                    progressBar.setProgress(0);
                    result = 0;
                } else {
                    progressBar.setProgress(
                            Math.round((Float.valueOf(s.toString()) * 100) / Float.valueOf(objectif))
                    );
                    result = Float.valueOf(s.toString());
                }
                for (int i = 0; i < espace.getListIndicateur().size(); i++) {
                    if (espace.getListIndicateur().get(i).getId() == currentIndicateur.getId()) {
                        espace.getListIndicateur().get(i).setValue(String.valueOf(result));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newConstraintLayout.addView(editText);

        constraintSetPB.clone(newConstraintLayout);
        constraintSetPB.connect(editText.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM, 16);
        constraintSetPB.connect(editText.getId(), ConstraintSet.START, textView1.getId(), ConstraintSet.END, 0);
        constraintSetPB.applyTo(newConstraintLayout);

        // Create Objectif TextViex
        TextView textView2 = new TextView(getApplicationContext());
        textView2.setText("Objectif : " + objectif);
        textView2.setTextSize(15);
        textView2.setId(View.generateViewId());

        newConstraintLayout.addView(textView2);

        constraintSetPB.clone(newConstraintLayout);
        constraintSetPB.connect(textView2.getId(), ConstraintSet.TOP, progressBar.getId(), ConstraintSet.BOTTOM, 0);
        constraintSetPB.connect(textView2.getId(), ConstraintSet.END, progressBar.getId(), ConstraintSet.END, 0);
        constraintSetPB.applyTo(newConstraintLayout);

        cardView.addView(newConstraintLayout);
        constraintLayout.addView(cardView);

        SetCardViewConstraint(cardView);

        lastView = cardView;


    }
}
