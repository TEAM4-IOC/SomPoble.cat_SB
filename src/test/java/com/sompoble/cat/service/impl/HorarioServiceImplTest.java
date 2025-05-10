package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.repository.HorarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HorarioServiceImplTest {

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private HorarioServiceImpl horarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Horario horario = new Horario();
        when(horarioRepository.save(horario)).thenReturn(horario);

        Horario result = horarioService.save(horario);
        assertEquals(horario, result);
        verify(horarioRepository).save(horario);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        doNothing().when(horarioRepository).delete(id);

        horarioService.deleteById(id);
        verify(horarioRepository).delete(id);
    }

    @Test
    void testFindById() {
        Horario horario = new Horario();
        horario.setIdHorario(1L);
        when(horarioRepository.findById(1L)).thenReturn(horario);

        Optional<Horario> result = horarioService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdHorario());
    }

    @Test
    void testFindAll() {
        Horario horario = new Horario();
        when(horarioRepository.findAll()).thenReturn(Arrays.asList(horario));

        List<Horario> result = horarioService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindByHorarioInicioBetween() {
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(12, 0);
        when(horarioRepository.findByHorarioInicioBetween(inicio, fin))
                .thenReturn(Arrays.asList(new Horario()));

        List<Horario> result = horarioService.findByHorarioInicioBetween(inicio, fin);
        assertFalse(result.isEmpty());
    }

   
}
