
package com.ayman.waterbills.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class Building(
    @PrimaryKey val id: String,
    val name: String,
    val apts: Int
)

@Entity(primaryKeys = ["buildingId", "aptNo"])
data class Tenant(
    val buildingId: String,
    val aptNo: Int,
    val name: String = "مستأجر",
    val phone: String = ""
)

@Entity(primaryKeys = ["buildingId", "aptNo", "year", "month"])
@Serializable
data class MeterRecord(
    val buildingId: String,
    val aptNo: Int,
    val year: Int,
    val month: Int,
    val prevReading: Int = 0,
    val currReading: Int = 0,
    val unitPrice: Int = 0,
    val periodValue: Int = 0,
    val arrears: Int = 0,
    val dueAmount: Int = 0,
    val paid: Int = 0,
    val fullPaid: Boolean = false,
    val note: String = "",
    val payDate: String = "",
    val toDad: Boolean = false,
    val dadDate: String = ""
) {
    val diff: Int get() = (currReading - prevReading).coerceAtLeast(0)
    val rest: Int get() = (dueAmount - paid).coerceAtLeast(0)
}
