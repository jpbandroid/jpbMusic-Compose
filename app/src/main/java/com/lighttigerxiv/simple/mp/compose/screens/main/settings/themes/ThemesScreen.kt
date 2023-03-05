package com.lighttigerxiv.simple.mp.compose.screens.main.settings.themes

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.ThemeSelector
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer

@Composable
fun ThemesScreen(
    mainVM: MainVM,
    settingsVM: SettingsVM,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val themeAccent = settingsVM.themeAccentSetting.collectAsState().value


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
    ) {

        CustomToolbar(
            backText = remember { getAppString(context, R.string.Settings) },
            onBackClick = { onBackClick() }
        )

        MediumHeightSpacer()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {

            ThemeSelector(
                selectedTheme = themeAccent,
                onThemeClick = {
                    settingsVM.updateThemeAccentSetting(it)
                }
            )
        }
    }
}

