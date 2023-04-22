package com.example.tutorial3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import java.util.List;

import android.graphics.Color;


public class LoadCSV extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);
        Button BackButton = (Button) findViewById(R.id.button_back);
        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);

        ArrayList<String[]> csvData = new ArrayList<>();
        ArrayList<String[]> csvData2 = new ArrayList<>();

        csvData = CsvRead("/sdcard/csv_dir/data.csv");
        csvData2 = CsvRead("/sdcard/csv_dir/data2.csv");
        LineDataSet lineDataSet1 = new LineDataSet(DataValues(csvData, false),"Data Set 1");
        LineDataSet lineDataSet2 = new LineDataSet(DataValues(csvData2, true),"Data Set 2");
        lineDataSet2.setColor(Color.rgb(0, 255, 0));
        lineDataSet2.setCircleColor(Color.rgb(0, 255, 0));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData data = new LineData(dataSets);
//        LineData data2 = new LineData(dataSets.get(1));
        lineChart.setData(data);
//        lineChart.setData(data2);
        lineChart.invalidate();




        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickBack();
            }
        });
    }

    private void ClickBack(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private ArrayList<String[]> CsvRead(String path){
        ArrayList<String[]> CsvData = new ArrayList<>();
        try {
            File file = new File(path);
            CSVReader reader = new CSVReader(new FileReader(file));
            String[]nextline;
            while((nextline = reader.readNext())!= null){
                if(nextline != null){
                    CsvData.add(nextline);

                }
            }

        }catch (Exception e){}
    return CsvData;
    }

    private ArrayList<Entry> DataValues(ArrayList<String[]> csvData, boolean isNormal){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        for (int i = 0; i < csvData.size(); i++){
            if (isNormal) {
                dataVals.add(new Entry(i, Float.parseFloat(csvData.get(i)[1])));
            }
            else {
                dataVals.add(new Entry(i, Integer.parseInt(csvData.get(i)[1])));
            }

        }
            return dataVals;
    }

}