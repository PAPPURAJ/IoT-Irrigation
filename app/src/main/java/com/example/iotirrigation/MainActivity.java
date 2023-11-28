package com.example.iotirrigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4,waterTankLevelPb;
    private TextView sen1, sen2,sen3,sen4,fieldWaterPumpTv,tankWaterPumpTv,waterTankLevelTv;
    private ImageView tankWaterPumpIv, fieldWaterPumpIv;

    private double[] deviceValue;  //sen1:sen2:sen3:sen4:fieldPump
    private ArrayList<Double> arrayList1=new ArrayList();
    private ArrayList<Double> arrayList2=new ArrayList();
    private ArrayList<Double> arrayList3=new ArrayList();
    private ArrayList<Double> arrayList4=new ArrayList();

    int i=0,tankPump,waterLevel,fieldPump;

    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tankWaterPumpIv=findViewById(R.id.tankWaterPumpIv);
        tankWaterPumpTv=findViewById(R.id.tankWaterPumpTv);

        progressBar1=findViewById(R.id.progress_bar1);
        progressBar2=findViewById(R.id.progress_bar2);
        progressBar3=findViewById(R.id.progress_bar3);
        progressBar4=findViewById(R.id.progress_bar4);



        sen1=findViewById(R.id.senPer1Tv);
        sen2=findViewById(R.id.senPer2Tv);
        sen3=findViewById(R.id.senPer3Tv);
        sen4=findViewById(R.id.senPer4Tv);

        fieldWaterPumpIv=findViewById(R.id.fieldWaterPumpIv);
        fieldWaterPumpTv=findViewById(R.id.fieldWaterPumpTv);

        waterTankLevelTv=findViewById(R.id.tankWaterLevelTv);
        waterTankLevelPb=findViewById(R.id.tankWaterLevelPb);


        loadFire();





    }



    void loadFire(){


        databaseReference.child("Device").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String val=snapshot.getValue(String.class);
                // Toast.makeText(getApplicationContext(), val, Toast.LENGTH_SHORT).show();

                deviceValue= Arrays.stream(val.split(":")).mapToDouble(value -> Double.parseDouble(value)).toArray();

                arrayList1.add(deviceValue[0]);
                arrayList2.add(deviceValue[1]);
                arrayList3.add(deviceValue[2]);
                arrayList4.add(deviceValue[3]);
                fieldPump= (int) deviceValue[4];

                loadUI();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Pump'water").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String val=snapshot.getValue(String.class);
               int[]  pump_water= Arrays.stream(val.split("'")).mapToInt(Integer::parseInt).toArray();
               tankPump=pump_water[0];
               waterLevel=pump_water[1];
               loadUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    void loadUI(){
        try{

            fieldWaterPumpTv.setText("Field water pump is "+(fieldPump==1?"On":"Off"));
            fieldWaterPumpIv.setImageResource(fieldPump==1?R.drawable.pump_on:R.drawable.pump_off);

            progressBar1.setProgress((int) deviceValue[0]);
            progressBar2.setProgress((int) deviceValue[1]);
            progressBar3.setProgress((int) deviceValue[2]);
            progressBar4.setProgress((int) deviceValue[3]);
            sen1.setText(deviceValue[0]+"%");
            sen2.setText(deviceValue[1]+"%");
            sen3.setText(deviceValue[2]+"%");
            sen4.setText(deviceValue[3]+"%");


            waterTankLevelPb.setProgress(waterLevel);
            waterTankLevelTv.setText("Tank water :"+waterLevel+"%");
            tankWaterPumpTv.setText("Tank water pump is "+(tankPump==1?"On":"Off"));
            tankWaterPumpIv.setImageResource(tankPump==1?R.drawable.pump_on:R.drawable.pump_off);
        }catch (Exception e){

        }


//        String text="Moisture sensor1: "+deviceValue[0]+"%\nMoisture sensor2: "+deviceValue[1]+"%\nMoisture sensor3: "+deviceValue[2]+"%\nMoisture sensor4: "+deviceValue[3]+"%\nWater pump: "+(deviceValue[4]==1?"On":"Off")+"\n" +"Tank pump: "+(deviceValue[5]==1?"On":"Off")+"\nTank Status: "+(deviceValue[6]==1?"Load":"Empty")+"\n";
//        logTv.setText(text);
        //  sen1.setText(val+"%");
    }

    public void moisture1GraphClick(View view) {
        Intent intent=new Intent(this,GraphActivity.class);

        double[] d=new double[arrayList1.size()];
        for(int i=0;i<arrayList1.size();i++)
            d[i]=arrayList1.get(i);

        intent.putExtra("data",d);
        startActivity(intent);
    }

    public void moisture2GraphClick(View view) {
        Intent intent=new Intent(this,GraphActivity.class);

        double[] d=new double[arrayList2.size()];
        for(int i=0;i<arrayList2.size();i++)
            d[i]=arrayList2.get(i);

        intent.putExtra("data",d);
        startActivity(intent);
    }

    public void moisture3GraphClick(View view) {
        Intent intent=new Intent(this,GraphActivity.class);

        double[] d=new double[arrayList3.size()];
        for(int i=0;i<arrayList3.size();i++)
            d[i]=arrayList3.get(i);

        intent.putExtra("data",d);
        startActivity(intent);
    }

    public void moisture4GraphClick(View view) {
        Intent intent=new Intent(this,GraphActivity.class);

        double[] d=new double[arrayList4.size()];
        for(int i=0;i<arrayList4.size();i++)
            d[i]=arrayList4.get(i);

        intent.putExtra("data",d);
        startActivity(intent);
    }
}