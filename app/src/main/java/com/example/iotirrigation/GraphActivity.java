package com.example.iotirrigation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity {
    private GraphView graphView;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    double[] arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graphView = findViewById(R.id.idGraphView);
        arrayList = getIntent().getExtras().getDoubleArray("data");

        // Set up the graph
        graphView.setTitle("My Graph View");
        graphView.setTitleColor(R.color.black);
        graphView.setTitleTextSize(18);
        graphView.addSeries(series);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(arrayList.length); // Adjust this value as needed
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (!isValueX) {
                    return String.format("%.0fcm", value);
                }
                return super.formatLabel(value, isValueX);
            }
        });

        loadData();
    }

    void loadData() {
        Log.e("=======Data=====", arrayList.length + "");
        for (int i = 0; i < arrayList.length; i++) {
            try {
                series.appendData(new DataPoint(i, arrayList[i]), true, arrayList.length + 5);
                // No need to call series.notify(), graphView.addSeries(series) here
            } catch (Exception e) {
                Log.e("GraphActivity", "Error updating graph", e);
            }
        }

        // No need to call graphView.notify(), graphView.invalidate(), graphView.computeScroll() here
    }
}
