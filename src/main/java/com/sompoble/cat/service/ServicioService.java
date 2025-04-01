package com.sompoble.cat.service;

import com.sompoble.cat.domain.Servicio;
import java.util.List;

/**
 * Interfaz que define los métodos relacionados con la gestión de servicios.
 */
public interface ServicioService {

    /**
     * Obtiene un servicio por su identificador.
     *
     * @param id Identificador único del servicio.
     * @return El servicio correspondiente o {@code null} si no se encuentra.
     */
    Servicio obtenerPorId(Long id);

    /**
     * Actualiza un servicio existente en la base de datos.
     *
     * @param servicio Objeto {@link Servicio} con la información actualizada.
     */
    void actualizarServicio(Servicio servicio);

    /**
     * Agrega un nuevo servicio a la base de datos.
     *
     * @param servicio Objeto {@link Servicio} a persistir.
     */
    void agregarServicio(Servicio servicio);

    /**
     * Obtiene todos los servicios asociados a una empresa por su identificador.
     *
     * @param empresaId Identificador único de la empresa.
     * @return Lista de servicios asociados a la empresa.
     */
    List<Servicio> obtenerPorEmpresaId(Long empresaId);

    /**
     * Obtiene todos los servicios de una empresa a partir de su identificador fiscal.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de servicios asociados a la empresa con el identificador fiscal dado.
     */
    List<Servicio> obtenerPorEmpresaIdentificador(String identificadorFiscal);

    /**
     * Verifica si un servicio existe en la base de datos mediante su identificador.
     *
     * @param id Identificador único del servicio.
     * @return {@code true} si el servicio existe, {@code false} en caso contrario.
     */
    boolean existePorId(Long id);

    /**
     * Elimina un servicio de la base de datos por su identificador.
     *
     * @param id Identificador único del servicio a eliminar.
     */
    void eliminarPorId(Long id);
}
