package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.dto.EmailDTO;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.repository.impl.ReservaHibernate;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.NotificationService;
import com.sompoble.cat.service.ReservaService;
import com.sompoble.cat.service.ServicioService;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la entidad {@code Reserva}.
 *
 * Proporciona endpoints para la gestión de reservas, diferenciando entre
 * clientes y empresas.
 *
 *
 * @author SomPoble
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    /**
     * Servicio que gestiona las operaciones relacionadas con las reservas.
     */
    @Autowired
    private ReservaService reservaService;
    /**
     * Servicio que proporciona acceso y operaciones sobre los datos de los
     * clientes.
     */
    @Autowired
    private ClienteService clienteService;
    /**
     * Servicio encargado de gestionar los datos de empresas o autónomos.
     */
    @Autowired
    private EmpresaService empresaService;
    /**
     * Servicio que maneja la lógica de negocio asociada a los servicios
     * ofrecidos por las empresas.
     */
    @Autowired
    private ServicioService servicioService;
    /**
     * Componente para convertir y gestionar reservas utilizando Hibernate
     * directamente.
     */
    @Autowired
    private ReservaHibernate reservaHibernate;
    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    /**
     * Obtiene todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     * @return una lista de DTOs de reservas realizadas por el cliente.
     */
    @GetMapping("/clientes/{dni}")
    public ResponseEntity<List<ReservaDTO>> getReservasByCliente(@PathVariable String dni) {
        ClienteDTO cliente = clienteService.findByDni(dni);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con DNI " + dni + " no encontrado.");
        }

        List<ReservaDTO> reservas = reservaService.findByClienteDni(dni);
        return ResponseEntity.ok(reservas);
    }

    /**
     * Obtiene todas las reservas asociadas a una empresa (o autónomo) mediante
     * su identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return una lista de DTOs de reservas asociadas a la empresa.
     */
    @GetMapping("/empresas/{identificadorFiscal}")
    public ResponseEntity<List<ReservaDTO>> getReservasByEmpresa(@PathVariable String identificadorFiscal) {
        EmpresaDTO empresa = empresaService.findByIdentificadorFiscal(identificadorFiscal);
        if (empresa == null) {
            throw new ResourceNotFoundException("Empresa con identificador fiscal " + identificadorFiscal + " no encontrado.");
        }

        List<ReservaDTO> reservas = reservaService.findByEmpresaIdentificadorFiscal(identificadorFiscal);
        return ResponseEntity.ok(reservas);
    }

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return el DTO con la reserva correspondiente, o 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable Long id) {
        ReservaDTO reservaDTO = reservaService.findById(id);
        if (reservaDTO == null) {
            throw new ResourceNotFoundException("No se encontraron datos para la reserva con ID " + id + ".");
        }
        return ResponseEntity.ok(reservaDTO);
    }

    /**
     * Crea una nueva reserva a partir de los datos proporcionados en la
     * solicitud.
     *
     * <p>
     * Este método realiza múltiples validaciones antes de crear la reserva,
     * incluyendo:
     * <ul>
     * <li>Verificar que el cliente exista basado en su DNI.</li>
     * <li>Verificar que la empresa exista basado en su identificador
     * fiscal.</li>
     * <li>Verificar que el servicio exista y pertenezca a la empresa
     * especificada.</li>
     * <li>Verificar que la fecha, hora y estado sean proporcionados.</li>
     * <li>Verificar que no se haya alcanzado el límite de reservas para el
     * servicio en la fecha indicada.</li>
     * <li>Verificar que la hora de reserva esté dentro de los horarios
     * disponibles del servicio.</li>
     * </ul>
     * Después de crear la reserva, el método también:
     * <ul>
     * <li>Envía un correo electrónico de confirmación al cliente.</li>
     * <li>Registra una notificación en la base de datos para el cliente.</li>
     * </ul>
     *
     * @param request Un mapa que contiene los datos de la reserva, en el
     * siguiente formato:
     * <ul>
     * <li>reserva: {
     * <ul>
     * <li>fechaReserva (String)</li>
     * <li>hora (String)</li>
     * <li>estado (String)</li>
     * <li>cliente: { dni (String) }</li>
     * <li>empresa: { identificadorFiscal (String) }</li>
     * <li>servicio: { idServicio (Long o Integer) }</li>
     * </ul>
     * }</li>
     * </ul>
     * @return Un {@link ResponseEntity} con los siguientes posibles resultados:
     * <ul>
     * <li>201 Created si la reserva se creó exitosamente.</li>
     * <li>400 Bad Request si hay algún error de validación (cliente, empresa,
     * servicio inexistente, límite de reservas superado, hora inválida,
     * etc.).</li>
     * <li>500 Internal Server Error si ocurre un error al enviar el correo o
     * registrar la notificación.</li>
     * </ul>
     *
     * @throws BadRequestException si ocurre algún error de validación durante
     * la creación de la reserva.
     */
    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Map<String, Object> request) {
        // 1. Validaciones previas (empresa, cliente, servicio, disponibilidad horaria, etc.)
        Map<String, Object> reservaData = (Map<String, Object>) request.get("reserva");
        Map<String, Object> clienteData = (Map<String, Object>) reservaData.get("cliente");
        String dniCliente = (String) clienteData.get("dni");

        if (!clienteService.existsByDni(dniCliente)) {
            throw new BadRequestException("No existe un cliente con DNI " + dniCliente + ".");
        }
        Cliente cliente = clienteService.findByDniFull(dniCliente);

        Map<String, Object> empresaData = (Map<String, Object>) reservaData.get("empresa");
        String identificadorFiscal = (String) empresaData.get("identificadorFiscal");

        if (!empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
            throw new BadRequestException("No existe una empresa con identificador fiscal " + identificadorFiscal);
        }
        Empresa empresa = empresaService.findByIdentificadorFiscalFull(identificadorFiscal);

        Map<String, Object> servicioData = (Map<String, Object>) reservaData.get("servicio");
        Long idServicio = Long.valueOf((Integer) servicioData.get("idServicio"));

        if (!servicioService.existePorId(idServicio)) {
            throw new BadRequestException("No existe un servicio con el ID " + idServicio);
        }
        Servicio servicio = servicioService.obtenerPorId(idServicio);

        if (!servicio.getEmpresa().getIdentificadorFiscal().equals(identificadorFiscal)) {
            throw new BadRequestException("El servicio con ID " + idServicio + " no pertenece a la empresa con identificador fiscal "
                    + identificadorFiscal);
        }

        String fechaReserva = (String) reservaData.get("fechaReserva");
        String hora = (String) reservaData.get("hora");
        String estado = (String) reservaData.get("estado");

        if (fechaReserva == null || hora == null || estado == null) {
            throw new BadRequestException("Fecha, hora y estado son obligatorios");
        }

        int currentReservations = reservaService.countReservasByServicioIdAndFecha(idServicio, fechaReserva);
        Integer limiteReservasServicio = servicio.getLimiteReservas();

        if (currentReservations >= limiteReservasServicio) {
            throw new BadRequestException("Se ha alcanzado el límite de reservas para este servicio en la fecha indicada");
        }

        LocalTime horaReserva = LocalTime.parse(hora);
        List<Horario> horariosServicio = servicio.getHorarios();

        if (horariosServicio == null || horariosServicio.isEmpty()) {
            throw new BadRequestException("El servicio no tiene horarios definidos");
        }

        boolean horaValida = false;
        for (Horario horario : horariosServicio) {
            if (horaReserva.isAfter(horario.getHorarioInicio())
                    && horaReserva.isBefore(horario.getHorarioFin())
                    || horaReserva.equals(horario.getHorarioInicio())) {
                horaValida = true;
                break;
            }
        }

        if (!horaValida) {
            throw new BadRequestException("La hora de reserva no está dentro del horario disponible para este servicio");
        }

        // 2. Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(fechaReserva);
        reserva.setHora(hora);
        reserva.setEstado(estado);
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);
        reservaService.addReserva(reserva);

        // 3. Enviar correo electrónico al cliente
        try {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setDestinatario(cliente.getEmail());
            emailDTO.setAsunto("Confirmación de Reserva");
            emailDTO.setMensaje(String.format(
                    "Estimado/a %s,\n\nSu reserva ha sido confirmada con éxito.\n"
                    + "Detalles de la reserva:\n"
                    + "- Fecha: %s\n"
                    + "- Hora: %s\n"
                    + "- Servicio: %s\n"
                    + "- Empresa: %s",
                    cliente.getNombre(),
                    fechaReserva,
                    hora,
                    servicio.getNombre(),
                    empresa.getNombre()
            ));
            emailService.sendMail(emailDTO);

            // 4. Registrar la notificación en la base de datos
            Notificacion notificacion = new Notificacion();
            notificacion.setCliente(cliente);
            notificacion.setMensaje(String.format(
                    "Se ha realizado una nueva reserva para el servicio '%s' el día %s a las %s.",
                    servicio.getNombre(),
                    fechaReserva,
                    hora
            ));
            notificacion.setTipo(Notificacion.TipoNotificacion.INFORMACION);
            notificationService.saveNotification(notificacion);

        } catch (Exception e) {
            // Manejar errores de envío de correo o persistencia de notificaciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo o registrar notificación: " + e.getMessage());
        }

        // 5. Respuesta exitosa
        return ResponseEntity.created(null).build();
    }

    /**
     * Actualiza una reserva existente basada en los campos proporcionados en la
     * solicitud.
     *
     * <p>
     * Este método permite actualizar parcialmente una reserva, validando los
     * siguientes aspectos:
     * <ul>
     * <li>Que la reserva exista.</li>
     * <li>Que los identificadores de cliente, empresa y servicio proporcionados
     * existan.</li>
     * <li>Que el servicio pertenezca a la empresa correspondiente.</li>
     * <li>Que se respete el límite máximo de reservas para el servicio en la
     * fecha indicada.</li>
     * <li>Que la hora proporcionada esté dentro del rango de horarios definidos
     * para el servicio.</li>
     * </ul>
     * También se envía un correo electrónico de confirmación al cliente y se
     * registra una notificación en la base de datos al finalizar exitosamente
     * la actualización.
     *
     * @param id El ID de la reserva a actualizar.
     * @param updates Un mapa con los campos a actualizar. Puede incluir:
     * <ul>
     * <li>fechaReserva (String)</li>
     * <li>hora (String)</li>
     * <li>estado (String)</li>
     * <li>idServicio (Long) o un objeto servicio con idServicio</li>
     * <li>cliente (objeto que contiene el dni del cliente)</li>
     * <li>empresa (objeto que contiene el identificadorFiscal de la
     * empresa)</li>
     * </ul>
     * @return Un {@link ResponseEntity} indicando el resultado de la operación:
     * <ul>
     * <li>200 OK si la reserva fue actualizada exitosamente.</li>
     * <li>400 Bad Request si hubo un error de validación (cliente, empresa o
     * servicio inválidos, límite de reservas alcanzado, hora inválida,
     * etc.).</li>
     * <li>404 Not Found si no se encuentra la reserva.</li>
     * <li>500 Internal Server Error si ocurre un problema enviando el correo o
     * registrando la notificación.</li>
     * </ul>
     *
     * @throws ResourceNotFoundException si no se encuentra la reserva.
     * @throws BadRequestException si ocurre algún error de validación durante
     * la actualización.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            // Obtener la reserva existente completa para comparar valores originales
            Reserva originalReserva = reservaService.findByIdFull(id);
            if (originalReserva == null) {
                throw new ResourceNotFoundException("No se encontró una reserva con ID " + id);
            }

            ReservaDTO existingReserva = reservaService.findById(id);

            // Guardar valores originales para comparación
            String originalFecha = existingReserva.getFechaReserva();
            Long originalServicioId = existingReserva.getIdServicio();
            String originalHora = existingReserva.getHora();
            String originalDniCliente = existingReserva.getDniCliente();
            String originalIdentificadorFiscalEmpresa = existingReserva.getIdentificadorFiscalEmpresa();

            Long newServicioId = originalServicioId;
            String newIdentificadorFiscalEmpresa = originalIdentificadorFiscalEmpresa;
            String newDniCliente = originalDniCliente;

            if (updates.containsKey("empresa")) {
                Map<String, Object> empresaData = (Map<String, Object>) updates.get("empresa");
                if (empresaData != null && empresaData.containsKey("identificadorFiscal")) {
                    String identificadorFiscal = (String) empresaData.get("identificadorFiscal");
                    if (!empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
                        throw new BadRequestException("No existe una empresa con identificador fiscal " + identificadorFiscal);
                    }
                    newIdentificadorFiscalEmpresa = identificadorFiscal;
                }
            }

            if (updates.containsKey("cliente")) {
                Map<String, Object> clienteData = (Map<String, Object>) updates.get("cliente");
                if (clienteData != null && clienteData.containsKey("dni")) {
                    String dniCliente = (String) clienteData.get("dni");
                    if (!clienteService.existsByDni(dniCliente)) {
                        throw new BadRequestException("No existe un cliente con DNI " + dniCliente);
                    }
                    newDniCliente = dniCliente;
                }
            }

            if (updates.containsKey("idServicio")) {
                newServicioId = Long.valueOf(updates.get("idServicio").toString());
            } else if (updates.containsKey("servicio")) {
                Map<String, Object> servicioData = (Map<String, Object>) updates.get("servicio");
                if (servicioData != null && servicioData.containsKey("idServicio")) {
                    newServicioId = Long.valueOf(servicioData.get("idServicio").toString());
                }
            }

            // Si el ID del servicio ha cambiado, verificamos que pertenezca a la empresa
            if (!newServicioId.equals(originalServicioId)) {
                // Verificar que el servicio existe
                if (!servicioService.existePorId(newServicioId)) {
                    throw new BadRequestException("No existe un servicio con el ID " + newServicioId);
                }

                // Obtener el servicio para verificar a qué empresa pertenece
                Servicio servicio = servicioService.obtenerPorId(newServicioId);

                // Determinar el identificador fiscal de la empresa (podría estar cambiando también)
                String identificadorFiscalEmpresa = newIdentificadorFiscalEmpresa;

                // Verificar que el servicio pertenezca a la empresa correcta
                if (!servicio.getEmpresa().getIdentificadorFiscal().equals(identificadorFiscalEmpresa)) {
                    throw new BadRequestException("El servicio con ID " + newServicioId
                            + " no pertenece a la empresa con identificador fiscal " + identificadorFiscalEmpresa);
                }
            }

            updates.forEach((key, value) -> {
                if (value != null) {
                    switch (key) {
                        case "fechaReserva" ->
                            existingReserva.setFechaReserva(value.toString());
                        case "hora" ->
                            existingReserva.setHora(value.toString());
                        case "estado" ->
                            existingReserva.setEstado(value.toString());
                        case "dniCliente" ->
                            existingReserva.setDniCliente(value.toString());
                        case "identificadorFiscalEmpresa" ->
                            existingReserva.setIdentificadorFiscalEmpresa(value.toString());
                        case "idServicio" ->
                            existingReserva.setIdServicio(Long.valueOf(value.toString()));
                        case "cliente" -> {
                            Map<String, Object> clienteData = (Map<String, Object>) value;
                            if (clienteData.containsKey("dni")) {
                                existingReserva.setDniCliente((String) clienteData.get("dni"));
                            }
                        }
                        case "empresa" -> {
                            Map<String, Object> empresaData = (Map<String, Object>) value;
                            if (empresaData.containsKey("identificadorFiscal")) {
                                existingReserva.setIdentificadorFiscalEmpresa((String) empresaData.get("identificadorFiscal"));
                            }
                        }
                    }
                }
            });

            boolean fechaChanged = !originalFecha.equals(existingReserva.getFechaReserva());
            boolean servicioChanged = !originalServicioId.equals(existingReserva.getIdServicio());
            boolean horaChanged = !originalHora.equals(existingReserva.getHora());

            if (fechaChanged || servicioChanged || horaChanged) {
                // Obtener el servicio para verificar el límite
                Long servicioId = existingReserva.getIdServicio();
                String fechaReserva = existingReserva.getFechaReserva();
                Servicio servicio = servicioService.obtenerPorId(servicioId);

                // Contar reservas actuales para esa fecha y servicio, excluyendo esta reserva
                int currentReservations = reservaService.countReservasByServicioIdAndFecha(servicioId, fechaReserva);

                // Si no hemos cambiado la fecha ni el servicio, estamos contando la reserva actual también
                // así que debemos restar 1 para no contarla dos veces
                if (!fechaChanged && !servicioChanged) {
                    currentReservations--;
                }

                Integer limiteReservasServicio = servicio.getLimiteReservas();

                if (currentReservations >= limiteReservasServicio) {
                    throw new BadRequestException("Se ha alcanzado el límite de reservas para este servicio en la fecha indicada");
                }

                String hora = existingReserva.getHora();
                LocalTime horaReserva = LocalTime.parse(hora);

                List<Horario> horariosServicio = servicio.getHorarios();
                if (horariosServicio == null || horariosServicio.isEmpty()) {
                    throw new BadRequestException("El servicio no tiene horarios definidos");
                }

                boolean horaValida = false;
                for (Horario horario : horariosServicio) {
                    if (horaReserva.isAfter(horario.getHorarioInicio())
                            && horaReserva.isBefore(horario.getHorarioFin())
                            || horaReserva.equals(horario.getHorarioInicio())) {
                        horaValida = true;
                        break;
                    }
                }

                if (!horaValida) {
                    throw new BadRequestException("La hora de reserva no está dentro del horario disponible para este servicio");
                }
            }

            // Convertir y guardar la reserva actualizada
            Reserva reserva = reservaHibernate.convertToEntity(existingReserva);
            reservaService.updateReserva(reserva);

            // Enviar correo electrónico al cliente
            Cliente cliente = clienteService.findByDniFull(existingReserva.getDniCliente());
            try {
                EmailDTO emailDTO = new EmailDTO();
                emailDTO.setDestinatario(cliente.getEmail());
                emailDTO.setAsunto("Actualización de Reserva");
                emailDTO.setMensaje(String.format(
                        "Estimado/a %s,\n\nSu reserva ha sido actualizada con éxito.\n"
                        + "Detalles actualizados:\n"
                        + "- Fecha: %s\n"
                        + "- Hora: %s",
                        cliente.getNombre(),
                        existingReserva.getFechaReserva(),
                        existingReserva.getHora()
                ));
                emailService.sendMail(emailDTO);

                // Registrar la notificación en la base de datos
                Notificacion notificacion = new Notificacion();
                notificacion.setCliente(cliente);
                notificacion.setMensaje(String.format(
                        "Se ha actualizado su reserva para el día %s a las %s.",
                        existingReserva.getFechaReserva(),
                        existingReserva.getHora()
                ));
                notificacion.setTipo(Notificacion.TipoNotificacion.INFORMACION);
                notificationService.saveNotification(notificacion);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al enviar correo o registrar notificación: " + e.getMessage());
            }

            return ResponseEntity.ok("Reserva con ID " + id + " actualizada correctamente");
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error al actualizar la reserva: " + e.getMessage());
        }
    }

    /**
     * Elimina una reserva específica mediante su identificador único.
     *
     * <p>
     * Este método realiza las siguientes acciones:</p>
     * <ul>
     * <li>Verifica que exista una reserva con el ID proporcionado.</li>
     * <li>Elimina la reserva de la base de datos.</li>
     * <li>Envía un correo electrónico al cliente informando sobre la
     * cancelación de la reserva.</li>
     * <li>Registra una notificación en la base de datos asociada al
     * cliente.</li>
     * </ul>
     *
     * <p>
     * Si ocurre algún error durante el proceso (por ejemplo, al enviar correos
     * o registrar notificaciones), se devuelve una respuesta HTTP con código
     * 500 Internal Server Error.</p>
     *
     * @param id el identificador único de la reserva que se desea eliminar.
     * @return una respuesta HTTP con código 200 OK si la reserva se elimina
     * correctamente.
     * @throws ResourceNotFoundException si no existe una reserva con el ID
     * proporcionado.
     * @see ReservaService#findById(Long)
     * @see ReservaService#deleteById(Long)
     * @see EmailService#sendMail(EmailDTO)
     * @see NotificationService#saveNotification(Notificacion)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReserva(@PathVariable Long id) {
        ReservaDTO existing = reservaService.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("No se encontró una reserva con ID " + id);
        }

        // Obtener el cliente asociado a la reserva
        Cliente cliente = clienteService.findByDniFull(existing.getDniCliente());

        // Eliminar la reserva
        reservaService.deleteById(id);

        // Enviar correo electrónico al cliente
        try {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setDestinatario(cliente.getEmail());
            emailDTO.setAsunto("Cancelación de Reserva");
            emailDTO.setMensaje(String.format(
                    "Estimado/a %s,\n\nSu reserva ha sido cancelada.\n"
                    + "Detalles de la reserva cancelada:\n"
                    + "- Fecha: %s\n"
                    + "- Hora: %s",
                    cliente.getNombre(),
                    existing.getFechaReserva(),
                    existing.getHora()
            ));
            emailService.sendMail(emailDTO);

            // Registrar la notificación en la base de datos
            Notificacion notificacion = new Notificacion();
            notificacion.setCliente(cliente);
            notificacion.setMensaje(String.format(
                    "Se ha cancelado su reserva para el día %s a las %s.",
                    existing.getFechaReserva(),
                    existing.getHora()
            ));
            notificacion.setTipo(Notificacion.TipoNotificacion.ADVERTENCIA);
            notificationService.saveNotification(notificacion);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo o registrar notificación: " + e.getMessage());
        }

        return ResponseEntity.ok("Reserva con ID " + id + " eliminada correctamente");
    }

    /**
     * Elimina todas las reservas asociadas a un cliente mediante su DNI.
     *
     * <p>
     * Este método realiza las siguientes acciones:</p>
     * <ul>
     * <li>Verifica que exista un cliente con el DNI proporcionado.</li>
     * <li>Elimina todas las reservas asociadas al cliente en la base de
     * datos.</li>
     * <li>Envía un correo electrónico al cliente informando sobre la
     * cancelación de sus reservas.</li>
     * <li>Registra una notificación en la base de datos asociada al
     * cliente.</li>
     * </ul>
     *
     * <p>
     * Si ocurre algún error durante el proceso (por ejemplo, al enviar correos
     * o registrar notificaciones), se devuelve una respuesta HTTP con código
     * 500 Internal Server Error.</p>
     *
     * @param dni el documento nacional de identidad del cliente cuyas reservas
     * se desean eliminar.
     * @return una respuesta HTTP con código 200 OK si las reservas se eliminan
     * correctamente.
     * @throws BadRequestException si no existe un cliente con el DNI
     * proporcionado.
     * @see ClienteService#existsByDni(String)
     * @see ReservaService#deleteByClienteDni(String)
     * @see EmailService#sendMail(EmailDTO)
     * @see NotificationService#saveNotification(Notificacion)
     */
    @DeleteMapping("/clientes/{dni}")
    public ResponseEntity<String> deleteReservasByCliente(@PathVariable String dni) {
        // Verificar que el cliente exista
        if (!clienteService.existsByDni(dni)) {
            throw new BadRequestException("No existe un cliente con DNI " + dni + ".");
        }

        // Obtener el cliente asociado al DNI
        Cliente cliente = clienteService.findByDniFull(dni);

        // Eliminar todas las reservas del cliente
        reservaService.deleteByClienteDni(dni);

        // Enviar correo electrónico al cliente
        try {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setDestinatario(cliente.getEmail());
            emailDTO.setAsunto("Cancelación de Reservas");
            emailDTO.setMensaje(String.format(
                    "Estimado/a %s,\n\nTodas sus reservas han sido canceladas.",
                    cliente.getNombre()
            ));
            emailService.sendMail(emailDTO);

            // Registrar la notificación en la base de datos
            Notificacion notificacion = new Notificacion();
            notificacion.setCliente(cliente);
            notificacion.setMensaje("Se han cancelado todas sus reservas.");
            notificacion.setTipo(Notificacion.TipoNotificacion.ADVERTENCIA);
            notificationService.saveNotification(notificacion);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar correo o registrar notificación: " + e.getMessage());
        }

        return ResponseEntity.ok("Reservas para el cliente con DNI " + dni + " eliminadas correctamente");
    }

    /**
     * Elimina todas las reservas asociadas a una empresa o autónomo mediante su
     * identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa o
     * autónomo.
     * @return 200 OK si las reservas se eliminan.
     */
    @DeleteMapping("/empresas/{identificadorFiscal}")
    public ResponseEntity<String> deleteReservasByEmpresa(@PathVariable String identificadorFiscal) {
        if (!empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
            throw new BadRequestException("No existe una empresa con identificador fiscal " + identificadorFiscal);
        }
        reservaService.deleteByEmpresaIdentificadorFiscal(identificadorFiscal);
        return ResponseEntity.ok("Reservas para la empresa con identificador fiscal " + identificadorFiscal + " eliminadas correctamente");
    }
}
