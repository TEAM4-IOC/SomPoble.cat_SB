package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.repository.HorarioRepository;
import com.sompoble.cat.service.HorarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar horarios.
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos definidos en
 * la interfaz HorarioService, gestionando las operaciones relacionadas con
 * horarios a través del repositorio correspondiente.
 * </p>
 */
@Service
public class HorarioServiceImpl implements HorarioService {

    /**
     * Repositorio para acceder a los datos de horarios.
     */
    @Autowired
    private HorarioRepository horarioRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        horarioRepository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Horario> findById(Long id) {
        return Optional.ofNullable(horarioRepository.findById(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByEmpresa_IdEmpresa(Long idEmpresa) {
        return horarioRepository.findByEmpresa_IdEmpresa(idEmpresa);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByDiasLaborablesContainingIgnoreCase(String dia) {
        return horarioRepository.findByDiasLaborablesContainingIgnoreCase(dia);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByHorarioInicioBetween(LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByHorarioInicioBetween(inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByHorarioFinBetween(LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByHorarioFinBetween(inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByEmpresa_IdEmpresaAndDiasLaborablesContaining(
            Long idEmpresa, String dia) {
        return horarioRepository.findByEmpresa_IdEmpresaAndDiasLaborablesContaining(idEmpresa, dia);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByHorarioInicioBefore(LocalTime hora) {
        return horarioRepository.findByHorarioInicioBefore(hora);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByHorarioFinAfter(LocalTime hora) {
        return horarioRepository.findByHorarioFinAfter(hora);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByOrderByHorarioInicioAsc() {
        return horarioRepository.findByOrderByHorarioInicioAsc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByEmpresa_IdEmpresaAndHorarioInicioBetween(
            Long idEmpresa, LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByEmpresa_IdEmpresaAndHorarioInicioBetween(idEmpresa, inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByEmpresa_IdEmpresaAndHorarioFinBetween(
            Long idEmpresa, LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByEmpresa_IdEmpresaAndHorarioFinBetween(idEmpresa, inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByDiaExacto(String dia) {
        return horarioRepository.findByDiaExacto(dia);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByEmpresaAndHorarioBetween(
            Long idEmpresa, LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByEmpresaAndHorarioBetween(idEmpresa, inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByHorarioOverlap(LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByHorarioOverlap(inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Horario findByEmpresaDiaYHorarioExacto(
            Long idEmpresa, String dia, LocalTime inicio, LocalTime fin) {
        return horarioRepository.findByEmpresaDiaYHorarioExacto(idEmpresa, dia, inicio, fin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findOrphanHorarios() {
        return horarioRepository.findOrphanHorarios();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countByEmpresaId(Long idEmpresa) {
        return horarioRepository.countByEmpresaId(idEmpresa);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByDiasMultiples(String dia1, String dia2) {
        return horarioRepository.findByDiasMultiples(dia1, dia2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByHorarioRanges(
            LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return horarioRepository.findByHorarioRanges(inicio1, fin1, inicio2, fin2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByFechaAltaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return horarioRepository.findByFechaAltaBetween(fechaInicio, fechaFin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Horario> findByFechaModificacionAfter(LocalDateTime fecha) {
        return horarioRepository.findByFechaModificacionAfter(fecha);
    }
}
