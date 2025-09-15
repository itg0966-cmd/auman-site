
package com.ayman.waterbills.print

import com.ayman.waterbills.data.MeterRecord

private fun shell(title: String, tag: String, body: String) = """
<!doctype html><html dir="rtl" lang="ar"><meta charset="utf-8">
<style>
  :root{--c1:#0e2740;--c2:#1d3b63}
  body{font-family:Arial,system-ui;margin:24px;color:#122}
  .hdr{display:flex;justify-content:space-between;align-items:center;margin-bottom:12px}
  .title{font-size:22px;font-weight:800}
  .tag{background:#e8f0ff;border:1px solid #b9c6e6;padding:6px 10px;border-radius:10px;font-weight:700}
  table{width:100%;border-collapse:collapse}
  th{background:#1d3b63;color:#fff}
  th,td{border:1px solid #9bb0d7;padding:8px;text-align:center}
  tbody tr:nth-child(even){background:#f4f7ff}
  .foot{position:fixed;left:0;right:0;bottom:0;text-align:center;opacity:.75;font-size:12px;padding:4px 0}
  @page{margin:18mm}
</style>
<body>
  <div class="hdr"><div class="title">$title</div><div class="tag">$tag</div></div>
  $body
  <div class="foot">v1.0 — تم تطوير التطبيق عن طريق أيمن الشقاري</div>
</body></html>
""".trimIndent()

fun invoiceHtml(r: MeterRecord, title: String, m: Int, y: Int): String {
    val body = """
<table>
 <thead>
  <tr>
   <th>القراءة السابقة</th><th>القراءة الحالية</th><th>الفرق</th>
   <th>سعر الوحدة</th><th>قيمة استهلاك الفترة</th><th>متأخرات</th><th>المبلغ المستحق</th>
   <th>المدفوع</th><th>المتبقي</th><th>حوّلت لأبي</th><th>تاريخ التحويل</th>
  </tr>
 </thead>
 <tbody>
  <tr>
   <td>${r.prevReading}</td><td>${r.currReading}</td><td>${r.diff}</td>
   <td>${r.unitPrice}</td><td>${r.periodValue}</td><td>${r.arrears}</td><td>${r.dueAmount}</td>
   <td>${r.paid}</td><td>${(r.dueAmount - r.paid).coerceAtLeast(0)}</td>
   <td>${if (r.toDad) "✔︎" else "—"}</td><td>${if (r.dadDate.isBlank()) "—" else r.dadDate}</td>
  </tr>
 </tbody>
</table>
""".trimIndent()
    return shell(title, "$y/$m", body)
}
