package com.example.shoutcastplayer.ui.viewmodel

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.shoutcastplayer.data.db.FavoriteStation
import com.example.shoutcastplayer.data.model.StationDto
import com.example.shoutcastplayer.data.model.TagDto
import com.example.shoutcastplayer.data.repository.RadioRepository
import com.example.shoutcastplayer.service.RadioService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(
    private val repository: RadioRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _stations = MutableStateFlow<List<StationDto>>(emptyList())
    val stations: StateFlow<List<StationDto>> = _stations.asStateFlow()

    private val _tags = MutableStateFlow<List<TagDto>>(emptyList())
    val tags: StateFlow<List<TagDto>> = _tags.asStateFlow()

    private val _currentStation = MutableStateFlow<StationDto?>(null)
    val currentStation: StateFlow<StationDto?> = _currentStation.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    val favorites: StateFlow<List<FavoriteStation>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    private val _playbackStatus = MutableStateFlow("Idle")
    val playbackStatus: StateFlow<String> = _playbackStatus.asStateFlow()

    private val _metadata = MutableStateFlow<String?>(null)
    val metadata: StateFlow<String?> = _metadata.asStateFlow()

    init {
        Log.d("SHOUTCAST", "RadioViewModel init")
        initializeMediaController()
        loadTopStations()
    }

    private fun initializeMediaController() {
        val sessionToken = SessionToken(context, ComponentName(context, RadioService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture?.addListener({
            mediaController = mediaControllerFuture?.get()
            mediaController?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                    updateStatus()
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    updateStatus()
                }

                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    _playbackStatus.value = "Error: ${error.message}"
                }

                override fun onMediaMetadataChanged(mediaMetadata: androidx.media3.common.MediaMetadata) {
                    val title = mediaMetadata.title?.toString()
                    val artist = mediaMetadata.artist?.toString()
                    _metadata.value = if (!artist.isNullOrBlank() && !title.isNullOrBlank()) {
                        "$artist - $title"
                    } else {
                        title ?: artist
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private fun updateStatus() {
        mediaController?.let { player ->
            _playbackStatus.value = when (player.playbackState) {
                Player.STATE_BUFFERING -> "Buffering..."
                Player.STATE_ENDED -> "Ended"
                Player.STATE_IDLE -> "Idle"
                Player.STATE_READY -> if (player.isPlaying) "Playing" else "Paused"
                else -> "Unknown"
            }
        }
    }

    fun loadTopStations() {
        Log.d("SHOUTCAST", "loadTopStations called")
        viewModelScope.launch {
            val result = repository.getTopStations()
            result.onSuccess { 
                Log.d("SHOUTCAST", "loadTopStations success: ${it.size} stations")
                _stations.value = it 
            }.onFailure {
                Log.e("SHOUTCAST", "loadTopStations failed", it)
            }
        }
    }

    fun loadTags() {
        viewModelScope.launch {
            val result = repository.getTags()
            result.onSuccess {
                _tags.value = it
            }.onFailure {
                Log.e("SHOUTCAST", "loadTags failed", it)
            }
        }
    }

    fun searchStations(query: String) {
        viewModelScope.launch {
            val result = repository.searchStations(query)
            result.onSuccess { _stations.value = it }
        }
    }
    
    fun getStationsByTag(tag: String) {
        viewModelScope.launch {
            val result = repository.getStationsByTag(tag)
            result.onSuccess { _stations.value = it }
        }
    }

    fun playStation(station: StationDto) {
        _currentStation.value = station
        val mediaItem = MediaItem.fromUri(station.urlResolved)
        mediaController?.setMediaItem(mediaItem)
        mediaController?.prepare()
        mediaController?.play()
    }

    fun togglePlayPause() {
        if (mediaController?.isPlaying == true) {
            mediaController?.pause()
        } else {
            mediaController?.play()
        }
    }

    fun toggleFavorite(station: StationDto) {
        viewModelScope.launch {
            if (repository.isFavorite(station.stationUuid)) {
                repository.removeFavorite(station.stationUuid)
            } else {
                repository.addFavorite(station)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
    }
}
