package scarneado.com.kodiremote;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by crono_000 on 12/19/2016.
 */

public class JSONBuilder {
    private static String requestStart = "{jsonrpc: '2.0', method: ";
    private static String simpleRequestEnd = ", id: ";
    private static String requestParams = ", params: {playerid: ";
    private static String addonRequestParam = ", params: {addonid: ";
    private static String generalRequestParam = ", params: {";
    private static String requestEnd = " }, id: ";
    private static String end = "}";

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
            jObj = new JSONObject(requestStart + requestType + addonRequestParam + addonID + requestEnd +
                    reqId + end);
        }catch(JSONException e){
            System.out.println("JSON creation error");
        }

        return jObj;
    }

    public static JSONObject createJSONObject(String requestType, String param, String pData,
                                              int reqID){
        JSONObject jObj = null;

        try{
            jObj = new JSONObject(requestStart + requestType + generalRequestParam + param + ": " +
            pData + requestEnd + reqID + end);
        }catch (JSONException e){
            System.out.println("JSON creation error");
        }

        return jObj;
    }
}
