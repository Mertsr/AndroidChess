# Hızlı Başlangıç Kılavuzu

## 🎯 3 Adımda APK Oluştur

### 1️⃣ Android Studio'yu İndir ve Kur
- https://developer.android.com/studio adresinden indir
- Kurulumu tamamla
- İlk açılışta SDK'ları indir (otomatik)

### 2️⃣ Projeyi Aç
1. Android Studio'yu aç
2. "Open" seçeneğine tıkla
3. Bu proje klasörünü seç
4. "OK" tıkla
5. Gradle sync'in bitmesini bekle (5-10 dk)

### 3️⃣ APK Oluştur

**Test APK (Basit):**
- Menu: Build → Build Bundle(s) / APK(s) → Build APK(s)
- Bildirimde "locate" tıkla
- APK hazır: `app/build/outputs/apk/debug/app-debug.apk`

**Yayın APK (İmzalı):**
- Menu: Build → Generate Signed Bundle / APK
- APK seç → Next
- Create new... (keystore oluştur)
- Şifre belirle (unutma!)
- Next → release seç → Finish
- APK hazır: `app/build/outputs/apk/release/app-release.apk`

## 📱 APK'yı Yükle

**En Kolay Yol:**
1. APK'yı telefona kopyala (USB/email)
2. Telefonda APK dosyasına dokun
3. "Bilinmeyen Kaynaklar" iznini ver
4. Yükle'ye tıkla
5. Bitti! 🎉

**ADB ile:**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 🎮 Oyun Nasıl Oynanır

1. Uygulamayı aç
2. Zorluk seviyesini seç (Kolay → Uzman)
3. Beyaz taşları oynarsın (sen başlarsın)
4. Bir taşa dokun (seçilir)
5. Hamle yapmak istediğin kareye dokun
6. Robot otomatik cevap verir
7. Oyunu kazan! 🏆

## ❓ Sorun mu Var?

**Gradle sync hatası:**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

**SDK bulunamadı:**
- Android Studio: Tools → SDK Manager → SDK'ları kur

**Detaylı yardım için:**
- `BUILD_GUIDE.md` oku (detaylı rehber)
- `README.md` oku (proje dokümantasyonu)

## 📚 Dosyalar

- `README.md` - Tam proje dokümantasyonu
- `BUILD_GUIDE.md` - Detaylı APK oluşturma rehberi
- `PYTHON_TO_ANDROID.py` - Python'dan Android'e dönüşüm açıklaması
- `QUICKSTART.md` - Bu dosya

## 🚀 İleri Seviye

**Komut satırından APK oluştur:**
```bash
./gradlew assembleDebug
```

**Google Play Store'a yükle:**
```bash
./gradlew bundleRelease
# AAB dosyası: app/build/outputs/bundle/release/app-release.aab
```

---

**İPUCU:** İlk kez yapıyorsan Android Studio kullan. Çok daha kolay! ✨
