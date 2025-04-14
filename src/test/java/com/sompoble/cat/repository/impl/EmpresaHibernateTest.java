package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresaHibernateTest {

    @Autowired
    private EmpresaHibernate empresaHibernate;

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
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        Empresa empresaPersistida = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNotNull(empresaPersistida);
        assertEquals(empresa.getIdentificadorFiscal(), empresaPersistida.getIdentificadorFiscal());
    }

    @Test
    void updateEmpresaTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        empresa.setNombre("Nueva Empresa S.A.");
        empresaHibernate.updateEmpresa(empresa);

        Empresa empresaActualizada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNotNull(empresaActualizada);
        assertEquals("Nueva Empresa S.A.", empresaActualizada.getNombre());
    }

    @Test
    void findByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        EmpresaDTO result = empresaHibernate.findByIdentificadorFiscal("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscal());
        
        // Probar con un identificador fiscal que no existe
        EmpresaDTO resultNoExiste = empresaHibernate.findByIdentificadorFiscal("Z98765432");
        assertNull(resultNoExiste);
    }
    
    @Test
    void findByIdentificadorFiscalFullTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        Empresa result = empresaHibernate.findByIdentificadorFiscalFull("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscal());
        
        // Probar con un identificador fiscal que no existe
        Empresa resultNoExiste = empresaHibernate.findByIdentificadorFiscalFull("Z98765432");
        assertNull(resultNoExiste);
    }

    @Test
    void existsByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        boolean result = empresaHibernate.existsByIdentificadorFiscal("A12345678");
        assertTrue(result);
        
        // Probar con un identificador fiscal que no existe
        boolean resultNoExiste = empresaHibernate.existsByIdentificadorFiscal("Z98765432");
        assertFalse(resultNoExiste);
    }

    @Test
    void deleteByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        empresaHibernate.deleteById(empresa.getIdEmpresa());

        Empresa empresaEliminada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNull(empresaEliminada);
    }
    
    /*
    * Comentado ya que de lo contrario da error al tener registros en la base de datos
    @Test
    void findAllTest() {
        Empresa empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Falsa, 123");
        empresa1.setTelefono("123456789");
        empresa1.setEmail("contacto@empresa.com");
        empresa1.setActividad("Desarrollo software");
        empresa1.setTipo(1);
        empresa1.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B98765432");
        empresa2.setNombre("Otra Empresa S.A.");
        empresa2.setDireccion("Calle Verdadera, 456");
        empresa2.setTelefono("987654321");
        empresa2.setEmail("contacto2@empresa.com");
        empresa2.setActividad("Consultoría");
        empresa2.setTipo(2);
        empresa2.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa2);

        List<EmpresaDTO> empresas = empresaHibernate.findAll();
        assertNotNull(empresas);
        assertEquals(2, empresas.size());
    }
    */
    
    @Test
    void existsByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        boolean result = empresaHibernate.existsById(empresa.getIdEmpresa());
        assertTrue(result);
        
        // Probar con un ID que no existe
        boolean resultNoExiste = empresaHibernate.existsById(-1L);
        assertFalse(resultNoExiste);
    }

    @Test
    void deleteByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresa(empresa);

        empresaHibernate.deleteByIdentificadorFiscal("A12345678");

        Empresa empresaEliminada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNull(empresaEliminada);
    }
    
    @Test
    void convertToEntityTest() {
        EmpresaDTO empresaDTO = new EmpresaDTO(
            null, // id no establecido aún
            "12345678A", // DNI del empresario
            "A12345678", // identificador fiscal
            "Empresa S.A.",
            "Desarrollo software",
            "Calle Falsa, 123",
            "contacto@empresa.com",
            "123456789",
            1,
            new ArrayList<>(), // reservas
            new ArrayList<>() // servicios
        );
        
        Empresa empresa = empresaHibernate.convertToEntity(empresaDTO);
        
        assertNotNull(empresa);
        assertEquals(empresaDTO.getIdentificadorFiscal(), empresa.getIdentificadorFiscal());
        assertEquals(empresaDTO.getNombre(), empresa.getNombre());
        assertEquals(empresaDTO.getActividad(), empresa.getActividad());
        assertEquals(empresaDTO.getDireccion(), empresa.getDireccion());
        assertEquals(empresaDTO.getEmail(), empresa.getEmail());
        assertEquals(empresaDTO.getTelefono(), empresa.getTelefono());
        assertEquals(empresaDTO.getTipo(), empresa.getTipo());
    }
}