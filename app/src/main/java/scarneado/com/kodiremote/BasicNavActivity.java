package scarneado.com.kodiremote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class BasicNavActivity extends AppCompatActivity {

    private String ipAddress;
    private String httpURL;
    private Button selectedButton, upButton, downButton, leftButton, rightButton,
    backButton, homeButton, infoButton;
    private RequestQueue queue;
    private int requestId;
    private JSONObject jsonReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_nav);

        //Initiates request queue
        queue = RequestSender.getRequestQueue(this.getApplicationContext());

        //Initialize buttons
        selectedButton = (Button) findViewById(R.id.select_button);
        upButton = (Button) findViewById(R.id.up_button);
        downButton = (Button) findViewById(R.id.down_button);
        leftButton = (Button) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.right_button);
        backButton = (Button) findViewById(R.id.back_button);
        infoButton = (Button) findViewById(R.id.info_button);
        homeButton = (Button) findViewById(R.id.home_button);

        //Get calling intent
        Intent callingActivity = getIntent();
        ipAddress = getIntent().getStringExtra("ipAddress");

        //Create url string
        httpURL = "http://" + ipAddress + ":8080/jsonrpc";
        requestId = 0;
        jsonReq = null;

        //Test IP address capture
        Toast.makeText(this, httpURL, Toast.LENGTH_SHORT).show();
    }

    public void clickPlayPause(View view) {

        //Only handles single player, will need to use
        //"Player.GetActivePlayers" to handle future use cases
        int playerId = 1;
        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Player.PlayPause", playerId, requestId);
        sendRequest(jsonReq);
    }

    public void clickSelect(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Select", requestId);
        sendRequest(jsonReq);
    }

    public void clickBack(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Back", requestId);
        sendRequest(jsonReq);
    }

    public void clickHome(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Home", requestId);
        sendRequest(jsonReq);
    }

    public void clickInfo(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Info", requestId);
        sendRequest(jsonReq);
    }

    public void clickUp(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Up", requestId);
        sendRequest(jsonReq);
    }

    public void clickDown(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Down", requestId);
        sendRequest(jsonReq);
    }

    public void clickLeft(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Left", requestId);
        sendRequest(jsonReq);
    }

    public void clickRight(View view) {

        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.Right", requestId);
        sendRequest(jsonReq);
    }

    public void clickAddons(View view) {
        //Create intent for addons view
        Intent addonsActivity = new Intent(this, AddonsActivity.class);
        addonsActivity.putExtra("url", httpURL);
        addonsActivity.putExtra("reqID", requestId);
        addonsActivity.putExtra("ip", ipAddress);

        startActivity(addonsActivity);
    }

    private void sendRequest(JSONObject jsonReq){

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST, httpURL, jsonReq, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(jsonRequest);
    }


    public void clickPower(View view) {
        //Verify user wants to power off Kodi
        AlertDialog.Builder builder = new AlertDialog.Builder(BasicNavActivity.this);

        builder.setMessage(R.string.power_off_message);

        builder.setPositiveButton(R.string.power_off_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Send Kodi Shutdown request
                requestId++;

                jsonReq = JSONBuilder.createJSONObject("System.Shutdown", requestId);
                sendRequest(jsonReq);
            }
        });

        builder.setNegativeButton(R.string.power_off_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dialog Cancelled
            }
        });

        //Create and display dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clickMenu(View view) {
        requestId++;

        jsonReq = JSONBuilder.createJSONObject("Input.ContextMenu", requestId);
        sendRequest(jsonReq);
    }
}
