package com.example.hossam.teleprompter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hossam.teleprompter.helper.Helper;
import com.example.hossam.teleprompter.helper.RecyclerViewNewsAdabter;
import com.google.gson.Gson;
import com.xw.repo.BubbleSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TextRotation extends AppCompatActivity {



    EditText inputText;

    TextView movingHeader;
    ImageView newfeeds;
    LinearLayout editTextdata;
    Boolean externalsource = false ;

    RecyclerView recyclerView;
    int textsize=10,speed=10;
    View view;
    boolean settingDilog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_rotation);
        movingHeader=(TextView) findViewById(R.id.et_editText);
        inputText = (EditText) findViewById(R.id.input_text);
        newfeeds =(ImageView) findViewById(R.id.etnews_sprator);
        editTextdata =(LinearLayout) findViewById(R.id.linearLayout2);
        recyclerView = (RecyclerView)findViewById(R.id.textviewer);

        view   = new View(this);

        SharedPreferences prefs = getSharedPreferences("usersetting", MODE_PRIVATE);

           externalsource = prefs.getBoolean("externalsource",false);
            textsize = prefs.getInt("textsize", 10); //0 is the default value.
            speed = prefs.getInt("speed",10);

         //   startActivity(new Intent(this,MainActivity2.class));



    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//            Log.i("****","onRestoreInstanceStatecalled;");
//       textsize = savedInstanceState.getInt("textsize",10);
//       speed =savedInstanceState.getInt("speed",10);
//       externalsource = savedInstanceState.getBoolean("externalsource",false);
//
//    }

    @Override
    protected void onStop() {
        super.onStop();
        settingDilog = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = getSharedPreferences("usersetting", MODE_PRIVATE).edit();
        editor.putBoolean("externalsource",externalsource);
        editor.putInt("textsize", textsize);
        editor.putInt("speed", speed);
        editor.commit();



    }

    @Override
    protected void onStart() {
        super.onStart();

        settingDilog = true;
        if (externalsource)
        {
            //setRecyclerView(speed,textsize);
            startNewsservice(this,view);
        }
        else
        {
            rotateText(textsize,speed);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        // Handle item selection
        switch (item.getItemId()) {
                                    case R.id.setting: {
                                    createSettingDilaog();
                                       return true;
                                    }
                                        default: return super.onOptionsItemSelected(item);
                                    }


    }
    public void  createSettingDilaog()


    {
        settingDilog = false ;
        final BubbleSeekBar sbtextsize, sbspeed;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog, null);

        dialogBuilder.setView(dialogView).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TextRotation.this, "noting Updated", Toast.LENGTH_SHORT).show();
            }
        });
        Switch getNews = (Switch) dialogView.findViewById(R.id.get_news);

        sbtextsize= (BubbleSeekBar) dialogView.findViewById(R.id.sb_text_size);

        sbspeed=(BubbleSeekBar)dialogView.findViewById(R.id.sb_speed) ;
        sbspeed.setProgress(speed);
        sbtextsize.setProgress(textsize);


        setChangeSetting( getNews);

        dialogBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(TextRotation.this, "change apply", Toast.LENGTH_SHORT).show();
                textsize= sbtextsize.getProgress();
                speed= sbspeed.getProgress();
                settingDilog = true;

                if (externalsource)
                {

                    Timer timer = new Timer ();
                    TimerTask hourlyTask = new TimerTask () {
                        @Override
                        public void run () {
                            // your code here...
                            if(externalsource) {
                                startNewsservice(getApplicationContext(), view);
                            }
                        }
                    };

// schedule the task to run starting now and then every 10min
                    timer.schedule (hourlyTask, 0l, 10000*60);   // 1000*10*60 every 10 minute


                }
                else
                {
                    rotateText(textsize,speed);
                }
            }
        });



        dialogBuilder.setTitle("Setting");

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

/*
* this method is called to set  news from Api from in recycler view */
    public void setRecyclerView (final int speed, int textSize,ArrayList<String>data)
        {

            editTextdata.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            final RecyclerViewNewsAdabter gadbter;
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            gadbter  = new  RecyclerViewNewsAdabter(data/*newsDescription*/, textSize);
            recyclerView.setAdapter(gadbter);

            Thread mythread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (externalsource &&settingDilog) {
                        recyclerView.scrollToPosition(gadbter.getItemCount()-1);
                        for (int i = 0; i <= Helper.getScreenWidth(getWindowManager())*gadbter.getItemCount(); i = i + 10)

                        {

                            if (!settingDilog)
                            {
                                break;
                            }
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            final int finalI = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                 recyclerView.scrollBy(-speed, 5);


                                }
                            });
                        }
                  }
                }
            });

            mythread.start();
        }



        /*
        this method is called when you want to update text locallay in app
        it's take text size and speed from setting

         */
    public void rotateText(final int textSize, final int speed)
    {   final Thread mythread;

        editTextdata.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        movingHeader.setText("wellcome ENter your text here");
        movingHeader.setTranslationX(0);
        newfeeds.setTranslationX(0);



       mythread  = new Thread(new Runnable() {
            @Override
            public void run() {
                while ((!externalsource)&&settingDilog) {
                    final String text;
                    text = inputText.getText().toString();
                    for (int i = 0; i <=/* getScreenWidth()*/Helper.getScreenWidth(getWindowManager()); i =i+speed)

                    {
                        if (!settingDilog)
                        {
                            break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // movingHeader.scrollBy();
                                movingHeader.setTranslationX(finalI);

                                newfeeds.setTranslationX(finalI);
                                movingHeader.setTextSize(textSize);
                                movingHeader.setText(text);


                            }
                        });


                    }

                }
            }
        });

        mythread.start();

    }

            /*
            *this method is called when we want to set change from local news or exteral news
            * */

            public  void setChangeSetting(Switch getNews)
            {
                if (externalsource)
                {
                    getNews.setChecked(true);
                }

                else
                {
                    getNews.setChecked(false);
                }
                getNews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b)
                        {
                            externalsource = true ;

                        }
                        else
                        {
                            externalsource = false;

                        }
                    }
                });



            }




    public void startNewsservice(final Context context, View view)
    {
        final ArrayList<String> news ;
        news = new ArrayList<>();


        String url= "http://yo.yo100.me/api/news/list?selectedTab=topNews&providerId=10";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String  encodedSting= URLEncoder.encode(response,"ISO-8859-1");
                    response= URLDecoder.decode(encodedSting,"UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectNews = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObjectNews.getJSONArray("news");
                    for(int counter= 0; counter<jsonArray.length();counter++)
                    {
                        JSONObject currentobject = jsonArray.getJSONObject(counter);
                        news.add(currentobject.getString("shortDescription"));

                    }


                Gson gson = new Gson();

                com.example.hossam.teleprompter.helper.Response response1 =   gson.fromJson(response,com.example.hossam.teleprompter.helper.Response.class);


                Log.i("****",""+response1.data.news.get(0));




//

                    setRecyclerView(speed,textsize,news);



                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "something went wrong no internet connection", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }



//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("externalsource",externalsource);
//        outState.putInt("textsize", textsize );
//        outState.putInt("speed", speed );
//    }
}
