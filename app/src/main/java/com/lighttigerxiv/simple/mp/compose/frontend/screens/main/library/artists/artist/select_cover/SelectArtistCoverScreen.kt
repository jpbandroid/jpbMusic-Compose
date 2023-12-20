package com.jpb.music.compose.frontend.screens.main.library.artists.artist.select_cover

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.frontend.composables.SecondaryButton
import com.jpb.music.compose.frontend.composables.Toolbar
import com.jpb.music.compose.frontend.composables.VSpacer
import com.jpb.music.compose.frontend.utils.Sizes

@Composable
fun SelectArtistCoverScreen(
    artistId: Long,
    navController: NavHostController,
    vm: SelectArtistCoverScreenVM = viewModel(factory = SelectArtistCoverScreenVM.Factory)
) {

    Column{
        Toolbar(navController = navController)

        VSpacer(size = Sizes.LARGE)

        SecondaryButton(
            text = stringResource(id = R.string.use_default_cover),
            onClick = {  },
            fillWidth = true
        )

        VSpacer(size = Sizes.SMALL)

        SecondaryButton(
            text = stringResource(id = R.string.select_local_cover),
            onClick = {  },
            fillWidth = true
        )
    }
}