# 📚 LiterAlura - Catálogo de Libros

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)
![Maven](https://img.shields.io/badge/Maven-Project-red)

## 📖 Descripción

LiterAlura es una aplicación de consola desarrollada con Spring Boot que permite gestionar un catálogo de libros consumiendo la API de [Gutendex](https://gutendex.com/). Los usuarios pueden buscar libros, registrarlos en una base de datos PostgreSQL y realizar consultas sobre libros y autores.

Este proyecto fue desarrollado como parte del desafío de formación **Backend Java** de **Alura Latam**.

## ✨ Características

- 🔍 **Búsqueda de libros** por título utilizando la API de Gutendex
- 💾 **Persistencia de datos** con PostgreSQL y JPA/Hibernate
- 📚 **Listado de libros** registrados en la base de datos
- ✍️ **Gestión de autores** con relación uno a muchos
- 🌍 **Filtrado por idioma** (Inglés, Español, Francés, Portugués)
- 📅 **Consulta de autores vivos** en un año determinado
- 🚫 **Prevención de duplicados** en libros y autores
- 🎯 **Interfaz de consola** interactiva y fácil de usar

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.3**
  - Spring Data JPA
  - Spring Boot Starter
- **PostgreSQL** - Base de datos relacional
- **Jackson** - Serialización/Deserialización JSON
- **Maven** - Gestor de dependencias
- **Gutendex API** - Fuente de datos de libros

## 📋 Requisitos Previos

- ☕ Java 17 o superior
- 🐘 PostgreSQL instalado y en ejecución
- 📦 Maven (incluido con el wrapper `mvnw`)
- 🌐 Conexión a Internet para consumir la API de Gutendex

## ⚙️ Configuración

### 1. Configurar Base de Datos

Crea una base de datos PostgreSQL llamada `literalura`:

```sql
CREATE DATABASE literalura;
```

### 2. Variables de Entorno

Configura las siguientes variables de entorno:

```bash
DB_HOST=localhost:5432
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
```

**En Windows PowerShell:**

```powershell
$env:DB_HOST="localhost:5432"
$env:DB_USER="postgres"
$env:DB_PASSWORD="tu_contraseña"
```

### 3. Configuración de application.properties

El archivo `src/main/resources/application.properties` ya está configurado para usar las variables de entorno:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}/literalura
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

## 🚀 Instalación y Ejecución

### Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd literalura
```

### Compilar y ejecutar con Maven

**En Windows:**

```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**En Linux/Mac:**

```bash
./mvnw clean install
./mvnw spring-boot:run
```

## 📱 Uso de la Aplicación

Al ejecutar la aplicación, se mostrará un menú interactivo:

```
--------------------------------
Elija la opción a través de su número:
1 - Buscar Libro por su Título 
2 - Listar Libros Registrados
3 - Listar Autores Registrados
4 - Listar Autores vivos en un determinado Año
5 - Listar Libros por Idioma
0 - Salir
```

### Funcionalidades

#### 1️⃣ Buscar Libro por Título
- Busca un libro en la API de Gutendex
- Registra el primer resultado en la base de datos
- Verifica duplicados antes de guardar
- Reutiliza autores existentes

#### 2️⃣ Listar Libros Registrados
- Muestra todos los libros guardados en la base de datos
- Incluye información del autor, idioma y número de descargas

#### 3️⃣ Listar Autores Registrados
- Muestra todos los autores con sus datos biográficos
- Incluye la lista de libros de cada autor

#### 4️⃣ Listar Autores Vivos en un Año
- Filtra autores que estaban vivos en un año específico
- Considera autores sin fecha de fallecimiento como vivos

#### 5️⃣ Listar Libros por Idioma
- Filtra libros por idioma (en, es, fr, pt)
- Búsqueda insensible a mayúsculas/minúsculas

## 🏗️ Arquitectura del Proyecto

```
literalura/
├── src/main/java/com/aluracursos/literalura/
│   ├── LiteraluraApplication.java          # Clase principal
│   ├── model/
│   │   ├── Autor.java                       # Entidad JPA Autor
│   │   ├── Libro.java                       # Entidad JPA Libro
│   │   ├── DatosAutor.java                  # DTO para deserialización
│   │   ├── DatosLibro.java                  # DTO para deserialización
│   │   └── GutendexResponse.java            # DTO respuesta API
│   ├── repository/
│   │   ├── AutorRepository.java             # Repositorio JPA Autor
│   │   └── LibroRepository.java             # Repositorio JPA Libro
│   ├── service/
│   │   ├── ConsumoAPI.java                  # Cliente HTTP
│   │   ├── IConvierteDatos.java             # Interfaz conversión
│   │   └── ConvierteDatos.java              # Implementación Jackson
│   └── principal/
│       └── Principal.java                   # Lógica del menú
└── src/main/resources/
    └── application.properties               # Configuración Spring
```

## 🗄️ Modelo de Datos

### Entidad Libro

```java
- id: Long (PK, auto-generado)
- titulo: String
- resumen: String (max 1500 caracteres)
- idioma: String
- numeroDescargas: Integer
- autor: Autor (ManyToOne)
```

### Entidad Autor

```java
- id: Long (PK, auto-generado)
- nombre: String (unique)
- fechaNacimiento: Integer
- fechaFallecimiento: Integer
- libros: List<Libro> (OneToMany)
```

### Relación

- Un **Autor** puede tener muchos **Libros** (OneToMany)
- Un **Libro** pertenece a un **Autor** (ManyToOne)

## 🔍 Consultas JPA Personalizadas

### AutorRepository

```java
// Buscar autor por nombre
Optional<Autor> findByNombre(String nombre);

// Encontrar autores vivos en un año específico
@Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio 
        AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :anio)")
List<Autor> findAutoresVivosEnAnio(@Param("anio") Integer anio);
```

### LibroRepository

```java
// Buscar libro por título y autor (prevenir duplicados)
Optional<Libro> findByTituloAndAutor_Nombre(String titulo, String nombreAutor);

// Obtener idiomas distintos
@Query("SELECT DISTINCT l.idioma FROM Libro l")
List<String> findDistinctIdiomas();

// Buscar libros por idioma (case-insensitive)
List<Libro> findByIdiomaIgnoreCase(String idioma);
```

## 🌐 API Utilizada

**Gutendex API** - https://gutendex.com/

Endpoints utilizados:
- `GET /books/?search={titulo}` - Búsqueda de libros por título

Ejemplo de respuesta:
```json
{
  "count": 38,
  "next": "https://gutendex.com/books/?page=2&search=pride",
  "previous": null,
  "results": [
    {
      "id": 1342,
      "title": "Pride and Prejudice",
      "authors": [
        {
          "name": "Jane Austen",
          "birth_year": 1775,
          "death_year": 1817
        }
      ],
      "languages": ["en"],
      "download_count": 50000
    }
  ]
}
```

## 🎯 Principios de Diseño Aplicados

- **Separación de Responsabilidades**: Cada clase tiene una función específica
- **Inyección de Dependencias**: Uso de Spring para gestionar dependencias
- **DTOs (Data Transfer Objects)**: Records para mapeo de JSON
- **Repository Pattern**: Abstracción de acceso a datos
- **Prevención de Duplicados**: Validación antes de persistir
- **Manejo de Relaciones JPA**: Gestión correcta de entidades asociadas

## 🐛 Solución de Problemas Comunes

### Error: "Detached entity passed to persist"

**Causa**: Intentar persistir una entidad relacionada que ya existe en la base de datos.

**Solución**: Guardar el autor explícitamente antes de asociarlo al libro:

```java
Autor autor = autorRepository.findByNombre(nombreAutor)
    .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));
```

### Error de conexión a PostgreSQL

Verifica que:
- PostgreSQL esté en ejecución
- Las variables de entorno estén configuradas correctamente
- La base de datos `literalura` exista

## 📚 Ejemplos de Uso

### Buscar y Guardar un Libro

```
1. Seleccionar opción: 1
2. Ingresar título: "Pride and Prejudice"
3. El sistema busca en Gutendex
4. Verifica si ya existe
5. Guarda el libro y el autor (si no existen)
```

### Consultar Autores del Siglo XIX

```
1. Seleccionar opción: 4
2. Ingresar año: 1850
3. Se muestran autores nacidos antes de 1850 y que:
   - Aún viven (sin fecha de fallecimiento), o
   - Fallecieron después de 1850
```

## 👨‍💻 Autor

Proyecto desarrollado como parte del programa **ONE - Oracle Next Education** de Alura Latam.

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos como parte del programa de formación de Alura Latam.

## 🙏 Agradecimientos

- **Alura Latam** por el desafío y la formación
- **Gutendex** por proporcionar la API gratuita de libros del Proyecto Gutenberg
- **Oracle Next Education** por el programa de capacitación

---

⭐ Si este proyecto te fue útil, no olvides darle una estrella
