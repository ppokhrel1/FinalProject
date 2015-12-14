package com.example.abhishek.final_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MyAndroidAppActivity extends Activity {

    private RadioGroup radioChoiceGroup;
    private RadioButton radioChoiceButton;
    private Button btnDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextt);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        radioChoiceGroup = (RadioGroup) findViewById(R.id.radioNext);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioChoiceGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioChoiceButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(MyAndroidAppActivity.this,
                        radioChoiceButton.getText(), Toast.LENGTH_SHORT).show();

            }

        });

    }
}