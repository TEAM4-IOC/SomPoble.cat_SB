package com.sompoble.cat.repository.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sompoble.cat.Application;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresarioHibernateTest {

    /*@Autowired
    private EmpresarioHibernate empresarioHibernate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void addEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        Empresario empresarioPersistido = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNotNull(empresarioPersistido);
        assertEquals(empresario.getDni(), empresarioPersistido.getDni());
    }

    @Test
    void updateEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        empresario.setNombre("Jose");
        empresarioHibernate.updateEmpresario(empresario);

        Empresario empresarioActualizado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNotNull(empresarioActualizado);
        assertEquals("Jose", empresarioActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        Empresario result = empresarioHibernate.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(empresario.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        boolean result = empresarioHibernate.existsByDni("12345678A");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        empresarioHibernate.deleteById(empresario.getIdPersona());

        Empresario empresarioEliminado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNull(empresarioEliminado);
    }

    /*
    @Test
    void findAllTest() {
        Empresario empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Carlos");
        empresario1.setApellidos("Lopez Martinez");
        empresario1.setEmail("carlos@empresa.com");
        empresario1.setTelefono("650180800");
        empresario1.setPass("pass");
        empresarioHibernate.addEmpresario(empresario1);

        Empresario empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Jose");
        empresario2.setApellidos("Garcia Ruiz");
        empresario2.setEmail("jose@empresa.com");
        empresario2.setTelefono("650180801");
        empresario2.setPass("pass");
        empresarioHibernate.addEmpresario(empresario2);

        List<Empresario> empresarios = empresarioHibernate.findAll();
        assertNotNull(empresarios);
        assertEquals(2, empresarios.size());
    }

    */
    /*
    @Test
    void existsByIdTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        boolean result = empresarioHibernate.existsById(empresario.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        empresarioHibernate.deleteByDni(empresario.getDni());

        Empresario empresarioEliminado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNull(empresarioEliminado);
    }

    @Test
    void existsByEmailTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        boolean result = empresarioHibernate.existsByEmail("carlos@empresa.com");
        assertTrue(result);

        boolean resultNoExist = empresarioHibernate.existsByEmail("noexistente@sergio.es");
        assertFalse(resultNoExist);

    }

    @Test
    void findByEmailTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        EmpresarioDTO result = empresarioHibernate.findByEmail("carlos@empresa.com");
        assertNotNull(result);
        assertEquals(empresario.getEmail(), result.getEmail());

        EmpresarioDTO resultNoExiste = empresarioHibernate.findByEmail("noexiste@ejemplo.com");
        assertNull(resultNoExiste);
    }

    @Test
    void findByEmailFullTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        Empresario result = empresarioHibernate.findByEmailFull("carlos@empresa.com");
        assertNotNull(result);
        assertEquals(empresario.getEmail(), result.getEmail());

        Empresario resultNoExiste = empresarioHibernate.findByEmailFull("noexiste@ejemplo.com");
        assertNull(resultNoExiste);
    }

    @Test
    void convertToEntityTest() {
        EmpresarioDTO empresarioDTO = new EmpresarioDTO(
                null,
                "12345678A",
                "Carlos",
                "Lopez Martinez",
                "carlos@empresa.com",
                "650180800",
                "pass",
                new ArrayList<>(),
                new ArrayList<>()
        );

        Empresario empresario = empresarioHibernate.convertToEntity(empresarioDTO);

        assertNotNull(empresario);
        assertEquals(empresarioDTO.getDni(), empresario.getDni());
        assertEquals(empresarioDTO.getNombre(), empresario.getNombre());
        assertEquals(empresarioDTO.getApellidos(), empresario.getApellidos());
        assertEquals(empresarioDTO.getEmail(), empresario.getEmail());
    }

    @Test
    void convertToEntityWithEmpresasTest() {
        List<EmpresaDTO> empresasDTO = new ArrayList<>();
        empresasDTO.add(new EmpresaDTO(
                null,
                "12345678A",
                "B12345678",
                "Empresa Test",
                "Desarrollo software",
                "Calle Test 123",
                "empresa@test.com",
                "912345678",
                1,
                new ArrayList<>(),
                new ArrayList<>()
        ));

        EmpresarioDTO empresarioDTO = new EmpresarioDTO(
                null,
                "12345678A",
                "Carlos",
                "Lopez Martinez",
                "carlos@empresa.com",
                "650180800",
                "pass",
                new ArrayList<>(),
                empresasDTO
        );

        Empresario empresario = empresarioHibernate.convertToEntity(empresarioDTO);

        assertNotNull(empresario);
        assertNotNull(empresario.getEmpresas());
        assertEquals(1, empresario.getEmpresas().size());
        assertEquals("Empresa Test", empresario.getEmpresas().get(0).getNombre());
        assertEquals("B12345678", empresario.getEmpresas().get(0).getIdentificadorFiscal());
    }
}

    }*/
}
