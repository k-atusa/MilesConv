package com.example.ku7_milesconv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private EditText numInput;
    private TextView unitShow;
    private TextView resultText;
    private boolean isReverse = false;
    private NoteIO note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init page view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_main);
        note = new NoteIO(false, this);
        numInput = findViewById(R.id.num_input);
        unitShow = findViewById(R.id.unit_text);
        resultText = findViewById(R.id.result_text);

        // set listeners
        Button button11 = findViewById(R.id.button11);
        button11.setOnClickListener(v -> buttonAction(11));

        Button button12 = findViewById(R.id.button12);
        button12.setOnClickListener(v -> buttonAction(12));

        Button button13 = findViewById(R.id.button13);
        button13.setOnClickListener(v -> buttonAction(13));

        Button button14 = findViewById(R.id.button14);
        button14.setOnClickListener(v -> buttonAction(14));

        Button button21 = findViewById(R.id.button21);
        button21.setOnClickListener(v -> buttonAction(21));

        Button button22 = findViewById(R.id.button22);
        button22.setOnClickListener(v -> buttonAction(22));

        Button button23 = findViewById(R.id.button23);
        button23.setOnClickListener(v -> buttonAction(23));

        Button button24 = findViewById(R.id.button24);
        button24.setOnClickListener(v -> buttonAction(24));
    }

    // convert number or start note activity
    private void buttonAction(int buttonId) {
        String input = numInput.getText().toString();

        // start note activity
        if (note.password.equals(input)) {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);

        // reset internal data
        } else if ("000000000000".equals(input)) {
            File file = new File(this.getFilesDir(), "data.bin");
            file.delete();

        // unit conversion
        } else if (!input.isEmpty()) {
            double value = Double.parseDouble(input);
            double convConst = 1.0;
            String unitStart = "";
            String unitEnd = "";
            switch (buttonId) {
                case 11:
                    convConst = 2.54;
                    unitStart = "in";
                    unitEnd = "cm";
                    break;
                case 12:
                    convConst = 0.3048;
                    unitStart = "ft";
                    unitEnd = "m";
                    break;
                case 13:
                    convConst = 0.9144;
                    unitStart = "yd";
                    unitEnd = "m";
                    break;
                case 14:
                    convConst = 1.60934;
                    unitStart = "mi";
                    unitEnd = "km";
                    break;
                case 21:
                    convConst = 28.3495;
                    unitStart = "oz";
                    unitEnd = "g";
                    break;
                case 22:
                    convConst = 0.453592;
                    unitStart = "lb";
                    unitEnd = "kg";
                    break;
                case 23:
                    unitStart = "'F";
                    unitEnd = "'C";
                    if (isReverse) {
                        value = value * 9.0 / 5.0 + 32.0;
                    } else {
                        value = (value - 32.0) * 5.0 / 9.0;
                    }
                    break;
                case 24:
                    convConst = 3.78541;
                    unitStart = "gal";
                    unitEnd = "L";
                    break;
            }

            // calculate and show
            if (isReverse) {
                value = value / convConst;
                String temp = unitStart;
                unitStart = unitEnd;
                unitEnd = temp;
            } else {
                value = value * convConst;
            }
            isReverse = !isReverse;
            unitShow.setText("  " + unitStart);
            resultText.setText(String.format("%.3f", value) + " " + unitEnd);
        }
    }
}