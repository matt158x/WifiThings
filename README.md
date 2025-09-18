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
