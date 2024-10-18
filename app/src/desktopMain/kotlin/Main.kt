import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.alexjlockwood.twentyfortyeight.App
import com.alexjlockwood.twentyfortyeight.domain.UserData
import com.alexjlockwood.twentyfortyeight.repository.GameRepository
import com.alexjlockwood.twentyfortyeight.repository.USER_DATA_FILE_NAME
import io.github.xxfast.kstore.file.storeOf
import net.harawata.appdirs.AppDirsFactory
import okio.FileSystem
import okio.Path.Companion.toPath

private const val PACKAGE_NAME = "com.alexjlockwood.twentyfortyeightcompose"
private const val VERSION = "1.0.0"
private const val AUTHOR = "alexjlockwood"

fun main() = application {
    val filesDir = AppDirsFactory.getInstance().getUserDataDir(PACKAGE_NAME, VERSION, AUTHOR).toPath()
    with(FileSystem.SYSTEM) { if (!exists(filesDir)) createDirectories(filesDir) }
    val store = storeOf(file = filesDir.resolve(USER_DATA_FILE_NAME), default = UserData.EMPTY_USER_DATA)

    Window(
        onCloseRequest = ::exitApplication,
        title = "2048 Compose",
    ) {
        App(
            repository = GameRepository(store),
        )
    }
}
