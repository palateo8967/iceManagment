
# ❄️ IceManagement

**IceManagement** es una aplicación móvil Android pensada para la gestión integral de ventas, pedidos e inventario de **heladerías**. Su objetivo es simplificar y optimizar las operaciones diarias mediante un sistema moderno, intuitivo y seguro.

---

## ✨ Características principales

✅ Registro y control de ventas.  
✅ Gestión de pedidos de clientes.  
✅ Control de inventario de productos.  
✅ Autenticación de usuarios con Firebase.  
✅ Respaldo seguro de datos en la nube.  
✅ Navegación fluida con Jetpack Compose.  
✅ Diseño adaptable y moderno.

**Próximas mejoras:**
- Módulo de estadísticas y reportes.
- Gestión de turnos de trabajo.
- Alertas y notificaciones personalizadas.

---

## 🛠️ Tecnologías utilizadas

- **Kotlin** – Lenguaje principal.
- **Jetpack Compose** – UI declarativa.
- **Firebase con Firestore** – Autenticación y sincronización en la nube.
- **MySQL** – Base de datos relacional.
- **Hilt** – Inyección de dependencias.
- **Clean Architecture** – Separación de responsabilidades.

---

## 🏗️ Arquitectura del proyecto

El proyecto está organizado según **Clean Architecture** para asegurar escalabilidad y mantenibilidad:

```
com.icerojects.icemanagment
│
├── core                # Componentes reutilizables
├── di                  # Inyección de dependencias (Hilt)
├── data                # Repositorios, Room y APIs
│   ├── local
│   └── remote
├── domain              # Casos de uso y modelos de negocio
├── ui        # Pantallas y navegación con Compose
│   ├── navigation
│   ├── theme
│   └── features
└── utils               # Helpers y extensiones
```

---

## 👥 Autores

- **Darian Altamirano**
- **Mateo Palacios**

---

## 💬 Contacto

Si tienes preguntas o quieres colaborar:

- ✉️ **Emails de contacto:**
    - Darian Altamirano: *(darianaltamirano0@gmail.com)*
    - Mateo Palacios: *(palateo8967@gmail.com)*

- 🐙 GitHub: [palateo8967](https://github.com/palateo8967)
- 🐙 GitHub: [Darian55](https://github.com/Darian55)

---

**IceManagement – Simplificando la gestión de tu heladería.**
