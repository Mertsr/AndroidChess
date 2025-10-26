# Android APK Oluşturma Kılavuzu

## Hızlı Başlangıç

Bu kılavuz, Android satranç oyunu APK'sını oluşturmanız için adım adım talimatlar içerir.

## Ön Gereksinimler

### Gerekli Yazılımlar

1. **Java Development Kit (JDK) 11 veya üzeri**
   - İndir: https://adoptium.net/
   - Kontrol: `java -version`

2. **Android Studio** (Önerilen - En Kolay Yol)
   - İndir: https://developer.android.com/studio
   - En son stabil sürümü kullanın

3. **Alternatif: Sadece Android SDK Command Line Tools**
   - İndir: https://developer.android.com/studio#command-tools
   - Daha hafif ama daha teknik

## Yöntem 1: Android Studio ile APK Oluşturma (Önerilen)

### Adım 1: Projeyi Açın

1. Android Studio'yu başlatın
2. **"Open"** veya **"Open an Existing Project"** seçeneğine tıklayın
3. Bu projenin ana klasörünü seçin (Mert-Sara- klasörü)
4. **"OK"** tıklayın
5. Gradle sync işleminin tamamlanmasını bekleyin (ilk sefer 5-10 dakika sürebilir)

### Adım 2: Bağımlılıkları Senkronize Edin

- Android Studio otomatik olarak bağımlılıkları indirecektir
- Sağ altta "Gradle sync" progress bar'ı göreceksiniz
- Hata alırsanız: **File → Sync Project with Gradle Files**

### Adım 3: Debug APK Oluşturma (Test İçin)

1. **Build → Build Bundle(s) / APK(s) → Build APK(s)** menüsüne gidin
2. Yapılandırma tamamlanınca sağ altta bildirim çıkacak
3. **"locate"** linkine tıklayın
4. APK dosyası açılacak: `app/build/outputs/apk/debug/app-debug.apk`

### Adım 4: Release APK Oluşturma (Yayın İçin)

1. **Build → Generate Signed Bundle / APK** seçeneğine tıklayın
2. **APK** seçeneğini seçin ve **Next**
3. **Create new...** tıklayarak yeni keystore oluşturun:
   - Keystore path: İstediğiniz bir konum seçin (örn: `my-release-key.jks`)
   - Password: Güçlü bir şifre belirleyin (unutmayın!)
   - Alias: `my-key-alias`
   - Validity: 25 yıl (varsayılan)
   - Sertifika bilgilerini doldurun
4. **Next** → **release** build variant'ını seçin
5. **Finish** tıklayın
6. APK oluşturulacak: `app/build/outputs/apk/release/app-release.apk`

## Yöntem 2: Komut Satırı ile APK Oluşturma

### Linux/Mac:

```bash
# Proje dizinine gidin
cd /path/to/Mert-Sara-

# Gradle wrapper'a çalıştırma izni verin
chmod +x gradlew

# Debug APK oluştur
./gradlew assembleDebug

# Release APK oluştur (imzasız)
./gradlew assembleRelease
```

### Windows:

```cmd
cd C:\path\to\Mert-Sara-

gradlew.bat assembleDebug

gradlew.bat assembleRelease
```

### APK Konumları:

- **Debug APK:** `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK:** `app/build/outputs/apk/release/app-release-unsigned.apk`

## Yöntem 3: Sadece Command Line Tools ile

### Kurulum:

1. Android SDK Command Line Tools'u indirin
2. Klasörü açın ve SDK Manager'ı çalıştırın:

```bash
# Linux/Mac
./sdkmanager --sdk_root=/path/to/sdk "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Windows
sdkmanager.bat --sdk_root=C:\path\to\sdk "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

3. `local.properties` dosyası oluşturun:

```properties
sdk.dir=/path/to/Android/Sdk
```

4. APK oluşturun:

```bash
./gradlew assembleDebug
```

## APK'yı Cihaza Yükleme

### Yöntem 1: ADB (Android Debug Bridge)

