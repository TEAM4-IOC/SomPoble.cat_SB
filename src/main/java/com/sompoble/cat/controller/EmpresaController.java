/**
 * Controlador encargado de gestionar las operaciones relacionadas con las empresas y empresarios.
 */
package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.repository.impl.EmpresarioHibernate;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.service.EmpresarioService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador REST que expone endpoints para administrar empresas y
 * empresarios.
 */
@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    /**
     * Servicio que maneja las operaciones relacionadas con las empresas.
     */
    @Autowired
    private EmpresaService empresaService;
    /**
     * Servicio que maneja las operaciones relacionadas con los empresarios.
     */
    @Autowired
    private EmpresarioService empresarioService;
    /**
     * Repositorio Hibernate para acceder a los datos de los empresarios.
     */
    @Autowired
    private EmpresarioHibernate empresarioHibernate;

    /**
     * Obtiene todas las empresas registradas en la base de datos.
     *
     * @return ResponseEntity con una lista de empresas si existen, o una
     * excepción si no hay empresas.
     * @throws ResourceNotFoundException si no se encuentran empresas en la base
     * de datos.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<EmpresaDTO> empresas = empresaService.findAll();
        if (empresas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron empresas en la base de datos");
        }
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (EmpresaDTO empresa : empresas) {
            Map<String, Object> response = new HashMap<>();
            response.put("empresa", empresa);
            response.put("dni", empresa.getDniEmpresario() != null ? empresa.getDniEmpresario() : "No disponible");
            responseList.add(response);
        }
        return ResponseEntity.ok(responseList);
    }

    /**
     * Obtiene una empresa por su identificador fiscal.
     *
     * @param identificadorFiscal Identificador único de la empresa.
     * @return ResponseEntity con la empresa si se encuentra, o una excepción si
     * no se encuentra.
     * @throws ResourceNotFoundException si la empresa con el identificador
     * fiscal especificado no existe.
     */
    @GetMapping("/{identificadorFiscal}")
    public ResponseEntity<?> getByIdentificadorFiscal(@PathVariable String identificadorFiscal) {
        try {
            EmpresaDTO empresa = empresaService.findByIdentificadorFiscal(identificadorFiscal);
            //String dni = empresa.getDniEmpresario();

            Map<String, Object> response = new HashMap<>();
            response.put("empresa", empresa);
            response.put("dni", empresa.getDniEmpresario() != null ? empresa.getDniEmpresario() : "No disponible");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Empresa o autónomo con " + identificadorFiscal + " no encontrada");
        }
    }

    //IMPORTANTE: se comenta este metodo ya que se hace uso del que permite realizar la subida de imagenes. No se borra
    //ya que se desea mantener por si en un futuro es necesario recuperarlo
    /*
     * Crea una nueva empresa asociada a un empresario.
     *
     * @param request Mapa que contiene los datos de la empresa y el DNI del
     * empresario.
     * @return ResponseEntity indicando que la empresa ha sido creada.
     * @throws BadRequestException si el identificador fiscal ya existe o si el
     * empresario no existe.
     */
    /*@PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> empresaData = (Map<String, Object>) request.get("empresa");

        String identificadorFiscal = (String) empresaData.get("identificadorFiscal");
        if (empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
            throw new BadRequestException("Empresa con identificador fiscal " + identificadorFiscal + " ya existe");
        }

        EmpresarioDTO empresarioDTO = null;
        String dni = null;
        boolean exists = false;

        if (request.containsKey("dni")) {
            dni = (String) request.get("dni");
            exists = empresarioService.existsByDni(dni);
        } else {
            throw new BadRequestException("No se ha informado el DNI del empresario");
        }

        Empresario empresario = null;

        if (!exists) {
            throw new BadRequestException("No existe un empresario con DNI " + dni);
        } else {
            empresario = empresarioService.findEmpresarioByDNI(dni);

            empresarioDTO = empresarioService.findByDni(dni);
        }

        if (empresarioDTO.getEmpresas() != null && !empresarioDTO.getEmpresas().isEmpty()) {
            throw new BadRequestException("El empresario con DNI " + dni + " ya tiene una empresa/autónomo asignada");
        }

        // Crear la empresa con los datos recibidos
        Empresa empresa = new Empresa();
        empresa.setEmpresario(empresario);
        empresa.setIdentificadorFiscal(identificadorFiscal);
        empresa.setDireccion((String) empresaData.get("direccion"));
        empresa.setEmail((String) empresaData.get("email"));
        empresa.setTelefono((String) empresaData.get("telefono"));

        if (empresaData.containsKey("actividad") && empresaData.get("actividad") != null && !empresaData.get("actividad").toString().isEmpty()) {
            empresa.setActividad((String) empresaData.get("actividad"));
            //Autonomo
            empresa.setTipo(2);
        } else {
            empresa.setNombre((String) empresaData.get("nombre"));
            //Empresa
            empresa.setTipo(1);
        }

        empresaService.addEmpresa(empresa);
        return ResponseEntity.created(null).build();
    }*/

    /**
     * Crea una nueva empresa asociada a un empresario, con posibilidad de
     * incluir imagen. Este método es idéntico al create original pero acepta
     * una imagen adicional.
     *
     * @param empresaJson JSON con los datos de la empresa
     * @param dniJson JSON con el DNI del empresario
     * @param imagen Imagen de la empresa (opcional)
     * @return ResponseEntity indicando que la empresa ha sido creada
     * @throws BadRequestException si el identificador fiscal ya existe o si el
     * empresario no existe
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createWithImage(
            @RequestPart("empresa") String empresaJson,
            @RequestPart("dni") String dniJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> empresaData = mapper.readValue(empresaJson, Map.class);
            Map<String, Object> dniMap = mapper.readValue(dniJson, Map.class);
            String dni = (String) dniMap.get("dni");

            String identificadorFiscal = (String) empresaData.get("identificadorFiscal");
            if (empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
                throw new BadRequestException("Empresa con identificador fiscal " + identificadorFiscal + " ya existe");
            }

            EmpresarioDTO empresarioDTO = null;
            boolean exists = false;
            if (dni != null) {
                exists = empresarioService.existsByDni(dni);
            } else {
                throw new BadRequestException("No se ha informado el DNI del empresario");
            }

            Empresario empresario = null;
            if (!exists) {
                throw new BadRequestException("No existe un empresario con DNI " + dni);
            } else {
                empresario = empresarioService.findEmpresarioByDNI(dni);
                empresarioDTO = empresarioService.findByDni(dni);
            }

            if (empresarioDTO.getEmpresas() != null && !empresarioDTO.getEmpresas().isEmpty()) {
                throw new BadRequestException("El empresario con DNI " + dni + " ya tiene una empresa/autónomo asignada");
            }

            Empresa empresa = new Empresa();
            empresa.setEmpresario(empresario);
            empresa.setIdentificadorFiscal(identificadorFiscal);
            empresa.setDireccion((String) empresaData.get("direccion"));
            empresa.setEmail((String) empresaData.get("email"));
            empresa.setTelefono((String) empresaData.get("telefono"));

            if (empresaData.containsKey("actividad") && empresaData.get("actividad") != null && !((String) empresaData.get("actividad")).isEmpty()) {
                empresa.setActividad((String) empresaData.get("actividad"));
                empresa.setTipo(2);
            } else {
                empresa.setNombre((String) empresaData.get("nombre"));
                empresa.setTipo(1);
            }

            empresaService.addEmpresa(empresa);

            if (imagen != null && !imagen.isEmpty()) {
                empresaService.uploadEmpresaImage(empresa.getIdentificadorFiscal(), imagen);
            }

            return ResponseEntity.created(null).build();

        } catch (IOException e) {
            throw new BadRequestException("Error al procesar los datos de la empresa: " + e.getMessage());
        }
    }
    
    //IMPORTANTE: se comenta este metodo ya que se hace uso del que permite realizar la subida de imagenes. No se borra
    //ya que se desea mantener por si en un futuro es necesario recuperarlo
    /*
     * Actualiza una empresa existente.
     *
     * @param identificadorFiscal Identificador único de la empresa a
     * actualizar.
     * @param updates Mapa con los nuevos valores para los campos de la empresa.
     * @return ResponseEntity indicando que la empresa ha sido actualizada
     * correctamente.
     * @throws ResourceNotFoundException si la empresa con el identificador
     * fiscal especificado no existe.
     */
    /*@PutMapping("/{identificadorFiscal}")
    public ResponseEntity<?> update(@PathVariable String identificadorFiscal, @RequestBody Map<String, Object> updates) {
        try {
            Empresa existingEmpresa = empresaService.findByIdentificadorFiscalFull(identificadorFiscal);
            updates.forEach((key, value) -> {
                if (value != null) {
                    switch (key) {
                        case "identificadorFiscal" ->
                            existingEmpresa.setIdentificadorFiscal(value.toString());
                        case "nombre" ->
                            existingEmpresa.setNombre(value.toString());
                        case "actividad" ->
                            existingEmpresa.setActividad(value.toString());
                        case "direccion" ->
                            existingEmpresa.setDireccion(value.toString());
                        case "email" ->
                            existingEmpresa.setEmail(value.toString());
                        case "telefono" ->
                            existingEmpresa.setTelefono(value.toString());
                    }
                }
            });

            empresaService.updateEmpresa(existingEmpresa);
            return ResponseEntity.ok("Empresa o autónomo con identificador fiscal " + identificadorFiscal + " actualizada correctamente");
        } catch (Exception e) {
            throw new ResourceNotFoundException("No se encontró una empresa o autónomo con el identificador fiscal " + identificadorFiscal);
        }
    }*/

    /**
     * Actualiza una empresa existente, incluyendo su imagen si se proporciona.
     *
     * @param identificadorFiscal Identificador único de la empresa a
     * actualizar.
     * @param empresaJson Datos de la empresa en formato JSON.
     * @param imagen Archivo de imagen (opcional).
     * @return ResponseEntity indicando que la empresa ha sido actualizada
     * correctamente.
     * @throws ResourceNotFoundException si la empresa con el identificador
     * fiscal especificado no existe.
     */
    @PutMapping(value = "/{identificadorFiscal}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable String identificadorFiscal,
            @RequestPart("empresa") String empresaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Empresa existingEmpresa = empresaService.findByIdentificadorFiscalFull(identificadorFiscal);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> updates = mapper.readValue(empresaJson, Map.class);

            updates.forEach((key, value) -> {
                if (value != null) {
                    switch (key) {
                        case "identificadorFiscal" ->
                            existingEmpresa.setIdentificadorFiscal(value.toString());
                        case "nombre" ->
                            existingEmpresa.setNombre(value.toString());
                        case "actividad" ->
                            existingEmpresa.setActividad(value.toString());
                        case "direccion" ->
                            existingEmpresa.setDireccion(value.toString());
                        case "email" ->
                            existingEmpresa.setEmail(value.toString());
                        case "telefono" ->
                            existingEmpresa.setTelefono(value.toString());
                    }
                }
            });

            empresaService.updateEmpresa(existingEmpresa);

            if (imagen != null && !imagen.isEmpty()) {
                empresaService.uploadEmpresaImage(existingEmpresa.getIdentificadorFiscal(), imagen);
            }

            return ResponseEntity.ok("Empresa o autónomo con identificador fiscal " + identificadorFiscal + " actualizada correctamente");
        } catch (Exception e) {
            throw new ResourceNotFoundException("No se encontró una empresa o autónomo con el identificador fiscal " + identificadorFiscal + ": " + e.getMessage());
        }
    }

    /**
     * Elimina una empresa existente por su identificador fiscal.
     *
     * @param identificadorFiscal Identificador único de la empresa a eliminar.
     * @return ResponseEntity indicando que la empresa ha sido eliminada
     * correctamente.
     * @throws ResourceNotFoundException si la empresa con el identificador
     * fiscal especificado no existe.
     */
    @DeleteMapping("/{identificadorFiscal}")
    public ResponseEntity<?> delete(@PathVariable String identificadorFiscal) {
        if (!empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
            throw new ResourceNotFoundException("No se encontró una empresa con el identificador fiscal " + identificadorFiscal);
        }

        empresaService.deleteByIdentificadorFiscal(identificadorFiscal);

        return ResponseEntity.ok("Empresa o autónomo con identificador fiscal " + identificadorFiscal + " eliminada correctamente");
    }
}
