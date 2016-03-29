package com.app.luis.androidapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anton46.collectionitempicker.CollectionPicker;
import com.anton46.collectionitempicker.Item;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.api.factory.FactoryResponse;
import com.app.luis.androidapp.api.models.ErrorResponse;
import com.app.luis.androidapp.models.PerfilActivo;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.Environment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TagsInit extends AppCompatActivity {

    private CollectionPicker mPicker;
    private Usuario usuario;
    private List<Item> tags;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_init_tags);

        usuario = PerfilActivo.getInstance().getFromSharedPreferences(getApplicationContext());
        mPicker = (CollectionPicker) findViewById(R.id.tags_item_picker);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = ProgressDialog.show(TagsInit.this, "", "Obteniendo tags...", true);
        getTagsServer();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getTagsServer() {

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tags = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.d("LISTA_ADD", object.getString("id") + "-" + object.getString("tag"));
                        tags.add(new Item(object.getString("id"), object.getString("tag")));
                    }

                    Collections.shuffle(tags);
                    mPicker.setItems(tags);
                    mPicker.drawItemView();
                    Toast.makeText(getApplicationContext(), "Total de tags: " + tags.size() + "", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressDialog.dismiss();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    ErrorResponse responseClass = new FactoryResponse().createHttpResponse(error.networkResponse.statusCode);
                    ErrorResponse errorResponse = new Gson().fromJson(responseBody, responseClass.getClass());

                    Toast.makeText(getApplicationContext(), errorResponse.getUserMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException | NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    progressDialog.dismiss();
                }
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Environment.getInstance(getApplicationContext()).getBASE_URL() + "tags",
                jsonObjectListener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Accept", Environment.ACCEPT_HEADER);
                map.put("Content-Type", Environment.CONTENT_TYPE);
                map.put("Authorization", "Bearer " + usuario.getToken());
                return map;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(TagsInit.this).add(jsonObjectRequest);
    }
}
