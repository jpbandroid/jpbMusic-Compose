package com.jpb.music.compose.frontend.screens.main.settings

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jpb.music.compose.R
import com.jpb.music.compose.MusicApplication
import com.jpb.music.compose.backend.repositories.LibraryRepository
import com.jpb.music.compose.backend.repositories.SettingsRepository
import com.jpb.music.compose.backend.settings.SettingsOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenVM(
    private val settingsRepository: SettingsRepository,
    private val libraryRepository: LibraryRepository,
    private val application: Application
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MusicApplication)
                val settingsRepository = app.container.settingsRepository
                val libraryRepository = app.container.libraryRepository

                SettingsScreenVM(settingsRepository, libraryRepository, app)
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val colorScheme: String = "",
        val useOledInDarkTheme: Boolean = false,
        val lightTheme: String = "",
        val darkTheme: String = "",
        val durationFilter: Int = 0,
        val downloadArtistCover: Boolean = false,
        val downloadArtistCoverWithData: Boolean = false,
        val showColorSchemeDialog: Boolean = false,
        val showLightThemeDialog: Boolean = false,
        val showDarkThemeDialog: Boolean = false,
        val showDurationFilterDialog: Boolean = false,
        val colorSchemeDialogSelectedRadioButton: String = "",
        val lightThemeDialogSelectedTheme: String = "",
        val darkThemeDialogSelectedTheme: String = "",
        val durationFilterDialogText: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.settingsFlow.collect { newSettings ->
                _uiState.update {
                    uiState.value.copy(
                        isLoading = false,
                        colorScheme = newSettings.colorScheme,
                        useOledInDarkTheme = newSettings.useOledOnDarkTheme,
                        lightTheme = newSettings.lightTheme,
                        darkTheme = newSettings.darkTheme,
                        durationFilter = newSettings.durationFilter,
                        downloadArtistCover = newSettings.downloadArtistCover,
                        downloadArtistCoverWithData = newSettings.downloadArtistCoverWithData,
                        colorSchemeDialogSelectedRadioButton = newSettings.colorScheme,
                        lightThemeDialogSelectedTheme = newSettings.lightTheme,
                        darkThemeDialogSelectedTheme = newSettings.darkTheme,
                        durationFilterDialogText = newSettings.durationFilter.toString()
                    )
                }
            }
        }
    }

    fun updateShowColorSchemeDialog(v: Boolean) {
        _uiState.update { uiState.value.copy(showColorSchemeDialog = v) }
    }

    fun updateShowLightThemeDialog(v: Boolean) {
        _uiState.update { uiState.value.copy(showLightThemeDialog = v) }
    }

    fun updateShowDarkThemeDialog(v: Boolean) {
        _uiState.update { uiState.value.copy(showDarkThemeDialog = v) }
    }

    fun updateShowDurationFilterDialog(v: Boolean) {
        _uiState.update { uiState.value.copy(showDurationFilterDialog = v) }
    }

    fun updateColorScheme(v: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateColorScheme(v)
        }
    }

    fun updateUseOledInDarkTheme(v: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateUseOledInDarkTheme(v)
        }
    }

    fun updateDownloadArtistCover(v: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateDownloadArtistCover(v)
        }
    }

    fun updateDownloadArtistCoverWithData(v: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.updateDownloadArtistCoverWithData(v)
        }
    }

    fun updateColorSchemeDialogSelectedRadioButton(v: String) {
        _uiState.update { uiState.value.copy(colorSchemeDialogSelectedRadioButton = v) }
    }

    fun updateLightThemeDialogSelectedTheme(v: String) {
        _uiState.update { uiState.value.copy(lightThemeDialogSelectedTheme = v) }
    }

    fun updateLightTheme(v: String) {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.updateLightTheme(v)
        }
    }

    fun updateDarkTheme(v: String) {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.updateDarkTheme(v)
        }
    }

    fun updateDarkThemeDialogSelectedTheme(v: String) {
        _uiState.update { uiState.value.copy(darkThemeDialogSelectedTheme = v) }
    }

    fun updateDurationFilterDialogText(v: String) {
        _uiState.update { uiState.value.copy(durationFilterDialogText = v) }
    }

    fun updateDurationFilter() {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.updateDurationFilter(uiState.value.durationFilterDialogText.toInt())
            libraryRepository.initLibrary()
        }
    }

    @Composable
    fun getThemeName(theme: String): String {
        return when (theme) {
            SettingsOptions.Themes.MATERIAL_YOU -> stringResource(id = R.string.material_you)
            SettingsOptions.Themes.RED -> stringResource(id = R.string.red)
            SettingsOptions.Themes.GREEN -> stringResource(id = R.string.green)
            SettingsOptions.Themes.BLUE -> stringResource(id = R.string.blue)
            SettingsOptions.Themes.PURPLE -> stringResource(id = R.string.purple)
            SettingsOptions.Themes.PINK -> stringResource(id = R.string.pink)
            SettingsOptions.Themes.YELLOW -> stringResource(id = R.string.yellow)
            SettingsOptions.Themes.ORANGE -> stringResource(id = R.string.orange)
            SettingsOptions.Themes.LATTE_ROSEWATER -> "Latte Rosewater"
            SettingsOptions.Themes.LATTE_FLAMINGO -> "Latte Flamingo"
            SettingsOptions.Themes.LATTE_PINK -> "Latte Pink"
            SettingsOptions.Themes.LATTE_MAUVE -> "Latte Mauve"
            SettingsOptions.Themes.LATTE_RED -> "Latte Red"
            SettingsOptions.Themes.LATTE_MAROON -> "Latte Maroon"
            SettingsOptions.Themes.LATTE_PEACH -> "Latte Peach"
            SettingsOptions.Themes.LATTE_YELLOW -> "Latte Yellow"
            SettingsOptions.Themes.LATTE_GREEN -> "Latte Green"
            SettingsOptions.Themes.LATTE_TEAL -> "Latte Teal"
            SettingsOptions.Themes.LATTE_SKY -> "Latte Sky"
            SettingsOptions.Themes.LATTE_SAPPHIRE -> "Latte Sapphire"
            SettingsOptions.Themes.LATTE_BLUE -> "Latte Blue"
            SettingsOptions.Themes.LATTE_LAVENDER -> "Latte Lavender"
            SettingsOptions.Themes.FRAPPE_ROSEWATER -> "Frappe Rosewater"
            SettingsOptions.Themes.FRAPPE_FLAMINGO -> "Frappe Flamingo"
            SettingsOptions.Themes.FRAPPE_PINK -> "Frappe Pink"
            SettingsOptions.Themes.FRAPPE_MAUVE -> "Frappe Mauve"
            SettingsOptions.Themes.FRAPPE_RED -> "Frappe Red"
            SettingsOptions.Themes.FRAPPE_MAROON -> "Frappe Maroon"
            SettingsOptions.Themes.FRAPPE_PEACH -> "Frappe Peach"
            SettingsOptions.Themes.FRAPPE_YELLOW -> "Frappe Yellow"
            SettingsOptions.Themes.FRAPPE_GREEN -> "Frappe Green"
            SettingsOptions.Themes.FRAPPE_TEAL -> "Frappe Teal"
            SettingsOptions.Themes.FRAPPE_SKY -> "Frappe Sky"
            SettingsOptions.Themes.FRAPPE_SAPPHIRE -> "Frappe Sapphire"
            SettingsOptions.Themes.FRAPPE_BLUE -> "Frappe Blue"
            SettingsOptions.Themes.FRAPPE_LAVENDER -> "Frappe Lavender"
            SettingsOptions.Themes.MACCHIATO_ROSEWATER -> "Macchiato Rosewater"
            SettingsOptions.Themes.MACCHIATO_FLAMINGO -> "Macchiato Flamingo"
            SettingsOptions.Themes.MACCHIATO_PINK -> "Macchiato Pink"
            SettingsOptions.Themes.MACCHIATO_MAUVE -> "Macchiato Mauve"
            SettingsOptions.Themes.MACCHIATO_RED -> "Macchiato Red"
            SettingsOptions.Themes.MACCHIATO_MAROON -> "Macchiato Maroon"
            SettingsOptions.Themes.MACCHIATO_PEACH -> "Macchiato Peach"
            SettingsOptions.Themes.MACCHIATO_YELLOW -> "Macchiato Yellow"
            SettingsOptions.Themes.MACCHIATO_GREEN -> "Macchiato Green"
            SettingsOptions.Themes.MACCHIATO_TEAL -> "Macchiato Teal"
            SettingsOptions.Themes.MACCHIATO_SKY -> "Macchiato Sky"
            SettingsOptions.Themes.MACCHIATO_SAPPHIRE -> "Macchiato Sapphire"
            SettingsOptions.Themes.MACCHIATO_BLUE -> "Macchiato Blue"
            SettingsOptions.Themes.MACCHIATO_LAVENDER -> "Macchiato Lavender"
            SettingsOptions.Themes.MOCHA_ROSEWATER -> "Mocha Rosewater"
            SettingsOptions.Themes.MOCHA_FLAMINGO -> "Mocha Flamingo"
            SettingsOptions.Themes.MOCHA_PINK -> "Mocha Pink"
            SettingsOptions.Themes.MOCHA_MAUVE -> "Mocha Mauve"
            SettingsOptions.Themes.MOCHA_RED -> "Mocha Red"
            SettingsOptions.Themes.MOCHA_MAROON -> "Mocha Maroon"
            SettingsOptions.Themes.MOCHA_PEACH -> "Mocha Peach"
            SettingsOptions.Themes.MOCHA_YELLOW -> "Mocha Yellow"
            SettingsOptions.Themes.MOCHA_GREEN -> "Mocha Green"
            SettingsOptions.Themes.MOCHA_TEAL -> "Mocha Teal"
            SettingsOptions.Themes.MOCHA_SKY -> "Mocha Sky"
            SettingsOptions.Themes.MOCHA_SAPPHIRE -> "Mocha Sapphire"
            SettingsOptions.Themes.MOCHA_BLUE -> "Mocha Blue"
            SettingsOptions.Themes.MOCHA_LAVENDER -> "Mocha Lavender"
            else -> "n/a"
        }
    }
}