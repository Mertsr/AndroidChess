# Hızlı Başlangıç: APK Build Hatası Çözümü

Bu dosya, en yaygın Android APK build hatalarının hızlı çözümlerini içerir.

## ⚡ En Yaygın Hata: "Plugin [id: 'com.android.application'] was not found"

### Çözüm (2 adımda):

**1. Adım: İnternet Bağlantısını Test Edin**
```bash
ping google.com
```
Eğer bağlantı yoksa veya güvenlik duvarı engelliyorsa, VPN kullanın veya IT departmanına danışın.

**2. Adım: Gradle Cache'i Temizleyin ve Yeniden Deneyin**
```bash
./gradlew clean
./gradlew assembleDebug
```

### Bu Çalışmazsa:

**Android Studio Kullanın (Önerilen)**
1. Android Studio'yu açın
2. **File → Open** ile projeyi açın
3. Gradle sync bitsin (5-10 dk)
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. APK hazır: `app/build/outputs/apk/debug/app-debug.apk`

## ⚡ İkinci Yaygın Hata: "SDK location not found"

### Çözüm (1 adımda):

Proje kök dizininde `local.properties` dosyası oluşturun:

**Mac:**
```bash
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
```

**Linux:**
```bash
echo "sdk.dir=$HOME/Android/Sdk" > local.properties
```

**Windows (PowerShell):**
```powershell
"sdk.dir=$env:LOCALAPPDATA\Android\Sdk" | Out-File local.properties -Encoding ASCII
```

## ⚡ Üçüncü Yaygın Hata: "Failed to find Build Tools"

### Çözüm:

Android Studio'da:
1. **Tools → SDK Manager**
2. **SDK Tools** sekmesi
3. **Android SDK Build-Tools** işaretle
4. **Apply**

## 🔍 Daha Fazla Yardım

Eğer yukarıdaki çözümler işe yaramazsa:

1. **Detaylı sorun giderme:** [TROUBLESHOOTING_TR.md](TROUBLESHOOTING_TR.md)
2. **Tam build kılavuzu:** [BUILD_GUIDE.md](BUILD_GUIDE.md)
3. **GitHub Issue açın** ve hata loglarını paylaşın:
   ```bash
   ./gradlew assembleDebug --stacktrace > hata.txt 2>&1
   ```

## ✅ Başarı Kontrolü

APK oluşturuldu mu?
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

Dosya görünüyorsa tebrikler! 🎉

## 💡 İpuçları

- İlk build 10-15 dakika sürebilir (bağımlılıklar indirilir) - normal!
- En az 5 GB disk alanı gerekli
- İnternet bağlantısı şart (ilk build için)
- Java 17 gerekli (Java 11 veya 8 değil!)
  ```bash
  java -version  # Kontrol edin
  ```

---

**Hala sorun mu var?** → [TROUBLESHOOTING_TR.md](TROUBLESHOOTING_TR.md)
