package businessapp.technorbit.shree.pccontrolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import LibPack.MyImage;

public class MainActivity extends Activity {


    ImageView[] pcImages;
    TextView[] pcId;
    MyImage allActivePcImages;
    public static String selectedPcId = "";
    Handler handler;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        pcImages = new ImageView[5];
        pcImages[0] = (ImageView) findViewById(R.id.imageView1);
        pcImages[1] = (ImageView) findViewById(R.id.imageView2);
        pcImages[2] = (ImageView) findViewById(R.id.imageView3);
        pcImages[3] = (ImageView) findViewById(R.id.imageView4);
        pcImages[4] = (ImageView) findViewById(R.id.imageView5);

        pcId = new TextView[5];
        pcId[0] = (TextView) findViewById(R.id.textView1);
        pcId[1] = (TextView) findViewById(R.id.textView2);
        pcId[2] = (TextView) findViewById(R.id.textView3);
        pcId[3] = (TextView) findViewById(R.id.textView4);
        pcId[4] = (TextView) findViewById(R.id.textView5);


        pcImages[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Clikced: 1", Toast.LENGTH_SHORT).show();
                selectedPcId = allActivePcImages.allPcIds.get(0);
                stop_Handler();
                show_Dialog();
            }
        });

        pcImages[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MainActivity.this, "Clikced: 2", Toast.LENGTH_SHORT).show();
                selectedPcId = allActivePcImages.allPcIds.get(1);
                stop_Handler();

                show_Dialog();
            }
        });

        pcImages[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPcId = allActivePcImages.allPcIds.get(2);
                stop_Handler();
//                Intent ii = new Intent(getApplicationContext(), MonitorActivity.class);
//                startActivity(ii);
//                finish();
                show_Dialog();
            }
        });

        pcImages[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPcId = allActivePcImages.allPcIds.get(3);
                stop_Handler();
//                Intent ii = new Intent(getApplicationContext(), MonitorActivity.class);
//                startActivity(ii);
//                finish();
                show_Dialog();
            }
        });

        pcImages[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPcId = allActivePcImages.allPcIds.get(4);
                stop_Handler();
//                Intent ii = new Intent(getApplicationContext(), MonitorActivity.class);
//                startActivity(ii);
//                finish();
                show_Dialog();
            }
        });


        callAsynchronousTask();


    }


    void show_Dialog() {
        String[] options = new String[2];
        options[0] = "Monitor View";
        options[1] = "Control View";


        ContextThemeWrapper ctw = new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Black);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                //Toast.makeText(ComplaintActivity.this, "Selected: " + which, Toast.LENGTH_SHORT).show();
                if (which == 0) {

                    try {
                        Intent ii = new Intent(getApplicationContext(), MonitorActivity.class);
                        startActivity(ii);
                        finish();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else if (which == 1) {
                    try {
                        Intent ii = new Intent(getApplicationContext(), ControlActivity.class);
                        startActivity(ii);
                        finish();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.show();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        callAsynchronousTask();
//    }

    void stop_Handler() {
        try {
            timer.cancel();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAsynchronousTask() {

        handler = new Handler();
        timer = new Timer();
        try {


            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {

                    handler.post(new Runnable() {
                        public void run() {
                            try {

                                LongOperation performBackgroundTask = new LongOperation();
                                // PerformBackgroundTask this class is the class that extends AsynchTask
                                performBackgroundTask.execute("");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    });


                }
            };
            timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }


    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                //allActivePcImages = null;
                System.out.println("Calling Servlet");
                String urlstr = "http://" + IpActivity.serverIP + ":8084/WebServer/ReadFeed";
                System.out.println("Ip Address:::::::::::::::::::::::::::::::::::::::::: " + urlstr);
                URL url = new URL(urlstr);
                URLConnection connection = url.openConnection();

                connection.setDoOutput(true);
                connection.setDoInput(true);

                // don't use a cached version of URL connection
                connection.setUseCaches(false);
                connection.setDefaultUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // specify the content type that binary data is sent
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                // System.out.println("Widht::::::::::::::: " + width + "   Height: " + height);


                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                // send and serialize the object
                out.writeObject("1");
                out.close();

                // define a new ObjectInputStream on the input stream
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                // receive and deserialize the object, note the cast
                allActivePcImages = (MyImage) in.readObject();
                in.close();


            } catch (Exception e) {
                System.out.println("Error: " + e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (allActivePcImages == null) {
                //Do nothing
                pcId[0].setText("No Active Pc in List...");
                for (int i = 0; i < 5; i++) {
                    pcImages[i].setVisibility(View.GONE);
                    if (i > 0) {
                        pcId[i].setVisibility(View.GONE);
                    }

                }
            } else {
                if (allActivePcImages.allPcIds.size() > 0) {
                    //System.out.println("Active PC:::::::::::::: " + allActivePcImages.allImages.size());
                    for (int i = 0; i < allActivePcImages.allPcIds.size(); i++) {
                        pcImages[i].setVisibility(View.VISIBLE);
                        pcId[i].setVisibility(View.VISIBLE);
                        //   System.out.println("Pc ID:::::::::::::::::::::::@@@  " + allActivePcImages.allPcIds.get(i));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(allActivePcImages.allImages.get(i).mybyteArray, 0, allActivePcImages.allImages.get(i).mybyteArray.length);
                        pcImages[i].setImageBitmap(bitmap);
                        pcId[i].setText("Pc ID: " + allActivePcImages.allPcIds.get(i));
                    }
                    int maxActivePC = (allActivePcImages.allPcIds.size());

                    for (int i = maxActivePC; i < 5; i++) {
                        pcImages[i].setVisibility(View.GONE);
                        pcId[i].setVisibility(View.GONE);
                    }
                } else {
                    pcId[0].setText("No Active Pc in List...");
                    for (int i = 0; i < 5; i++) {
                        pcImages[i].setVisibility(View.GONE);
                        if (i > 0) {
                            pcId[i].setVisibility(View.GONE);
                        }

                    }
                }

            }
        }
    }

}
