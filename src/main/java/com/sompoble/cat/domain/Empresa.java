package com.sompoble.cat.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
/**
 * Entidad que representa una empresa registrada en el sistema.
 * <p>
 * Contiene información fiscal, de contacto, así como relaciones con reservas,
 * servicios y horarios. Cada empresa está asociada a un empresario.
 * </p>
 */
@Entity
@Table(name = "EMPRESA")
public class Empresa implements Serializable {
    /**
     * Identificador único de la empresa.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_EMPRESA")
    private Long idEmpresa;
        /**
     * Empresario propietario de la empresa.
     * Relación uno a uno con la entidad {@link Empresario}.
     */
    @OneToOne
    @JoinColumn(name="ID_PERSONA", nullable = false)
    @NotNull(message = "Debe asociarse a un empresario")
    @JsonBackReference
    private Empresario empresario;
        /**
     * Identificador fiscal de la empresa (NIF/CIF).
     */
    @Column(name = "IDENTIFICADOR_FISCAL", nullable = false, length = 9) 
    @NotNull(message = "El identificador fiscal es obligatorio")
    @Size(max = 9)
    private String identificadorFiscal;
        /**
     * Nombre comercial de la empresa.
     */
    @Column(name = "NOMBRE", nullable = true, length = 100) 
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String nombre;
        /**
     * Actividad económica de la empresa.
     */
    @Column(name = "ACTIVIDAD", nullable = true, length = 100) 
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String actividad;
        /**
     * Dirección física de la empresa.
     */
    @Column(name = "DIRECCION", nullable = false, length = 255) 
    @NotNull(message = "La dirección es obligatoria")
    @Size(max = 255, message = "El campo no puede exceder los 255 caracteres")
    private String direccion;
        /**
     * Correo electrónico de la empresa.
     */
    @Column(name = "EMAIL", nullable = false, length = 100) 
    @NotNull(message = "El email es obligatorio")
    @Size(max = 100 , message = "El campo no puede exceder los 100 caracteres")
    private String email;
        /**
     * Número de teléfono de la empresa.
     */
    @Column(name = "TELEFONO", nullable = false, length = 20) 
    @NotNull(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El campo no puede exceder los 20 caracteres")
    private String telefono;
        /**
     * Tipo de empresa. Se representa mediante un valor numérico.
     */
    @Column(name = "TIPO", nullable = false) 
    @NotNull(message = "Es tipo es obligatorio")
    private int tipo;
        /**
     * URL de la imagen de la empresa almacenada en Cloudinary.
     */
    @Column(name = "IMAGEN_URL", nullable = true, length = 255)
    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    private String imagenUrl;
        /**
     * ID público de la imagen en Cloudinary (necesario para eliminación).
     */
    @Column(name = "IMAGEN_PUBLIC_ID", nullable = true, length = 255)
    @Size(max = 255, message = "El ID público de la imagen no puede exceder los 255 caracteres")
    private String imagenPublicId;
        /**
     * Fecha de alta en el sistema. Se establece automáticamente.
     */
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaAlta;
        /**
     * Fecha de la última modificación. Se actualiza automáticamente.
     */
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    private LocalDateTime fechaModificacion;
        /**
     * Reservas asociadas a la empresa.
     */
    @OneToMany(mappedBy = "empresa")
    private List<Reserva> reservas;
        /**
     * Servicios que ofrece la empresa.
     */
    @OneToMany(mappedBy = "empresa")
    private List<Servicio> servicios;
        /**
     * Horarios definidos por la empresa.
     */
    @OneToMany(mappedBy = "empresa")
    private List<Horario> horarios;
        /**
     * Constructor vacío requerido por JPA.
     */
    public Empresa() {
    }
        /**
     * Constructor para inicializar los campos principales de la empresa.
     *
     * @param empresario         Empresario asociado.
     * @param identificadorFiscal Identificador fiscal.
     * @param nombre             Nombre de la empresa.
     * @param actividad          Actividad económica.
     * @param direccion          Dirección física.
     * @param email              Correo electrónico.
     * @param telefono           Teléfono de contacto.
     * @param tipo               Tipo de empresa.
     */
    public Empresa(Empresario empresario, String identificadorFiscal, String nombre, String actividad, String direccion, String email, String telefono, int tipo) {
        this.empresario = empresario;
        this.identificadorFiscal = identificadorFiscal;
        this.nombre = nombre;
        this.actividad = actividad;
        this.direccion = direccion;
        this.email = email.toLowerCase();
        this.telefono = telefono;
        this.tipo = tipo;
    }
        /**
     * @return Identificador único de la empresa.
     */
    public Long getIdEmpresa() {
        return idEmpresa;
    }
        /**
     * Establece el ID de la empresa.
     * @param idEmpresa ID único de la empresa.
     */
    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
    /**
     * @return Empresario asociado.
     */
    public Empresario getEmpresario() {
        return empresario;
    }

    /**
     * Establece el empresario asociado.
     * @param empresario Empresario a asociar.
     */
    public void setEmpresario(Empresario empresario) {
        this.empresario = empresario;
    }
        /**
     * Obtiene el identificador fiscal de la empresa.
     * @return el identificador fiscal.
     */
    public String getIdentificadorFiscal() {
        return identificadorFiscal;
    }
        /**
     * Establece el identificador fiscal de la empresa.
     * @param identificadorFiscal identificador fiscal a establecer.
     */
    public void setIdentificadorFiscal(String identificadorFiscal) {
        this.identificadorFiscal = identificadorFiscal;
    }
        /**
     * Obtiene el nombre de la empresa.
     * @return el nombre de la empresa.
     */
    public String getNombre() {
        return nombre;
    }
        /**
     * Establece el nombre de la empresa.
     * @param nombre nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
        /**
     * Obtiene la actividad económica de la empresa.
     * @return la actividad de la empresa.
     */
    public String getActividad() {
        return actividad;
    }
        /**
     * Establece la actividad económica de la empresa.
     * @param actividad actividad a establecer.
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }
        /**
     * Obtiene la dirección de la empresa.
     * @return dirección de la empresa.
     */
    public String getDireccion() {
        return direccion;
    }
        /**
     * Establece la dirección de la empresa.
     * @param direccion dirección a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
        /**
     * Obtiene el email de contacto de la empresa.
     * @return email de la empresa.
     */
    public String getEmail() {
        return email;
    }
        /**
     * Establece el email de la empresa. Se guarda en minúsculas.
     * @param email email a establecer.
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
        /**
     * Obtiene el número de teléfono de la empresa.
     * @return teléfono de la empresa.
     */
    public String getTelefono() {
        return telefono;
    }
        /**
     * Establece el número de teléfono de la empresa.
     * @param telefono teléfono a establecer.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
        /**
     * Obtiene el tipo de empresa.
     * @return tipo de empresa (valor numérico).
     */
    public int getTipo() {
        return tipo;
    }
        /**
     * Establece el tipo de empresa.
     * @param tipo tipo a establecer (valor numérico).
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
        /**
     * Obtiene la URL de la imagen de la empresa.
     * @return URL de la imagen.
     */
    public String getImagenUrl() {
        return imagenUrl;
    }
        /**
     * Establece la URL de la imagen de la empresa.
     * @param imagenUrl URL de la imagen a establecer.
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
        /**
     * Obtiene el ID público de la imagen en Cloudinary.
     * @return ID público de la imagen.
     */
    public String getImagenPublicId() {
        return imagenPublicId;
    }
        /**
     * Establece el ID público de la imagen en Cloudinary.
     * @param imagenPublicId ID público a establecer.
     */
    public void setImagenPublicId(String imagenPublicId) {
        this.imagenPublicId = imagenPublicId;
    }
        /**
     * Obtiene la fecha de alta de la empresa.
     * @return fecha de creación en el sistema.
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
        /**
     * Obtiene la fecha de última modificación de la empresa.
     * @return fecha de la última actualización.
     */
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
        /**
     * Obtiene la lista de reservas asociadas a la empresa.
     * @return lista de {@link Reserva}.
     */
    public List<Reserva> getReservas() {
        return reservas;
    }
        /**
     * Establece la lista de reservas asociadas a la empresa.
     * @param reservas lista de reservas a establecer.
     */
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
        /**
     * Obtiene la lista de servicios ofrecidos por la empresa.
     * @return lista de {@link Servicio}.
     */
    public List<Servicio> getServicios() {
        return servicios;
    }
        /**
     * Establece la lista de servicios ofrecidos por la empresa.
     * @param servicios lista de servicios a establecer.
     */
    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }
        /**
     * Obtiene los horarios definidos por la empresa.
     * @return lista de {@link Horario}.
     */
    public List<Horario> getHorarios() {
        return horarios;
    }
        /**
     * Establece los horarios definidos por la empresa.
     * @param horarios lista de horarios a establecer.
     */
    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}