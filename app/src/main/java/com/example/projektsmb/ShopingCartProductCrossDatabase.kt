import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projektsmb.CartProductCrossRef
import com.example.projektsmb.CartProductCrossRefDao

@Database(entities = [CartProductCrossRef::class], version = 1)
abstract class ShopingCartProductCrossDatabase : RoomDatabase() {
    abstract val shopingCartProductCrossDao: CartProductCrossRefDao
}
