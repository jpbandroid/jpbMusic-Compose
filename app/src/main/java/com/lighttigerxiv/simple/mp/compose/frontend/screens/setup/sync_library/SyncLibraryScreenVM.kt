package com.jpb.music.compose.frontend.screens.setup.sync_library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jpb.music.compose.MusicApplication
import com.jpb.music.compose.backend.repositories.LibraryRepository
import com.jpb.music.compose.backend.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncLibraryScreenVM(
    private val application: Application,
    private val settingsRepository: SettingsRepository,
    private val libraryRepository: LibraryRepository
) :  ViewModel(){

    companion object Factory{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MusicApplication)
                val settingsRepository = application.container.settingsRepository
                val libraryRepository = application.container.libraryRepository

                SyncLibraryScreenVM(application, settingsRepository, libraryRepository)
            }
        }
    }

    init {

        //Indexes The Library
        viewModelScope.launch(Dispatchers.Default){
            libraryRepository.indexLibrary(application){
                withContext(Dispatchers.Default){
                    settingsRepository.updateSetupCompleted(true)
                }
            }
        }
    }
}