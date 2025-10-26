# Satranç Robotu (Chess Robot) - Android Uygulaması

Python satranç motorundan dönüştürülmüş Android satranç oyunu uygulaması.

## Özellikler

- ♟️ Tam işlevsel satranç oyunu
- 🤖 Yapay zeka destekli robot rakip
- 📊 4 farklı zorluk seviyesi (Kolay, Orta, Zor, Uzman)
- 🎨 Modern ve temiz kullanıcı arayüzü
- 🇹🇷 Türkçe dil desteği

## Zorluk Seviyeleri

1. **Kolay**: Rastgele hamle seçimi
2. **Orta**: Temel hamle değerlendirmesi ve yakalama stratejisi
3. **Zor**: Gelişmiş pozisyon değerlendirmesi
4. **Uzman**: Derin hamle analizi ve materyal dengelemesi

## Gereksinimler

- Android Studio Arctic Fox veya üzeri
- JDK 17 veya üzeri (Android Gradle Plugin 8.0 için gerekli)
- Android SDK 24 (Android 7.0) minimum
- Android SDK 34 (Android 14) hedef
- Gradle 8.0
- Android Gradle Plugin 8.0.2

## Kurulum ve APK Oluşturma

### 1. Projeyi Klonlayın

```bash
git clone https://github.com/Mertsr/Mert-Sara-.git
cd Mert-Sara-
```

### 2. Android Studio'da Açın

1. Android Studio'yu açın
2. "Open an Existing Project" seçeneğini tıklayın
3. Proje klasörünü seçin ve "OK" tıklayın
4. Gradle sync işleminin tamamlanmasını bekleyin

### 3. APK Oluşturma

#### Debug APK (Test için)

```bash
# Terminal'de proje dizininde:
./gradlew assembleDebug
```

APK dosyası burada oluşturulacak:
```
app/build/outputs/apk/debug/app-debug.apk
```

#### Release APK (Yayın için)

```bash
./gradlew assembleRelease
```

APK dosyası burada oluşturulacak:
```
app/build/outputs/apk/release/app-release.apk
```

**Not:** Release APK için imzalama gereklidir. İmzalamak için:

1. Android Studio'da: Build → Generate Signed Bundle / APK
2. APK seçeneğini seçin
3. Yeni bir keystore oluşturun veya mevcut olanı kullanın
4. Release build variant'ını seçin
5. Finish'e tıklayın

### 4. Cihaza Yükleme

#### ADB ile Yükleme

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Manuel Yükleme

1. APK dosyasını Android cihazınıza aktarın
2. Dosya yöneticisini açın
3. APK dosyasına tıklayın
4. Bilinmeyen kaynaklardan yüklemeye izin verin
5. Yükle'ye tıklayın

## Gradle Komutları

```bash
# Projeyi temizle
./gradlew clean

# Projeyi derle
./gradlew build

# Debug APK oluştur
./gradlew assembleDebug

# Release APK oluştur
./gradlew assembleRelease

# Testleri çalıştır
./gradlew test

# Bağımlılıkları güncelle
./gradlew dependencies
```

## Proje Yapısı

```
.
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/chessrobot/
│   │       │   ├── MainActivity.kt           # Ana aktivite
│   │       │   ├── ChessBoardView.kt        # Satranç tahtası görünümü
│   │       │   └── SimpleChessEngine.kt     # Satranç motoru
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml    # Ana ekran düzeni
│   │       │   ├── values/
│   │       │   │   ├── strings.xml          # Metin kaynakları
│   │       │   │   ├── colors.xml           # Renk paletleri
│   │       │   │   └── themes.xml           # Tema tanımları
│   │       │   └── mipmap/                  # Uygulama ikonları
│   │       └── AndroidManifest.xml          # Uygulama manifestosu
│   └── build.gradle                         # Uygulama yapılandırması
├── gradle/                                  # Gradle wrapper dosyaları
├── build.gradle                             # Proje yapılandırması
└── settings.gradle                          # Proje ayarları
```

## Kullanılan Teknolojiler

- **Kotlin**: Ana programlama dili
- **Android SDK**: Android uygulama geliştirme
- **Chesslib**: Satranç kuralları ve hamle doğrulama
- **Material Design**: Modern UI tasarımı
- **Coroutines**: Asenkron işlemler

## Nasıl Oynanır

1. Uygulamayı açın
2. Zorluk seviyesini seçin
3. Beyaz taşları oynatırsınız (her zaman siz başlarsınız)
4. Bir taşa dokunarak seçin
5. Hareket ettirmek istediğiniz kareye dokunun
6. Robot otomatik olarak cevap verecektir
7. Oyunu kazanan veya beraberlik ile sonuçlanan durumda bildirim alırsınız

## Özellik Detayları

### Satranç Motoru

Python kodunuza benzer şekilde, Android uygulaması da:
- Yasal hamleleri kontrol eder
- Farklı zorluk seviyelerinde AI hamlesi üretir
- Oyun durumunu (mat, beraberlik) tespit eder
- Hamle geçmişini tutar

### AI Stratejisi

- **Kolay**: Tamamen rastgele
- **Orta**: Yakalama hamleleri ve merkez kontrolü
- **Zor**: Top 3 hamleden birini seçme
- **Uzman**: Derin pozisyon analizi ve materyal değerlendirmesi

## Sorun Giderme

**APK oluştururken hata alıyorsanız, detaylı çözümler için [TROUBLESHOOTING_TR.md](TROUBLESHOOTING_TR.md) dosyasına bakın.**

Ayrıca detaylı build kılavuzu için: [BUILD_GUIDE.md](BUILD_GUIDE.md)

### Gradle Sync Hatası

```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### APK İmzalama Hatası

Release APK için keystore oluşturun:
```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

### Bağımlılık Hatası

`settings.gradle` dosyasında maven repository'lerin doğru olduğundan emin olun:
```gradle
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

## Katkıda Bulunma

1. Bu depoyu fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add amazing feature'`)
4. Branch'inizi push edin (`git push origin feature/amazing-feature`)
5. Pull Request açın

## Lisans

Bu proje açık kaynak kodludur.

## İletişim

Proje Sahibi: Mert & Sara
GitHub: [@Mertsr](https://github.com/Mertsr)

## Teşekkürler

- Python satranç motoru kodu referans alınmıştır
- Chesslib kütüphanesi kullanılmıştır
- Material Design rehberleri takip edilmiştir
