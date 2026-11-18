# AETHER NOVA Joystick — Android APK

El proyecto Android envuelve la aplicación web del joystick en una WebView de pantalla completa.

## Construcción

Cada cambio en `android/**` ejecuta el workflow **Build Android APK**. También se puede iniciar manualmente desde GitHub → Actions.

El archivo generado es `app-debug.apk`, disponible como artifact **AETHER-NOVA-Joystick-Android**.

## Emparejamiento QR

La app registra el esquema `aethernova://connect?code=CODIGO`. El juego muestra ese enlace en el QR. Al escanearlo con el APK instalado, Android abre la app y esta carga el controlador con el código ya aplicado.

El enlace HTTPS alternativo sigue disponible para usar el joystick desde el navegador.

## Requisitos

- Android 6.0 o superior (API 23).
- Conexión a Internet para cargar la interfaz y conectarse a Supabase Realtime.

La primera compilación es una APK de depuración, adecuada para instalar y probar. Para distribución pública conviene generar una versión release firmada con una clave privada protegida en GitHub Secrets.
