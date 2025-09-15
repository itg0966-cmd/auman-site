// ===== Service Worker (بدون ASSETS) =====
const BASE_PATH  = "/auman-site";       // عدّل لو المستودع غير هذا الاسم
const CACHE_NAME = "auman-site-v1";     // زِد الرقم عند كل تعديل كبير

// ثبّت SW وحمّل نسخة أولية من الصفحة (اختياري لكن مفيد للأوفلاين)
self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(c =>
      c.addAll([
        `${BASE_PATH}/`,
        `${BASE_PATH}/index.html`,
      ]).catch(() => null) // لو فشل التحميل، نكمل عادي
    )
  );
  self.skipWaiting();
});

// نظّف الكاشات القديمة
self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then(keys =>
      Promise.all(keys.map(k => (k === CACHE_NAME ? null : caches.delete(k))))
    )
  );
  self.clients.claim();
});

// استراتيجية:
// - لصفحات HTML (التنقل): Network-first مع مهلة، ثم الكاش ثم index.html
// - لباقي طلبات نفس النطاق: Cache-first ثم الشبكة وتحديث الكاش
self.addEventListener("fetch", (event) => {
  const req = event.request;
  const url = new URL(req.url);

  // نتعامل فقط مع نفس النطاق
  if (url.origin !== location.origin) return;

  // طلبات HTML / تنقل
  const isPage =
    req.mode === "navigate" ||
    (req.headers.get("accept") || "").includes("text/html");

  if (isPage) {
    event.respondWith(networkFirst(req));
    return;
  }

  // باقي الملفات (css/js/img…)
  if (req.method === "GET") {
    event.respondWith(cacheFirst(req));
  }
});

async function networkFirst(req) {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 4000); // 4 ثواني

  try {
    const fresh = await fetch(req, { signal: controller.signal });
    clearTimeout(timeoutId);
    const cache = await caches.open(CACHE_NAME);
    cache.put(req, fresh.clone());
    return fresh;
  } catch (e) {
    clearTimeout(timeoutId);
    const cached = await caches.match(req);
    if (cached) return cached;
    // fallback للصفحة الرئيسية
    return caches.match(`${BASE_PATH}/index.html`);
  }
}

async function cacheFirst(req) {
  const cached = await caches.match(req);
  if (cached) return cached;
  const res = await fetch(req);
  const cache = await caches.open(CACHE_NAME);
  cache.put(req, res.clone());
  return res;
}
