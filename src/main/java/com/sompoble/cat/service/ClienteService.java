package com.sompoble.cat.service;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import java.util.List;

/**
 * Servicio que define las operaciones para gestionar clientes.
 * <p>
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre
 * clientes, así como búsquedas por diferentes criterios.
 * </p>
 */
public interface ClienteService {

    /**
     * Busca un cliente completo por su DNI.
     *
     * @param dni el Documento Nacional de Identidad del cliente.
     * @return un objeto {@code Cliente} completo si el cliente existe, o null
     * si no se encuentra.
     */
    Cliente findByDniFull(String dni);

    /**
     * Busca un cliente por su DNI y devuelve su representación DTO.
     *
     * @param dni el Documento Nacional de Identidad del cliente.
     * @return un objeto {@code ClienteDTO} si el cliente existe, o null si no
     * se encuentra.
     */
    ClienteDTO findByDni(String dni);

    /**
     * Actualiza la información de un cliente en la base de datos.
     *
     * @param cliente el objeto {@link Cliente} con la información actualizada.
     */
    void updateCliente(Cliente cliente);

    /**
     * Agrega un nuevo cliente a la base de datos.
     *
     * @param cliente el objeto {@link Cliente} a guardar.
     */
    void addCliente(Cliente cliente);

    /**
     * Obtiene todos los clientes registrados en la base de datos.
     *
     * @return una lista de objetos {@code ClienteDTO} con la información de los
     * clientes.
     */
    List<ClienteDTO> findAll();

    /**
     * Verifica si un cliente existe en la base de datos por su ID.
     *
     * @param id el identificador del cliente.
     * @return true si el cliente existe, false en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Elimina un cliente de la base de datos mediante su ID.
     *
     * @param id el identificador del cliente a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si un cliente existe en la base de datos por su DNI.
     *
     * @param dni el Documento Nacional de Identidad del cliente.
     * @return true si el cliente existe, false en caso contrario.
     */
    boolean existsByDni(String dni);

    /**
     * Elimina un cliente de la base de datos mediante su DNI.
     *
     * @param dni el Documento Nacional de Identidad del cliente a eliminar.
     */
    void deleteByDni(String dni);

    /**
     * Verifica si un cliente existe en la base de datos por su correo
     * electrónico.
     *
     * @param email el correo electrónico del cliente.
     * @return true si el cliente existe, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Busca un cliente por su correo electrónico.
     *
     * @param email el correo electrónico del cliente.
     * @return un objeto {@code ClienteDTO} si el cliente existe, o null si no
     * se encuentra.
     */
    ClienteDTO findByEmail(String email);
}
