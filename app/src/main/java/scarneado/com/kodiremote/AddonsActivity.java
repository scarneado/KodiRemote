package scarneado.com.kodiremote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddonsActivity extends AppCompatActivity {

    //Declare local variables
    private RequestQueue queue;
    private Intent callingIntent;
    private JSONObject jObj;
    private int requestID;
    private String httpURL;
    private ListView listView;
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addons);

        //Get callingIntent and passed IP
        callingIntent = getIntent();
        httpURL = callingIntent.getStringExtra("url");
        requestID = callingIntent.getIntExtra("reqID", 0);
        ipAddress = callingIntent.getStringExtra("ip");

        listView = (ListView) findViewById(R.id.addons_view);

        queue = RequestSender.getRequestQueue(this.getApplicationContext());

        getAddonList();
    }

    //Build JSON object for RPC and send to request method
    private void getAddonList(){
        jObj = JSONBuilder.createJSONObject("Addons.GetAddons", "type", "xbmc.python.pluginsource", requestID);
        requestID++;
        System.out.println(jObj);

        sendAddonRequest(jObj);
    }

    private void sendAddonRequest(JSONObject jsonReq){
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST, httpURL, jsonReq, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                //Verify valid response is received from server
                try {
                    JSONArray jArray = response.getJSONObject("result").getJSONArray("addons");
                    System.out.println(jArray.getJSONObject(0).get("addonid"));
                    createListView(jArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(jsonRequest);
    }

    private void createListView(JSONArray jArray) throws JSONException {
        ArrayList<String> addons =  new ArrayList<>();
        String temp;

        for(int i = 0; i < jArray.length(); i++){
            temp = (String) jArray.getJSONObject(i).get("addonid");
            if(temp.contains("video"))
                addons.add(temp);
        }

        ArrayAdapter<String> addonAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, addons.toArray(new String[addons.size()]));

        listView.setAdapter(addonAdapter);
        listView.setOnItemClickListener(addonClickedHandler);
    }

    private AdapterView.OnItemClickListener addonClickedHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
            jObj = JSONBuilder.createJSONObject("Addons.ExecuteAddon",
                    adapterView.getItemAtPosition(pos).toString(), requestID);
            requestID++;
            System.out.println(jObj);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.POST, httpURL, jObj, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    backToNav();
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.toString());
                }
            });

            queue.add(jsonRequest);
        }
    };

    public void backToNav(){
        Intent backToNav = new Intent(this, BasicNavActivity.class);
        backToNav.putExtra("ipAddress", ipAddress);

        startActivity(backToNav);
    }
}
