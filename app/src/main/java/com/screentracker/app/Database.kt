package com.screentracker.app

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

enum class EventType {
    SCREEN_ON,
    USER_PRESENT,
    FLASHLIGHT_ON,
    FLASHLIGHT_OFF
}

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: EventType,
    val timestamp: Long
)

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: Event)
    
    @Query("SELECT * FROM events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<Event>>
    
    @Query("SELECT * FROM events WHERE type = :type ORDER BY timestamp DESC")
    fun getEventsByType(type: EventType): Flow<List<Event>>
    
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
    
    @Query("SELECT COUNT(*) FROM events WHERE type = :type")
    suspend fun getEventCount(type: EventType): Int
}

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    
    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null
        
        fun getInstance(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "screen_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromEventType(value: EventType): String {
        return value.name
    }
    
    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }
}
