# 🛍️ Android-Ecommerce-App

### Aplicación móvil de comercio electrónico para Android

Aplicación Android desarrollada en **Java** que simula una tienda online y permite iniciar sesión, consultar un catálogo de productos, visualizar sus detalles, registrar compras y consultar un historial local. El proyecto integra una **API REST** para obtener información y utiliza **SQLite** para almacenar las compras en el dispositivo.

## 🎯 Objetivos

- Desarrollar una aplicación móvil nativa para Android.
- Integrar servicios web mediante solicitudes HTTP y procesar respuestas JSON.
- Organizar la navegación entre distintas pantallas de la aplicación.
- Implementar almacenamiento local para conservar el historial de compras.
- Aplicar componentes de interfaz, gestión de sesiones y notificaciones de Android.

## ⚙️ Funcionalidades

- **🔐 Inicio de sesión:** autenticación mediante la API y opción de recordar la sesión con `SharedPreferences`.
- **🛒 Catálogo de productos:** descarga y presentación de productos obtenidos de Fake Store API.
- **🔎 Detalle de producto:** consulta de imagen, nombre, precio, categoría, descripción y valoraciones.
- **💳 Registro de compras:** simulación de compras, guardadas en una base de datos SQLite; las compras repetidas incrementan su cantidad.
- **📋 Historial de compras:** visualización de los productos adquiridos y sus cantidades.
- **🔔 Notificaciones:** aviso local al registrar una compra, sujeto a los permisos del dispositivo.
- **📤 Compartir productos:** opción para compartir información e imagen de un producto con otras aplicaciones.
- **🚪 Cierre de sesión:** permite volver a la pantalla de autenticación.

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
| --- | --- |
| **Java** | Desarrollo de la lógica de la aplicación |
| **Android SDK / Android Studio** | Desarrollo y ejecución de la aplicación Android |
| **XML** | Definición de las interfaces gráficas |
| **Fake Store API** | Autenticación de ejemplo y obtención del catálogo de productos |
| **HTTP / JSON** | Comunicación con la API y tratamiento de sus respuestas |
| **Gson** | Conversión de datos JSON a objetos Java |
| **Glide** | Carga y visualización de imágenes de productos |
| **SQLite** | Persistencia local del historial de compras |
| **SharedPreferences** | Almacenamiento de la preferencia de sesión |
| **Gradle** | Gestión del proyecto y sus dependencias |

## 🧩 Estructura del proyecto

```text
Android-Ecommerce-App/
├── app/
│   ├── src/main/
│   │   ├── java/es/upm/etsiinf/gib/proyecto/
│   │   │   ├── LoginActivity.java           # Pantalla de inicio de sesión
│   │   │   ├── LoginThread.java             # Autenticación con la API
│   │   │   ├── MainActivity.java            # Catálogo y navegación principal
│   │   │   ├── FetchProductsThread.java     # Descarga de productos
│   │   │   ├── ProductDetailActivity.java   # Detalle y compra de productos
│   │   │   ├── PurchaseHistoryActivity.java # Historial de compras
│   │   │   ├── PurchaseDatabaseHelper.java  # Base de datos SQLite
│   │   │   └── ...                          # Modelos y adaptadores
│   │   ├── res/                             # Diseños XML y recursos gráficos
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/                                 # Configuración y wrapper de Gradle
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

## 🚀 Instalación y ejecución

**Requisitos:** Android Studio, conexión a Internet para acceder a la API y un emulador o dispositivo con **Android 14 (API 34) o superior**, según la configuración actual del proyecto.

1. Clona el repositorio:

   ```bash
   git clone https://github.com/USUARIO/Android-Ecommerce-App.git
   ```

2. Abre la carpeta del proyecto en **Android Studio**.
3. Deja que **Gradle** sincronice e instale las dependencias necesarias.
4. Selecciona un emulador o conecta un dispositivo Android compatible.
5. Ejecuta la aplicación mediante **Run ▶**.
6. Inicia sesión con credenciales válidas de prueba de [Fake Store API](https://fakestoreapi.com/).

> Sustituye `USUARIO` por el propietario real del repositorio.

## 🔄 Funcionamiento general

1. El usuario inicia sesión mediante una petición HTTP a la API.
2. La aplicación descarga el catálogo y muestra los productos disponibles.
3. Al seleccionar un producto, se abre una pantalla con su información detallada.
4. Al pulsar **Comprar**, la operación se registra localmente en SQLite y se puede mostrar una notificación.
5. El historial permite consultar las compras almacenadas en el dispositivo.

## 📚 Contexto académico

Proyecto desarrollado para la asignatura **Programación para Dispositivos Móviles** del **Grado en Ingeniería Informática** de la **Universidad Politécnica de Madrid (UPM)**.

El trabajo pone en práctica el desarrollo de aplicaciones Android, la comunicación con servicios REST, el tratamiento de datos JSON, la persistencia local y la interacción entre diferentes pantallas.

## 📝 Consideraciones

- Se trata de una **aplicación académica de demostración**: las compras se registran localmente y **no procesan pagos reales**.
- El catálogo y la autenticación dependen de la disponibilidad del servicio externo **Fake Store API**.
- Para utilizar las notificaciones puede ser necesario conceder el permiso correspondiente en Android.

---

**Proyecto académico · Programación para Dispositivos Móviles · Universidad Politécnica de Madrid**
