Smart Monitoring Room (IoT + Android + MQTT + ESP32)

Sistem monitoring ruangan real-time menggunakan Android dan ESP32.
Proyek ini dirancang agar sederhana untuk dipelajari, namun cukup solid untuk dipakai sebagai dasar IoT monitoring sungguhan.

🚀 Overview

Smart Monitoring Room adalah aplikasi Android berbasis MVVM + StateFlow yang menerima data sensor dari ESP32 secara real-time melalui MQTT.

Device ESP32 mengirimkan data:

🌡️ Temperature (DHT22)

💧 Humidity (DHT22)

💡 Light Level (LDR)

Android menerima data tersebut melalui HiveMQ Public Broker, lalu menampilkan status ruangan secara langsung dalam bentuk dashboard.

✨ Key Features
📡 Real-time MQTT Streaming

Menggunakan Paho MQTT Java Client (bukan library jadul Android).

Koneksi stabil dan aman untuk Android 12+.

🏛 MVVM Architecture (Clean & Scalable)

ViewModel + Repository + StateFlow

Perubahan data langsung ter-reflect di UI.

📊 Live Sensor Dashboard

Temperatur, kelembaban, dan intensitas cahaya tampil otomatis tanpa refresh.

UI berubah otomatis berdasarkan kondisi ruangan:

Mode Morning (terang)

Mode Night (gelap)

🔌 IoT Friendly

ESP32 publish JSON ke topic MQTT:

{
  "temperature": 30.7,
  "humidity": 68.2,
  "light": 100
}

📱 Support Android Modern

Min SDK 30

Compatible Android 12, 13, 14

Tidak menggunakan library deprecated yang menyebabkan crash.

🛠 Tech Stack
Android

Kotlin

MVVM Architecture

StateFlow & Coroutines

ViewBinding

Paho MQTT Client v1.2.5

IoT

ESP32

DHT22 (Temperature & Humidity)

LDR Sensor

HiveMQ Public Broker

📂 Project Structure
app/
 └── java/com.example.smartmonitoringroom/
      ├── data/
      │    ├── model/SensorData.kt
      │    ├── mqtt/MqttClientManager.kt
      │    └── repository/SensorRepository.kt
      │
      ├── ui/
      │    ├── view/MainActivity.kt
      │    └── viewmodel/
      │         ├── SensorUiState.kt
      │         ├── SensorViewModel.kt
      │         └── SensorViewModelFactory.kt
      │
      └── utils/
           └── MqttConfig.kt


Struktur dibuat agar mudah dikembangkan lagi:

tambah chart

tambah histori database

tambah kontrol lampu / relay

tambah notifikasi MQTT

⚙️ Cara Menjalankan (Android)

Clone repository

git clone https://github.com/username/smart-monitoring-room.git


Buka di Android Studio (Hedgehog atau terbaru)

Pastikan dependency ini ada:

implementation "org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5"


Edit broker di:

MqttConfig.SERVER_URL


Default:

tcp://broker.hivemq.com:1883


Jalankan aplikasi di emulator / HP.

🔧 Contoh Data dari ESP32

Publish ke topic:

SMR/Android/Data


Payload:

{
  "temperature": 28.4,
  "humidity": 63.1,
  "light": 1500
}

📸 Screenshots (Tambahkan Sendiri)

Tambahkan screenshot UI kamu di sini:

Dashboard terang

Dashboard gelap

Log real-time

📈 Roadmap (Pengembangan Lanjutan)

 Tambah grafik historis

 Simpan data ke Room Database

 Tambahkan notifikasi jika suhu / kelembaban ekstrem

 Tambah kontrol (lampu, AC, kipas) via MQTT

 Mode gelap UI

🤝 Kontribusi

Pull request terbuka untuk siapa pun yang ingin:

menambah fitur,

memperbaiki bug,

atau menyumbang UI lebih keren.

🧑‍💻 Creator

Dibuat sebagai proyek IoT & Android modern yang dapat dikembangkan menjadi sistem monitoring skala lebih besar.
