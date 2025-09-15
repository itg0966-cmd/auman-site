
package com.ayman.waterbills.data

import androidx.room.*

@Dao
interface BuildingDao {
    @Query("SELECT * FROM Building")
    suspend fun all(): List<Building>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<Building>)
}

@Dao
interface TenantDao {
    @Query("SELECT * FROM Tenant WHERE buildingId=:b AND aptNo=:apt")
    suspend fun get(b: String, apt: Int): Tenant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(t: Tenant)
}

@Dao
interface MeterDao {
    @Query("SELECT * FROM MeterRecord WHERE buildingId=:b AND year=:y AND month=:m ORDER BY aptNo")
    suspend fun monthOf(b: String, y: Int, m: Int): List<MeterRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(r: MeterRecord)

    @Query("SELECT COALESCE(SUM(dueAmount),0) FROM MeterRecord WHERE buildingId=:b AND year=:y AND month=:m")
    suspend fun sumDue(b: String, y: Int, m: Int): Int

    @Query("SELECT COALESCE(SUM(paid),0) FROM MeterRecord WHERE buildingId=:b AND year=:y AND month=:m")
    suspend fun sumPaid(b: String, y: Int, m: Int): Int
}
