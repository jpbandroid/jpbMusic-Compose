package com.jpb.music.compose.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.layoutId
import androidx.navigation.NavHostController
import com.jpb.music.compose.R
import com.jpb.music.compose.frontend.navigation.goBack

@Composable
fun Toolbar(
    navController: NavHostController,
    endContent: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable { navController.goBack() }
                .layoutId("toolbar"),
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            endContent()
        }
    }
}

@Composable
fun FullscreenDialogToolbar(
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable { onCancelClick() }
                .layoutId("toolbar"),
            painter = painterResource(id = R.drawable.close),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryButton(text = stringResource(id = R.string.save)) { onSaveClick() }
        }
    }
}