package com.sompoble.cat.service;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.EmpresaDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Servicio que define las operaciones para gestionar empresas.
 * <p>
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre
 * empresas, así como búsquedas por diferentes criterios.
 * </p>
 */
public interface EmpresaService {

    /**
     * Busca una empresa por su identificador fiscal y devuelve su
     * representación DTO.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return un objeto {@code EmpresaDTO} si la empresa existe, o null si no
     * se encuentra.
     */
    EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal);

    /**
     * Busca una empresa completa por su identificador fiscal.
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

    /**
     * Sube o reemplaza la imagen de una empresa.
     *
     * @param identificadorFiscal identificador fiscal de la empresa de la empresa
     * @param imagen Nueva imagen a subir
     * @return Empresa actualizada con la nueva imagen
     */
    Empresa uploadEmpresaImage(String identificadorFiscal, MultipartFile imagen);
}
