package com.app.luis.androidapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.luis.androidapp.R;
import com.app.luis.androidapp.models.LugarExample;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Luis Macias on 02/04/2016.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.PersonViewHolder> {
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    List<LugarExample> persons;

    public TestRecyclerViewAdapter(List<LugarExample> contents) {
        this.persons = contents;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false);
                PersonViewHolder personViewHolder = new PersonViewHolder(view);
                return personViewHolder;
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_small, parent, false);
                PersonViewHolder personViewHolder = new PersonViewHolder(view);
                return personViewHolder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        switch (getItemViewType(i)) {
            case TYPE_HEADER:
                personViewHolder.personName.setText(persons.get(i).getNombre());
                personViewHolder.personAge.setText(persons.get(i).getDireccion());
                new DownLoadImageTask(personViewHolder.personPhoto).execute(persons.get(i).getLogo());
                break;
            case TYPE_CELL:
                personViewHolder.personName.setText(persons.get(i).getNombre());
                personViewHolder.personAge.setText(persons.get(i).getDireccion());
                new DownLoadImageTask(personViewHolder.personPhoto).execute(persons.get(i).getLogo());
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personAge = (TextView) itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
