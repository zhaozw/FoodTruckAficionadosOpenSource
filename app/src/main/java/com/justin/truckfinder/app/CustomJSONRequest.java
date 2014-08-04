package com.justin.truckfinder.app;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/*
 * Created by justin on 4/9/14
 *
 * This class is a customized version of the corresponding class from the Volley library
 * Customized was necessary since the Volley Library (as of March 2014) did not provide
 * built-in functionality to setTag for individual requests (only supports batch request tagging)
 */
public class CustomJSONRequest extends JsonObjectRequest {

    public CustomJSONRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject myJson = new JSONObject(jsonString);
            myJson.put("RESPONSEKEY", getTag());
            return Response.success(myJson,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
