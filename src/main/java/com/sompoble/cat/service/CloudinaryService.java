package com.sompoble.cat.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

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

    public boolean deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar imagen de Cloudinary", e);
        }
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}