```bash
# Cihazı kontrol et
adb devices

# APK'yı yükle
adb install app/build/outputs/apk/debug/app-debug.apk

# Veya güncelleme için
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Yöntem 2: Manuel Yükleme

1. APK dosyasını telefona/tablete kopyalayın (USB, email, cloud)
2. Telefonda **Ayarlar → Güvenlik → Bilinmeyen Kaynaklar** (veya benzeri) açın
3. Dosya yöneticisini açın
4. APK dosyasına dokunun
5. **Yükle** butonuna basın
6. Kurulum tamamlanınca **Aç** deyin

### Yöntem 3: Google Play Store (İleri Düzey)

1. Google Play Console'a kayıt olun (ücretli)
2. Signed Bundle oluşturun: `./gradlew bundleRelease`
3. AAB dosyasını yükleyin: `app/build/outputs/bundle/release/app-release.aab`
4. Google'ın inceleme sürecini bekleyin

## Release APK İmzalama (Komut Satırı)

### Keystore Oluşturma:

```bash
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias my-key-alias
```

### APK İmzalama:

```bash
# Önce unsigned APK oluştur
./gradlew assembleRelease

# Sonra imzala (jarsigner)
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore my-release-key.jks \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  my-key-alias

# Zipalign yap
zipalign -v 4 \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/app-release.apk
```

## Sorun Giderme

### "SDK location not found"

**Çözüm:** `local.properties` dosyası oluşturun:

```properties
sdk.dir=/Users/kullanici/Library/Android/sdk  # Mac
sdk.dir=/home/kullanici/Android/Sdk           # Linux
sdk.dir=C\:\\Users\\kullanici\\AppData\\Local\\Android\\Sdk  # Windows
```

### "Gradle sync failed"

**Çözüm:**

```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### "Could not resolve com.github.bhlangonijr:chesslib:1.3.3"

**Çözüm:** İnternet bağlantınızı kontrol edin ve `settings.gradle` dosyasında JitPack repository'nin olduğundan emin olun.

### "Build Tools revision X is missing"

**Çözüm:** Android Studio'da **Tools → SDK Manager → SDK Tools** sekmesine gidin ve Build Tools'u yükleyin.

### APK Yüklenmiyor (Cihazda)

**Çözüm:**
1. **Bilinmeyen Kaynaklar** iznini açın
2. Önceki versiyonu kaldırın
3. Cihazı yeniden başlatın
4. Farklı dosya yöneticisi deneyin

## APK Boyutunu Küçültme

### ProGuard/R8 Aktif Et:

`app/build.gradle` dosyasında:

```gradle
buildTypes {
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### APK Analizi:

Android Studio'da: **Build → Analyze APK** ile APK içeriğini inceleyin.

## Performans İpuçları

1. **İlk build uzun sürer** (bağımlılıklar indirilir) - normal
2. **Gradle daemon** kullanın (varsayılan)
3. **Build cache** açık tutun
4. **İnternet bağlantısı** olsun (ilk build için)
5. **Disk alanı** yeterli olsun (en az 5GB)

## Sık Kullanılan Gradle Komutları

```bash
# Temizle
./gradlew clean

# Tüm build variant'ları oluştur
./gradlew build

# Sadece debug
./gradlew assembleDebug

# Sadece release
./gradlew assembleRelease

# Test çalıştır
./gradlew test

# Bağımlılıkları listele
./gradlew dependencies

# Görevleri listele
./gradlew tasks
```

## Başarılı Build Kontrolü

APK oluşturuldu mu kontrol edin:

```bash
# Linux/Mac
ls -lh app/build/outputs/apk/debug/
ls -lh app/build/outputs/apk/release/

# Windows
dir app\build\outputs\apk\debug\
dir app\build\outputs\apk\release\
```

APK dosyası varsa başarılı! 🎉

## Ek Kaynaklar

- [Android Developer Docs](https://developer.android.com/studio/build)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [APK Signing Guide](https://developer.android.com/studio/publish/app-signing)

---

**Not:** İlk kez APK oluşturuyorsanız Android Studio kullanmanızı önemle tavsiye ederiz. Çok daha kolay ve hata ayıklama araçları mevcuttur.
