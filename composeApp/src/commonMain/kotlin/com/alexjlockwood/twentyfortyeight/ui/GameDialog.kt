package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GameDialog(
    title: String,
    message: String,
    onConfirmListener: () -> Unit,
    onDismissListener: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    // TODO: update this to match iOS style dialog on iOS
    AlertDialog(
        modifier = modifier,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = { TextButton(onClick = { onConfirmListener() }) { Text("OK") } },
        dismissButton = onDismissListener?.let { { TextButton(onClick = it) { Text("Cancel") } } },
        onDismissRequest = { onDismissListener?.invoke() },
    )
}
