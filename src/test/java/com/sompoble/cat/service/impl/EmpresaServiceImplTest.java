package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresaDTO;
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
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        EmpresaDTO empresaPersistida = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
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
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        empresa.setNombre("Nueva Empresa S.A.");
        empresaService.updateEmpresa(empresa);

        EmpresaDTO empresaActualizada = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
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
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        EmpresaDTO result = empresaService.findByIdentificadorFiscal("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscal());
        
        EmpresaDTO resultNoExiste = empresaService.findByIdentificadorFiscal("Z98765432");
        assertNull(resultNoExiste);
    }
    
    @Test
    void findByIdentificadorFiscalFullTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        Empresa result = empresaService.findByIdentificadorFiscalFull("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscal());
        
        Empresa resultNoExiste = empresaService.findByIdentificadorFiscalFull("Z98765432");
        assertNull(resultNoExiste);
    }

    @Test
    void existsByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        boolean result = empresaService.existsByIdentificadorFiscal("A12345678");
        assertTrue(result);
        
        boolean resultNoExiste = empresaService.existsByIdentificadorFiscal("Z98765432");
        assertFalse(resultNoExiste);
    }

    @Test
    void deleteByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        empresaService.deleteById(empresa.getIdEmpresa());

        EmpresaDTO empresaEliminada = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
        assertNull(empresaEliminada);
    }
    
    /*
    * Comentado ya que de lo contrario da error al tener registros en la base de datos
    @Test
    void findAllTest() {
        Empresa empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Ficticia, 123");
        empresa1.setTelefono("912345678");
        empresa1.setEmail("empresa@empresa.com");
        empresa1.setActividad("Desarrollo software");
        empresa1.setTipo(2);
        empresa1.setEmpresario(empresario);
        empresaService.addEmpresa(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B98765432");
        empresa2.setNombre("Otra Empresa S.L.");
        empresa2.setDireccion("Calle Real, 456");
        empresa2.setTelefono("913456789");
        empresa2.setEmail("otra@empresa.com");
        empresa2.setActividad("Consultoría");
        empresa2.setTipo(1);
        empresa2.setEmpresario(empresario);
        empresaService.addEmpresa(empresa2);

        List<EmpresaDTO> empresas = empresaService.findAll();
        assertNotNull(empresas);
        assertEquals(2, empresas.size());
    }
    */
    
    @Test
    void existsByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(2);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        boolean result = empresaService.existsById(empresa.getIdEmpresa());
        assertTrue(result);
        
        boolean resultNoExiste = empresaService.existsById(-1L);
        assertFalse(resultNoExiste);
    }

    @Test
    void deleteByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Ficticia, 123");
        empresa.setTelefono("912345678");
        empresa.setEmail("empresa@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        empresaService.addEmpresa(empresa);

        empresaService.deleteByIdentificadorFiscal(empresa.getIdentificadorFiscal());

        EmpresaDTO empresaEliminada = empresaRepository.findByIdentificadorFiscal(empresa.getIdentificadorFiscal());
        assertNull(empresaEliminada);
    }
}