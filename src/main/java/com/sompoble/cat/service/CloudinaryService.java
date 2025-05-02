package com.sompoble.cat.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para gestionar operaciones con Cloudinary.
 * <p>
 * Este servicio proporciona métodos para subir, eliminar y obtener URLs de
 * imágenes almacenadas en la plataforma Cloudinary, facilitando la gestión de
 * recursos multimedia en la aplicación.
 * </p>
 */
@Service
public class CloudinaryService {

    /**
     * Instancia de Cloudinary configurada para interactuar con la cuenta.
     */
    private final Cloudinary cloudinary;

    /**
     * Constructor que inicializa el servicio con la configuración de
     * Cloudinary.
     *
     * @param cloudinary Instancia configurada de Cloudinary inyectada
     * automáticamente
     */
    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Sube una imagen a Cloudinary y devuelve información sobre el recurso
     * subido.
     * <p>
     * Este método permite opcionalmente especificar una carpeta de destino en
     * Cloudinary para organizar los recursos.
     * </p>
     *
     * @param file Archivo de imagen a subir
     * @param folder Carpeta de destino en Cloudinary (opcional)
     * @return Mapa con información del recurso subido (publicId, url,
     * secureUrl)
     * @throws RuntimeException Si ocurre un error durante la subida
     */
    public Map<String, String> uploadImage(MultipartFile file, String folder) {
        try {
            Map<String, Object> params = new HashMap<>();

            if (folder != null && !folder.isEmpty()) {
                params.put("folder", folder);
            }

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            Map<String, String> result = new HashMap<>();
            result.put("publicId", (String) uploadResult.get("public_id"));
            result.put("url", (String) uploadResult.get("url"));
            result.put("secureUrl", (String) uploadResult.get("secure_url"));

            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }

    /**
     * Elimina una imagen de Cloudinary utilizando su identificador público.
     *
     * @param publicId Identificador público de la imagen en Cloudinary
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws RuntimeException Si ocurre un error durante la eliminación
     */
    public boolean deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen de Cloudinary", e);
        }
    }

    /**
     * Obtiene la URL de una imagen almacenada en Cloudinary.
     *
     * @param publicId Identificador público de la imagen en Cloudinary
     * @return URL de acceso a la imagen
     */
    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}
