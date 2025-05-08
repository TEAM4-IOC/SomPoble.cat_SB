package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.service.CloudinaryService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.HorarioService;
import com.sompoble.cat.service.ReservaService;
import com.sompoble.cat.service.ServicioService;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
    
    @MockBean
    private CloudinaryService cloudinaryService;
    
    @MockBean
    private ReservaService reservaService;
    
    @MockBean
    private ServicioService servicioService;
    
    @MockBean
    private HorarioService horarioService;

    private Empresario empresario;
    private Empresa empresa1;
    private Empresa empresa2;

    @BeforeEach
    void setUp() {
        // Crear y persistir un empresario
        empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");

        entityManager.persist(empresario);
        entityManager.flush();
        
        // Preparar empresas para tests
        empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Ficticia, 123");
        empresa1.setTelefono("912345678");
        empresa1.setEmail("empresa@empresa.com");
        empresa1.setActividad("Desarrollo software");
        empresa1.setTipo(1);
        empresa1.setEmpresario(empresario);
        
        empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B98765432");
        empresa2.setNombre("Otra Empresa S.L.");
        empresa2.setDireccion("Calle Real, 456");
        empresa2.setTelefono("913456789");
        empresa2.setEmail("otra@empresa.com");
        empresa2.setActividad("Consultoría");
        empresa2.setTipo(2);
        empresa2.setEmpresario(empresario);
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
    
    @Test
    void findAllTest() {
        // Agregamos las dos empresas a la base de datos
        empresaService.addEmpresa(empresa1);
        empresaService.addEmpresa(empresa2);
        
        // Verificamos que podemos recuperar la lista completa
        List<EmpresaDTO> empresas = empresaService.findAll();
        
        // Verificaciones
        assertNotNull(empresas);
        assertTrue(empresas.size() >= 2, "Deberían haber al menos 2 empresas en la lista");
        
        // Verificar que nuestras empresas estén en la lista
        boolean encontroEmpresa1 = false;
        boolean encontroEmpresa2 = false;
        
        for (EmpresaDTO dto : empresas) {
            if (dto.getIdentificadorFiscal().equals(empresa1.getIdentificadorFiscal())) {
                encontroEmpresa1 = true;
            }
            if (dto.getIdentificadorFiscal().equals(empresa2.getIdentificadorFiscal())) {
                encontroEmpresa2 = true;
            }
        }
        
        assertTrue(encontroEmpresa1, "La empresa1 debería estar en la lista");
        assertTrue(encontroEmpresa2, "La empresa2 debería estar en la lista");
    }
    
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
        // Crear y persistir la empresa
        empresaService.addEmpresa(empresa1);
        Long idEmpresa = empresa1.getIdEmpresa();
        
        // Crear horarios y servicios mockados para esta empresa
        Horario horario1 = new Horario("Lunes-Viernes", LocalTime.of(9, 0), LocalTime.of(14, 0), empresa1);
        Horario horario2 = new Horario("Lunes-Viernes", LocalTime.of(16, 0), LocalTime.of(20, 0), empresa1);
        List<Horario> horarios = Arrays.asList(horario1, horario2);
        
        Servicio servicio1 = new Servicio("Servicio 1", "Descripción 1", 60, 50, 15, empresa1);
        Servicio servicio2 = new Servicio("Servicio 2", "Descripción 2", 30, 25, 10, empresa1);
        List<Servicio> servicios = Arrays.asList(servicio1, servicio2);
        
        // Configurar los mocks
        when(horarioService.findByEmpresa_IdEmpresa(idEmpresa)).thenReturn(horarios);
        when(servicioService.obtenerPorEmpresaId(idEmpresa)).thenReturn(servicios);
        doNothing().when(reservaService).deleteByEmpresaIdentificadorFiscal(empresa1.getIdentificadorFiscal());
        doNothing().when(horarioService).deleteById(anyLong());
        doNothing().when(servicioService).eliminarPorId(anyLong());
        
        // Ejecutar el método a probar
        empresaService.deleteByIdentificadorFiscal(empresa1.getIdentificadorFiscal());
        
        // Verificar que los servicios relacionados fueron llamados correctamente
        verify(reservaService, times(1)).deleteByEmpresaIdentificadorFiscal(empresa1.getIdentificadorFiscal());
        verify(horarioService, times(1)).findByEmpresa_IdEmpresa(idEmpresa);
        verify(servicioService, times(1)).obtenerPorEmpresaId(idEmpresa);   
        
        // Verificar que la empresa fue eliminada
        EmpresaDTO empresaEliminada = empresaRepository.findByIdentificadorFiscal(empresa1.getIdentificadorFiscal());
        assertNull(empresaEliminada, "La empresa debería haber sido eliminada");
    }
    
    @Test
    void uploadEmpresaImageTest() {
        // Crear y persistir la empresa
        empresaService.addEmpresa(empresa1);
        
        // Crear un archivo de imagen simulado
        MockMultipartFile imagenMock = new MockMultipartFile(
            "imagen", 
            "test-image.jpg", 
            "image/jpeg", 
            "contenido de prueba".getBytes()
        );
        
        // Preparar respuesta simulada de Cloudinary
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secureUrl", "https://res.cloudinary.com/demo/image/upload/v1234567890/empresas/imagen1.jpg");
        uploadResult.put("publicId", "empresas/imagen1");
        
        // Configurar el comportamiento del mock para Cloudinary
        when(cloudinaryService.uploadImage(any(MultipartFile.class), eq("empresas"))).thenReturn(uploadResult);
        
        // Ejecutar el método a probar
        Empresa empresaActualizada = empresaService.uploadEmpresaImage(empresa1.getIdentificadorFiscal(), imagenMock);
        
        // Verificaciones
        assertNotNull(empresaActualizada);
        assertEquals(uploadResult.get("secureUrl"), empresaActualizada.getImagenUrl());
        assertEquals(uploadResult.get("publicId"), empresaActualizada.getImagenPublicId());
        
        // Verificar que el servicio de Cloudinary fue llamado
        verify(cloudinaryService, times(1)).uploadImage(any(MultipartFile.class), eq("empresas"));
        verify(cloudinaryService, never()).deleteImage(anyString()); // No debería llamarse al no tener imagen previa
    }
    
    @Test
    void uploadEmpresaImageEmptyFileTest() {
        // Crear y persistir la empresa
        empresaService.addEmpresa(empresa1);
        
        // Crear un archivo vacío
        MockMultipartFile imagenVacia = new MockMultipartFile(
            "imagen", 
            "empty.jpg", 
            "image/jpeg", 
            new byte[0]
        );
        
        // Verificar que se lanza la excepción esperada
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empresaService.uploadEmpresaImage(empresa1.getIdentificadorFiscal(), imagenVacia);
        });
        
        assertEquals("La imagen no puede estar vacía", exception.getMessage());
        
        // Verificar que no se llamó a ningún servicio de Cloudinary
        verify(cloudinaryService, never()).uploadImage(any(MultipartFile.class), anyString());
        verify(cloudinaryService, never()).deleteImage(anyString());
    }
    
    @Test
    void uploadEmpresaImageNullFileTest() {
        // Crear y persistir la empresa
        empresaService.addEmpresa(empresa1);
        
        // Verificar que se lanza la excepción esperada con archivo null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            empresaService.uploadEmpresaImage(empresa1.getIdentificadorFiscal(), null);
        });
        
        assertEquals("La imagen no puede estar vacía", exception.getMessage());
        
        // Verificar que no se llamó a ningún servicio de Cloudinary
        verify(cloudinaryService, never()).uploadImage(any(MultipartFile.class), anyString());
        verify(cloudinaryService, never()).deleteImage(anyString());
    }
}