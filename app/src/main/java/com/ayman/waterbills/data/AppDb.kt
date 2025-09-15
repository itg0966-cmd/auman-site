
package com.ayman.waterbills.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Building::class, Tenant::class, MeterRecord::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun buildingDao(): BuildingDao
    abstract fun tenantDao(): TenantDao
    abstract fun meterDao(): MeterDao

    companion object {
        fun create(ctx: Context) = Room.databaseBuilder(ctx, AppDb::class.java, "waterbills.db").build()
    }
}
