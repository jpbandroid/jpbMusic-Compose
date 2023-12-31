package com.jpb.music.compose.frontend.screens.main.add_song_to_playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.backend.realm.collections.Playlist
import com.jpb.music.compose.frontend.composables.HSpacer
import com.jpb.music.compose.frontend.composables.IconDialog
import com.jpb.music.compose.frontend.composables.SecondaryButton
import com.jpb.music.compose.frontend.composables.TextField
import com.jpb.music.compose.frontend.composables.Toolbar
import com.jpb.music.compose.frontend.composables.VSpacer
import com.jpb.music.compose.frontend.navigation.goBack
import com.jpb.music.compose.frontend.utils.FontSizes
import com.jpb.music.compose.frontend.utils.Sizes

@Composable
fun AddSongToPlaylistScreen(
    rootController: NavHostController,
    songId: Long,
    vm: AddSongToPlaylistScreenVM = viewModel(factory = AddSongToPlaylistScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value

    Column(modifier = Modifier.padding(Sizes.LARGE)) {
        Toolbar(navController = rootController) {
            if (!uiState.isLoading) {
                SecondaryButton(
                    text = stringResource(id = R.string.create_playlist),
                    onClick = { vm.openCreatePlaylistDialog() }
                )
            }
        }

        VSpacer(size = Sizes.LARGE)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Sizes.SMALL)
        ) {
            items(
                items = uiState.playlists,
                key = { it._id.toHexString() }
            ) { playlist ->

                PlaylistCard(rootController = rootController, vm = vm, playlist = playlist, songId)
            }
        }

        CreatePlaylistDialog(uiState = uiState, vm = vm)
    }
}

@Composable
fun CreatePlaylistDialog(
    uiState: AddSongToPlaylistScreenVM.UiState,
    vm: AddSongToPlaylistScreenVM
) {

    IconDialog(
        show = uiState.showCreatePlaylistDialog,
        onDismiss = { vm.cancelCreatePlaylistDialog() },
        iconId = R.drawable.playlist_filled,
        title = stringResource(id = R.string.create_playlist),
        primaryButtonText = stringResource(id = R.string.create),
        onPrimaryButtonClick = { vm.createPlaylist() }
    ) {

        TextField(
            text = uiState.nameTextCreatePlaylistDialog,
            onTextChange = { vm.updateNameTextCreatePlaylistDialog(it) },
            placeholder = stringResource(id = R.string.playlist_name)
        )
    }
}

@Composable
fun PlaylistCard(
    rootController: NavHostController,
    vm: AddSongToPlaylistScreenVM,
    playlist: Playlist,
    songId: Long
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Sizes.LARGE))
            .clickable {
                vm.addSong(playlist._id, songId, onSuccess = {
                    rootController.goBack()
                })
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(Sizes.LARGE))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.playlist_filled),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        HSpacer(size = Sizes.LARGE)

        Text(
            text = playlist.name,
            fontWeight = FontWeight.Medium,
            fontSize = FontSizes.HEADER_2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}