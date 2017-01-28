package scarneado.com.kodiremote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class HostActivity extends AppCompatActivity {

    //Define Variables
    private String ipAddress;
    private EditText ipField;
    private Button connectButton;
    private TextView badIPText;
    private String httpURL;
    private RequestQueue queue;
    private JSONObject jObj;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        //Initialize Variable
        ipField = (EditText) findViewById(R.id.ip_text_field);
        connectButton = (Button) findViewById(R.id.button);
        badIPText = (TextView) findViewById(R.id.bad_IP_text_view);
        queue = RequestSender.getRequestQueue(this.getApplicationContext());
        jObj = null;
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //Check for existing IP
        ipAddress = sharedPref.getString("ip", "");

        //If IP retrieved from shared preferences, send to validation
        if(ipAddress != ""){
            validateIp();
        }
    }

    //Parses current IP address variable into httpURL string and sends
    //request to server for validation.
    public void validateIp(){

        httpURL = "http://" + ipAddress + ":8080/jsonrpc";
        jObj = JSONBuilder.createJSONObject("JSONRPC.Version", 0);
        sendRequest(jObj);
    }

    //The testIP is used for manually entering an IP if one cannot be found
    public void newIP(View view) {
        //Get IP Address entered by user and sends for validation
        ipAddress = ipField.getText().toString();
        validateIp();
    }

    //Transition to remote screen
    public void goToRemote(){
        //Create intent for basic nav screen
        Intent getBasicNavIntent = new Intent(this, BasicNavActivity.class);
        getBasicNavIntent.putExtra("ipAddress", ipAddress);

        //Open basic nav screen
        startActivity(getBasicNavIntent);
    }

    //Sends JSONRPC version request to Kodi server. Upon successful
    //validation, moves app to basic navigation activity
    private void sendRequest(JSONObject jsonReq){

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST, httpURL, jsonReq, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                //Verify valid response is received from server
                try {
                    if(response.getJSONObject("result").has("version")){
                        //Save valid IP in shared preferences
                        badIPText.setVisibility(View.INVISIBLE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ip", ipAddress);
                        editor.commit();
                        goToRemote();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                badIPText.setVisibility(View.VISIBLE);
                System.out.println("Bad IP Bro");
                System.out.println(error.toString());
            }
        });

        queue.add(jsonRequest);
    }
}
