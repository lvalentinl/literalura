# 📖 LiterAlura - Catálogo de Libros

¡Bienvenido a LiterAlura! Este es un proyecto de catálogo de libros interactivo desarrollado en Java con Spring Boot. La aplicación te permite buscar, registrar y consultar libros y autores directamente desde la consola, obteniendo datos de la API de Gutendex.

## ✨ Características

* **Búsqueda de libros:** Busca libros por título y guarda los datos en la base de datos.
* **Gestión de datos:** Persiste libros y autores en una base de datos PostgreSQL.
* **Listados:** Consulta y muestra una lista de todos los libros y autores registrados.
* **Filtros avanzados:** Filtra autores que estaban vivos en un año específico y libros por idioma.
* **Entorno con Docker:** Usa un contenedor de Docker para gestionar la base de datos de forma fácil y reproducible.

---

## 🛠️ Tecnologías

* **Java 17+**
* **Spring Boot 3:** Para una configuración simplificada y gestión de dependencias.
* **Spring Data JPA:** Para la persistencia de datos y consultas a la base de datos.
* **Jackson Databind:** Para mapear las respuestas JSON de la API a objetos Java.
* **PostgreSQL:** Sistema de gestión de base de datos relacional.
* **Docker:** Para gestionar el entorno de la base de datos de forma aislada.

---

## 🚀 Cómo Empezar

### 📋 Prerrequisitos

Asegúrate de tener instalados los siguientes programas:
* **Java JDK 17 o superior**
* **Maven**
* **Docker**

### ⚙️ Configuración

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/literalura.git](https://github.com/tu-usuario/literalura.git)
    cd literalura
    ```

2.  **Inicia la base de datos con Docker:**
    Abre una terminal y ejecuta el siguiente comando para crear y arrancar el contenedor de PostgreSQL.
    ```bash
    docker run --name literalura-db -e POSTGRES_USER=literalura -e POSTGRES_PASSWORD=literalura -e POSTGRES_DB=literalura -p 5432:5432 -d postgres:14
    ```

3.  **Configura la conexión a la base de datos:**
    Abre el archivo `src/main/resources/application.properties` y asegúrate de que los valores de `username`, `password` y `url` coincidan con los del comando de Docker.

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
    spring.datasource.username=literalura
    spring.datasource.password=literalura
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

---

### ▶️ Ejecución

Desde la terminal, en la carpeta raíz del proyecto, ejecuta el siguiente comando de Maven para compilar y ejecutar la aplicación Spring Boot:

```bash
mvn spring-boot:run