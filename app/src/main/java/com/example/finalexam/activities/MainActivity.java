package com.example.finalexam.activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalexam.R;
import com.example.finalexam.databinding.ActivityMainBinding;
import com.example.finalexam.lib.Utils;
import com.example.finalexam.model.RandomNumber;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final String key = "Number";

    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;

    private TextInputEditText etFrom, etTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Find the TextInputEditText by its ID
        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);

        // Initialize the RandomNumber object
        mRandomNumber = new RandomNumber();

        // Initialize history list
        initializeHistoryList(savedInstanceState, key);

        // Set the FAB onClickListener
        findViewById(R.id.fab).setOnClickListener(v -> {
            // Get the text from the EditText and convert it to a String
            String fromText = etFrom.getText().toString();
            String toText = etTo.getText().toString();

            // Convert the String to an int (or other number type)
            int fromNumber = 0;
            int toNumber = 0;

            // Handle possible empty input and parse the text to an integer
            try {
                fromNumber = Integer.parseInt(fromText);
                toNumber = Integer.parseInt(toText);
            } catch (NumberFormatException e) {
                // Handle error, maybe show a Toast message or log it
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            int finalFromNumber = fromNumber;
            int finalToNumber = toNumber;

            //set the random number range
            mRandomNumber.setFromTo(finalFromNumber, finalToNumber);

            //get the random number
            int rand = mRandomNumber.getCurrentRandomNumber();

            mNumberHistory.add(rand);

            TextView resultsTextView = findViewById(R.id.results);
            resultsTextView.setText(String.valueOf(rand));

            // You can perform your action here, using the parsed numbers
            Snackbar.make(v, "Random Number: " + rand, Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show();
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.show_history) {
            Utils.showInfoDialog (MainActivity.this,
                    "History", mNumberHistory.toString());
            return true;
        } else if (id == R.id.clear_history){
            mNumberHistory.clear();
            return true;
        } else if (id == R.id.about){
            Toast.makeText(this, getString(R.string.about_text), Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeHistoryList (Bundle savedInstanceState, String key)
    {
        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (key, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(key, mNumberHistory);

    }
}