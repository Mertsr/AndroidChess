# Proje Yapısı Açıklaması

## 📁 Dizin Yapısı

```
Mert-Sara-/                              # Ana proje dizini
│
├── 📄 README.md                         # Ana dokümantasyon (Türkçe)
├── 📄 QUICKSTART.md                     # Hızlı başlangıç kılavuzu
├── 📄 BUILD_GUIDE.md                    # Detaylı APK oluşturma rehberi
├── 📄 PYTHON_TO_ANDROID.py              # Python-Android karşılaştırması
├── 📄 .gitignore                        # Git ignore kuralları
│
├── 📄 settings.gradle                   # Gradle proje ayarları
├── 📄 build.gradle                      # Proje seviye build dosyası
├── 📄 gradle.properties                 # Gradle özellikleri
│
├── 📁 gradle/
│   └── 📁 wrapper/
│       └── 📄 gradle-wrapper.properties # Gradle wrapper ayarları
│
└── 📁 app/                              # Android uygulama modülü
    │
    ├── 📄 build.gradle                  # Uygulama build ayarları
    ├── 📄 proguard-rules.pro            # ProGuard kuralları
    │
    └── 📁 src/
        └── 📁 main/
            │
            ├── 📄 AndroidManifest.xml   # Uygulama manifestosu
            │
            ├── 📁 java/com/chessrobot/  # Kotlin kaynak kodları
            │   ├── 📄 MainActivity.kt           # ⭐ Ana aktivite
            │   ├── 📄 ChessBoardView.kt         # ⭐ Satranç tahtası görünümü
            │   └── 📄 SimpleChessEngine.kt      # ⭐ Satranç motoru AI
            │
            ├── 📁 res/                  # Android kaynakları
            │   │
            │   ├── 📁 layout/
            │   │   └── 📄 activity_main.xml     # Ana ekran düzeni
            │   │
            │   ├── 📁 values/
            │   │   ├── 📄 strings.xml           # Türkçe metinler
            │   │   ├── 📄 colors.xml            # Renk tanımları
            │   │   ├── 📄 themes.xml            # Tema ayarları
            │   │   └── 📄 ic_launcher_background.xml
            │   │
            │   ├── 📁 drawable/
            │   │   └── 📄 ic_launcher_foreground.xml
            │   │
            │   └── 📁 mipmap-anydpi-v26/
            │       ├── 📄 ic_launcher.xml
            │       ├── 📄 ic_launcher_round.xml
            │       └── 📄 ic_launcher_foreground.xml
            │
            └── 📁 assets/               # (Boş - ileride kullanılabilir)
```

## 🎯 Önemli Dosyalar

### 📱 Kotlin Kaynak Kodları

| Dosya | Açıklama | Python Karşılığı |
|-------|----------|------------------|
| `MainActivity.kt` | Ana uygulama ekranı ve oyun kontrolü | `main()` fonksiyonu |
| `ChessBoardView.kt` | Görsel satranç tahtası (custom view) | `print_board()` |
| `SimpleChessEngine.kt` | Satranç kuralları ve AI motoru | `RobotChessBrain` sınıfı |

### 🎨 UI/UX Kaynakları

| Dosya | Açıklama |
|-------|----------|
| `activity_main.xml` | Ana ekran layout'u (tahta, butonlar, vs) |
| `strings.xml` | Tüm Türkçe metinler |
| `colors.xml` | Renk paletleri (tahta, UI) |
| `themes.xml` | Material Design temaları |

### ⚙️ Build Yapılandırması

| Dosya | Açıklama |
|-------|----------|
| `settings.gradle` | Proje ayarları, repository'ler |
| `build.gradle` (root) | Proje seviye build ayarları |
| `app/build.gradle` | Uygulama bağımlılıkları, SDK versiyonları |
| `gradle.properties` | Gradle performans ayarları |

## 🔑 Temel Kavramlar

### Android Uygulama Bileşenleri

1. **Activity** (`MainActivity.kt`)
   - Uygulamanın ana ekranı
   - Kullanıcı etkileşimlerini yönetir
   - Python'daki `main()` fonksiyonu gibi

2. **Custom View** (`ChessBoardView.kt`)
   - Özel çizim yapan UI bileşeni
   - Satranç tahtasını görselleştirir
   - Dokunma olaylarını yakalar

3. **Logic Class** (`SimpleChessEngine.kt`)
   - İş mantığı ve algoritma
   - Python `RobotChessBrain` sınıfının eşdeğeri
   - UI'dan bağımsız çalışır

### Build Sistemi

```
Gradle Wrapper (gradlew)
    ↓
settings.gradle (proje yapısı)
    ↓
build.gradle (root) (plugin versiyonları)
    ↓
app/build.gradle (bağımlılıklar, SDK)
    ↓
Kaynak Kodlar → Derleme → APK
```

## 📦 APK Oluşturma Süreci

