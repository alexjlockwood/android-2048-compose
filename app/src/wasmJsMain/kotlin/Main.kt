import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.alexjlockwood.twentyfortyeight.App
import com.alexjlockwood.twentyfortyeight.domain.UserData
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import io.github.xxfast.kstore.storage.storeOf

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val store = storeOf(key = USER_DATA_FILE_NAME, default = UserData.EMPTY_USER_DATA)
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App(
            repository = GameRepository(store),
        )
    }
}
