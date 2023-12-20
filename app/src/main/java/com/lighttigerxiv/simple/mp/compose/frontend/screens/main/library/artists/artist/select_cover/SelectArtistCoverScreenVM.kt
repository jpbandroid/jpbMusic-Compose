package com.jpb.music.compose.frontend.screens.main.library.artists.artist.select_cover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jpb.music.compose.SimpleMPApplication
import com.jpb.music.compose.backend.repositories.LibraryRepository

class SelectArtistCoverScreenVM(
    libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository

                SelectArtistCoverScreenVM(libraryRepository)
            }
        }
    }
}