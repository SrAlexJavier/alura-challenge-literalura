package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.DatosLibro;
import com.aluracursos.literalura.model.GutendexResponse;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }


    public void muestraElMenu()
    {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    --------------------------------
                    Elija la opción a través de su número:
                    1 - Buscar Libro por su Título 
                    2 - Listar Libros Registrados
                    3 - Listar Autores Registrados
                    4 - Listar Autores vivos en un determinado Año
                    5 - Listar Libros por Idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                default:
                    if (opcion != 0) {
                        System.out.println(
                                "Opción no válida. Por favor, elija una opción del menú\n" +
                                "---------------------------\n");
                    }
                    break;
            }

        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println(menuIdiomas());
        var idioma = teclado.nextLine();
        var resultado = libroRepository.findByIdiomaIgnoreCase(idioma);
        if (resultado.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            resultado.forEach(System.out::println);
            teclado.nextLine();
        }

    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar:");
        var anio = teclado.nextInt();
        autorRepository.findAutoresVivosEnAnio(anio).forEach(System.out::println);
        teclado.nextLine();
    }

    private void listarAutoresRegistrados() {
        autorRepository.findAll().forEach(System.out::println);
        teclado.nextLine();
    }

    private void listarLibrosRegistrados() {
        libroRepository.findAll().forEach(System.out::println);
        teclado.nextLine();
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var titulo = teclado.nextLine();
        GutendexResponse respuesta = obtenerLibro(titulo);

        if (respuesta.getResults() == null || respuesta.getResults().isEmpty()) {
            System.out.println("Libro no encontrado.");
            return;
        }
        DatosLibro datosLibro = respuesta.getResults().get(0);
        procesarYGuardarLibro(datosLibro);
        teclado.nextLine();
    }

    private GutendexResponse obtenerLibro(@NonNull String titulo) {
        String url = URL_BASE + "?search=" + titulo.toLowerCase().replace(" ", "%20");
        String json = consumoApi.obtenerDatos(url);
        System.out.println(json);
        return conversor.obtenerDatos(json, GutendexResponse.class);
    }
    private void procesarYGuardarLibro(@NonNull DatosLibro datosLibro) {
        String nombreAutor = datosLibro.getAutor().nombre();

        if (verificarLibroExistente(datosLibro.titulo(), nombreAutor)) {
            return;
        }

        Autor autor = obtenerOCrearAutor(nombreAutor, datosLibro);
        guardarLibro(datosLibro, autor);
    }

    private boolean verificarLibroExistente(String titulo, String nombreAutor) {
        Optional<Libro> libroExistente = libroRepository
                .findByTituloAndAutor_Nombre(titulo, nombreAutor);

        if (libroExistente.isPresent()) {
            System.out.println("No se puede registrar el mismo libro mas de una vez.");
            System.out.println(libroExistente.get().toString());
            return true;
        }
        return false;
    }

    private Autor obtenerOCrearAutor(String nombreAutor, DatosLibro datosLibro) {
        return autorRepository.findByNombre(nombreAutor)
                .orElseGet(() -> autorRepository.save(new Autor(datosLibro.getAutor())));
    }

    private void guardarLibro(DatosLibro datosLibro, Autor autor) {
        Libro libro = new Libro(datosLibro, autor);
        libroRepository.save(libro);
        System.out.println(libro.toString());
    }
    private String menuIdiomas() {
        return """
                Ingrese el idioma para buscar los libros:\s
                en - Inglés
                es - Español
                fr - Francés
                pt - Portugués
                """;
    }



}
