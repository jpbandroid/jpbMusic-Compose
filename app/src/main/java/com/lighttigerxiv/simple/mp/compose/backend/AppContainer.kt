package com.jpb.music.compose.backend

import com.jpb.music.compose.backend.repositories.LibraryRepository
import com.jpb.music.compose.backend.repositories.PlaybackRepository
import com.jpb.music.compose.backend.repositories.PlaylistsRepository
import com.jpb.music.compose.backend.repositories.SettingsRepository

interface AppContainer {
    val libraryRepository: LibraryRepository
    val settingsRepository: SettingsRepository
    val playbackRepository: PlaybackRepository
    val playlistsRepository: PlaylistsRepository
}