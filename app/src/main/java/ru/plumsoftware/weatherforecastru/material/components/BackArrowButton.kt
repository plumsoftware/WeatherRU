package ru.plumsoftware.weatherforecastru.material.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import ru.plumsoftware.weatherforecast.R
import ru.plumsoftware.weatherforecastru.application.App
import ru.plumsoftware.weatherforecastru.material.extensions.ExtensionSize

@Composable
fun BackArrowButton(modifier: Modifier, enabled: Boolean = true, onClick: () -> Unit) {
    with(MaterialTheme) {
        IconButton(
            modifier = Modifier
                .size(size = ExtensionSize.IconSize._20dp)
                .clip(shape = shapes.extraLarge)
                .then(other = modifier),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.Transparent
            ),
            onClick = onClick,
            enabled = enabled
        ) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = App.INSTANCE.getString(R.string.back_arrow_icon_description)
            )
        }
    }
}