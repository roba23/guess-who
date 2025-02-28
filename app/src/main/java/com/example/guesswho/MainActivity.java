package com.example.guesswho;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.color.utilities.Score;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    LinearLayout errorPage;
    ImageView celeberity;
    LinearLayout container;
    int chosen;
    int num;
    int score =0;
    String percentage;
    TextView scoreView;
    TextView percentView;
    LinearLayout scoreBadge;
    Set<String> asked = new HashSet<>();


    String[] artist = {"Johny Deep","Arnold Schwarzenegger","Jim Carrey","Robert Downey Jr", "Leonardo DiCaprio","Tom Cruise",
            "Emma Watson","Daniel Radcliffe","Chris Evans","Brad Pitt"
    };
    String[] urls = {
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Johnny_Depp_2020.jpg/330px-Johnny_Depp_2020.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Arnold_Schwarzenegger_by_Gage_Skidmore_4.jpg/800px-Arnold_Schwarzenegger_by_Gage_Skidmore_4.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Jim_Carrey_2008.jpg/330px-Jim_Carrey_2008.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Robert_Downey_Jr._2014_Comic-Con.jpg/800px-Robert_Downey_Jr._2014_Comic-Con.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/4/46/Leonardo_Dicaprio_Cannes_2019.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Tom_Cruise_by_Gage_Skidmore_2.jpg/800px-Tom_Cruise_by_Gage_Skidmore_2.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/7/7f/Emma_Watson_2013.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/DanielRadcliffe.jpg/800px-DanielRadcliffe.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/ChrisEvans2023.jpg/800px-ChrisEvans2023.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Brad_Pitt-69858.jpg/330px-Brad_Pitt-69858.jpg"
    };


    public void tryAgain(View view){
        recreate();

    }
    public void exitGame(View view){
        System.exit(0);
    }



//function to disable between answering one question and setting up another question
    public void buttonDisable(String state){
        container = findViewById(R.id.container);
        if(state == "off") {
            for (int i = 0; i < container.getChildCount(); i++) {
                Button myBtn = (Button) container.getChildAt(i);
                myBtn.setEnabled(false);
            }
        }
        else if(state == "on"){
            for (int i = 0; i < container.getChildCount(); i++) {
                Button myBtn = (Button) container.getChildAt(i);
                myBtn.setEnabled(true);
            }
        }
    }



    public void setImage(String address){
        DownloadImage task = new DownloadImage();

        Bitmap myBit = null;
        try {
            myBit = task.execute(address).get();
            celeberity = findViewById(R.id.celeberity);
            celeberity.setImageBitmap(myBit);
        } catch (ExecutionException e) {
            Log.i("Ethiopia", "Problem 1");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Log.i("Ethiopia","Problem 2");
            throw new RuntimeException(e);
        }








    }

    public void ask(){

            errorPage = findViewById(R.id.errorPage);
            errorPage.setVisibility(View.GONE);


            chosen = (int)(Math.random() * 10);

            //to check if the celeberity name has already been asked
            while(asked.contains(artist[chosen])){
                chosen = (int)(Math.random()*10);

        }
        asked.add(artist[chosen]);


        setImage(urls[chosen]);
       // List<View> myLIst = new ArrayList<>();
        container = findViewById(R.id.container);
        List<Integer> selected =  new ArrayList<>();
        int correctButton  = (int)(Math.random()*4);
        //set one of the four buttons with the correct answer at random
        Button correct = (Button)container.getChildAt(correctButton);
        correct.setText(artist[chosen]);
        selected.add(chosen);

        //set the buttons text with artist names with random celeberties e
        for(int i = 0; i < container.getChildCount(); i++){
            if(i == correctButton){
                continue;

            }
            //myLIst.add(container.getChildAt(i));
                 num = (int)(Math.random()* 10);
            while(selected.contains(num)){
                num = (int)(Math.random()*10);
            }

            /*Log.i("Ethiopia", "num now is " + selected.toString());
            Log.i("Ethiopia","value of i is = "+ String.valueOf(i));
            Log.i("Ethiopia", "value of artist on the button is " + artist[num]);
            */

            selected.add(num);

            Button mybtn  = (Button)container.getChildAt(i);
            container.setVisibility(View.VISIBLE);
            mybtn.setText(artist[num]);
            buttonDisable("on");

        }






    }
    public void answer(View view){
        container = findViewById(R.id.container);
        container.setVisibility(View.INVISIBLE);
        buttonDisable("off");

        if(((Button)view).getText() == artist[chosen]){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            score += 1;
        }
        else{
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
        }
        if(asked.size() >= 9)
        {
            Log.i("Ethiopia", "LoggedOut");

           // Toast.makeText(this, "Done!!", Toast.LENGTH_LONG).show();
            celeberity = findViewById(R.id.celeberity);
            celeberity.setVisibility(View.INVISIBLE);

            container = findViewById(R.id.container);
            container.setVisibility(View.INVISIBLE);

            percentage = String.valueOf(score * 10);
            percentView = findViewById(R.id.percent);
            percentView.setText( percentage+ "%");
            scoreView = findViewById(R.id.score);
            scoreView.setText(score + "/10");
            scoreBadge = findViewById(R.id.scoreBadge);
            scoreBadge.setVisibility(View.VISIBLE);


            new CountDownTimer(4000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    score = 0;
                    percentage = "";
                    System.exit(0);

                }
            }.start();


        }
        else {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    ask();

                }
            }.start();

        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap myBit = BitmapFactory.decodeStream(in);
                return myBit;
            } catch (MalformedURLException e) {
                Log.i("Ethiopia", "Connection problem 1");
                throw new RuntimeException(e);
            } catch (IOException e) {
                Log.i("Ethiopia", "Connection problem 2");
                errorPage = findViewById(R.id.errorPage);
                errorPage.setVisibility(View.VISIBLE);
                //throw new RuntimeException(e);
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        errorPage = findViewById(R.id.errorPage);
        errorPage.setVisibility(View.GONE);

        scoreBadge = findViewById(R.id.scoreBadge);
        scoreBadge.setVisibility(View.INVISIBLE);
        ask();

    }
}