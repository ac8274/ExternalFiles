package com.example.externalfiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    int REQUEST_CODE_PERMISSION = 1;
    private final String FILENAME = "inttest.txt";
    EditText Text_Input;
    TextView textView;
    Button Save_Button;
    Button Reset_Button;
    Button Exit_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Text_Input = findViewById(R.id.Text_Input);
        textView = findViewById(R.id.textView2);
        Save_Button = findViewById(R.id.Save_Button);
        Reset_Button = findViewById(R.id.Reset_Button);
        Exit_Button = findViewById(R.id.Exit_Button);
        textView.setText(file_exists());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Credits) {
            Intent si = new Intent(this, Credits.class);
            startActivity(si);
        }
        return true;
    }
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission to access external storage granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
            }
        }
    }


    public String file_exists() {
        if(!checkPermission())
        {
            requestPermission();
        }
        if(checkPermission() && isExternalStorageAvailable()) {
            File file = new File(Environment.getExternalStorageDirectory(), FILENAME);
            if (file.exists()) {
                return Extenal_reader();
            } else {
                External_writer("");
            }
        }
        return "";
    }

    public void Save_Internal_File(View view) {
        External_writer(Extenal_reader() + Text_Input.getText().toString());
        textView.setText(Extenal_reader()+"");
    }


    public void Reset_Internal_File(View view) {
        External_writer("");
        textView.setText("");
    }

    public void Exit_func(View view) {
        External_writer(Extenal_reader() + Text_Input.getText().toString());
        System.exit(1);
    }

    public String Extenal_reader() {
        StringBuilder sB = new StringBuilder();
        sB.append("");
        if(checkPermission() && isExternalStorageAvailable()) {
            try {
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileReader reader = new FileReader(file);
                BufferedReader bR = new BufferedReader(reader);
                String line = bR.readLine();
                while (line != null) {
                    sB.append(line + '\n');
                    line = bR.readLine();
                }
                bR.close();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sB.toString();
    }

    public void External_writer(String new_line) {
        if (checkPermission() && isExternalStorageAvailable()) {
            try {
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(new_line);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
