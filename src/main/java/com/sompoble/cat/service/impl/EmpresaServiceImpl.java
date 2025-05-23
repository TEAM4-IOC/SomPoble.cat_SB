package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.service.CloudinaryService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.HorarioService;
import com.sompoble.cat.service.ReservaService;
import com.sompoble.cat.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementación de la interfaz {@link EmpresaService}.
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos definidos en
 * la interfaz EmpresaService, gestionando las operaciones relacionadas con las
 * empresas a través del repositorio correspondiente.
 * </p>
 */
@Service
public class EmpresaServiceImpl implements EmpresaService {

    /**
     * Repositorio para acceder a los datos de las empresas.
     */
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    @Lazy
    private ReservaService reservaService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private HorarioService horarioService;

    /**
     * {@inheritDoc}
     */
    @Override
    public EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal) {
        return empresaRepository.findByIdentificadorFiscal(identificadorFiscal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Empresa findByIdentificadorFiscalFull(String identificadorFiscal) {
        return empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEmpresa(Empresa empresa) {
        empresaRepository.updateEmpresa(empresa);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEmpresa(Empresa empresa) {
        empresaRepository.addEmpresa(empresa);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return empresaRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByIdentificadorFiscal(String identificadorFiscal) {
        return empresaRepository.existsByIdentificadorFiscal(identificadorFiscal);
    }

    /**
     * {@inheritDoc} Elimina una empresa por su identificador fiscal, así como
     * todos sus horarios, servicios y reservas asociados.
     */
    @Override
    public void deleteByIdentificadorFiscal(String identificadorFiscal) {
        reservaService.deleteByEmpresaIdentificadorFiscal(identificadorFiscal);

        Empresa empresa = empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal);
        Long idEmpresa = empresa.getIdEmpresa();

        List<Horario> horarios = horarioService.findByEmpresa_IdEmpresa(idEmpresa);
        for (Horario horario : horarios) {
            horarioService.deleteById(horario.getIdHorario());
        }
        
        List<Servicio> servicios = servicioService.obtenerPorEmpresaId(idEmpresa);
        for (Servicio servicio : servicios) {
            servicioService.eliminarPorId(servicio.getIdServicio());
        }

        empresaRepository.deleteByIdentificadorFiscal(identificadorFiscal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Empresa uploadEmpresaImage(String identificadorFiscal, MultipartFile imagen) {
        Empresa empresa = empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal);

        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("La imagen no puede estar vacía");
        }

        if (empresa.getImagenPublicId() != null && !empresa.getImagenPublicId().isEmpty()) {
            cloudinaryService.deleteImage(empresa.getImagenPublicId());
        }

        Map<String, String> uploadResult = cloudinaryService.uploadImage(imagen, "empresas");

        empresa.setImagenUrl(uploadResult.get("secureUrl"));
        empresa.setImagenPublicId(uploadResult.get("publicId"));

        empresaRepository.updateEmpresa(empresa);

        return empresa;
    }
}
