# Hızlı Başlangıç Kılavuzu (Quick Start Guide)

## 🚀 5 Dakikada Başlayın

### 1. Kurulum
```bash
npm install
npm start
```

Tarayıcınızda `http://localhost:3000` adresini açın.

### 2. Kişisel Bilgilerinizi Ekleyin

`server.js` dosyasını açın ve `personalInfo` nesnesini düzenleyin:

```javascript
const personalInfo = {
    name: 'Mert Sara',  // Adınızı buraya yazın
    title: 'Full Stack Developer',  // Mesleğinizi yazın
    bio: 'Yazılım geliştirmeye tutkulu...',  // Kendinizi tanıtın
    email: 'mert@example.com',
    phone: '+90 555 123 45 67',
    location: 'İstanbul, Türkiye',
    // ... devamı
};
```

### 3. Profil Resminizi Ekleyin

1. Profil resminizi `public/images/` klasörüne kopyalayın (örn: `profile.jpg`)
2. `public/index.html` dosyasında 50. satırı bulun:
```html
<img src="images/profile-placeholder.svg" alt="Profile Picture">
```
3. Resim yolunu değiştirin:
```html
<img src="images/profile.jpg" alt="Profile Picture">
```

### 4. Sosyal Medya Hesaplarınızı Ekleyin

`server.js` dosyasında sosyal medya bağlantılarınızı güncelleyin:

```javascript
socialMedia: {
    linkedin: 'https://linkedin.com/in/kullaniciadi',
    github: 'https://github.com/kullaniciadi',
    twitter: 'https://twitter.com/kullaniciadi',
    instagram: 'https://instagram.com/kullaniciadi'
}
```

### 5. Yeteneklerinizi Ekleyin

```javascript
skills: [
    'JavaScript',
    'React',
    'Node.js',
    'Python',
    'MongoDB'
]
```

### 6. İş Deneyiminizi Ekleyin

```javascript
experience: [
    {
        title: 'Senior Developer',
        company: 'ABC Şirketi',
        period: '2020 - Günümüz',
        description: 'Web uygulamaları geliştirme...'
    }
]
```

### 7. Eğitim Bilgilerinizi Ekleyin

```javascript
education: [
    {
        degree: 'Bilgisayar Mühendisliği',
        school: 'İstanbul Teknik Üniversitesi',
        period: '2014 - 2018',
        description: 'Yazılım Mühendisliği'
    }
]
```

### 8. Projelerinizi Ekleyin

```javascript
projects: [
    {
        title: 'E-Ticaret Sitesi',
        description: 'React ve Node.js ile geliştirilmiş...',
        link: 'https://github.com/kullaniciadi/proje'
    }
]
```

## 🎨 Renkleri Değiştirme (İsteğe Bağlı)

`public/css/style.css` dosyasının başındaki CSS değişkenlerini düzenleyin:

```css
:root {
    --primary-color: #2563eb;  /* Ana renk */
    --secondary-color: #1e40af;  /* İkincil renk */
    --accent-color: #3b82f6;  /* Vurgu rengi */
}
```

## ✅ Tamamlandı!

Sunucuyu yeniden başlatın ve değişikliklerinizi görün:

```bash
# Ctrl+C ile durdurun
npm start
```

## 💡 İpuçları

- Her değişiklikten sonra tarayıcıyı yenileyin (F5)
- Resimleri optimize edin (max 500KB önerilir)
- Sosyal medya linklerini güncel tutun
- Düzenli olarak içeriği güncelleyin

## 📚 Daha Fazla Bilgi

Detaylı bilgi için `README.md` dosyasına bakın.
