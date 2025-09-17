ESP 8266 configuration Arduino IDE:



#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

const char* apSSID = "WIFI NAME";
const char* apPassword = "WIFI PASSWORD"; 

const int relayPin = D1;
ESP8266WebServer server(80);
bool State = false;

void setup() {
  Serial.begin(115200);
  
  pinMode(relayPin, OUTPUT);
  digitalWrite(relayPin, LOW);
  
  WiFi.mode(WIFI_AP);
  WiFi.softAP(apSSID, apPassword);
  
  Serial.println();
  Serial.println("=== TRYB ACCESS POINT ===");
  Serial.print("SSID AP: ");
  Serial.println(apSSID);
  Serial.print("Hasło AP: ");
  Serial.println(apPassword);
  Serial.print("IP address: ");
  Serial.println(WiFi.softAPIP());
  
  server.on("/", HTTP_GET, handleRoot);
  server.on("/on", HTTP_GET, handleOn);
  server.on("/off", HTTP_GET, handleOff);
  server.on("/status", HTTP_GET, handleStatus);
  server.on("/toggle", HTTP_GET, handleToggle);
  
  server.begin();
  Serial.println("HTTP server started");
}

void loop() {
  server.handleClient();
}

void scanNetworks() {
  Serial.println("Skanowanie sieci WiFi...");
  int n = WiFi.scanNetworks();
  
  if (n == 0) {
    Serial.println("Brak dostępnych sieci!");
  } else {
    Serial.print("Znaleziono ");
    Serial.print(n);
    Serial.println(" sieci:");
    
    for (int i = 0; i < n; ++i) {
      Serial.print(i + 1);
      Serial.print(": ");
      Serial.print(WiFi.SSID(i));
      Serial.print(" (");
      Serial.print(WiFi.RSSI(i));
      Serial.print(" dBm) ");
      Serial.println((WiFi.encryptionType(i) == ENC_TYPE_NONE) ? "otwarta" : "zabezpieczona");
    }
  }
  Serial.println();
}

void printWiFiStatus() {
  switch(WiFi.status()) {
    case WL_IDLE_STATUS:
      Serial.println("WL_IDLE_STATUS");
      break;
    case WL_NO_SSID_AVAIL:
      Serial.println("WL_NO_SSID_AVAIL - sieć niedostępna");
      break;
    case WL_SCAN_COMPLETED:
      Serial.println("WL_SCAN_COMPLETED");
      break;
    case WL_CONNECTED:
      Serial.println("WL_CONNECTED");
      break;
    case WL_CONNECT_FAILED:
      Serial.println("WL_CONNECT_FAILED - błąd połączenia");
      break;
    case WL_CONNECTION_LOST:
      Serial.println("WL_CONNECTION_LOST");
      break;
    case WL_DISCONNECTED:
      Serial.println("WL_DISCONNECTED");
      break;
    default:
      Serial.print("Nieznany status: ");
      Serial.println(WiFi.status());
      break;
  }
}

void handleRoot() {
  String html = "<html><head><meta name='viewport' content='width=device-width, initial-scale=1'>";
  html += "<style>body{font-family:Arial;text-align:center;margin-top:50px;}";
  html += ".btn{padding:15px 25px;font-size:18px;margin:10px;border:none;border-radius:5px;cursor:pointer;}";
  html += ".on{background-color:#4CAF50;color:white;}";
  html += ".off{background-color:#f44336;color:white;}</style></head>";
  html += "<body><h1>Sterowanie Lustrem LED</h1>";
  html += "<p>Status: " + String(State ? "WŁĄCZONE" : "WYŁĄCZONE") + "</p>";
  html += "<a href='/on'><button class='btn on'>WŁĄCZ</button></a>";
  html += "<a href='/off'><button class='btn off'>WYŁĄCZ</button></a>";
  html += "<a href='/toggle'><button class='btn'>PRZEŁĄCZ</button></a>";
  html += "</body></html>";
  
  server.send(200, "text/html", html);
}

void handleOn() {
  digitalWrite(relayPin, HIGH);
  State = true;
  server.send(200, "text/plain", "WŁĄCZONE");
  Serial.println("Włączone");
}

void handleOff() {
  digitalWrite(relayPin, LOW);
  State = false;
  server.send(200, "text/plain", "WYŁĄCZONE");
  Serial.println("Wyłączone");
}

void handleToggle() {
  State = !State;
  digitalWrite(relayPin, State ? HIGH : LOW);
  server.send(200, "text/plain", State ? "WŁĄCZONE" : "WYŁĄCZONE");
  Serial.println(State ? "Włączone" : "Wyłączone");
}

void handleStatus() {
  server.send(200, "text/plain", State ? "ON" : "OFF");
}
