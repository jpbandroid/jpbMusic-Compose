package com.jpb.music.compose.frontend.screens.main.library.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.backend.settings.SettingsOptions
import com.jpb.music.compose.frontend.composables.Card
import com.jpb.music.compose.frontend.composables.MenuItem
import com.jpb.music.compose.frontend.composables.TextField
import com.jpb.music.compose.frontend.composables.VSpacer
import com.jpb.music.compose.frontend.navigation.goToAlbum
import com.jpb.music.compose.frontend.screens.main.library.albums.album.AlbumScreenVM
import com.jpb.music.compose.frontend.screens.main.library.artists.ArtistsScreenVM
import com.jpb.music.compose.frontend.utils.Sizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AlbumsScreen(
    vm: AlbumsScreenVM = viewModel(factory = AlbumsScreenVM.Factory),
    navController: NavHostController
){

    val uiState = vm.uiState.collectAsState().value
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(uiState.sortType){
        withContext(Dispatchers.Main){
            if(uiState.albums.isNotEmpty()){
                lazyGridState.scrollToItem(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TextField(
            text = uiState.searchText,
            onTextChange = { vm.updateSearchText(it) },
            placeholder = stringResource(id = R.string.search_albums),
            startIcon = R.drawable.sort,
            onStartIconClick = {vm.updateShowMenu(true)}
        )

        Menu(vm = vm, uiState = uiState)

        VSpacer(size = Sizes.LARGE)

        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
            horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
        ) {
            items(
                items = uiState.albums,
                key = { it.id }
            ) { album ->

                Card(
                    image = vm.getAlbumArt(album.id),
                    defaultIconId = R.drawable.album,
                    text = album.name,
                    onClick = { navController.goToAlbum(album.id) }
                )
            }
        }
    }
}

@Composable
fun Menu(
    vm: AlbumsScreenVM,
    uiState: AlbumsScreenVM.UiState
) {
    Column {
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            expanded = uiState.showMenu,
            onDismissRequest = { vm.updateShowMenu(false) }
        ) {
            MenuItem(
                iconId = R.drawable.sort,
                text = stringResource(id = R.string.sort_by_default),
                onClick = {

                    vm.updateShowMenu(false)

                    vm.updateSort(
                        if (uiState.sortType == SettingsOptions.Sort.DEFAULT_REVERSED)
                            SettingsOptions.Sort.DEFAULT
                        else
                            SettingsOptions.Sort.DEFAULT_REVERSED
                    )
                }
            )

            MenuItem(
                iconId = R.drawable.alphabetic_sort,
                text = stringResource(id = R.string.sort_alphabetically),
                onClick = {

                    vm.updateShowMenu(false)

                    vm.updateSort(
                        if (uiState.sortType == SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT)
                            SettingsOptions.Sort.ALPHABETICALLY_DESCENDENT
                        else
                            SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT
                    )
                }
            )
        }
    }
}