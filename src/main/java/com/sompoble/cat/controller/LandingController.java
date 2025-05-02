package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.dto.LandingEmpresaDTO;
import com.sompoble.cat.dto.LandingServicioDTO;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.repository.ServicioRepository;
import com.sompoble.cat.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la página de inicio (landing page). Proporciona un
 * endpoint optimizado que combina datos de empresas y sus servicios para
 * reducir el número de llamadas API necesarias desde el frontend.
 *
 * @author SomPoble Backend Team
 * @version 1.0
 * @since 2025-05-01
 */
@RestController
@RequestMapping("/api/landing")
public class LandingController {

    /**
     * Servicio para acceder a los datos de empresas.
     */
    @Autowired
    private EmpresaService empresaService;

    /**
     * Repositorio para acceder a los datos de servicios.
     */
    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Obtiene datos combinados de empresas y sus servicios para la página de
     * inicio. Este método reduce múltiples llamadas API en una sola, mejorando
     * el rendimiento del frontend al cargar la página de inicio.
     *
     * @return ResponseEntity con una lista de empresas y sus servicios en
     * formato DTO optimizado
     * @throws ResourceNotFoundException si no se encuentran empresas en la base
     * de datos
     */
    @GetMapping
    public ResponseEntity<List<LandingEmpresaDTO>> getLandingData() {
        List<EmpresaDTO> empresas = empresaService.findAll();
        if (empresas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron empresas en la base de datos");
        }

        List<LandingEmpresaDTO> responseList = new ArrayList<>();

        for (EmpresaDTO empresa : empresas) {
            // Obtener servicios para la empresa actual
            List<Servicio> servicios = servicioRepository.findAllByEmpresaId(empresa.getIdEmpresa());

            List<LandingServicioDTO> serviciosDTO = servicios.stream()
                    .map(servicio -> new LandingServicioDTO(servicio.getNombre()))
                    .collect(Collectors.toList());

            LandingEmpresaDTO landingEmpresa = new LandingEmpresaDTO(
                    empresa.getNombre(),
                    empresa.getDireccion(),
                    empresa.getTelefono(),
                    empresa.getEmail(),
                    empresa.getImagenUrl(),
                    empresa.getIdentificadorFiscal(),
                    serviciosDTO
            );

            responseList.add(landingEmpresa);
        }

        return ResponseEntity.ok(responseList);
    }
}
