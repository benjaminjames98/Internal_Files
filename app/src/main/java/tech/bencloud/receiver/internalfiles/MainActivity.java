package tech.bencloud.receiver.internalfiles;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String FILENAME = "MyInternalFile";
    final String NEW_LINE = System.lineSeparator();

    private String faveColour;
    private Integer faveNumber;
    private Boolean silentMode;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readInternalFile();
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeInternalFile();
    }

    public void writeInternalFile() {
        // Update our preference properties with the latest values the user has entered into the UI
        EditText tempET = findViewById(R.id.faveColourET);
        faveColour = tempET.getText().toString();
        tempET = findViewById(R.id.faveNumberET);
        String faveNumberString = tempET.getText().toString();
        faveNumberString = faveNumberString.equals("") ? "0" : faveNumberString;
        faveNumber = Integer.valueOf(faveNumberString);
        Log.d(TAG, faveNumber.toString());
        CheckBox cb = findViewById(R.id.silentModeCB);
        silentMode = cb.isChecked();

        // Open via "try-with-resources" which auto-closes our FileOutputStream (saving contents)
        try (FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            // Write the string to the file using the getBytes() method
            fos.write(faveColour.getBytes());
            fos.write(NEW_LINE.getBytes());

            fos.write(faveNumber.toString().getBytes());
            fos.write(NEW_LINE.getBytes());

            fos.write(silentMode.toString().getBytes());
            fos.write(NEW_LINE.getBytes());
        } catch (IOException fnfe) {
            Log.e(TAG, Objects.requireNonNull(fnfe.getMessage()));
        }
    }

    public void readInternalFile() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)))) {
            // We know the format of the file is 3 strings, so we can work with that assumption
            faveColour = br.readLine();

            String temp = br.readLine();
            faveNumber = Integer.valueOf(temp);

            temp = br.readLine();
            silentMode = Boolean.valueOf(temp);

            // Assuming we were able to read file okay, then update layout with the file details
            EditText tempET = findViewById(R.id.faveColourET);
            tempET.setText(faveColour);
            tempET = findViewById(R.id.faveNumberET);
            tempET.setText(String.format("%s", faveNumber.toString()));
            CheckBox cb = findViewById(R.id.silentModeCB);
            cb.setChecked(silentMode);
        } catch (IOException fnfe) {
            Log.e(TAG, Objects.requireNonNull(fnfe.getMessage()));
        }

    }

}