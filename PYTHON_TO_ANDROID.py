"""
Orijinal Python Satranç Motoru - Android'e Dönüşüm Kılavuzu

Bu dosya, orijinal Python satranç motorunun Android uygulamasına nasıl dönüştürüldüğünü açıklar.

===================================================================================
PYTHON KODU (Orijinal)
===================================================================================
"""

import chess
import chess.engine

ENGINE_PATH = "/usr/games/stockfish"  # senin sistemine göre

class RobotChessBrain:
    def __init__(self, engine_path, skill_level=5, movetime_ms=500):
        # Satranç tahtası (başlangıç dizilişi)
        self.board = chess.Board()

        # Motoru aç
        self.engine = chess.engine.SimpleEngine.popen_uci(engine_path)

        # Zorluk ayarları
        self.skill_level = skill_level        # 0 (çok kolay) - 20 (çok güçlü)
        self.movetime_ms = movetime_ms        # motorun düşünme süresi (ms)

    def set_difficulty(self, skill_level=None, movetime_ms=None):
        if skill_level is not None:
            self.skill_level = skill_level
        if movetime_ms is not None:
            self.movetime_ms = movetime_ms

    def robot_move(self):
        """Motorun bir hamle üretmesini sağlar ve tahtaya uygular."""
        # Motoru konfigüre et
        self.engine.configure({"Skill Level": self.skill_level})

        # Motor hamlesini al
        result = self.engine.play(
            self.board,
            chess.engine.Limit(time=self.movetime_ms / 1000.0)
        )
        move = result.move  # chess.Move objesi

        # Tahtayı güncelle
        self.board.push(move)

        return move  # örn: Move.from_uci('e2e4')

    def human_move(self, move_uci: str):
        """İnsanın hamlesini UCI formatında uygular. Örn 'e2e4'."""
        move = chess.Move.from_uci(move_uci)

        if move in self.board.legal_moves:
            self.board.push(move)
            return True
        else:
            return False

    def print_board(self):
        print(self.board)

    def is_game_over(self):
        return self.board.is_game_over()

    def result(self):
        return self.board.result()  # "1-0", "0-1", "1/2-1/2"

    def close(self):
        self.engine.quit()


"""
===================================================================================
ANDROID KOTLIN KODU (Dönüştürülmüş)
===================================================================================

KARŞILAŞTIRMA:

Python                          →    Android Kotlin
---------------------------------------------------------------------------
chess.Board()                   →    Board() (chesslib)
chess.engine.SimpleEngine       →    SimpleChessEngine (özel AI)
chess.Move                      →    Move (chesslib)
print()                         →    TextView / Toast
input()                         →    Dokunmatik ekran (ChessBoardView)
while döngüsü                   →    Event-driven UI (onClick handlers)

DOSYA KARŞILIKLARI:

1. RobotChessBrain (Python)     →    SimpleChessEngine.kt
   - __init__()                 →    reset()
   - set_difficulty()           →    setDifficulty()
   - robot_move()               →    makeRobotMove()
   - human_move()               →    makePlayerMove()
   - is_game_over()             →    isGameOver()
   - result()                   →    getGameResult()

2. main() fonksiyonu (Python)   →    MainActivity.kt
   - print_board()              →    ChessBoardView (özel View)
   - input() döngüsü            →    onTouchEvent() / onClick()
   - while döngüsü              →    Event handlers

3. Yeni Android Bileşenleri:
   - ChessBoardView.kt          →    Görsel satranç tahtası
   - activity_main.xml          →    UI layoutu
   - strings.xml                →    Türkçe metinler
   - themes.xml                 →    Görsel tema

TEMEL FARKLAR:

1. Motor: Python Stockfish → Android özel AI algoritması
   - Stockfish Android'de native binary gerektirirdi
   - Basit AI ile daha kolay deployment

2. UI: Terminal → Dokunmatik arayüz
   - Metin girişi → Dokunmatik hamle seçimi
   - ASCII tahta → Grafik tahta

3. Asenkron İşlem:
   - Python: senkron, blocking
   - Android: Coroutines ile async

4. Zorluk Seviyeleri:
   - Python: Stockfish skill level (0-20)
   - Android: 4 seviye (0-3) özel AI

KULLANIM ÖRNEĞİ:

Python:
-------
brain = RobotChessBrain(ENGINE_PATH, skill_level=5)
brain.human_move("e2e4")
ai_move = brain.robot_move()

Android:
--------
val engine = SimpleChessEngine()
engine.setDifficulty(1) // Orta
engine.makePlayerMove(fromSquare, toSquare)
engine.makeRobotMove()

APK OLUŞTURMA:
--------------
1. Android Studio'yu aç
2. Projeyi import et
3. Build → Generate Signed Bundle / APK
4. Release seçeneğini seç
5. app-release.apk dosyası oluşur

VEYA komut satırından:
./gradlew assembleRelease

APK dosyası: app/build/outputs/apk/release/app-release.apk
"""
