package com.alexjlockwood.twentyfortyeight.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.alexjlockwood.twentyfortyeight.R

@Composable
fun GameDialog(
    title: String,
    message: String,
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit,
    positiveBtn: Int = R.string.dialog_positive,
    negativeBtn: Int = R.string.dialog_negative
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = { onConfirmListener.invoke() }) {
                Text(
                    stringResource(
                        positiveBtn
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissListener.invoke() }) {
                Text(
                    stringResource(
                        negativeBtn
                    )
                )
            }
        },
        onDismissRequest = { onDismissListener.invoke() },
    )
}
