package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.service.ServicioService;
import com.sompoble.cat.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los servicios de la empresa.
 */
@Service
public class ServicioServiceImpl implements ServicioService  {

    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Obtiene un servicio por su identificador.
     *
     * @param id Identificador único del servicio.
     * @return El servicio correspondiente o {@code null} si no se encuentra.
     */
    @Override
    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id);
    }

    /**
     * Actualiza un servicio existente en la base de datos.
     *
     * @param servicio Objeto {@link Servicio} con la información actualizada.
     */
    @Override
    public void actualizarServicio(Servicio servicio) {
        servicioRepository.updateServicio(servicio);
    }

    /**
     * Agrega un nuevo servicio a la base de datos.
     *
     * @param servicio Objeto {@link Servicio} a persistir.
     */
    @Override
    public void agregarServicio(Servicio servicio) {
        servicioRepository.addServicio(servicio);
    }

    /**
     * Obtiene todos los servicios asociados a una empresa por su identificador.
     *
     * @param empresaId Identificador único de la empresa.
     * @return Lista de servicios asociados a la empresa.
     */
    @Override
    public List<Servicio> obtenerPorEmpresaId(Long empresaId) {
        return servicioRepository.findAllByEmpresaId(empresaId);
    }

    /**
     * Obtiene todos los servicios de una empresa a partir de su identificador fiscal.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de servicios asociados a la empresa con el identificador fiscal dado.
     */
    @Override
    public List<Servicio> obtenerPorEmpresaIdentificador(String identificadorFiscal) {
        return servicioRepository.findAllByEmpresaIdentificador(identificadorFiscal);
    }

    /**
     * Verifica si un servicio existe en la base de datos mediante su identificador.
     *
     * @param id Identificador único del servicio.
     * @return {@code true} si el servicio existe, {@code false} en caso contrario.
     */
    @Override
    public boolean existePorId(Long id) {
        return servicioRepository.existsById(id);
    }

    /**
     * Elimina un servicio de la base de datos por su identificador.
     *
     * @param id Identificador único del servicio a eliminar.
     */
    @Override
    public void eliminarPorId(Long id) {
        servicioRepository.deleteById(id);
    }
}
