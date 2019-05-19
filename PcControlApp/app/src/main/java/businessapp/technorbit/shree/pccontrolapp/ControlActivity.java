package businessapp.technorbit.shree.pccontrolapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import LibPack.MyImage;

public class ControlActivity extends Activity {

    ImageView imageViewScreen;
    MyImage desktopSnapInfo;
    TextView textViewControlAbortInfo;
    MyImage toServer;
    float posX, posY;
    int cmdType;
    int myWidth = 0, myHeight = 0;

    ImageView btUp, btDown, btRight, btLeft,btcontrols,bt_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_control);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        myHeight = displaymetrics.heightPixels;
        myWidth = displaymetrics.widthPixels;
        posX = -1;
        posY = -1;
        cmdType = -1;

        imageViewScreen = (ImageView) findViewById(R.id.imageViewControlScreen);
        textViewControlAbortInfo = (TextView) findViewById(R.id.textViewControlAbortInfo);
        btUp = (ImageView) findViewById(R.id.bt_up);
        btDown = (ImageView) findViewById(R.id.bt_down);
        btRight = (ImageView) findViewById(R.id.bt_right);
        btLeft = (ImageView) findViewById(R.id.bt_left);
        btcontrols = (ImageView) findViewById(R.id.bt_controls);
        bt_list = (ImageView) findViewById(R.id.bt_list);
        toServer = new MyImage();
        callAsynchronousTask();


        imageViewScreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                posX = event.getX();
                posY = event.getY();
                cmdType = 1;//1 -> Move x,y cursor Command
                // showPopup(textViewControlAbortInfo);
                return false;
            }
        });
        final LinearLayout layoutcontrol = (LinearLayout) findViewById(R.id.control_layout);
        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(textViewControlAbortInfo);
            }
        });
        btcontrols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutcontrol.getVisibility() == View.VISIBLE) {
                    layoutcontrol.setVisibility(View.GONE);
                } else {
                    layoutcontrol.setVisibility(View.VISIBLE);
                }
            }
        });
        btRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posX = posX + 7;
                cmdType = 1;//1 -> Move x,y cursor Command
            }
        });
        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posX = posX - 7;
                cmdType = 1;//1 -> Move x,y cursor Command
            }
        });
        btUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posY = posY - 7;
                cmdType = 1;//1 -> Move x,y cursor Command
            }
        });
        btDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                posY = posY + 7;
                cmdType = 1;//1 -> Move x,y cursor Command
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Left Click")) {
                    cmdType = 2;
                } else if (item.getTitle().equals("Right Click")) {
                    cmdType = 3;
                } else if (item.getTitle().equals("Double Click")) {
                  //  Toast.makeText(getApplicationContext(), "Double Click", Toast.LENGTH_SHORT).show();
                    cmdType = 4;
                }
                return true;
            }


        });
        popup.show();
    }


    private void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        try {

                            LongOperationControlPC performBackgroundTask = new LongOperationControlPC();
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ii);
        finish();
    }

    private class LongOperationControlPC extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("Calling Servlet");
                String urlstr = "http://" + IpActivity.serverIP + ":8084/WebServer/ControlPcServlet";

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
                toServer.pcID = MainActivity.selectedPcId;
                toServer.x = posX;
                toServer.y = posY;
                toServer.clickCmd = cmdType;
                cmdType = -1;
                toServer.mobileWidth = myWidth;
                toServer.mobileHeight = myHeight;
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                // send and serialize the object
                out.writeObject(toServer);
                out.close();

                // define a new ObjectInputStream on the input stream
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                // receive and deserialize the object, note the cast
                desktopSnapInfo = (MyImage) in.readObject();
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
            if (desktopSnapInfo.hostAvailableStatus == -1) {
                //System.out.println("In Host unavailable.........");
                textViewControlAbortInfo.setVisibility(View.VISIBLE);
                imageViewScreen.setVisibility(View.GONE);
            } else {
                imageViewScreen.setVisibility(View.VISIBLE);
                textViewControlAbortInfo.setVisibility(View.GONE);
                if (desktopSnapInfo.imageArray != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(desktopSnapInfo.imageArray, 0, desktopSnapInfo.imageArray.length);
                    imageViewScreen.setImageBitmap(bitmap);

                }
            }
        }
    }

}
