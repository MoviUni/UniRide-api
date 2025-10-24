package com.example.unirideapi.unit;

import com.example.unirideapi.repository.RutaRepository;
import com.example.unirideapi.service.RutaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ruta service")
public class RutaServiceTest {
    @Mock
    private RutaRepository rutaRepository;
    @InjectMocks
    private RutaService rutaService;

}
