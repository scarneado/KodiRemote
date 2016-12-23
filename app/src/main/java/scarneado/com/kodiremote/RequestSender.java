package scarneado.com.kodiremote;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class to provide request sending capabilities to
 * all activities.
 */

public class RequestSender {

    private static RequestSender rSender = null;
    private RequestQueue queue;
    private static Context rContext;

    private RequestSender(Context context){
        super();
        rContext = context;
        queue = Volley.newRequestQueue(rContext.getApplicationContext());
    }

    public static RequestQueue getRequestQueue(Context context){

        if(rSender == null){
            rSender = new RequestSender(context);
        }

        return rSender.queue;
    }
}
