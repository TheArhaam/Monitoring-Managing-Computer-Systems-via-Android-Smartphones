package businessapp.technorbit.shree.pccontrolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IpActivity extends AppCompatActivity {

    public static String serverIP="";
    Button btnSetIp;
    EditText txtServerIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        txtServerIp = (EditText) findViewById(R.id.editTextIP);
        btnSetIp = (Button) findViewById(R.id.btnSetIp);

        btnSetIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtServerIp.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Server Ip", Toast.LENGTH_SHORT).show();
                } else {
                    serverIP = txtServerIp.getText().toString();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }

}
