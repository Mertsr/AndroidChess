# Kişisel Tanıtım Web Sitesi

Modern ve responsive bir kişisel tanıtım web sitesi. Front-end ve back-end ile birlikte gelir.

## 🚀 Özellikler

- **Responsive Tasarım**: Tüm cihazlarda mükemmel görünüm
- **Modern UI/UX**: Gradient renkler ve smooth animasyonlar
- **API Entegrasyonu**: Express.js ile RESTful API
- **Dinamik İçerik**: JavaScript ile dinamik olarak yüklenen içerikler
- **İletişim Formu**: Ziyaretçilerin sizinle iletişime geçmesi için
- **Sosyal Medya Entegrasyonu**: LinkedIn, GitHub, Twitter, Instagram bağlantıları

## 📋 Gereksinimler

- Node.js (v14 veya üzeri)
- npm veya yarn

## 🔧 Kurulum

1. Bağımlılıkları yükleyin:
```bash
npm install
```

2. Sunucuyu başlatın:
```bash
npm start
```

3. Tarayıcınızda açın:
```
http://localhost:3000
```

## 🎨 Kişiselleştirme

### 1. Profil Resmini Değiştirme

`public/images/` klasörüne kendi profil resminizi ekleyin ve `public/index.html` dosyasında günceleyin:

```html
<img src="images/sizin-resminiz.jpg" alt="Profile Picture" id="profile-image">
```

### 2. Kişisel Bilgileri Güncelleme

`server.js` dosyasındaki `personalInfo` nesnesini düzenleyin:

```javascript
const personalInfo = {
    name: 'Adınız Soyadınız',
    title: 'Mesleğiniz veya Ünvanınız',
    bio: 'Kendiniz hakkında bilgi...',
    email: 'email@example.com',
    phone: '+90 XXX XXX XX XX',
    location: 'Şehir, Türkiye',
    // ... diğer bilgiler
};
```

### 3. Sosyal Medya Hesaplarını Ekleme

```javascript
socialMedia: {
    linkedin: 'https://linkedin.com/in/kullaniciadi',
    github: 'https://github.com/kullaniciadi',
    twitter: 'https://twitter.com/kullaniciadi',
    instagram: 'https://instagram.com/kullaniciadi'
}
```

### 4. Yetenekleri Ekleme

```javascript
skills: [
    'JavaScript',
    'React',
    'Node.js',
    'Python',
    'Git'
]
```

### 5. Deneyim Bilgilerini Ekleme

```javascript
experience: [
    {
        title: 'İş Ünvanı',
        company: 'Şirket Adı',
        period: '2020 - Günümüz',
        description: 'İş tanımı ve sorumluluklar...'
    }
]
```

### 6. Eğitim Bilgilerini Ekleme

```javascript
education: [
    {
        degree: 'Lisans / Yüksek Lisans',
        school: 'Üniversite Adı',
        period: '2014 - 2018',
        description: 'Bölüm veya uzmanlık alanı'
    }
]
```

### 7. Projelerinizi Ekleme

```javascript
projects: [
    {
        title: 'Proje Adı',
        description: 'Proje açıklaması ve kullanılan teknolojiler...',
        link: 'https://proje-linki.com'
    }
]
```

## 🎨 Renkleri Özelleştirme

`public/css/style.css` dosyasında CSS değişkenlerini düzenleyin:

```css
:root {
    --primary-color: #2563eb;
    --secondary-color: #1e40af;
    --accent-color: #3b82f6;
    /* ... diğer renkler */
}
```

## 📱 Bölümler

1. **Ana Sayfa (Hero)**: İlk karşılama ve tanıtım
2. **Hakkımda**: Detaylı biyografi ve iletişim bilgileri
3. **Yetenekler**: Teknik ve profesyonel yetenekler
4. **Deneyim**: İş deneyimi ve eğitim geçmişi
5. **Projeler**: Portföy ve projeler
6. **İletişim**: İletişim formu ve bilgileri

## 🔌 API Endpoints

- `GET /api/info` - Tüm kişisel bilgileri getirir
- `GET /api/info/:section` - Belirli bir bölümü getirir (örn: /api/info/skills)
- `POST /api/info` - Kişisel bilgileri günceller

## 🛠️ Geliştirme

Development modunda çalıştırmak için:

```bash
npm run dev
```

Bu komut nodemon kullanarak otomatik yeniden başlatma sağlar.

## 📦 Dosya Yapısı

```
.
├── server.js              # Express sunucu
├── package.json           # Proje bağımlılıkları
├── public/                # Statik dosyalar
│   ├── index.html        # Ana HTML dosyası
│   ├── css/
│   │   └── style.css     # Stil dosyası
│   ├── js/
│   │   └── script.js     # JavaScript dosyası
│   └── images/           # Resim dosyaları
└── README.md             # Bu dosya
```

## 🚀 Production'a Alma

1. Sunucuyu production modunda çalıştırın
2. PORT environment variable'ı ayarlayın
3. HTTPS kullanın
4. Static dosyaları CDN'e yükleyebilirsiniz

## 📄 Lisans

MIT

## 🤝 Katkıda Bulunma

Pull request'ler memnuniyetle karşılanır!