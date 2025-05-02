package com.sompoble.cat.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio CloudinaryService.
 * <p>
 * Esta clase contiene pruebas que verifican el funcionamiento correcto de los
 * métodos de subida, eliminación y obtención de URLs de imágenes en Cloudinary,
 * utilizando mocks para simular las respuestas de la API de Cloudinary sin
 * necesidad de realizar llamadas reales.
 * </p>
 */
public class CloudinaryServiceTest {

    /**
     * Mock de la clase Cloudinary para simular sus comportamientos.
     */
    @Mock
    private Cloudinary cloudinary;

    /**
     * Mock del uploader de Cloudinary para simular operaciones de subida y
     * eliminación.
     */
    @Mock
    private Uploader uploader;

    /**
     * Mock del generador de URLs de Cloudinary.
     */
    @Mock
    private com.cloudinary.Url cloudinaryUrl;

    /**
     * Instancia del servicio a probar, con sus dependencias mockeadas.
     */
    @InjectMocks
    private CloudinaryService cloudinaryService;

    /**
     * Configura el entorno de pruebas antes de cada test.
     * <p>
     * Inicializa los mocks y prepara las respuestas simuladas de Cloudinary.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(cloudinary.url()).thenReturn(cloudinaryUrl);
    }

    /**
     * Prueba la subida exitosa de una imagen a Cloudinary.
     * <p>
     * Verifica que el método uploadImage procese correctamente la subida y
     * devuelva un mapa con la información esperada del recurso.
     * </p>
     *
     * @throws IOException Si ocurre un error durante la simulación de subida
     */
    @Test
    public void testUploadImage_Success() throws IOException {
        // Preparar datos de prueba
        MockMultipartFile file = new MockMultipartFile(
                "testImage",
                "test.jpg",
                "image/jpeg",
                "contenido de prueba".getBytes()
        );

        String folder = "test-folder";

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("public_id", "test_public_id");
        uploadResult.put("url", "http://test-url.com/image.jpg");
        uploadResult.put("secure_url", "https://test-url.com/image.jpg");

        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        Map<String, String> result = cloudinaryService.uploadImage(file, folder);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("test_public_id", result.get("publicId"));
        assertEquals("http://test-url.com/image.jpg", result.get("url"));
        assertEquals("https://test-url.com/image.jpg", result.get("secureUrl"));

        verify(uploader).upload(any(byte[].class), argThat(map
                -> map.containsKey("folder") && map.get("folder").equals(folder)
        ));
    }

    /**
     * Prueba el manejo de errores durante la subida de una imagen.
     * <p>
     * Verifica que el método uploadImage lance una RuntimeException cuando
     * ocurre un error de IO durante la subida.
     * </p>
     *
     * @throws IOException Simulación de error durante la subida
     */
    @Test
    public void testUploadImage_Error() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "testImage",
                "test.jpg",
                "image/jpeg",
                "contenido de prueba".getBytes()
        );

        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Error simulado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cloudinaryService.uploadImage(file, "test-folder");
        });

        assertEquals("Error al subir imagen a Cloudinary", exception.getMessage());
        assertTrue(exception.getCause() instanceof IOException);
    }

    /**
     * Prueba la eliminación exitosa de una imagen de Cloudinary.
     * <p>
     * Verifica que el método deleteImage procese correctamente la solicitud de
     * eliminación y devuelva true cuando la operación es exitosa.
     * </p>
     *
     * @throws IOException Si ocurre un error durante la simulación de
     * eliminación
     */
    @Test
    public void testDeleteImage_Success() throws IOException {
        String publicId = "test_public_id";

        Map<String, Object> deleteResult = new HashMap<>();
        deleteResult.put("result", "ok");

        when(uploader.destroy(eq(publicId), anyMap())).thenReturn(deleteResult);

        boolean result = cloudinaryService.deleteImage(publicId);

        assertTrue(result);

        verify(uploader).destroy(eq(publicId), anyMap());
    }

    /**
     * Prueba una eliminación fallida de imagen en Cloudinary.
     * <p>
     * Verifica que el método deleteImage devuelva false cuando la operación de
     * eliminación no es exitosa según la respuesta de Cloudinary.
     * </p>
     *
     * @throws IOException Si ocurre un error durante la simulación de
     * eliminación
     */
    @Test
    public void testDeleteImage_Failure() throws IOException {
        String publicId = "test_public_id";

        Map<String, Object> deleteResult = new HashMap<>();
        deleteResult.put("result", "not_found");

        when(uploader.destroy(eq(publicId), anyMap())).thenReturn(deleteResult);

        boolean result = cloudinaryService.deleteImage(publicId);

        assertFalse(result);
    }

    /**
     * Prueba el manejo de errores durante la eliminación de una imagen.
     * <p>
     * Verifica que el método deleteImage lance una RuntimeException cuando
     * ocurre un error de IO durante la eliminación.
     * </p>
     *
     * @throws IOException Simulación de error durante la eliminación
     */
    @Test
    public void testDeleteImage_Error() throws IOException {
        String publicId = "test_public_id";

        when(uploader.destroy(eq(publicId), anyMap())).thenThrow(new IOException("Error simulado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cloudinaryService.deleteImage(publicId);
        });

        assertEquals("Error al eliminar imagen de Cloudinary", exception.getMessage());
        assertTrue(exception.getCause() instanceof IOException);
    }

    /**
     * Prueba la obtención correcta de la URL de una imagen.
     * <p>
     * Verifica que el método getImageUrl devuelva la URL esperada para un
     * identificador público dado.
     * </p>
     */
    @Test
    public void testGetImageUrl() {
        // Preparar datos de prueba
        String publicId = "test_public_id";
        String expectedUrl = "https://test-url.com/test_public_id.jpg";

        when(cloudinaryUrl.generate(eq(publicId))).thenReturn(expectedUrl);

        String result = cloudinaryService.getImageUrl(publicId);

        assertEquals(expectedUrl, result);

        verify(cloudinaryUrl).generate(eq(publicId));
    }
}
