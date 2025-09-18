# WifiThings is a mobile app for remotely controlling smart devices, written in Kotlin for Android phones.

The app allows you to control IoT devices over a Wi-Fi network. Currently, the app only allows you to turn devices on and off, but can be easily expanded with additional features.
The app uses ESP8266 microcontrollers as communication bridges, allowing you to create an affordable smart home system without investing in expensive commercial solutions.

✨ Key Features
🏠 Multi-Device Control - Add and manage multiple IoT devices
📱 Intuitive Interface - Modern Design with Material Design 3
⚡ Instant Response - Real-time control over WiFi
💾 Auto-Save - Devices remain remembered after closing the app
🌐 HTTP Communication - Simple REST API for easy integration

Technologies:   
- Android SDK
- Kotlin
- Jetpack Compose 
- Hilt 
- Coroutines & Flow 
- DataStore
- OkHttp
- Navigation Component 
- Clean Architecture 
- MVVM

🔌 Hardware Requirements
ESP8266 (NodeMCU)
Relay 3.3V (SRD-05VDC-SL-C)
Power Supply - USB C or HiLink (HLK-PM01)
Control device/s

🔌Hardware Setup
1. Flash firmware ESP8266 from provided code
2. Connect the relay to the ESP8266 in ArduinoIDE - Check Serial Monitor to get ESP8266 IP
3. Connect the device with relay and relay with ESP8266  
4. Configure the device in the app
5. Enjoy !


<img width="440" height="940" alt="image" src="https://github.com/user-attachments/assets/3158c4d5-755a-4a7f-8e36-506ad213e390" />
<img width="446" height="944" alt="image" src="https://github.com/user-attachments/assets/4a946d87-a20e-47d7-b036-00e2fab5e482" />
<img width="446" height="943" alt="image" src="https://github.com/user-attachments/assets/383cd780-6ee4-4b2d-ad8f-a2b09f56989d" />


ESP8266 configuration:
```#include <ESP8266WiFi.h> #include <ESP8266WebServer.h> const char* ssid = "WIFI_SSID"; const char* password = "WIFI_PASSWORD; const int relayPin = D1; ESP8266WebServer server(80); bool State = false; void setup() { Serial.begin(115200); delay(1000); pinMode(relayPin, OUTPUT); digitalWrite(relayPin, LOW); Serial.println(); Serial.println("=== INICJALIZACJA ESP8266 ==="); Serial.print("Łączenie z WiFi: "); Serial.println(ssid); scanNetworks(); WiFi.begin(ssid, password); int attempts = 0; while (WiFi.status() != WL_CONNECTED && attempts < 30) { delay(500); Serial.print("."); attempts++; if (attempts % 10 == 0) { Serial.println(); Serial.print("Status WiFi: "); printWiFiStatus(); } } Serial.println(); if (WiFi.status() == WL_CONNECTED) { Serial.print("Połączono! IP: "); Serial.println(WiFi.localIP()); Serial.print("RSSI: "); Serial.print(WiFi.RSSI()); Serial.println(" dBm"); } else { Serial.println("Błąd połączenia z WiFi!"); Serial.print("Ostatni status: "); printWiFiStatus(); Serial.println("Sprawdź SSID i hasło, then resetuj ESP"); return; } server.on("/", HTTP_GET, handleRoot); server.on("/on", HTTP_GET, handleOn); server.on("/off", HTTP_GET, handleOff); server.on("/status", HTTP_GET, handleStatus); server.on("/toggle", HTTP_GET, handleToggle); server.begin(); Serial.println("HTTP server started"); Serial.println("Gotowe do sterowania !"); } void loop() { server.handleClient(); } void scanNetworks() { Serial.println("Skanowanie sieci WiFi..."); int n = WiFi.scanNetworks(); if (n == 0) { Serial.println("Brak dostępnych sieci!"); } else { Serial.print("Znaleziono "); Serial.print(n); Serial.println(" sieci:"); for (int i = 0; i < n; ++i) { Serial.print(i + 1); Serial.print(": "); Serial.print(WiFi.SSID(i)); Serial.print(" ("); Serial.print(WiFi.RSSI(i)); Serial.print(" dBm) "); Serial.println((WiFi.encryptionType(i) == ENC_TYPE_NONE) ? "otwarta" : "zabezpieczona"); } } Serial.println(); } void printWiFiStatus() { switch(WiFi.status()) { case WL_IDLE_STATUS: Serial.println("WL_IDLE_STATUS"); break; case WL_NO_SSID_AVAIL: Serial.println("WL_NO_SSID_AVAIL - sieć niedostępna"); break; case WL_SCAN_COMPLETED: Serial.println("WL_SCAN_COMPLETED"); break; case WL_CONNECTED: Serial.println("WL_CONNECTED"); break; case WL_CONNECT_FAILED: Serial.println("WL_CONNECT_FAILED - błąd połączenia"); break; case WL_CONNECTION_LOST: Serial.println("WL_CONNECTION_LOST"); break; case WL_DISCONNECTED: Serial.println("WL_DISCONNECTED"); break; default: Serial.print("Nieznany status: "); Serial.println(WiFi.status()); break; } } void handleRoot() { String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1'>"; html += "<style>body{font-family:Arial;text-align:center;margin-top:50px;}"; html += ".btn{padding:15px 25px;font-size:18px;margin:10px;border:none;border-radius:5px;cursor:pointer;}"; html += ".on{background-color:#4CAF50;color:white;}"; html += ".off{background-color:#f44336;color:white;}</style></head>"; html += "<body><h1>Sterowanie</h1>"; html += "<p>Status: " + String(State ? "WŁĄCZONE" : "WYŁĄCZONE") + "</p>"; html += "<a href='/on'><button class='btn on'>WŁĄCZ</button></a>"; html += "<a href='/off'><button class='btn off'>WYŁĄCZ</button></a>"; html += "<a href='/toggle'><button class='btn'>PRZEŁĄCZ</button></a>"; html += "</body></html>"; server.send(200, "text/html", html); } void handleOn() { digitalWrite(relayPin, HIGH); State = true; server.send(200, "text/plain", "WŁĄCZONE"); Serial.println("Włączone"); } void handleOff() { digitalWrite(relayPin, LOW); State = false; server.send(200, "text/plain", "WYŁĄCZONE"); Serial.println("Wyłączone"); } void handleToggle() { State = !State; digitalWrite(relayPin, State ? HIGH : LOW); server.send(200, "text/plain", State ? "WŁĄCZONE" : "WYŁĄCZONE"); Serial.println(State ? "Włączone" : "Wyłączone"); } void handleStatus() { server.send(200, "text/plain", State ? "ON" : "OFF"); }```
