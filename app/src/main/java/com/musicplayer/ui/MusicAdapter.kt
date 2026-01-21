package com.musicplayer.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicplayer.R
import com.musicplayer.databinding.ItemTrackYtmusicBinding
import com.musicplayer.domain.AudioTrack
import java.util.concurrent.TimeUnit

/**
 * Adapter for music track list (YouTube Music style)
 */
class MusicAdapter(
    private val onTrackClick: (AudioTrack, Int) -> Unit,
    private val onMoreClick: (AudioTrack, Int) -> Unit
) : ListAdapter<AudioTrack, MusicAdapter.TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackYtmusicBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, onTrackClick, onMoreClick)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class TrackViewHolder(
        private val binding: ItemTrackYtmusicBinding,
        private val onTrackClick: (AudioTrack, Int) -> Unit,
        private val onMoreClick: (AudioTrack, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: AudioTrack, position: Int) {
            binding.tvTrackTitle.text = track.title
            binding.tvTrackArtist.text = "${track.artist} • ${track.album}"

            // Load album art
            Glide.with(binding.root.context)
                .load(if (track.albumArtPath != null) Uri.parse(track.albumArtPath) else R.drawable.ic_music_note)
                .placeholder(R.drawable.ic_music_note)
                .error(R.drawable.ic_music_note)
                .centerCrop()
                .into(binding.ivTrackArt)

            binding.root.setOnClickListener {
                onTrackClick(track, position)
            }
            
            binding.btnMore.setOnClickListener {
                onMoreClick(track, position)
            }
        }

        private fun formatDuration(durationMs: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
            return String.format("%d:%02d", minutes, seconds)
        }
    }

    class TrackDiffCallback : DiffUtil.ItemCallback<AudioTrack>() {
        override fun areItemsTheSame(oldItem: AudioTrack, newItem: AudioTrack): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AudioTrack, newItem: AudioTrack): Boolean {
            return oldItem == newItem
        }
    }
}
