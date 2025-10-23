# Güvenlik Notları (Security Notes)

## Genel Güvenlik Önerileri

### 1. Rate Limiting (Oran Sınırlama)

Şu anda bu uygulama temel bir kişisel tanıtım web sitesidir ve rate limiting içermemektedir. Production ortamında kullanmadan önce, DDoS saldırılarını önlemek için rate limiting eklemeniz önerilir.

**Önerilen Çözüm:**
```javascript
const rateLimit = require('express-rate-limit');

const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 dakika
    max: 100 // 100 istek limit
});

app.use(limiter);
```

### 2. Production Ortamı İçin Öneriler

- **HTTPS Kullanın**: Let's Encrypt ile ücretsiz SSL sertifikası alabilirsiniz
- **Environment Variables**: Hassas bilgileri `.env` dosyasında saklayın
- **CORS Ayarları**: Sadece güvendiğiniz domainlerden erişime izin verin
- **Input Validation**: İletişim formu için input doğrulama ekleyin
- **Helmet.js**: HTTP header güvenliği için helmet middleware ekleyin

### 3. İletişim Formu

Şu anda iletişim formu sadece console'a log yazmaktadır. Gerçek bir e-posta servisi entegre etmek için:

- SendGrid
- Nodemailer
- AWS SES

gibi servisleri kullanabilirsiniz.

### 4. Güvenlik Kontrolleri

- Düzenli olarak `npm audit` çalıştırın
- Bağımlılıkları güncel tutun
- `npm audit fix` ile otomatik güvenlik yamalarını uygulayın

## Bildirilen Güvenlik Sorunları

### CodeQL Analiz Sonuçları

**Alert: Missing Rate Limiting**
- **Lokasyon**: `server.js` line 95-97
- **Açıklama**: Ana sayfa route'u rate limiting içermiyor
- **Risk Seviyesi**: Düşük (kişisel web sitesi için)
- **Durum**: Dokumentasyon eklendi
- **Çözüm**: Production için yukarıdaki rate limiting örneğini kullanın

## İletişim

Güvenlik açığı bildirmek için lütfen repository sahibiyle iletişime geçin.
