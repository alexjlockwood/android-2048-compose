import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.alexjlockwood.twentyfortyeight.App
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import okio.Path.Companion.toPath

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val store = getStore(USER_DATA_FILE_NAME.toPath())
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App(
            repository = GameRepository(store),
        )
    }
}
