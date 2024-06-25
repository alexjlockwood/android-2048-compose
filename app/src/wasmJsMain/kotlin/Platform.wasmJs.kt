import com.alexjlockwood.twentyfortyeight.domain.UserData
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import okio.Path

actual fun getStore(filePath: Path): KStore<UserData> {
    return storeOf(key = filePath.name, default = UserData.EMPTY_USER_DATA)
}
