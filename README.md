# Escuela Colombiana de Ingeniería

## Arquitecturas de Software

### Integrantes

- **Manuel David Robayo Vega**
- **William Camilo Hernandez Deaza**

---

# API REST para la gestión de planos

El **BlueprintsRESTAPI** es un componente desarrollado para la gestión centralizada y estandarizada de planos arquitectónicos. Su diseño sigue principios de bajo acoplamiento, separando el API, la lógica de servicios y la persistencia utilizando la inyección de dependencias. La solución se implementó utilizando el framework Spring, específicamente:

- **Spring Boot** para la configuración y arranque de la aplicación.
- **Spring MVC** para la exposición de servicios RESTful.
- Un esquema de persistencia desacoplado de la lógica del API, facilitando la extensibilidad.

## Estructura y funcionalidades

### Parte I: Configuración básica y servicios GET

1. **Integración de Beans y configuración de dependencias:**  
   Se integraron los Beans desarrollados previamente, asegurando el uso correcto de las anotaciones `@Service` y `@Autowired` para la inyección de dependencias.

2. **Inicialización de datos en la capa de persistencia:**  
   La clase `InMemoryBlueprintPersistence` fue modificada para inicializarse, por defecto, con al menos tres planos, permitiendo que dos de ellos estén asociados al mismo autor.

3. **Exposición del recurso `/blueprints`:**  
   Se configuró el endpoint `/blueprints` para responder a peticiones GET, retornando en formato JSON el conjunto de todos los planos registrados en el sistema.  
   **Respuesta esperada:**  
   ![](/img/media/img1.png)  
   ![](/img/media/img2.png)

4. **Consulta de planos por autor:**  
   Se implementó el endpoint `/blueprints/{author}` para retornar todos los planos asociados a un autor específico. Si el autor no existe, se responde con un error HTTP 404.  
   **Respuesta esperada:**  
   ![](/img/media/img3.png)  
   ![](/img/media/img4.png)

5. **Consulta de un plano específico:**  
   El endpoint `/blueprints/{author}/{bpname}` permite consultar un plano específico de un autor dado. Si el plano o el autor no existen, se responde con HTTP 404.  
   **Respuesta esperada:**  
   ![](/img/media/img5.png)  
   ![](/img/media/img6.png)

### Parte II: Operaciones POST y PUT

1. **Creación de nuevos planos (POST):**  
   El endpoint `/blueprints` acepta peticiones POST para registrar nuevos planos. El cuerpo de la petición debe ser un objeto JSON con la estructura del plano a crear.  
   **Ejemplo de uso:**
   ```bash
   curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://localhost:8080/blueprints -d '{ObjetoJSON}'
   ```
   **Respuesta esperada:**  
   ![](/img/media/img7.png)

2. **Validación de la creación:**  
   Tras registrar un plano, este puede ser consultado mediante GET a `/blueprints/{author}/{bpname}` para verificar su persistencia.  
   **Respuesta esperada:**  
   ![](/img/media/img8.png)

3. **Actualización de planos (PUT):**  
   El endpoint `/blueprints/{author}/{bpname}` soporta el método PUT, permitiendo la actualización de un plano existente.

### Parte III: Concurrencia y regiones críticas

El componente BlueprintsRESTAPI está diseñado para operar en entornos concurrentes, atendiendo múltiples peticiones simultáneamente. Se identificaron posibles condiciones de carrera principalmente en la clase `InMemoryBlueprintPersistence`, específicamente en los métodos:

- `saveBlueprint()`
- `updateBlueprint()`
- Métodos de consulta como `getBlueprint()`, `getBlueprintsByAuthor()`, y `getAllBlueprints()` que acceden al mapa compartido de planos.

**Regiones críticas:**  
Las regiones críticas corresponden a los métodos mencionados anteriormente, ya que acceden y modifican el estado compartido del almacenamiento en memoria. Para evitar condiciones de carrera, es fundamental implementar mecanismos de control de concurrencia que no degraden el desempeño, como el uso de colecciones concurrentes o bloqueos finos en lugar de sincronizar métodos completos.

---

## Ejecución

Para correr la aplicación:

```bash
$ mvn compile
$ mvn spring-boot:run
```

Luego, se pueden realizar las pruebas sobre los endpoints utilizando un navegador web, Postman, o la herramienta `curl` desde la terminal.

---

**Nota:**  
Las imágenes incluidas en este documento corresponden a las respuestas esperadas de la API para cada uno de los requerimientos y deben ser tomadas como referencia para validar el comportamiento de la implementación.

