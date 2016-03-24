package com.app.luis.androidapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.anton46.collectionitempicker.CollectionPicker;
import com.anton46.collectionitempicker.Item;
import com.anton46.collectionitempicker.OnItemClickListener;
import com.app.luis.androidapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSActivity extends AppCompatActivity {

    private CollectionPicker mPicker;
    private TextView mTextView;
    private Button butonSelected;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs);

        mPicker = (CollectionPicker) findViewById(R.id.collection_item_picker);
        mPicker.setItems(generateItems());
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
        butonSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItemsSelected();
            }
        });
    }

    public void getItemsSelected() {
        HashMap<String, Object> lista = mPicker.getCheckedItems();

        for (Map.Entry me : lista.entrySet()) {
            Log.d("TAGS","Key: " + me.getKey() + " & Value: " + ((Item)me.getValue()).text);
        }

        Toast.makeText(getApplicationContext(), lista.size()+"", Toast.LENGTH_LONG).show();
    }

    private List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("a", "Friendly Staff"));
        items.add(new Item("b", "Cozy place"));
        items.add(new Item("c", "Pizza"));
        items.add(new Item("d", "Burger"));
        items.add(new Item("e", "Ice Cream"));
        items.add(new Item("f", "Books"));
        items.add(new Item("g", "Goods for working"));
        items.add(new Item("h", "Romantic Places"));
        items.add(new Item("f", "Japanese food"));
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
