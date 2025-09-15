
package com.ayman.waterbills.data

suspend fun seedIfEmpty(db: AppDb) {
    if (db.buildingDao().all().isNotEmpty()) return
    db.buildingDao().upsertAll(
        listOf(
            Building("airport", "عمارة المطار", 7),
            Building("wadi", "عمارة وادي أحمد", 9),
            Building("zayed", "عمارة زايد", 12),
            Building("popular", "البيت الشعبي", 1)
        )
    )
}
