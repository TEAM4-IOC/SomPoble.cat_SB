package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.EmpresaDTO;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad {@code Empresa}.
 * <p>
 * Proporciona métodos para gestionar empresas en la base de datos.
 * </p>
 */
@Repository
public interface EmpresaRepository {

    /**
     * Busca una empresa por su identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return un objeto {@code EmpresaDTO} si la empresa existe, o null si no
     * se encuentra.
     */
    EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal);

    /**
     * Busca una empresa por su identificador fiscal y retorna toda la
     * información de la empresa.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return un objeto {@code Empresa} completo si la empresa existe, o null
     * si no se encuentra.
     */
    Empresa findByIdentificadorFiscalFull(String identificadorFiscal);

    /**
     * Actualiza la información de una empresa en la base de datos.
     *
     * @param empresa el objeto {@link Empresa} con la información actualizada.
     */
    void updateEmpresa(Empresa empresa);

    /**
     * Agrega una nueva empresa a la base de datos.
     *
     * @param empresa el objeto {@link Empresa} a guardar.
     */
    void addEmpresa(Empresa empresa);

    /**
     * Obtiene todas las empresas registradas en la base de datos.
     *
     * @return una lista de objetos {@code EmpresaDTO} con la información de las
     * empresas.
     */
    List<EmpresaDTO> findAll();

    /**
     * Verifica si una empresa existe en la base de datos por su ID.
     *
     * @param id el identificador de la empresa.
     * @return true si la empresa existe, false en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Elimina una empresa de la base de datos mediante su ID.
     *
     * @param id el identificador de la empresa a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si una empresa existe en la base de datos por su identificador
     * fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return true si la empresa existe, false en caso contrario.
     */
    boolean existsByIdentificadorFiscal(String identificadorFiscal);

    /**
     * Elimina una empresa de la base de datos mediante su identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa a
     * eliminar.
     */
    void deleteByIdentificadorFiscal(String identificadorFiscal);
}
