import com.alexjlockwood.twentyfortyeight.domain.UserData
import io.github.xxfast.kstore.KStore
import okio.Path

expect fun getStore(filePath: Path): KStore<UserData>
