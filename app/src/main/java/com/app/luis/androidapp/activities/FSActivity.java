package com.app.luis.androidapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.anton46.collectionitempicker.OnItemClickListener;
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

public class FSActivity extends AppCompatActivity implements View.OnClickListener {

    int counter;
    private CollectionPicker mPicker;
    private TextView mTextView;
    private Button butonSelected;
    private Usuario usuario;
    private List<Item> tags;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs);
        usuario = PerfilActivo.getInstance().getFromSharedPreferences(getApplicationContext());

        mPicker = (CollectionPicker) findViewById(R.id.collection_item_picker);
        mPicker.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(Item item, int position) {
                if (item.isSelected) {
                    counter++;
                } else {
                    counter--;
                }
                mTextView.setText(counter + " Items Selected");
            }
        });

        mTextView = (TextView) findViewById(R.id.text);
        butonSelected = (Button) findViewById(R.id.buttonSelected);

        butonSelected.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = ProgressDialog.show(FSActivity.this, "", "Obteniendo tags...", true);
        getTagsServer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSelected:
                getItemsSelected();
                break;
        }
    }

    public void getItemsSelected() {
        HashMap<String, Object> lista = mPicker.getCheckedItems();

        for (Map.Entry me : lista.entrySet()) {
            Log.d("TAGS", "Key: " + me.getKey() + " & Value: " + ((Item) me.getValue()).text);
        }

        Toast.makeText(getApplicationContext(), lista.size() + "", Toast.LENGTH_LONG).show();
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

        Volley.newRequestQueue(FSActivity.this).add(jsonObjectRequest);
    }

    private List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("MKLZXbqyxXpmkPwy", "accesorios"));
        items.add(new Item("YdDM9KpD4bp3Pgba", "aeropostale"));
        items.add(new Item("EJBakjrgeXq2Dy9Y", "alta cocina"));
        items.add(new Item("Ng7jW1oV2EoDl6vd", "bar"));
        items.add(new Item("7jM5bmoZb9oGKBwn", "billar"));
        items.add(new Item("j90PNnoeOepLAVQX", "calvin klein"));
        items.add(new Item("PWkBVKo05vrylwe1", "camarones"));
        items.add(new Item("XB54kmo7LGr9j2Ry", "cena"));
        items.add(new Item("yzw5aDrMY4r3vW0R", "cerveza"));
        items.add(new Item("L7w8Bvodxmo2YE1j", "cerveza artesanal"));
        items.add(new Item("mQ4PJyrAJpne0aRM", "comida"));
        items.add(new Item("e", "Ice Cream"));
        items.add(new Item("g", "Goods for working"));
        items.add(new Item("h", "Romantic Places"));
        items.add(new Item("f", "Japanese food"));
        items.add(new Item("c", "Pizza"));
        items.add(new Item("d", "Burger"));
        items.add(new Item("e", "Ice Cream"));
        items.add(new Item("c", "Pizza"));
        items.add(new Item("d", "Burger"));
        items.add(new Item("e", "Ice Cream"));
        items.add(new Item("g", "Goods for working"));
        items.add(new Item("h", "Romantic Places"));
        items.add(new Item("f", "Japanese food"));
        items.add(new Item("c", "Pizza"));
        items.add(new Item("d", "Burger"));
        items.add(new Item("e", "Ice Cream"));
        items.add(new Item("c", "Pizza"));
        items.add(new Item("d", "Burger"));
        items.add(new Item("e", "Ice Cream"));
        items.add(new Item("g", "Goods for working"));
        items.add(new Item("h", "Romantic Places"));
        items.add(new Item("f", "Japanese food"));
        items.add(new Item("c", "Pizza"));
        items.add(new Item("d", "Burger"));
        items.add(new Item("e", "Ice Cream"));
        return items;
    }
}
