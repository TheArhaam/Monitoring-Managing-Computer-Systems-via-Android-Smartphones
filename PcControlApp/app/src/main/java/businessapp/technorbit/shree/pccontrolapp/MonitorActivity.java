package businessapp.technorbit.shree.pccontrolapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import LibPack.MyImage;

public class MonitorActivity extends Activity {

    private TouchImageView image;
    public byte[] myImageArray;
    MyImage desktopSnapInfo;
    TextView textViewAbortInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitor);
        image = (TouchImageView) findViewById(R.id.img);
        textViewAbortInfo = (TextView) findViewById(R.id.textViewAbortInfo);
        textViewAbortInfo.setVisibility(View.GONE);
        image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {

            @Override
            public void onMove() {
                PointF point = image.getScrollPosition();
                RectF rect = image.getZoomedRect();
                float currentZoom = image.getCurrentZoom();
                boolean isZoomed = image.isZoomed();

            }
        });

        callAsynchronousTask();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ii);
        finish();
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

                            LongOperationMonitorPC performBackgroundTask = new LongOperationMonitorPC();
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

    private class LongOperationMonitorPC extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                System.out.println("Calling Servlet");
                String urlstr = "http://"+IpActivity.serverIP+":8084/WebServer/MonitorPcServlet";

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

                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                // send and serialize the object
                out.writeObject(MainActivity.selectedPcId);
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
                textViewAbortInfo.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            } else {
                image.setVisibility(View.VISIBLE);
                textViewAbortInfo.setVisibility(View.GONE);
                if (desktopSnapInfo.imageArray != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(desktopSnapInfo.imageArray, 0, desktopSnapInfo.imageArray.length);
                    image.setImageBitmap(bitmap);

                }
            }

        }
    }

}
