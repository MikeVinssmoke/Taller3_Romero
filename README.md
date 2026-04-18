# Taller 3 – Lista de Tareas con Fragments, Persistencia y Recordatorios

## ¿Qué se implementó?

Se desarrolló una aplicación de lista de tareas (To-Do App) para Android utilizando Kotlin,
con una arquitectura de Single Activity y múltiples Fragments.

### Funcionalidades principales

**Gestión de tareas**
- Crear nuevas tareas con título, descripción y hora de recordatorio
- Editar tareas existentes
- Eliminar tareas desde el menú de opciones
- Ver el detalle completo de cada tarea

**Navegación**
- Arquitectura Single Activity con Navigation Component
- Cuatro Fragments: lista de tareas, nueva tarea, editar tarea y detalle de tarea
- Navegación fluida entre pantallas con paso de argumentos mediante Safe Args

**Persistencia**
- Las tareas se guardan localmente usando SharedPreferences con serialización JSON (Gson)
- Los datos se conservan aunque la aplicación se cierre y se vuelva a abrir

**Interfaz**
- Barra de búsqueda en tiempo real que filtra tareas por título o descripción
- Menú de una flecha con opciones por tarea: Editar, Activar/Desactivar alarma y Borrar
- Selector de hora nativo de Android (TimePickerDialog) al configurar un recordatorio
- Botones de Calendario y Perfil reservados para funcionalidades futuras

**Permisos**
- La app solicita permiso de notificaciones al usuario al abrirse por primera vez,
  compatible con Android 13 en adelante

---

## Opción de recordatorio utilizada: Notificación local

Se implementó el recordatorio mediante **notificación local** usando `BroadcastReceiver`
y `AlarmManager`.

### ¿Cómo funciona?

1. El usuario selecciona una hora al crear o editar una tarea usando el reloj nativo del sistema
2. Desde la pantalla principal puede activar o desactivar el recordatorio de cada tarea
   mediante el menú de tres puntos
3. Al activar el recordatorio, `ReminderScheduler` programa una alarma exacta con `AlarmManager`
   que se dispara a la hora indicada, incluso si el dispositivo está en reposo
4. Cuando llega la hora, `TaskReminderReceiver` recibe la señal y muestra una notificación
   en la barra de estado del dispositivo con el título de la tarea
5. Si la hora ya pasó para el día actual, la alarma se programa automáticamente para el día siguiente

### Clases involucradas

- `TaskReminderReceiver` — BroadcastReceiver que construye y muestra la notificación
- `ReminderScheduler` — objeto que programa la alarma con AlarmManager

---

