package com.sompoble.cat.repository.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sompoble.cat.Application;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresaHibernateTest {

    /*@Autowired
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
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

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
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

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
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        Empresa result = empresaHibernate.findByIdentificadorFiscal("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscal());
    }

    @Test
    void existsByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        boolean result = empresaHibernate.existsByIdentificadorFiscal("A12345678");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        empresaHibernate.deleteById(empresa.getIdEmpresa());

        Empresa empresaEliminada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNull(empresaEliminada);
    }

    /*
    @Test
    void findAllTest() {
        Empresa empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Falsa, 123");
        empresa1.setTelefono("123456789");
        empresa1.setEmail("contacto@empresa.com");
        empresa1.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B98765432");
        empresa2.setNombre("Otra Empresa S.A.");
        empresa2.setDireccion("Calle Verdadera, 456");
        empresa2.setTelefono("987654321");
        empresa2.setEmail("contacto2@empresa.com");
        empresa2.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa2);

        List<Empresa> empresas = empresaHibernate.findAll();
        assertNotNull(empresas);
        assertEquals(2, empresas.size());
    }
    */
    /*
    @Test
    void existsByIdTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        boolean result = empresaHibernate.existsById(empresa.getIdEmpresa());
        assertTrue(result);
    }

    @Test
    void deleteByIdentificadorFiscalTest() {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        empresaHibernate.deleteByIdentificadorFiscal("A12345678");

        Empresa empresaEliminada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNull(empresaEliminada);
    }*/
}