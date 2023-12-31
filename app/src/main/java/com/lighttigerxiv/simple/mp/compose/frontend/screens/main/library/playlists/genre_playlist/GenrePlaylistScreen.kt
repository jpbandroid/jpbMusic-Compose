package com.jpb.music.compose.frontend.screens.main.library.playlists.genre_playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.frontend.composables.CollapsableHeader
import com.jpb.music.compose.frontend.composables.PlayShuffleRow
import com.jpb.music.compose.frontend.composables.SongCard
import com.jpb.music.compose.frontend.composables.Toolbar
import com.jpb.music.compose.frontend.composables.VSpacer
import com.jpb.music.compose.frontend.utils.FontSizes
import com.jpb.music.compose.frontend.utils.Sizes
import org.mongodb.kbson.ObjectId

@Composable
fun GenrePlaylistScreen(
    navController: NavHostController,
    playlistId: ObjectId,
    vm: GenrePlaylistScreenVM = viewModel(factory = GenrePlaylistScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value

    LaunchedEffect(uiState.requestedLoading) {
        if (!uiState.requestedLoading) {
            vm.load(playlistId)
        }
    }

    Column {
        Toolbar(navController)

        if (!uiState.loading) {

            CollapsableHeader(
                header = {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Sizes.XLARGE))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(Sizes.MEDIUM)
                            ) {
                                Icon(
                                    modifier = Modifier.size(220.dp),
                                    painter = painterResource(id = R.drawable.playlist_filled),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            VSpacer(size = Sizes.LARGE)

                            Text(
                                text = uiState.genreName,
                                fontSize = FontSizes.HEADER,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            ) {

                Column(Modifier.fillMaxSize()) {

                    VSpacer(size = Sizes.LARGE)

                    PlayShuffleRow(
                        onPlayClick = {
                            vm.playSong(uiState.songs[0])
                        },
                        onShuffleClick = {
                            vm.shuffleAndPlay()
                        }
                    )

                    VSpacer(size = Sizes.LARGE)

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Sizes.SMALL)
                    ) {
                        items(
                            items = uiState.songs,
                            key = { it.id }
                        ) { song ->

                            SongCard(
                                song = song,
                                artistName = vm.getArtistName(song.artistId),
                                art = vm.getAlbumArt(song.albumId),
                                onClick = { vm.playSong(song) },
                                highlight = uiState.currentSong?.id == song.id
                            )
                        }
                    }
                }
            }
        }
    }
}