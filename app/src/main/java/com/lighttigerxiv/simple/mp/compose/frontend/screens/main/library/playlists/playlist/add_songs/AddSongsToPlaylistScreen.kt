package com.jpb.music.compose.frontend.screens.main.library.playlists.playlist.add_songs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.frontend.composables.AddToPlaylistSongCard
import com.jpb.music.compose.frontend.composables.PrimaryButton
import com.jpb.music.compose.frontend.composables.SongCard
import com.jpb.music.compose.frontend.composables.TextField
import com.jpb.music.compose.frontend.composables.Toolbar
import com.jpb.music.compose.frontend.composables.VSpacer
import com.jpb.music.compose.frontend.navigation.goBack
import com.jpb.music.compose.frontend.utils.Sizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

@Composable
fun AddSongsToPlaylistScreen(
    playlistId: ObjectId,
    navController: NavHostController,
    vm: AddSongsToPlaylistScreenVM = viewModel(factory = AddSongsToPlaylistScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value
    val lazyColState = rememberLazyListState()

    LaunchedEffect(uiState.requestedLoading) {
        if (!uiState.requestedLoading) {
            vm.load(playlistId)
        }
    }

    LaunchedEffect(uiState.songs) {
        withContext(Dispatchers.Main) {
            if (uiState.songs.isNotEmpty()) {
                lazyColState.scrollToItem(0)
            }
        }
    }

    Column(Modifier.padding(Sizes.LARGE)) {
        Toolbar(navController = navController) {
            if (!uiState.isLoading) {
                PrimaryButton(
                    text = stringResource(id = R.string.save),
                    onClick = {
                        vm.addSongs()
                        navController.goBack()
                    }
                )
            }
        }

        VSpacer(size = Sizes.LARGE)

        TextField(
            text = uiState.searchText,
            onTextChange = { vm.updateSearchText(it) },
            placeholder = stringResource(id = R.string.search_songs)
        )

        VSpacer(size = Sizes.LARGE)

        if (!uiState.isLoading) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                state = lazyColState
            ) {
                if (uiState.songs.isNotEmpty()) {
                    items(
                        items = uiState.songs,
                        key = { it.id }
                    ) { song ->
                        AddToPlaylistSongCard(
                            song = song,
                            artistName = vm.getArtistName(song.artistId),
                            art = vm.getAlbumArt(song.albumId),
                            selected = uiState.selectedSongsIds.contains(song.id),
                            onClick = {
                                vm.toggleSong(song.id)
                            }
                        )
                    }
                }
            }
        }
    }
}