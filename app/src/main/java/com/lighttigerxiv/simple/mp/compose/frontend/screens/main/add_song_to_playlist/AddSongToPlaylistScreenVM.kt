package com.jpb.music.compose.frontend.screens.main.add_song_to_playlist

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jpb.music.compose.R
import com.jpb.music.compose.SimpleMPApplication
import com.jpb.music.compose.backend.realm.Queries
import com.jpb.music.compose.backend.realm.collections.Playlist
import com.jpb.music.compose.backend.realm.getRealm
import com.jpb.music.compose.backend.repositories.LibraryRepository
import com.jpb.music.compose.backend.repositories.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class AddSongToPlaylistScreenVM(
    private val application: Application,
    private val libraryRepository: LibraryRepository,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SimpleMPApplication)

                AddSongToPlaylistScreenVM(
                    app,
                    app.container.libraryRepository,
                    app.container.playlistsRepository
                )
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val playlists: List<Playlist> = ArrayList(),
        val showCreatePlaylistDialog: Boolean = false,
        val nameTextCreatePlaylistDialog: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val queries = Queries(getRealm())

    init {
        viewModelScope.launch(Dispatchers.Main) {
            _uiState.update {
                uiState.value.copy(
                    isLoading = false,
                    playlists = queries.getUserPlaylists()
                )
            }
        }
    }


    fun openCreatePlaylistDialog() {
        _uiState.update { uiState.value.copy(showCreatePlaylistDialog = true) }
    }

    fun cancelCreatePlaylistDialog() {
        _uiState.update {
            uiState.value.copy(
                showCreatePlaylistDialog = false,
                nameTextCreatePlaylistDialog = ""
            )
        }
    }

    fun updateNameTextCreatePlaylistDialog(v: String) {
        _uiState.update { uiState.value.copy(nameTextCreatePlaylistDialog = v) }
    }

    fun createPlaylist() {
        viewModelScope.launch(Dispatchers.Main) {
            queries.createPlaylist(uiState.value.nameTextCreatePlaylistDialog)

            playlistsRepository.loadPlaylists(libraryRepository.songs.value)

            _uiState.update {
                uiState.value.copy(
                    playlists = queries.getUserPlaylists(),
                    showCreatePlaylistDialog = false
                )
            }
        }
    }

    fun addSong(playlistId: ObjectId, songId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {

            val playlist = queries.getUserPlaylists().find { it._id == playlistId }
            playlist?.let {
                if (playlist.songs.contains(songId)) {
                    Toast.makeText(application, application.getString(R.string.song_already_in_playlist), Toast.LENGTH_SHORT).show()
                } else {
                    queries.addSongToPlaylist(playlistId, songId)
                    playlistsRepository.loadPlaylists(libraryRepository.songs.value)
                    onSuccess()
                }
            }
        }
    }
}