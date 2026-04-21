# 🎵 Spotify Escolar — Backend (Spring Boot)

## Requisitos
- Java 17
- Maven 3.8+
- PostgreSQL 16
- Cuenta gratuita en Cloudinary (para imágenes)
- Cuenta Gmail con App Password (para correos)

## Configuración local

1. Ejecuta el script `database.sql` en tu base de datos `spotify_escolar`
2. Copia tus MP3 en `src/main/resources/static/audio/<genero>/`
3. Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/spotify_escolar
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

spring.mail.username=TU_CORREO@gmail.com
spring.mail.password=TU_APP_PASSWORD_GMAIL

cloudinary.cloud-name=TU_CLOUD_NAME
cloudinary.api-key=TU_API_KEY
cloudinary.api-secret=TU_API_SECRET

app.base-url=http://localhost:8080
```

4. Ejecuta el proyecto:
```bash
mvn spring-boot:run
```

5. La API estará disponible en: `http://localhost:8080`

## Credenciales de administrador
- **Correo:** admin@spotify-escolar.com
- **Contraseña:** Admin123*

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /api/auth/registro | Registrar usuario |
| POST | /api/auth/verificar | Verificar cuenta con código |
| POST | /api/auth/login | Iniciar sesión |
| GET | /api/generos | Listar géneros |
| GET | /api/canciones | Listar canciones |
| GET | /api/canciones/genero/{id}/aleatoria | Canción aleatoria por género |
| GET | /api/canciones/buscar?q=texto | Buscar canciones |
| POST | /api/canciones/{id}/reproducir | Registrar reproducción |
| GET | /api/usuario/dashboard | Dashboard del usuario |
| GET | /api/usuario/recomendaciones | Recomendaciones IA |
| GET | /api/playlists | Mis playlists |
| POST | /api/playlists | Crear playlist |
| POST | /api/favoritos/{cancionId} | Toggle favorito |
| POST | /api/admin/canciones | Registrar canción (admin) |
| GET | /api/admin/dashboard | Dashboard admin |

## Despliegue en Render.com

1. Crea un nuevo Web Service en Render
2. Conecta tu repositorio de GitHub
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/escolar-1.0.0.jar`
5. Agrega las variables de entorno en el panel de Render

## Estructura del proyecto
```
src/main/java/com/spotify/escolar/
├── config/         → Seguridad y Cloudinary
├── controller/     → Endpoints REST
├── dto/            → Request y Response objects
├── entity/         → Entidades JPA
├── exception/      → Manejo global de errores
├── repository/     → Acceso a base de datos
├── security/       → JWT Filter y Util
└── service/        → Lógica de negocio
```
