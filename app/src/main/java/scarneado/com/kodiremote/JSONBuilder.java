package scarneado.com.kodiremote;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by crono_000 on 12/19/2016.
 */

public class JSONBuilder {
    public static String requestStart = "{jsonrpc: '2.0', method: ";
    public static String simpleRequestEnd = ", id: ";
    public static String requestParams = ", params: {playerid: ";
    public static String exodusParams = ", params: {addonid: ";
    public static String requestEnd = " }, id: ";
    public static String end = "}";

    public static JSONObject createJSONObject(String requestType, int reqId){
        JSONObject jObj = null;

        try{
            jObj = new JSONObject(requestStart + requestType + simpleRequestEnd + reqId + end);
        }catch(JSONException e){
            System.out.println("JSON creation error");
        }

        return jObj;
    }

    public static JSONObject createJSONObject(String requestType, int playId, int reqId){
        JSONObject jObj = null;

        try{
            jObj = new JSONObject(requestStart + requestType + requestParams + playId + requestEnd +
                    reqId + end);
        }catch(JSONException e){
            System.out.println("JSON creation error");
        }

        return jObj;
    }

    public static JSONObject createJSONObject(String requestType, String addonID, int reqId){
        JSONObject jObj = null;

        try{
            jObj = new JSONObject(requestStart + requestType + exodusParams + addonID + requestEnd +
                    reqId + end);
        }catch(JSONException e){
            System.out.println("JSON creation error");
        }

        return jObj;
    }
}
