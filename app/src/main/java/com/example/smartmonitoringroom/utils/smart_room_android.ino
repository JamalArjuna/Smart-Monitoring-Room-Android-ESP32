#include <WiFi.h>
#include <PubSubClient.h>
#include <Wire.h>
#include <U8g2lib.h>
#include "DHT.h"

// ----------------- WiFi & MQTT -----------------
const char* ssid        = "kepo";
const char* password    = "kayaknyasalah";

const char* mqttServer  = "broker.hivemq.com";
const int   mqttPort    = 1883;
const char* mqttTopic   = "SMR/Android/Data";

// client
WiFiClient espClient;
PubSubClient mqttClient(espClient);

// ----------------- DHT & LDR -----------------
#define DHTPIN 4
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);

#define LDR_PIN 32

int invertLDR(int rawValue) {
  return 4095 - rawValue;
}

// ----------------- OLED SH1106 -----------------
U8G2_SH1106_128X64_NONAME_F_HW_I2C u8g2(
  U8G2_R0,
  U8X8_PIN_NONE,
  22,
  21
);

// ----------------- WiFi & MQTT helper -----------------
void setupWiFi() {
  Serial.print("Menghubungkan WiFi: ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();
  Serial.print("WiFi connected, IP: ");
  Serial.println(WiFi.localIP());
}

void reconnectMqtt() {
  while (!mqttClient.connected()) {
    Serial.print("Menghubungkan ke MQTT...");
    String clientId = "esp32-room-";
    clientId += String(random(0xFFFF), HEX);

    if (mqttClient.connect(clientId.c_str())) {
      Serial.println("connected");
    } else {
      Serial.print("gagal, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" coba lagi 2 detik...");
      delay(2000);
    }
  }
}


void setup() {
  Serial.begin(115200);

  dht.begin();
  analogReadResolution(12);

  u8g2.begin();

  setupWiFi();
  mqttClient.setServer(mqttServer, mqttPort);
}


void loop() {
  if (!mqttClient.connected()) {
    reconnectMqtt();
  }
  mqttClient.loop();

  // ----- Baca sensor -----
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  int   ldrRaw    = analogRead(LDR_PIN);
  int   ldrFixed  = invertLDR(ldrRaw);
  int   lightPct  = map(ldrFixed, 0, 4095, 0, 100);

  // ----- Kirim ke MQTT Melalui Json -----
  char payload[128];
  snprintf(payload, sizeof(payload),
           "{\"temperature\":%.2f,\"humidity\":%.2f,\"light\":%d}",
           t, h, lightPct);

  mqttClient.publish(mqttTopic, payload);

  // ----- Tampilkan  OLED -----
  u8g2.clearBuffer();

  u8g2.setFont(u8g2_font_ncenB08_tr);
  u8g2.drawStr(10, 10, "Smart Monitoring");
  u8g2.drawStr(40, 21, "Room");
  u8g2.drawHLine(0, 24, 128);

  u8g2.setFont(u8g2_font_6x12_tr);
  char buf[32];

  snprintf(buf, sizeof(buf), "Suhu      : %.1f C", t);
  u8g2.drawStr(0, 38, buf);

  snprintf(buf, sizeof(buf), "Kelembapan: %.1f %%", h);
  u8g2.drawStr(0, 50, buf);

  snprintf(buf, sizeof(buf), "Cahaya    : %3d %%", lightPct);
  u8g2.drawStr(0, 62, buf);

  u8g2.sendBuffer();

  delay(1000);
}