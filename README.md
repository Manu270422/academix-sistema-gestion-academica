# 🎓 Academix  
## Sistema Web de Gestión Académica para Estudiantes Técnicos y Tecnológicos

Academix es un sistema web desarrollado como proyecto formativo del programa ADSO (Análisis y Desarrollo de Software).  
Permite a estudiantes técnicos y tecnológicos gestionar sus materias y tareas académicas de forma organizada y centralizada.

---

## 📌 Descripción del Proyecto

Sistema web basado en arquitectura MVC que permite:

- Registro de usuarios
- Inicio de sesión seguro
- Recuperación de contraseña vía correo electrónico
- Gestión de materias por usuario
- Creación y administración de tareas
- Control de estado de tareas (Pendiente / Entregada)

El sistema está diseñado bajo principios de separación de responsabilidades y buenas prácticas de desarrollo backend.

---

## 🛠 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **HTML5**
- **CSS3**
- **JavaScript**

---

## 🏗 Arquitectura

El proyecto implementa el patrón **MVC (Model – View – Controller)**:

- `model` → Entidades del sistema (Usuario, Materia, Tarea, TokenRecuperacion)
- `repository` → Acceso a datos con JPA
- `service` → Lógica de negocio
- `controller` → Controladores REST
- `static` → Frontend (HTML, CSS, JS)
- `templates/mail` → Plantillas de correo electrónico

---

## 🗄 Modelo de Base de Datos

Relaciones principales:

- Usuario (1) → (N) Materias  
- Materia (1) → (N) Tareas  

Se garantiza integridad referencial mediante claves foráneas en MySQL.

---

## ⚙️ Instalación y Ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/Manu270422/academix-sistema-gestion-academica.git

2. Configurar base de datos MySQL.

3. Ajustar credenciales en application.properties.

4. Ejecutar el proyecto: 

mvn spring-boot:run 

5. Acceder desde navegador:

http://localhost:8080 

🔐 Seguridad

Autenticación con Spring Security

Encriptación de contraseñas

Sistema de recuperación de contraseña mediante token

📈 Estado del Proyecto

✔ Registro de usuarios
✔ Autenticación
✔ Gestión de materias
✔ Gestión de tareas
✔ Recuperación de contraseña
🔄 En mejora continua

👨‍💻 Autor

Carlos Manuel Turizo Hernández
Estudiante de Ingeniería Informática
Proyecto desarrollado en el programa ADSO - SENA

📄 Licencia
