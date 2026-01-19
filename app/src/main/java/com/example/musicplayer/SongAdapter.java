package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    private Context context;
    private List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        super(context, 0, songs);
        this.context = context;
        this.songs = songs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        }

        Song song = songs.get(position);

        TextView titleTextView = convertView.findViewById(R.id.songItemTitle);
        TextView artistTextView = convertView.findViewById(R.id.songItemArtist);

        titleTextView.setText(song.getTitle());
        artistTextView.setText(song.getArtist());

        return convertView;
    }
}