```
Kotlin Kodları (.kt)
    ↓ Kotlin Compiler
Java Bytecode (.class)
    ↓ D8/R8 Compiler
Dalvik Bytecode (.dex)
    ↓
XML Kaynakları → AAPT2 → Binary XML
    ↓
Drawable'lar → Görsel Optimizasyon
    ↓
Hepsi birleştiriliyor
    ↓
APK Paketi (.apk)
    ↓ İmzalama (Release için)
İmzalı APK (.apk)
    ↓ Zipalign
Optimize APK (Yüklenmeye Hazır)
```

## 🎮 Oyun Akışı

```
Uygulama Başlar
    ↓
MainActivity onCreate()
    ↓
ChessBoardView initialize
SimpleChessEngine initialize
    ↓
Kullanıcı taşa dokunur
    ↓
ChessBoardView onTouchEvent()
    ↓
MainActivity handlePlayerMove()
    ↓
SimpleChessEngine makePlayerMove()
    ↓
Hamle geçerli mi? → Hayır → Hata mesajı
    ↓ Evet
Tahta güncellenir
    ↓
Oyun bitti mi? → Evet → Sonuç ekranı
    ↓ Hayır
SimpleChessEngine makeRobotMove()
    ↓
AI hamle hesaplar (zorluk seviyesine göre)
    ↓
Tahta güncellenir
    ↓
Oyun bitti mi? → Evet → Sonuç ekranı
    ↓ Hayır
Kullanıcı sırasını bekler
```

## 🔧 Bağımlılıklar

### Ana Bağımlılıklar

```gradle
// Android temel kütüphaneler
androidx.core:core-ktx              // Kotlin Android eklentileri
androidx.appcompat:appcompat         // Geriye uyumluluk
material:material                    // Material Design UI

// Satranç mantığı
chesslib:1.3.3                      // Satranç kuralları ve hamle doğrulama

// Asenkron işlemler
kotlinx-coroutines-android          // Kotlin coroutines
```

### Bağımlılık Kaynakları

```gradle
repositories {
    google()                         // Google'ın Android kütüphaneleri
    mavenCentral()                   // Maven merkez repo
    maven { url 'https://jitpack.io' }  // JitPack (chesslib için)
}
```

## 📊 Dosya Boyutları (Yaklaşık)

| Kategori | Boyut |
|----------|-------|
| Kotlin kaynak kodları | ~18 KB |
| XML kaynaklar | ~8 KB |
| Gradle dosyaları | ~2 KB |
| Chesslib bağımlılığı | ~500 KB |
| Android framework | Cihazda var |
| **Debug APK** | **~2-3 MB** |
| **Release APK (optimized)** | **~1.5-2 MB** |

## 🎨 UI Bileşenleri

### Ana Ekran (activity_main.xml)

```
┌─────────────────────────────┐
│     Satranç Robotu          │ ← TextView (başlık)
│     Senin sıran             │ ← TextView (durum)
├─────────────────────────────┤
│   ┌───────────────────┐     │
│   │                   │     │
│   │   Chess Board     │     │ ← ChessBoardView (custom)
│   │   8x8 Grid        │     │
│   │                   │     │
│   └───────────────────┘     │
├─────────────────────────────┤
│ Zorluk: [Orta       ▼]      │ ← Spinner (seçim)
│                             │
│ [Yeni Oyun]  [Sıfırla]      │ ← Buttons
└─────────────────────────────┘
```

## 🔐 İmzalama (Release APK için)

```
Keystore (.jks dosyası)
    ├── Keystore Password
    ├── Key Alias
    ├── Key Password
    └── Sertifika Bilgileri
        ├── İsim
        ├── Organizasyon
        └── Ülke
            ↓
APK İmzalanır
    ↓
Google Play kabul eder ✓
```

## 📱 Minimum Gereksinimler

| Özellik | Değer |
|---------|-------|
| Min SDK | 24 (Android 7.0 Nougat) |
| Target SDK | 34 (Android 14) |
| Compile SDK | 34 |
| Kotlin | 1.8.20 |
| Gradle | 8.0 |
| JVM Target | 1.8 (Java 8) |

## 🎓 Kod Karşılaştırması

### Python → Kotlin

```python
# Python
board = chess.Board()
engine.play(board, limit)
board.push(move)
```

```kotlin
// Kotlin
val board = Board()
engine.makeRobotMove()
board.doMove(move)
```

### Terminal → Android UI

```python
# Python
print("Tahta:")
print(board)
move = input("Hamle: ")
```

```kotlin
// Kotlin/Android
chessBoardView.invalidate()  // Görsel güncelle
chessBoardView.setOnTouchListener { ... }  // Dokunma
```

## 🚀 Geliştirme İpuçları

1. **Android Studio kullan** - En kolay yol
2. **Logcat** ile hata ayıkla
3. **Layout Inspector** ile UI'ı incele
4. **APK Analyzer** ile APK içeriğini gör
5. **Emulator** veya gerçek cihaz test et

## 📞 Destek

Sorun mu yaşıyorsunuz?

1. `BUILD_GUIDE.md` sorun giderme bölümü
2. `QUICKSTART.md` hızlı çözümler
3. GitHub Issues
4. Android Developer Dokümantasyonu

---

**Not:** Bu proje Python satranç motorundan Android'e dönüştürülmüştür. Detaylar için `PYTHON_TO_ANDROID.py` dosyasına bakın.
