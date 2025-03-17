package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.service.EmpresaService;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresaServiceImplTest {

    @Autowired
    private EmpresaService empresaService; 

    @Autowired
    private EmpresaRepository empresaRepository; 
    
    @Autowired
    private EntityManager entityManager; 

    private Empresario empresario;

    @BeforeEach
    void setUp() {
        empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");

        entityManager.persist(empresario);
        entityManager.flush();
    }

    @Test
    void addEmpresaTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        Empresa empresaPersistida = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
        assertNotNull(empresaPersistida);
        assertEquals(empresa.getIdentificadorFiscal(), empresaPersistida.getIdentificadorFiscal());
    }

    @Test
    void updateEmpresaTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        empresa.setNombre("Nueva Empresa S.A.");
        empresaService.updateEmpresa(empresa);

        Empresa empresaActualizada = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
        assertNotNull(empresaActualizada);
        assertEquals("Nueva Empresa S.A.", empresaActualizada.getNombre());
    }

    @Test
    void findByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        Empresa result = empresaService.findByIdentificadorFiscal("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscal());
    }

    @Test
    void existsByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        boolean result = empresaService.existsByIdentificadorFiscal("A12345678");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        empresaService.deleteById(empresa.getIdEmpresa());

        try {
            Empresa empresaEliminada = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
            assertNull(empresaEliminada);
        } catch (EmptyResultDataAccessException e) {
            assertTrue(true);
        }
    }

    @Test
    void findAllTest() {
        Empresa empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Ficticia, 123");
        empresa1.setTelefono("912345678");
        empresa1.setEmail("empresa@empresa.com");
        empresa1.setEmpresario(empresario);
        empresaService.addEmpresario(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B98765432");
        empresa2.setNombre("Otra Empresa S.L.");
        empresa2.setDireccion("Calle Real, 456");
        empresa2.setTelefono("913456789");
        empresa2.setEmail("otra@empresa.com");
        empresa2.setEmpresario(empresario);
        empresaService.addEmpresario(empresa2);

        List<Empresa> empresas = empresaService.findAll();
        assertNotNull(empresas);
        assertEquals(2, empresas.size());
    }

    @Test
    void existsByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        boolean result = empresaService.existsById(empresa.getIdEmpresa());
        assertTrue(result);
    }

    @Test
    void deleteByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        empresaService.deleteByIdentificadorFiscal(empresa.getIdentificadorFiscal());

        try {
            Empresa empresaEliminada = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
            assertNull(empresaEliminada);
        } catch (EmptyResultDataAccessException e) {
            assertTrue(true);
        }
    }
}
