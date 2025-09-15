
package com.ayman.waterbills.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayman.waterbills.data.*
import com.ayman.waterbills.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class UiBuilding(val id: String, val name: String, val apts: Int)

data class UiState(
    val buildings: List<UiBuilding> = emptyList(),
    val selected: UiBuilding? = null,
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val query: String = "",
    val rows: List<MeterRecord> = emptyList(),
    val totalDue: Int = 0,
    val totalPaid: Int = 0,
    val totalRest: Int = 0,
    val toast: String? = null
)

class MainViewModel(private val repo: Repo): ViewModel() {
    private val _ui = MutableStateFlow(UiState())
    val ui = _ui.asStateFlow()

    init { viewModelScope.launch {
        seedIfEmpty(repo.db)
        val bs = repo.buildings().map { UiBuilding(it.id, it.name, it.apts) }
        val sel = bs.firstOrNull()
        _ui.value = _ui.value.copy(buildings = bs, selected = sel)
        refresh()
    } }

    fun setMonth(m: Int) { _ui.value = _ui.value.copy(month = m); refresh() }
    fun setYear(y: Int) { _ui.value = _ui.value.copy(year = y); refresh() }
    fun setBuilding(id: String) {
        val b = _ui.value.buildings.firstOrNull { it.id == id }
        _ui.value = _ui.value.copy(selected = b); refresh()
    }
    fun setQuery(q: String) { _ui.value = _ui.value.copy(query = q); refresh() }

    fun refresh() = viewModelScope.launch {
        val b = _ui.value.selected ?: return@launch
        val rows = repo.month(Building(b.id, b.name, b.apts), _ui.value.year, _ui.value.month)
        val filtered = rows.filter {
            _ui.value.query.isBlank() || it.aptNo.toString().contains(_ui.value.query, true)
        }
        val (d,p,r) = repo.sums(b.id, _ui.value.year, _ui.value.month)
        _ui.value = _ui.value.copy(rows = filtered, totalDue = d, totalPaid = p, totalRest = r)
    }

    fun save(r: MeterRecord) = viewModelScope.launch { repo.save(r); _ui.value = _ui.value.copy(toast = "تم الحفظ ✅"); refresh() }

    suspend fun tenantName(apt: Int) : String {
        val b = _ui.value.selected ?: return "مستأجر $apt"
        return repo.tenantName(b.id, apt)
    }
    suspend fun tenantPhone(apt: Int) : String {
        val b = _ui.value.selected ?: return ""
        return repo.tenantPhone(b.id, apt)
    }

    fun today() = repo.today()
}
