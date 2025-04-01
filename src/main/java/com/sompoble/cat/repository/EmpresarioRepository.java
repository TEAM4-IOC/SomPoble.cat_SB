package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad {@code Empresario}.
 * <p>
 * Proporciona métodos para gestionar empresarios en la base de datos.
 * </p>
 * 
 * @author SomPoble
 */
@Repository
public interface EmpresarioRepository {

    /**
     * Busca un empresario por su DNI.
     *
     * @param dni el Documento Nacional de Identidad del empresario.
     * @return un objeto {@code EmpresarioDTO} si el empresario existe, o null si no se encuentra.
     */
    EmpresarioDTO findByDNI(String dni);
    
    /**
     * Busca un empresario completo por su DNI.
     *
     * @param dni el Documento Nacional de Identidad del empresario.
     * @return un objeto {@code EmpresarioDTO} si el empresario existe, o null si no se encuentra.
     */
    Empresario findEmpresarioByDNI(String dni);

    /**
     * Actualiza la información de un empresario en la base de datos.
     *
     * @param empresario el objeto {@link Empresario} con la información actualizada.
     */
    void updateEmpresario(EmpresarioDTO empresario);

    /**
     * Agrega un nuevo empresario a la base de datos.
     *
     * @param empresario el objeto {@link Empresario} a guardar.
     */
    void addEmpresario(Empresario empresario);

    /**
     * Obtiene todos los empresarios registrados en la base de datos.
     *
     * @return una lista de objetos {@code EmpresarioDTO} con la información de los empresarios.
     */
    List<EmpresarioDTO> findAll();

    /**
     * Verifica si un empresario existe en la base de datos por su ID.
     *
     * @param id el identificador del empresario.
     * @return true si el empresario existe, false en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Elimina un empresario de la base de datos mediante su ID.
     *
     * @param id el identificador del empresario a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si un empresario existe en la base de datos por su DNI.
     *
     * @param dni el Documento Nacional de Identidad del empresario.
     * @return true si el empresario existe, false en caso contrario.
     */
    boolean existsByDni(String dni);

    /**
     * Elimina un empresario de la base de datos mediante su DNI.
     *
     * @param dni el Documento Nacional de Identidad del empresario a eliminar.
     */
    void deleteByDni(String dni);

    /**
     * Verifica si un empresario existe en la base de datos por su correo electrónico.
     *
     * @param email el correo electrónico del empresario.
     * @return true si el empresario existe, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Busca un empresario por su correo electrónico.
     *
     * @param email el correo electrónico del empresario.
     * @return un objeto {@code EmpresarioDTO} si el empresario existe, o null si no se encuentra.
     */
    EmpresarioDTO findByEmail(String email);
}