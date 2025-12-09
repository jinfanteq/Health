package controller;

import model.entidades.Usuario;
import model.entidades.Medico;
import model.entidades.Admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class ArchivoController {

    // Ruta donde se guardará el archivo compartido
    private static final String RUTA_ARCHIVO = "archivos/planillaHospital.pdf";

    public ArchivoController() {
        File carpeta = new File("archivos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    // -----------------------------
    // 1. SUBIR ARCHIVO (solo ADMIN)
    // -----------------------------
    public boolean subirArchivo(Usuario usuario, File archivoSeleccionado) throws IOException {

        if (!esAdmin(usuario)) {
            System.out.println("No tienes permisos para subir archivos.");
            return false;
        }

        if (archivoSeleccionado == null || !archivoSeleccionado.exists()) {
            System.out.println("Archivo inválido.");
            return false;
        }

        copiarArchivo(archivoSeleccionado, new File(RUTA_ARCHIVO));

        System.out.println("Archivo subido exitosamente.");
        return true;
    }


    // --------------------------------
    // 2. ACTUALIZAR ARCHIVO (solo ADMIN)
    // --------------------------------
    public boolean actualizarArchivo(Usuario usuario, File archivoNuevo) throws IOException {

        if (!esAdmin(usuario)) {
            System.out.println("No tienes permiso para actualizar el archivo.");
            return false;
        }

        File archivoActual = new File(RUTA_ARCHIVO);

        if (!archivoActual.exists()) {
            System.out.println("No existe archivo previo. Usa subirArchivo().");
            return false;
        }

        copiarArchivo(archivoNuevo, archivoActual);

        System.out.println("Archivo actualizado.");
        return true;
    }


    // ------------------------------------------
    // 3. VER ARCHIVO (Admin y Médico)
    // ------------------------------------------
    public File obtenerArchivo(Usuario usuario) {

        if (!esAdmin(usuario) && !esMedico(usuario)) {
            System.out.println("No tienes permiso para ver este archivo.");
            return null;
        }

        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("Archivo no encontrado.");
            return null;
        }

        return archivo;
    }


    // ------------------------------------------
    // 4. ELIMINAR ARCHIVO (solo Admin)
    // ------------------------------------------
    public boolean eliminarArchivo(Usuario usuario) {

        if (!esAdmin(usuario)) {
            System.out.println("No tienes permiso para eliminar archivos.");
            return false;
        }

        File archivo = new File(RUTA_ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("No hay archivo para eliminar.");
            return false;
        }

        boolean eliminado = archivo.delete();

        if (eliminado) {
            System.out.println("Archivo eliminado.");
        } else {
            System.out.println("No se pudo eliminar el archivo.");
        }

        return eliminado;
    }


    // ------------------------------------------
    // MÉTODOS PRIVADOS
    // ------------------------------------------

    private boolean esAdmin(Usuario u) {
        return u != null && u.getRol().equalsIgnoreCase("admin");
    }

    private boolean esMedico(Usuario u) {
        return u != null && u.getRol().equalsIgnoreCase("medico");
    }

    private void copiarArchivo(File origen, File destino) throws IOException {
        try (FileInputStream fis = new FileInputStream(origen);
             FileOutputStream fos = new FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int tam;

            while ((tam = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, tam);
            }
        }
    }
}
