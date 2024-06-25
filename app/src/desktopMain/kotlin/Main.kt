import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alexjlockwood.twentyfortyeight.App
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import net.harawata.appdirs.AppDirsFactory
import okio.FileSystem
import okio.Path.Companion.toPath

private const val PACKAGE_NAME = "com.alexjlockwood.twentyfortyeightcompose"
private const val VERSION = "1.0.0"
private const val AUTHOR = "alexjlockwood"

fun main() = application {
    val filesDir = AppDirsFactory.getInstance().getUserDataDir(PACKAGE_NAME, VERSION, AUTHOR).toPath()
    if (!filesDir.toFile().exists()) {
        FileSystem.SYSTEM.createDirectories(filesDir)
    }
    val store = getStore(filesDir.resolve(USER_DATA_FILE_NAME))

    Window(
        onCloseRequest = ::exitApplication,
        title = "2048 Compose",
    ) {
        App(
            repository = GameRepository(store),
        )
    }
}
