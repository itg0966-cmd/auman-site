
package com.ayman.waterbills.repo

import com.ayman.waterbills.data.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Repo(val db: AppDb) {
    private val b = db.buildingDao()
    private val t = db.tenantDao()
    private val m = db.meterDao()

    suspend fun buildings() = b.all()

    suspend fun month(building: Building, y: Int, mo: Int): List<MeterRecord> {
        val map = m.monthOf(building.id, y, mo).associateBy { it.aptNo }.toMutableMap()
        for (i in 1..building.apts) if (!map.containsKey(i)) {
            map[i] = MeterRecord(building.id, i, y, mo)
        }
        return (1..building.apts).map { map[it]!! }
    }

    suspend fun tenantName(bid: String, apt: Int) =
        t.get(bid, apt)?.name ?: "مستأجر $apt"

    suspend fun setTenantName(bid: String, apt: Int, name: String) {
        val old = t.get(bid, apt) ?: Tenant(bid, apt)
        t.upsert(old.copy(name = name))
    }

    suspend fun setTenantPhone(bid: String, apt: Int, phone: String) {
        val old = t.get(bid, apt) ?: Tenant(bid, apt)
        t.upsert(old.copy(phone = phone))
    }

    suspend fun tenantPhone(bid: String, apt: Int) = t.get(bid, apt)?.phone ?: ""

    suspend fun save(r: MeterRecord) {
        val fixed = if (r.fullPaid) r.copy(paid = r.dueAmount) else r
        m.upsert(fixed)
    }

    suspend fun sums(bid: String, y: Int, mth: Int): Triple<Int, Int, Int> {
        val due = m.sumDue(bid, y, mth)
        val paid = m.sumPaid(bid, y, mth)
        return Triple(due, paid, (due - paid).coerceAtLeast(0))
    }

    fun today(): String {
        val d = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return "${d.year}-${d.monthNumber.toString().padStart(2,'0')}-${d.dayOfMonth.toString().padStart(2,'0')}"
    }
}
