package com.sompoble.cat.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.repository.EmpresarioRepository;
import com.sompoble.cat.service.EmpresarioService;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresarioServiceImplTest {

    @Autowired
    private EmpresarioService empresarioService;

    @Autowired
    private EmpresarioRepository empresarioRepository;

    @Test
    void addEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        EmpresarioDTO empresarioPersistido = empresarioRepository.findByDNI(empresario.getDni());
        assertNotNull(empresarioPersistido);
        assertEquals(empresario.getDni(), empresarioPersistido.getDni());
    }

    @Test
    void updateEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        
       
        empresarioService.addEmpresario(empresario);

        
        EmpresarioDTO dto = new EmpresarioDTO();
        dto.setDni(empresario.getDni());        
        dto.setNombre("Xavi");             
        dto.setApellidos(empresario.getApellidos());
        dto.setEmail(empresario.getEmail());
        dto.setTelefono(empresario.getTelefono());
        dto.setPass(empresario.getPass());

       
        empresarioService.updateEmpresario(dto);

       
        EmpresarioDTO empresarioActualizado = empresarioService.findByDni("12345888D");
        assertNotNull(empresarioActualizado, "No se encontró el empresario actualizado");
        assertEquals("Xavi", empresarioActualizado.getNombre(), "El nombre no fue actualizado correctamente");
        assertEquals("Garcia Martinez", empresarioActualizado.getApellidos(), "Los apellidos no coinciden");
        assertEquals("ivan@empresa.com", empresarioActualizado.getEmail(), "El email no coincide");
    }
    @Test
    void findByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        EmpresarioDTO result = empresarioService.findByDni("12345888D");
        assertNotNull(result);
        assertEquals(empresario.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        boolean result = empresarioService.existsByDni("12345888D");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        empresarioService.deleteById(empresario.getIdPersona());

        try {
            EmpresarioDTO empresarioEliminado = empresarioRepository.findByDNI(empresario.getDni());
            assertNull(empresarioEliminado);
        } catch (EmptyResultDataAccessException e) {
            assertTrue(true);
        }
    }


    @Test
    void findAllTest() {
        Empresario empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Carlos");
        empresario1.setApellidos("Sanchez Martinez");
        empresario1.setEmail("carlos@empresa.com");
        empresario1.setTelefono("650123456");
        empresario1.setPass("pass");
        empresarioService.addEmpresario(empresario1);

        Empresario empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Maria");
        empresario2.setApellidos("Lopez Garcia");
        empresario2.setEmail("maria@empresa.com");
        empresario2.setTelefono("650123457");
        empresario2.setPass("pass");
        empresarioService.addEmpresario(empresario2);

        List<EmpresarioDTO> empresarios = empresarioService.findAll();
        assertNotNull(empresarios);
        assertEquals(13, empresarios.size());
    }

    @Test
    void existsByIdTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        boolean result = empresarioService.existsById(empresario.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        empresarioService.deleteByDni(empresario.getDni());

        try {
            EmpresarioDTO empresarioEliminado = empresarioRepository.findByDNI(empresario.getDni());
            assertNull(empresarioEliminado);
        } catch (EmptyResultDataAccessException e) {
            assertTrue(true);
        }
    }

    @Test
    void existsByEmailTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345888D");
        empresario.setNombre("Ivan");
        empresario.setApellidos("Garcia Martinez");
        empresario.setEmail("ivan@empresa.com");
        empresario.setTelefono("670256123");
        empresario.setPass("pass");
        empresarioService.addEmpresario(empresario);

        boolean result = empresarioService.existsByEmail("ivan@empresa.com");
        assertTrue(result);
    }
}
