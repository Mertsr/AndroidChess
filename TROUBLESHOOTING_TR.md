# Android APK Derleme Hatalarını Giderme Kılavuzu

Bu belge, Android APK oluştururken karşılaşılan yaygın hataların çözümlerini içerir.

## Hata: Plugin [id: 'com.android.application'] was not found

### Çözüm 1: İnternet Bağlantısını Kontrol Edin

Android Gradle Plugin, Google'ın Maven deposundan indirilir. İnternet bağlantınızın çalıştığından emin olun.

**Test:**
```bash
ping dl.google.com
ping maven.google.com
```

Eğer bu sitelere erişemiyorsanız:
- Güvenlik duvarı ayarlarınızı kontrol edin
- VPN kullanıyorsanız devre dışı bırakın veya farklı bir VPN deneyin
- Kurumsal ağdaysanız, IT departmanına danışın

### Çözüm 2: Proxy Ayarları

Eğer proxy kullanıyorsanız, `gradle.properties` dosyasına aşağıdaki satırları ekleyin:

```properties
systemProp.http.proxyHost=proxy.sirketiniz.com
systemProp.http.proxyPort=8080
systemProp.http.nonProxyHosts=localhost|127.0.0.1
systemProp.https.proxyHost=proxy.sirketiniz.com
systemProp.https.proxyPort=8080
systemProp.https.nonProxyHosts=localhost|127.0.0.1
```

Kullanıcı adı ve şifre gerektiren proxy için:
```properties
systemProp.http.proxyUser=kullaniciadi
systemProp.http.proxyPassword=sifre
systemProp.https.proxyUser=kullaniciadi
systemProp.https.proxyPassword=sifre
```

### Çözüm 3: Gradle Cache Temizleme

```bash
# Gradle cache klasörünü temizle
rm -rf ~/.gradle/caches/

# Projedeki build klasörünü temizle
./gradlew clean

# Gradle bağımlılıklarını yeniden indir
./gradlew build --refresh-dependencies
```

### Çözüm 4: Android Studio Kullanın

Komut satırı yerine Android Studio kullanırsanız, bağımlılıklar otomatik olarak indirilir:

1. Android Studio'yu açın
2. **File → Open** ile projeyi açın
3. **Tools → SDK Manager** ile gerekli SDK'ları yükleyin
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)** ile APK oluşturun

## Hata: SDK location not found

### Çözüm: local.properties Dosyası Oluşturun

Proje kök dizininde `local.properties` dosyası oluşturun:

**Mac/Linux:**
```bash
echo "sdk.dir=/Users/KULLANICI_ADINIZ/Library/Android/sdk" > local.properties
# veya
echo "sdk.dir=/home/KULLANICI_ADINIZ/Android/Sdk" > local.properties
```

**Windows:**
```cmd
echo sdk.dir=C:\\Users\\KULLANICI_ADINIZ\\AppData\\Local\\Android\\Sdk > local.properties
```

**Not:** `KULLANICI_ADINIZ` yerine kendi kullanıcı adınızı yazın.

Android SDK konumunu bulmak için:
- **Android Studio:** File → Settings → Appearance & Behavior → System Settings → Android SDK
- **Manuel:** Genellikle yukarıdaki konumlardan birindedir

## Hata: Failed to find Build Tools revision

### Çözüm: Build Tools Yükleyin

**Android Studio ile:**
1. **Tools → SDK Manager** açın
2. **SDK Tools** sekmesine gidin
3. **Android SDK Build-Tools** işaretleyin (en son sürümü seçin)
4. **Apply** tıklayın

**Komut satırı ile:**
```bash
# SDK Manager'ı çalıştırın (Linux/Mac)
~/Android/Sdk/cmdline-tools/latest/bin/sdkmanager "build-tools;34.0.0"

# Windows
%USERPROFILE%\AppData\Local\Android\Sdk\cmdline-tools\latest\bin\sdkmanager.bat "build-tools;34.0.0"
```

## Hata: Could not resolve com.github.bhlangonijr:chesslib

### Çözüm: JitPack Repository Eklenmiş mi Kontrol Edin

`settings.gradle` dosyasında şu satırın olduğundan emin olun:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }  // Bu satır olmalı
    }
}
```

## Genel Çözümler

### 1. Gradle Daemon'u Yeniden Başlatın

```bash
./gradlew --stop
./gradlew clean build
```

### 2. Offline Mode'u Kapatın

Android Studio'da:
- **File → Settings → Build, Execution, Deployment → Gradle**
- **Offline work** işaretini kaldırın

### 3. Gradle Wrapper Güncelleyin

```bash
./gradlew wrapper --gradle-version=8.0
```

### 4. Java Versiyonunu Kontrol Edin

```bash
java -version
```

- Android Gradle Plugin 8.0 için **Java 17** gereklidir
- Java 17 kurulu değilse: https://adoptium.net/ adresinden indirin

### 5. Disk Alanı Kontrol Edin

```bash
# Linux/Mac
df -h

# Windows
dir
```

Gradle build için en az **5 GB** boş alan gerekir.

## Hataları Raporlama

Yukarıdaki çözümler işe yaramazsa, aşağıdaki bilgilerle GitHub issue açın:

1. **Tam hata mesajı:**
   ```bash
   ./gradlew assembleDebug --stacktrace > hata.txt 2>&1
   ```

2. **Sistem bilgileri:**
   ```bash
   ./gradlew --version
   java -version
   ```

3. **Dosyalar:**
   - `build.gradle`
   - `settings.gradle`
   - `gradle.properties`

## Başarılı Build Kontrolü

APK başarıyla oluşturulduysa şu konumda bulunmalıdır:

```
app/build/outputs/apk/debug/app-debug.apk
```

Dosyanın varlığını kontrol edin:

```bash
# Linux/Mac
ls -lh app/build/outputs/apk/debug/

# Windows
dir app\build\outputs\apk\debug\
```

## Ek Kaynaklar

- [Resmi Android Dokümantasyonu](https://developer.android.com/studio/build)
- [Gradle Kullanım Kılavuzu](https://docs.gradle.org/current/userguide/userguide.html)
- [Android Studio İndirme](https://developer.android.com/studio)
- [Java JDK İndirme](https://adoptium.net/)

---

**İpucu:** İlk kez Android projesi derliyorsanız, Android Studio kullanmanız şiddetle tavsiye edilir. Komut satırı daha gelişmiş kullanıcılar içindir.
