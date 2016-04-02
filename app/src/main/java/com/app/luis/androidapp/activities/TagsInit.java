package com.app.luis.androidapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anton46.collectionitempicker.CollectionPicker;
import com.anton46.collectionitempicker.Item;
import com.app.luis.androidapp.R;
import com.app.luis.androidapp.api.factory.FactoryResponse;
import com.app.luis.androidapp.api.models.ErrorResponse;
import com.app.luis.androidapp.models.PerfilActivo;
import com.app.luis.androidapp.models.Usuario;
import com.app.luis.androidapp.utils.AppConstants;
import com.app.luis.androidapp.utils.Environment;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class TagsInit extends AbstractActivity {


    @Bind(R.id.tags_item_picker)
    CollectionPicker mPicker;
    @Bind(R.id.tvMensajeTagsInicio)
    TextView mensajeTagInicio;

    private final int incrementList = 15;
    private int contador = 1;
    private Usuario usuario;
    private List<Item> tags;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_init_tags);
        ButterKnife.bind(this);

        usuario = PerfilActivo.getInstance().getFromSharedPreferences(getApplicationContext());
        mensajeTagInicio.setText(getString(R.string.TV_MENSAJE_TAGS_INICIO, usuario.getNombre()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = ProgressDialog.show(TagsInit.this, "", "Obteniendo tags...", true);
        getTagsServer();
    }

    @OnClick(R.id.btnMasTags)
    public void mostrarMasTags() {
        int start = mPicker.getItems().size() * contador;
        int end = mPicker.getItems().size() * ++contador;

        if (tags.size() > end) {
            mPicker.setItems(tags.subList(start, end));
            mPicker.drawItemView();
        }
    }

    @OnClick(R.id.btnContinuar)
    public void continuar() {
        HashMap<String, Object> mPickerSeleccionados = mPicker.getCheckedItems();
        try {
            JSONArray jsonArrayTags = new JSONArray();
            for (Map.Entry<String, Object> entry : mPickerSeleccionados.entrySet()) {

                String value = ((Item) entry.getValue()).text;
                JSONObject jsonTags = new JSONObject();
                jsonTags.put("id", entry.getKey());
                jsonTags.put("tag", value);
                jsonArrayTags.put(jsonTags);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tags", jsonArrayTags);

            String json_tags = new JSONObject().put("data", jsonObject).toString();
            postTags(json_tags);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTagsServer() {

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                try {
                    tags = new ArrayList<>();

                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        tags.add(new Item(object.getString("id"), object.getString("tag")));
                    }

                    Collections.shuffle(tags);
                    mPicker.setItems(tags.subList(0, incrementList));
                    mPicker.drawItemView();

                } catch (JSONException | IllegalArgumentException e) {
                    e.printStackTrace();

                    if (!(e instanceof IllegalArgumentException)) {
                        Toast.makeText(getApplicationContext(), "Error: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getString(R.string.TEXT_PROBLEMAS_INTERNET), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    ErrorResponse responseClass = new FactoryResponse().createHttpResponse(error.networkResponse.statusCode);
                    ErrorResponse errorResponse = new Gson().fromJson(responseBody, responseClass.getClass());
                    Toast.makeText(getApplicationContext(), errorResponse.getUserMessage(), Toast.LENGTH_LONG).show();

                } catch (UnsupportedEncodingException | NullPointerException | IllegalArgumentException e) {
                    e.printStackTrace();

                    if (!(e instanceof IllegalArgumentException)) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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

    private void postTags(String json_tags) {

        Map<String, String> params = new HashMap<>();
        params.put("tags", json_tags);

        int orientacion = getResources().getConfiguration().orientation;

        switch (orientacion) {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }

        progressDialog = ProgressDialog.show(TagsInit.this, "", "Guardando tus datos...", true);

        Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("tags");

                    SharedPreferences preferences = getApplicationContext()
                            .getSharedPreferences(AppConstants.USER_TAGS_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Usuario.UserTagAttribute.TAG_JSON, jsonArray.toString());
                    editor.commit();

                    startActivity(new Intent(TagsInit.this, MVPActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), getString(R.string.TEXT_PROBLEMAS_INTERNET), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    ErrorResponse responseClass = new FactoryResponse().createHttpResponse(error.networkResponse.statusCode);
                    ErrorResponse errorResponse = new Gson().fromJson(responseBody, responseClass.getClass());
                    Toast.makeText(getApplicationContext(), errorResponse.getUserMessage(), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };

        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                Environment.getInstance(getApplicationContext()).getBASE_URL() + "usuarios/"+usuario.getId()+"/tags",
                json_tags,
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

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(TagsInit.this).add(postRequest);
    }
}
