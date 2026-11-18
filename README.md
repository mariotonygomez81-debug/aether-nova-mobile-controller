# AETHER NOVA — Mobile Controller

Joystick web independiente para controlar AETHER NOVA desde un teléfono Android o iPhone.

## Funciones
- Movimiento izquierda/derecha mediante controles táctiles.
- Activación de Supernova y pausa.
- Diseño adaptable a orientación vertical y horizontal.
- Vinculación mediante código temporal de 12 caracteres.
- Comunicación en tiempo real con Supabase Realtime.
- Puede instalarse en la pantalla de inicio desde el navegador compatible.

## Publicación
El repositorio está preparado para GitHub Pages. En GitHub, abrí **Settings → Pages** y seleccioná **GitHub Actions** como fuente. El workflow publica el contenido de este repositorio.

Dirección esperada: https://mariotonygomez81-debug.github.io/aether-nova-mobile-controller/

## Seguridad
La aplicación usa una clave publishable/anon de Supabase, que está destinada a clientes públicos. Nunca colocar claves `service_role`, secretos ni credenciales administrativas en este repositorio. El código de emparejamiento aleatorio reduce conexiones accidentales, pero no reemplaza autenticación ni autorización robusta. Para un uso público a gran escala conviene migrar a canales privados con autorización y límites de frecuencia.

## Compatibilidad
Requiere conexión a Internet y un navegador móvil compatible con JavaScript, WebSocket y Supabase Realtime. La conexión real debe probarse con el juego abierto en una PC y el teléfono en la misma sesión de emparejamiento.
