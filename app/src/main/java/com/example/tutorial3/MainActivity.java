package com.example.tutorial3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.Random;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    LineChart mpLineChart;
    int counter = 1;
    int val = 40;
    Random gauss = new Random();
    double val2 = gauss.nextGaussian();
    private Handler mHandlar = new Handler();  //Handlar is used for delay definition in the loop


    public MainActivity() throws FileNotFoundException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }


        mpLineChart = (LineChart) findViewById(R.id.line_chart);
        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(), "Data Set 1");
        LineDataSet lineDataSet2 =  new LineDataSet(dataValues1(), "Data Set 2");
        lineDataSet2.setColor(Color.rgb(0, 255, 0));
        lineDataSet2.setCircleColor(Color.rgb(0, 255, 0));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();

        Button buttonClear = (Button) findViewById(R.id.button1);
        Button buttonCsvShow = (Button) findViewById(R.id.button2);

        buttonCsvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoadCSV();

            }
        });


        LineDataSet finalLineDataSet1 = lineDataSet1;
        LineDataSet finalLineDataSet2 = lineDataSet2;

        Runnable DataUpdate = new Runnable() {
            @Override
            public void run() {

                data.addEntry(new Entry(counter, val), 0);
                data.addEntry(new Entry(counter,(float) val2),1);
                finalLineDataSet1.notifyDataSetChanged(); // let the data know a dataSet changed
                finalLineDataSet2.notifyDataSetChanged(); // let the data know a dataSet changed
                mpLineChart.notifyDataSetChanged(); // let the chart know it's data changed
                mpLineChart.invalidate(); // refresh
                val = (int) (Math.random() * 80);
                val2 = gauss.nextGaussian();

                saveToCsv("/sdcard/csv_dir/", String.valueOf(counter), String.valueOf(val), false);
                saveToCsv("/sdcard/csv_dir/", String.valueOf(counter), String.valueOf(val2), true);

                counter += 1;
                mHandlar.postDelayed(this, 500);


            }
        };

        buttonClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
                LineData data = mpLineChart.getData();
                ILineDataSet set = data.getDataSetByIndex(0);
                ILineDataSet set2 = data.getDataSetByIndex(1);
                data.getDataSetByIndex(0);
                data.getDataSetByIndex(1);
                while (set.removeLast()) {}
                while (set2.removeLast()) {}
                val = 40;
                val2 = gauss.nextGaussian();
                counter = 1;

            }
        });


        mHandlar.postDelayed(DataUpdate, 500);
    }


    private ArrayList<Entry> dataValues1() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0, 0));
        return dataVals;
    }

    private void saveToCsv(String path, String str1, String str2, boolean isNormal) {
        try {
            File file = new File(path);
            file.mkdirs();
            String csv;
            if (isNormal) {
                csv = path + "data2.csv";
            }
            else {
                csv = path + "data.csv";
            }
//            String csv = path + "data.csv";
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv, true));
            String row[] = new String[]{str1, str2};
            csvWriter.writeNext(row);
            csvWriter.close();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    private void OpenLoadCSV() {
        Intent intent = new Intent(this, LoadCSV.class);
        startActivity(intent);
    }


}
