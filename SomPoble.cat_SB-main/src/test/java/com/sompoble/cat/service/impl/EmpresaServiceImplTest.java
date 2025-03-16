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
    private EmpresaService empresaService; // El servicio que estás probando

    @Autowired
    private EmpresaRepository empresaRepository; // Para interactuar con el repositorio directamente
    
    @Autowired
    private EntityManager entityManager; // Para interactuar directamente con la base de datos

    private Empresario empresario;

    @BeforeEach
    void setUp() {
        // Crear un nuevo empresario antes de cada prueba
        empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");

        entityManager.persist(empresario);
        entityManager.flush();
    }

    @Test
    void addEmpresaTest() {
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        Empresa empresaPersistida = empresaRepository.findByCif(empresa.getCif());
        assertNotNull(empresaPersistida);
        assertEquals(empresa.getCif(), empresaPersistida.getCif());
    }

    @Test
    void updateEmpresaTest() {
        // Crear una empresa inicial
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        // Actualizar la empresa
        empresa.setNombre("Nueva Empresa S.A.");
        empresaService.updateEmpresa(empresa);

        // Verificar que la empresa ha sido actualizada
        Empresa empresaActualizada = empresaRepository.findByCif(empresa.getCif());
        assertNotNull(empresaActualizada);
        assertEquals("Nueva Empresa S.A.", empresaActualizada.getNombre());
    }

    @Test
    void findByCifTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        // Buscar empresa por CIF
        Empresa result = empresaService.findByCif("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getCif(), result.getCif());
    }

    @Test
    void existsByCifTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        // Verificar si la empresa existe por CIF
        boolean result = empresaService.existsByCif("A12345678");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        // Eliminar la empresa por ID
        empresaService.deleteById(empresa.getIdEmpresa());

        // Verificar que la empresa ha sido eliminada
        try {
            Empresa empresaEliminada = empresaRepository.findByCif(empresa.getCif());
            assertNull(empresaEliminada);
        } catch (EmptyResultDataAccessException e) {
            // Exception is expected because the company has been deleted
            assertTrue(true);
        }
    }

    @Test
    void findAllTest() {
        // Crear empresas
        Empresa empresa1 = new Empresa();
        empresa1.setCif("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Ficticia, 123");
        empresa1.setTelefono("912345678");
        empresa1.setEmail("empresa@empresa.com");
        empresa1.setEmpresario(empresario);
        empresaService.addEmpresario(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setCif("B98765432");
        empresa2.setNombre("Otra Empresa S.L.");
        empresa2.setDireccion("Calle Real, 456");
        empresa2.setTelefono("913456789");
        empresa2.setEmail("otra@empresa.com");
        empresa2.setEmpresario(empresario);
        empresaService.addEmpresario(empresa2);

        // Verificar que ambas empresas están presentes
        List<Empresa> empresas = empresaService.findAll();
        assertNotNull(empresas);
        assertEquals(2, empresas.size());
    }

    @Test
    void existsByIdTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        // Verificar si la empresa existe por ID
        boolean result = empresaService.existsById(empresa.getIdEmpresa());
        assertTrue(result);
    }

    @Test
    void deleteByCifTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setEmpresario(empresario);
        empresaService.addEmpresario(empresa);

        // Eliminar la empresa por CIF
        empresaService.deleteByCif(empresa.getCif());

        // Verificar que la empresa ha sido eliminada
        try {
            Empresa empresaEliminada = empresaRepository.findByCif(empresa.getCif());
            assertNull(empresaEliminada);
        } catch (EmptyResultDataAccessException e) {
            // Exception is expected because the company has been deleted
            assertTrue(true);
        }
    }
}
