package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.repository.impl.ReservaHibernate;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.ReservaService;
import com.sompoble.cat.service.ServicioService;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la entidad {@code Reserva}.
 * <p>
 * Proporciona endpoints para la gestión de reservas, diferenciando entre
 * clientes y empresas.
 * </p>
 *
 * @author SomPoble
 */
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ReservaHibernate reservaHibernate;

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
        if (reservas == null || reservas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron reservas para el cliente con DNI " + dni + ".");
        }

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
        if (reservas == null || reservas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron reservas para la empresa con identificador fiscal " + identificadorFiscal + ".");
        }
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
     * Crea una nueva reserva. Se espera recibir un objeto {@link Reserva} en el
     * cuerpo de la petición.
     *
     * @param request
     * @return 200 OK si se crea la reserva.
     */
    // Crear una nueva empresa
    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Map<String, Object> request) {
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

        Reserva reserva = new Reserva();
        reserva.setFechaReserva(fechaReserva);
        reserva.setHora(hora);
        reserva.setEstado(estado);
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);

        reservaService.addReserva(reserva);

        return ResponseEntity.created(null).build();
    }

    /**
     * Actualiza una reserva existente. Se espera recibir un objeto
     * {@link Reserva} en el cuerpo de la petición.
     *
     * @param id el identificador de la reserva a actualizar.
     * @param updates json con la informacion de la reserva a updatear.
     * @return 200 OK si la reserva se actualiza, o 404 si no se encuentra.
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

            if (updates.containsKey("empresa")) {
                Map<String, Object> empresaData = (Map<String, Object>) updates.get("empresa");
                if (empresaData != null && empresaData.containsKey("identificadorFiscal")) {
                    String identificadorFiscal = (String) empresaData.get("identificadorFiscal");
                    if (!empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
                        throw new BadRequestException("No existe una empresa con identificador fiscal " + identificadorFiscal);
                    }
                }
            }

            if (updates.containsKey("cliente")) {
                Map<String, Object> clienteData = (Map<String, Object>) updates.get("cliente");
                if (clienteData != null && clienteData.containsKey("dni")) {
                    String dniCliente = (String) clienteData.get("dni");
                    if (!clienteService.existsByDni(dniCliente)) {
                        throw new BadRequestException("No existe un cliente con DNI " + dniCliente);
                    }
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

            return ResponseEntity.ok("Reserva con ID " + id + " actualizada correctamente");
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error al actualizar la reserva: " + e.getMessage());
        }
    }

    /**
     * Elimina una reserva mediante su identificador.
     *
     * @param id el identificador de la reserva a eliminar.
     * @return 200 OK si la reserva se elimina, o 404 si no se encuentra.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReserva(@PathVariable Long id) {
        ReservaDTO existing = reservaService.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("No se encontró una reserva con ID " + id);
        }
        reservaService.deleteById(id);
        return ResponseEntity.ok("Reserva con ID " + id + " eliminada correctamente");
    }

    /**
     * Elimina todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     * @return 200 OK si las reservas se eliminan.
     */
    @DeleteMapping("/clientes/{dni}")
    public ResponseEntity<String> deleteReservasByCliente(@PathVariable String dni) {
        if (!clienteService.existsByDni(dni)) {
            throw new BadRequestException("No existe un cliente con DNI " + dni + ".");
        }
        reservaService.deleteByClienteDni(dni);
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
