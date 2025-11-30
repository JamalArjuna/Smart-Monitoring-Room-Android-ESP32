# Smart-Monitoring-Room (Android + ESP32 + MQTT)

**Smart-Monitoring-Room** adalah sebuah aplikasi IoT + Android real-time untuk memonitor kondisi ruangan (suhu, kelembaban, intensitas cahaya) menggunakan ESP32 dan menampilkannya di aplikasi Android berbasis arsitektur modern. Cocok sebagai dasar proyek smart-home, sistem monitoring, atau demo IoT nyata.  

**Disclaimer:** Project ini fokus dalam pengintegrasian IoT dan Android dengan Android Studio dengan MVVM Architecture
## 🚀 Fitur Utama

- Real-time MQTT streaming — data sensor dari ESP32 langsung dikirim ke broker MQTT publik/privat  
- Dukungan sensor suhu & kelembaban (DHT22) + sensor cahaya (LDR) — sehingga kamu bisa pantau “lingkungan ruangan” secara komprehensif.  
- Arsitektur modern di Android: MVVM + StateFlow + Kotlin + Coroutines — membuat kode bersih, modular, mudah dipahami dan diskalakan.  
- Tampilan dashboard langsung mencerminkan data sensor — suhu, kelembaban, dan cahaya tampil otomatis. UI bisa berubah berdasarkan kondisi 
- Kompatibel dengan Android 12+ (min SDK 30).  

## Struktur Proyek & Teknologi
```bash
app/
└── kotlin/com/example/smartmonitoringroom/
├── data/
│ ├── model/
| |   |__ SensorData.kt
| |   |__ SensorUiState
│ ├── mqtt/
| |   |__ MqttClientManager.kt
| |   |__ MqttConfig
│ └── repository/
| |   |__ SensorRepository.kt
├── ui/
│ └── MainActivity.kt
├── viewmodel/
│     ├── SensorViewModel.kt
│     └── SensorViewModelFactory.kt
└── utils/
    |___smart_room_android_ino
```

## 📷 Screenshot / Preview UI

**Preview UI**
Saat Ruangan Tidak Ada Cahaya sama sekali:
![mode malam](https://github.com/user-attachments/assets/a280f94e-74af-4134-a67e-cc45588b23ce)

Saat Ruangan Terdapat Cahaya: 
![mode siang](https://github.com/user-attachments/assets/3f58b731-3507-4699-b9b8-4277e56154bc)

## **Preview MQTTX**
<img width="745" height="641" alt="image" src="https://github.com/user-attachments/assets/cbe46383-6289-4f14-a00a-ff8d018f597c" />

## **Preview Wiring ESP32**
<img width="401" height="451" alt="Smart Monitoring Room drawio" src="https://github.com/user-attachments/assets/f6688a9c-4242-463a-bca6-057cba6ec9cc" />

## 🎯 Setup & Cara Menjalankan (Android)
1. Clone repository  
   ```bash
   git clone https://github.com/username/Smart-Monitoring-Room-Android-ESP32.git
2. Buka di Android Studio (versi terbaru direkomendasikan)
3. Pastikan dependency berikut sudah ada di build.gradle:
  ```gradle
  implementation "org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5"
```
4. Atur URL broker di MqttConfig.SERVER_URL. Contoh default:
```Kotlin
tcp://broker.hivemq.com:1883
```
5. Jalankan aplikasi di emulator atau HP Android dan akan langsung mulai menerima data dari ESP32 (jika ESP32 sudah mengirim ke topik MQTT sesuai pengaturan)


## 📥 Contoh Payload dari ESP32
```json
{
  "temperature": 28.4,
  "humidity": 63.1,
  "light": 1500
}
```

## 🔭 Roadmap / Fitur Lanjutan yang Bisa Ditambahkan

- Menyimpan data historis ke database lokal (misalnya Room / SQLite) atau cloud + plotting grafik waktu
- Menambahkan notifikasi bila suhu / kelembaban / cahaya melewati batas aman
- Menambahkan kontrol perangkat (misalnya lampu / kipas / AC) via MQTT / relay / ESP32 — menjadikan ini sistem smart-home penuh
- Mode gelap (dark mode UI)
- UI dan UX makin rapi & menarik

## 🤝 Kontribusi
Pull request, isu, dan saran sangat dipersilakan.
Silakan fork proyek ini, kembangkan fitur baru, atau perbaiki bug lalu ajukan PR.
