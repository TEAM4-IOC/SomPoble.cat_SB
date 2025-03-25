package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Servicio;
import java.util.List;

/**
 * Repositorio para gestionar operaciones de acceso a datos de la entidad {@link Servicio}.
 */
public interface ServicioRepository {

    /**
     * Busca un servicio por su identificador único.
     *
     * @param id Identificador del servicio.
     * @return El servicio encontrado o {@code null} si no existe.
     */
    Servicio findById(Long id);

    /**
     * Actualiza un servicio existente en la base de datos.
     *
     * @param servicio Objeto {@link Servicio} con la información actualizada.
     */
    void updateServicio(Servicio servicio);

    /**
     * Agrega un nuevo servicio a la base de datos.
     *
     * @param servicio Objeto {@link Servicio} que se va a persistir.
     */
    void addServicio(Servicio servicio);

    /**
     * Obtiene todos los servicios asociados a una empresa específica.
     *
     * @param empresaId Identificador de la empresa.
     * @return Lista de servicios pertenecientes a la empresa indicada.
     */
    List<Servicio> findAllByEmpresaId(Long empresaId);

    /**
     * Obtiene todos los servicios asociados a una empresa específica a partir de su identificador fiscal.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de servicios pertenecientes a la empresa con el identificador fiscal dado.
     */
    List<Servicio> findAllByEmpresaIdentificador(String identificadorFiscal);

    /**
     * Verifica si existe un servicio con el identificador especificado.
     *
     * @param id Identificador del servicio.
     * @return {@code true} si el servicio existe, {@code false} en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Elimina un servicio por su identificador.
     *
     * @param id Identificador del servicio a eliminar.
     */
    void deleteById(Long id);
}