// 🟠 نسخة جاهزة من Service Worker لتشغيل موقعك أوفلاين
const CACHE_NAME = 'ayman-cache-v1';

// الملفات الأساسية اللي تنحفظ في الكاش (ضيف ملفاتك هنا لو عندك CSS/JS/صور)
const ASSETS = [
  './',
  './index.html',
  // './style.css',
  // './app.js',
  // './images/logo.png',
];

// عند التثبيت: نخزن الملفات المهمة
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => cache.addAll(ASSETS))
      .then(() => self.skipWaiting())
  );
});

// عند التفعيل: نحذف أي كاش قديم
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(
        keys.filter((k) => k !== CACHE_NAME).map((k) => caches.delete(k))
      )
    ).then(() => self.clients.claim())
  );
});

// عند أي طلب: نحاول من الشبكة أولاً، ولو فشل نجيب من الكاش
self.addEventListener('fetch', (event) => {
  const req = event.request;

  // لو الطلب HTML → شبكة أولاً
  if (req.headers.get('accept')?.includes('text/html')) {
    event.respondWith(
      fetch(req).then((res) => {
        const resClone = res.clone();
        caches.open(CACHE_NAME).then((cache) => cache.put(req, resClone));
        return res;
      }).catch(() => caches.match(req).then(r => r || caches.match('./index.html')))
    );
    return;
  }

  // باقي الملفات → كاش أولاً
  event.respondWith(
    caches.match(req).then((cached) => {
      if (cached) return cached;
      return fetch(req).then((res) => {
        if (req.method === 'GET' && res.ok) {
          const resClone = res.clone();
          caches.open(CACHE_NAME).then((cache) => cache.put(req, resClone));
        }
        return res;
      });
    })
  );
});
