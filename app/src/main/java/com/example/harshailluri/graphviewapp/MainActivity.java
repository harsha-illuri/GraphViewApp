package com.example.harshailluri.graphviewapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String[] v_labels= new String[10];
    String[] h_labels= new String[10];
    float[] values= new float[20];
    LinearLayout graph;
    GraphView g;
    Thread threadFunc;
    Boolean flag = false;
    Boolean flush = true;

//    Handler handler = new Handler(){
//        @Override
//        public void updateGraph(Message m){
//            for (int i = 0; i < 20; i++) {
//                values[i] = (float) Math.ceil(Math.random() * 180);
//            }
//            g.invalidate();
//            g.setValues(values);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 10; i++){
            v_labels[i]=String.valueOf((i * 10));
        }

        for (int i = 0; i < 10; i++) {
            h_labels[i]=String.valueOf((9-i) * 20);
        }
        g = new GraphView(MainActivity.this, values, "Patient Heart Beat", v_labels, h_labels, GraphView.LINE);
        graph= findViewById(R.id.graphSpace);
        graph.addView(g);

        threadFunc = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        while (flag){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (flush == true){
                                        flush = false;
                                        for (int i = 0; i < 20; i++) {
                                            values[i] = (float) Math.ceil(Math.random() * 180);
                                        }
                                        g.invalidate();
                                        g.setValues(values);
                                    }else{
                                        for (int i = 0; i < 19; i++) {
                                            values[i] = values[i+1];
                                        }
                                        values[19] = (float) Math.ceil(Math.random() * 180);
                                        g.invalidate();
                                        g.setValues(values);
                                    }
                                    // code responsible for changing the UI

                                }
                            });
                            Thread.sleep(1000);
                        }
                    }



                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }




            }
        });


    }

    public void startGraph(View view){
//        flag = true;
//        threadFunc.start();
        if (flag == false && !threadFunc.isAlive()){
            flag = true;
            Toast.makeText(MainActivity.this, "Start Graph", Toast.LENGTH_SHORT).show();
            threadFunc.start();
        }
        else if (threadFunc.isAlive()){
            flag = true;
            Toast.makeText(MainActivity.this, "Resume Graph", Toast.LENGTH_SHORT).show();
        }

    }



//    public void startGraph(View view){
//        for (int i = 0; i < 20; i++) {
//            values[i] = (float) Math.ceil(Math.random() * 180);
//        }
//        g.invalidate();
//        g.setValues(values);
//    }

    public void stopGraph(View view){
        flag = false;
        Toast.makeText(MainActivity.this, "Stopping Graph", Toast.LENGTH_SHORT).show();
        for (int i = 0; i < 20; i++) {
            values[i] = 0;
        }
        g.invalidate();
        g.setValues(values);
    }

}
