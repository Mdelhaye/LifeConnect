package com.example.lifeconnect.ui.listEspaces;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.lifeconnect.JavaClass.DialogNewEspace;
import com.example.lifeconnect.JavaClass.JsonOperation;
import com.example.lifeconnect.ListIndicateursActivity;
import com.example.lifeconnect.MenuActivity;
import com.example.lifeconnect.R;
import com.example.lifeconnect.model.Espace;
import com.example.lifeconnect.model.LocalStructures;
import com.example.lifeconnect.model.Structure;
import com.example.lifeconnect.model.User;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ListEspacesFragment extends Fragment {

    private ListEspacesViewModel listEspacesViewModel;

    private static final String FILENAME = "StructureDesEspaces.json";
    private static final JsonOperation jsonOp = new JsonOperation();

    private LocalStructures localStructures;
    private Structure structure;
    private View root;

    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listEspacesViewModel =
                ViewModelProviders.of(this).get(ListEspacesViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_list_espaces, container, false);
        this.root = root;

        // Get User settings
        MenuActivity menuActivity = (MenuActivity) getActivity();
        user = menuActivity.getUser();

        localStructures = (LocalStructures) jsonOp.getObjectFromJson(getContext(), FILENAME, 0);
        if (localStructures != null) {
            if (localStructures.getStructures() != null) {
                for (Structure structure_json : localStructures.getStructures()) {
                    if (structure_json.getUser() == user.getId()) {
                        structure = structure_json;
                    }
                }
            }
        }
        if (structure == null) {
            structure = new Structure(user.getId());
            structure.setEspaces(new ArrayList<Espace>());
        }

        final LinearLayout fLayout = refreshLinearLayout();

        Button addEspace = root.findViewById(R.id.addEspaceButton);
        addEspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        Button removeEspace = root.findViewById(R.id.removeEspaceButton);
        removeEspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Espace e = findEspace(fLayout, structure.getEspaces());
                structure.getEspaces().remove(e);

                if (localStructures.getStructures() == null) {
                    localStructures.setStructures(new ArrayList<Structure>());
                    localStructures.getStructures().add(structure);
                }

                for (Structure structure_local : localStructures.getStructures()) {
                    if (structure_local.getUser() == structure.getUser()) {
                        structure_local.setEspaces(structure.getEspaces());
                    }
                }

                jsonOp.SaveJson(getContext(), FILENAME, localStructures);
                refreshLinearLayout();

            }
        });

        Button modifyEspace = root.findViewById(R.id.modifyEspaceButton);
        modifyEspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Espace e = findEspace(fLayout, structure.getEspaces());
                if (structure != null && e != null) {
                    Intent intent = new Intent(getActivity(), ListIndicateursActivity.class);
                    intent.putExtra("Structure", structure);
                    intent.putExtra("Espace", e);
                    startActivityForResult(intent, 1);
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Object returnData = data.getSerializableExtra("Structure");
                if (returnData instanceof Structure) {
                    this.structure = (Structure) returnData;
                }

                localStructures = (LocalStructures) jsonOp.getObjectFromJson(getContext(), FILENAME, 0);
                for (Structure structure_local : localStructures.getStructures()) {
                    if (structure_local.getUser() == structure.getUser()) {
                        structure_local.setEspaces(structure.getEspaces());
                    }
                }

                jsonOp.SaveJson(getContext(), FILENAME, localStructures);
                refreshLinearLayout();
            }
            if (resultCode == RESULT_CANCELED) {
                //Do nothing?
            }
        }
    }

    private Espace findEspace(LinearLayout layout, ArrayList<Espace> espaces) {
        final int childCount = layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = layout.getChildAt(i);
            CardView cardView = (CardView) view;
            TextView textView = (TextView) cardView.getChildAt(0);
            if (textView.getTag() == "true") {
                for (int j = 0; j < espaces.size(); j++) {
                    if (espaces.get(j).getName() == textView.getText()) {
                        return espaces.get(j);
                    }
                }
            }
        }
        return null;
    }

    public LinearLayout refreshLinearLayout() {

        // Clear layout
        LinearLayout layout = root.findViewById(R.id.listEspaces);
        layout.removeAllViews();

        localStructures = (LocalStructures) jsonOp.getObjectFromJson(getContext(), FILENAME, 0);
        if (localStructures != null) {
            if (localStructures.getStructures() != null) {
                for (Structure structure_json : localStructures.getStructures()) {
                    if (structure_json.getUser() == user.getId()) {
                        structure = structure_json;
                    }
                }
            }
        }
        if (structure == null) {
            structure = new Structure(user.getId());
            structure.setEspaces(new ArrayList<Espace>());
        }

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        );

        final ArrayList<CardView> listCardView = new ArrayList<>();
        for (int i = 0; i < structure.getEspaces().size(); i++) {
            final CardView cardView = new CardView(getContext());
            params.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(params);
            cardView.setContentPadding(16, 16, 16, 16);
            cardView.setId(R.id.newCardView);

            TextView textViewTitle = new TextView(getContext());
            textViewTitle.setLayoutParams(params);
            textViewTitle.setText(structure.getEspaces().get(i).getName());
            textViewTitle.setTextSize(20);
            textViewTitle.setId(R.id.newCardViewTitle);
            cardView.addView(textViewTitle);

            TextView textViewNbIndicateur = new TextView(getContext());
            params.setMargins(16, 100, 16, 16);
            textViewNbIndicateur.setLayoutParams(params);

            int numberOfIndicateurs = 0;
            if (structure.getEspaces().get(i).getListIndicateur() != null) {
                numberOfIndicateurs = structure.getEspaces().get(i).getListIndicateur().size();
            }

            textViewNbIndicateur.setText("Possède " + numberOfIndicateurs + " indicateur(s)");
            textViewNbIndicateur.setTextSize(10);
            textViewNbIndicateur.setId(R.id.newCardViewContent);
            cardView.addView(textViewNbIndicateur);

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

    private void createDialog() {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (localStructures == null) localStructures = new LocalStructures(new ArrayList<Structure>());
            DialogNewEspace dialogNewEspace = new DialogNewEspace(localStructures, structure, this);
            dialogNewEspace.show(fragmentManager, "fragment_alert");
        }
    }
}