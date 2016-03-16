package com.application.android.edthelper;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Notation extends AppCompatActivity {

    private int note = 20;
    private int oldNote = note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notation);

        EditText nameTexte = (EditText) findViewById(R.id.name);
        nameTexte.addTextChangedListener(watcher);

        CheckBox c = (CheckBox)findViewById(R.id.checkBoxLayout);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxWatcher);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
             }
        );

        c = (CheckBox)findViewById(R.id.checkBoxList);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxAnimation);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxActivites);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxBDD);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxFichier);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxDialogue);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxHTTP);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );

        c = (CheckBox)findViewById(R.id.checkBoxLibrairie);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                     note += 2;
                 else
                     note -= 2;
                 updateNote();
             }
         }
        );
    }

    public void updateNote()
    {
        TextView resultat = (TextView)findViewById(R.id.note);
        if (oldNote > note) {
            final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translation);
            resultat.startAnimation(animation1);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
            resultat.startAnimation(animation2);
        }
        resultat.setText("Note total : " + note);
        oldNote = note;
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EditText nameTexte = (EditText) findViewById(R.id.name);

            TextView resultatTexte = (TextView) findViewById(R.id.eleve);
            resultatTexte.setText("Veuillez notez l'élève " + nameTexte.getText());
            Snackbar.make(findViewById(android.R.id.content), "Mise à jour de l'élève", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
