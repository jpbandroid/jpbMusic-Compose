package com.jpb.music.compose.frontend.screens.setup.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.backend.utils.isAtLeastAndroid13
import com.jpb.music.compose.frontend.composables.HSpacer
import com.jpb.music.compose.frontend.composables.PrimaryButton
import com.jpb.music.compose.frontend.composables.VDivider
import com.jpb.music.compose.frontend.composables.VSpacer
import com.jpb.music.compose.frontend.navigation.goBack
import com.jpb.music.compose.frontend.navigation.goToLightTheme
import com.jpb.music.compose.frontend.utils.ChangeNavigationBarsColor
import com.jpb.music.compose.frontend.utils.ChangeStatusBarColor
import com.jpb.music.compose.frontend.utils.Sizes
import com.jpb.music.compose.frontend.utils.modifyIf

@Composable
fun PermissionsScreen(
    navController: NavHostController
) {

    val vm: PermissionsScreenVM = viewModel()
    val storagePermissionGranted = vm.storagePermissionGranted.collectAsState().value
    val notificationsPermissionGranted = vm.notificationsPermissionGranted.collectAsState().value

    val storagePermissionlauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> vm.updateStoragePermissionGranted(granted) }
    )

    val notificationPermissionlauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> vm.updateNotificationsPermissionGranted(granted) }
    )

    ChangeNavigationBarsColor(color = MaterialTheme.colorScheme.surface)
    ChangeStatusBarColor(color = MaterialTheme.colorScheme.surface)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Sizes.LARGE)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.permissions),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
            }

            VSpacer(size = Sizes.LARGE)

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

                PermissionCard(
                    checked = storagePermissionGranted,
                    iconId = R.drawable.folder,
                    description = stringResource(R.string.EnableStorageExplanation)
                ) {
                    if (isAtLeastAndroid13()) {
                        storagePermissionlauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                    } else {
                        storagePermissionlauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }

                if (isAtLeastAndroid13()) {

                    PermissionCard(
                        checked = notificationsPermissionGranted,
                        iconId = R.drawable.notification,
                        description = stringResource(R.string.EnableNotificationExplanation)
                    ) {
                        notificationPermissionlauncher.launch((Manifest.permission.POST_NOTIFICATIONS))
                    }
                }
            }

        }

        VSpacer(size = Sizes.LARGE)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            PrimaryButton(text = stringResource(id = R.string.back)) {
                navController.goBack()
            }

            HSpacer(size = Sizes.MEDIUM)

            PrimaryButton(
                text = stringResource(id = R.string.next),
                disabled = !vm.hasAllPermissions()
            ) {
                navController.goToLightTheme()
            }
        }
    }
}

@Composable
fun PermissionCard(
    checked: Boolean,
    iconId: Int,
    description: String,
    onCheckedChange: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .modifyIf(!checked) {
                clickable { onCheckedChange() }
            }
            .padding(Sizes.LARGE),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HSpacer(size = Sizes.LARGE)

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        HSpacer(size = Sizes.SMALL)

        Switch(checked = checked, onCheckedChange = { if (!checked) onCheckedChange() })
    }
}
