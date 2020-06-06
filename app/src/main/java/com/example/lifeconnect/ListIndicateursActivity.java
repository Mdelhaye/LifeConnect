package com.example.lifeconnect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.Indicateur;
import com.example.lifeconnect.model.Structure;

import java.util.ArrayList;

public class ListIndicateursActivity extends AppCompatActivity {

    private Structure structure;
    private Espace espace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_indicateurs);

        Intent intent = getIntent();
        structure = (Structure) intent.getSerializableExtra("Structure");
        espace = (Espace) intent.getSerializableExtra("Espace");

        final LinearLayout fLayout = refreshLinearLayout();

        Button addIndicateur = findViewById(R.id.addIndicateurButton);
        addIndicateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListIndicateursActivity.this, AddIndicateurActivity.class);
                intent.putExtra("Structure", structure);
                intent.putExtra("Espace", espace);
                startActivityForResult(intent, 1);
            }
        });

        Button removeIndicateur = findViewById(R.id.removeIndicateurButton);
        removeIndicateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Indicateur i = findIndicateur(fLayout, espace.getListIndicateur());
                espace.getListIndicateur().remove(i);
                if (updateStructure(structure, espace)) {
                    refreshLinearLayout();
                }
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Structure", structure);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                Object returnData = data.getSerializableExtra("Structure");
                if (returnData instanceof Structure) {
                    this.structure = (Structure) returnData;
                }
                returnData = data.getSerializableExtra("Espace");
                if (returnData instanceof Espace) {
                    this.espace = (Espace) returnData;
                }
                refreshLinearLayout();
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }

    private Indicateur findIndicateur(LinearLayout layout, ArrayList<Indicateur> indicateurs) {
        final int childCount = layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = layout.getChildAt(i);
            CardView cardView = (CardView) view;
            TextView textView = (TextView) cardView.getChildAt(0);
            if (textView.getTag() == "true") {
                for (int j = 0; j < indicateurs.size(); j++) {
                    if (indicateurs.get(j).getName() == textView.getText()) {
                        return indicateurs.get(j);
                    }
                }
            }
        }
        return null;
    }

    private LinearLayout refreshLinearLayout() {

        // Clear layout
        LinearLayout layout = findViewById(R.id.listIndicateurs);
        layout.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        final ArrayList<CardView> listCardView = new ArrayList<>();
        int goTo = 0;
        if (this.espace.getListIndicateur() != null) {
            goTo = this.espace.getListIndicateur().size();
        }

        for (int i = 0; i < goTo; i++) {
            final CardView cardView = new CardView(getApplicationContext());
            params.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(params);
            cardView.setContentPadding(16, 16 ,16 ,16);
            cardView.setId(R.id.newCardView);

            TextView textViewTitle = new TextView(getApplicationContext());
            textViewTitle.setLayoutParams(params);
            textViewTitle.setText(this.espace.getListIndicateur().get(i).getName());
            textViewTitle.setTextSize(20);
            textViewTitle.setId(R.id.newCardViewTitle);
            cardView.addView(textViewTitle);

//            TextView textViewNbIndicateur = new TextView(getApplicationContext());
//            params.setMargins(16, 100, 16, 16);
//            textViewNbIndicateur.setLayoutParams(params);
//
//            int numberOfIndicateurs = 0;
//            if (data.getEspaces().get(i).getListIndicateur() != null) {
//                numberOfIndicateurs = data.getEspaces().get(i).getListIndicateur().size();
//            }
//
//            textViewNbIndicateur.setText("Possède " + numberOfIndicateurs + " indicateur(s)");
//            textViewNbIndicateur.setTextSize(10);
//            textViewNbIndicateur.setId(R.id.newCardViewContent);
//            cardView.addView(textViewNbIndicateur);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < listCardView.size(); j++) {
                        listCardView.get(j).setBackgroundColor(Color.WHITE);
                        listCardView.get(j).getChildAt(0).setTag("false");
                    }
                    cardView.setBackgroundColor(Color.LTGRAY);
                    cardView.getChildAt(0).setTag("true");
                }
            });

            params.setMargins(16, 16, 16, 16);
            listCardView.add(cardView);
            layout.addView(cardView);
        }

        return layout;
    }

    private Boolean updateStructure(Structure structure, Espace espace) {
        for (int i = 0; i < structure.getEspaces().size(); i++) {
            if (structure.getEspaces().get(i).getId() == espace.getId()) {
                structure.getEspaces().get(i).setName(espace.getName());
                structure.getEspaces().get(i).setListIndicateur(espace.getListIndicateur());
                return true;
            }
        }
        return false;
    }

}