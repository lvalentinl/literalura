package com.literalura.main;

import com.literalura.model.*;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import com.literalura.service.ConsumoAPI;
import com.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ----------- Menú -----------
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    ----------------------------
                    Elige una opción:
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Opción no válida, por favor ingrese un número.");
                teclado.nextLine();
                continue;
            }

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
                    listarAutoresVivosPorAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingresa el título del libro que quieres buscar:");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, ResultadosAPI.class);

        if (datos.libros().isEmpty()) {
            System.out.println("Libro no encontrado.");
            return;
        }

        DatosLibro datosLibro = datos.libros().get(0);

        // Verifica si el libro ya existe en la base de datos
        if (libroRepository.existsByTituloIgnoreCase(datosLibro.titulo())) {
            System.out.println("El libro ya está registrado en la base de datos.");
            return;
        }

        // Obtiene o crea el autor
        DatosAutor datosAutor = datosLibro.autores().get(0);
        Autor autor = autorRepository.findByNombre(datosAutor.nombre())
                .orElseGet(() -> {
                    Autor nuevoAutor = new Autor(datosAutor);
                    autorRepository.save(nuevoAutor);
                    return nuevoAutor;
                });

        // Crea el libro
        Libro libro = new Libro(datosLibro);
        libro.setAutor(autor);
        libroRepository.save(libro);
        System.out.println("Libro registrado exitosamente: " + libro);
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll().stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .toList();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("Ingresa el año para listar los autores vivos:");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine();
            List<Autor> autores = autorRepository.autoresVivosPorAnio(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en ese año.");
            } else {
                autores.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Año no válido, por favor ingrese un número.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingresa el idioma para listar los libros (es, en, fr, pt):");
        var idioma = teclado.nextLine().toLowerCase();
        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            libros.forEach(System.out::println);
        }
    }
}