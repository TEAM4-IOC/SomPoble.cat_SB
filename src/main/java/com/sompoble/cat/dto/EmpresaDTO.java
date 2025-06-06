package com.sompoble.cat.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO (Data Transfer Object) para la entidad Empresa. Se utiliza para
 * transferir datos de Empresa sin exponer la entidad completa.
 */
public class EmpresaDTO implements Serializable {

    private Long idEmpresa;
    private String dniEmpresario;
    private String identificadorFiscal;
    private String nombre;
    private String actividad;
    private String direccion;
    private String email;
    private String telefono;
    private int tipo;
    private List<Long> idReservas;
    private List<Long> idServicios;
    private String imagenUrl;
    private String imagenPublicId;

    /**
     * Constructor sin parámetros requerido para la
     * serialización/deserialización. Este constructor es necesario para
     * frameworks como Jackson que utilizan reflection para instanciar objetos
     * durante los procesos de mapeo JSON.
     */
    public EmpresaDTO() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param idEmpresa ID de la empresa
     * @param dniEmpresario DNI del empresario
     * @param identificadorFiscal Identificador fiscal de la empresa
     * @param nombre Nombre de la empresa
     * @param actividad Actividad de la empresa
     * @param direccion Dirección de la empresa
     * @param email Correo electrónico de la empresa
     * @param telefono Número de teléfono de la empresa
     * @param tipo Tipo de la empresa
     * @param idReservas Lista de IDs de reservas asociadas
     * @param idServicios Lista de IDs de servicios asociados
     */
    public EmpresaDTO(Long idEmpresa, String dniEmpresario, String identificadorFiscal, String nombre, String actividad,
            String direccion, String email, String telefono, int tipo,
            List<Long> idReservas, List<Long> idServicios) {
        this.idEmpresa = idEmpresa;
        this.dniEmpresario = dniEmpresario;
        this.identificadorFiscal = identificadorFiscal;
        this.nombre = nombre;
        this.actividad = actividad;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.tipo = tipo;
        this.idReservas = idReservas;
        this.idServicios = idServicios;
    }

    /**
     * Obtiene el ID de la empresa.
     *
     * @return ID de la empresa
     */
    public Long getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * Establece el ID de la empresa.
     *
     * @param idEmpresa ID de la empresa
     */
    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * Obtiene el DNI del empresario.
     *
     * @return DNI del empresario
     */
    public String getDniEmpresario() {
        return dniEmpresario;
    }

    /**
     * Establece el DNI del empresario.
     *
     * @param dniEmpresario DNI del empresario
     */
    public void setDniEmpresario(String dniEmpresario) {
        this.dniEmpresario = dniEmpresario;
    }

    /**
     * Obtiene el identificador fiscal de la empresa.
     *
     * @return Identificador fiscal de la empresa
     */
    public String getIdentificadorFiscal() {
        return identificadorFiscal;
    }

    /**
     * Establece el identificador fiscal de la empresa.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa
     */
    public void setIdentificadorFiscal(String identificadorFiscal) {
        this.identificadorFiscal = identificadorFiscal;
    }

    /**
     * Obtiene el nombre de la empresa.
     *
     * @return Nombre de la empresa
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la empresa.
     *
     * @param nombre Nombre de la empresa
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la actividad de la empresa.
     *
     * @return Actividad de la empresa
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * Establece la actividad de la empresa.
     *
     * @param actividad Actividad de la empresa
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    /**
     * Obtiene la dirección de la empresa.
     *
     * @return Dirección de la empresa
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de la empresa.
     *
     * @param direccion Dirección de la empresa
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el correo electrónico de la empresa.
     *
     * @return Correo electrónico de la empresa
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico de la empresa.
     *
     * @param email Correo electrónico de la empresa
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono de la empresa.
     *
     * @return Número de teléfono de la empresa
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono de la empresa.
     *
     * @param telefono Número de teléfono de la empresa
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el tipo de la empresa.
     *
     * @return Tipo de la empresa
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la empresa.
     *
     * @param tipo Tipo de la empresa
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la lista de identificadores de reservas asociadas.
     *
     * @return Una lista de IDs de reservas.
     */
    public List<Long> getIdReservas() {
        return idReservas;
    }

    /**
     * Establece la lista de identificadores de reservas asociadas.
     *
     * @param idReservas Una lista de IDs de reservas a asignar.
     */
    public void setIdReservas(List<Long> idReservas) {
        this.idReservas = idReservas;
    }

    /**
     * Obtiene la lista de identificadores de servicios asociados.
     *
     * @return Una lista de IDs de servicios.
     */
    public List<Long> getIdServicios() {
        return idServicios;
    }

    /**
     * Establece la lista de identificadores de servicios asociados.
     *
     * @param idServicios Una lista de IDs de servicios a asignar.
     */
    public void setIdServicios(List<Long> idServicios) {
        this.idServicios = idServicios;
    }

    /**
     * Obtiene la URL de la imagen de la empresa.
     *
     * @return URL de la imagen
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Establece la URL de la imagen de la empresa.
     *
     * @param imagenUrl URL de la imagen
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    /**
     * Obtiene el ID público de la imagen en Cloudinary.
     *
     * @return ID público de la imagen
     */
    public String getImagenPublicId() {
        return imagenPublicId;
    }

    /**
     * Establece el ID público de la imagen en Cloudinary.
     *
     * @param imagenPublicId ID público de la imagen
     */
    public void setImagenPublicId(String imagenPublicId) {
        this.imagenPublicId = imagenPublicId;
    }
}
