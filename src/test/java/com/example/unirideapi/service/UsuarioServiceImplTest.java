package com.example.unirideapi.service;

import com.example.unirideapi.dto.request.ConductorRequestDTO;
import com.example.unirideapi.dto.request.LoginRequestDTO;
import com.example.unirideapi.dto.request.PasajeroRequestDTO;
import com.example.unirideapi.dto.response.AuthResponseDTO;
import com.example.unirideapi.dto.response.UsuarioPerfilResponseDTO;
import com.example.unirideapi.mapper.UsuarioMapper;
import com.example.unirideapi.model.*;
import com.example.unirideapi.model.enums.ERol;
import com.example.unirideapi.repository.ConductorRepository;
import com.example.unirideapi.repository.PasajeroRepository;
import com.example.unirideapi.repository.RolRepository;
import com.example.unirideapi.repository.UsuarioRepository;
import com.example.unirideapi.security.TokenProvider;
import com.example.unirideapi.security.UserPrincipal;
import com.example.unirideapi.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/*
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("UsuarioServiceImplTest - Pruebas Unitarias")
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ConductorRepository conductorRepository;

    @Mock
    private PasajeroRepository pasajeroRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    // Common test objects
    private Rol rolPasajero;
    private Rol rolConductor;

    @BeforeEach
    void setUp() {
        rolPasajero = new Rol();
        rolPasajero.setIdRol(1);
        rolPasajero.setName(ERol.PASAJERO);

        rolConductor = new Rol();
        rolConductor.setIdRol(2);
        rolConductor.setName(ERol.CONDUCTOR);
    }

    // -----------------------
    // registroPasajero tests
    // -----------------------
    @Test
    @DisplayName("CP01 - Registro de pasajero")
    void registroPasajero_ValidData_Success() {
        // Arrange
        PasajeroRequestDTO dto = mock(PasajeroRequestDTO.class);
        when(dto.email()).thenReturn("pablo@example.com");
        when(dto.password()).thenReturn("secret");
        when(dto.dni()).thenReturn("87654321");
        when(dto.nombre()).thenReturn("Pablo");
        when(dto.apellido()).thenReturn("Lopez");
        when(dto.edad()).thenReturn(30);
        when(dto.descripcionPasajero()).thenReturn("Buen pasajero");

        when(rolRepository.findByName(ERol.PASAJERO)).thenReturn(Optional.of(rolPasajero));
        when(pasajeroRepository.existsByDni("87654321")).thenReturn(false);
        when(usuarioRepository.existsByEmail("pablo@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");

        Usuario savedUsuario = new Usuario();
        savedUsuario.setIdUsuario(100);
        savedUsuario.setEmail("pablo@example.com");
        // mapper result
        UsuarioPerfilResponseDTO expectedDto = mock(UsuarioPerfilResponseDTO.class);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);
        when(usuarioMapper.toUsuarioPerfilResponseDTO(savedUsuario)).thenReturn(expectedDto);

        // Act
        UsuarioPerfilResponseDTO result = usuarioService.registroPasajero(dto);

        // Assert
        assertThat(result).isSameAs(expectedDto);
        verify(rolRepository).findByName(ERol.PASAJERO);
        verify(pasajeroRepository).existsByDni("87654321");
        verify(usuarioRepository).existsByEmail("pablo@example.com");
        verify(passwordEncoder).encode("secret");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).toUsuarioPerfilResponseDTO(savedUsuario);
    }

    @Test
    @DisplayName("CP02 - DNI Duplicado (Pasajero)")
    void registroPasajero_DniDuplicate_Throws() {
        // Arrange
        PasajeroRequestDTO dto = mock(PasajeroRequestDTO.class);
        when(dto.dni()).thenReturn("11111111");

        when(rolRepository.findByName(ERol.PASAJERO)).thenReturn(Optional.of(rolPasajero));
        when(pasajeroRepository.existsByDni("11111111")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.registroPasajero(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un pasajero con el DNI: 11111111");

        verify(usuarioRepository, never()).save(any());
    }

    // -----------------------
    // registroConductor tests
    // -----------------------
    @Test
    @DisplayName("CP03 - Registro de conductor")
    void registroConductor_ValidData_Success() {
        // Arrange
        ConductorRequestDTO dto = mock(ConductorRequestDTO.class);
        when(dto.email()).thenReturn("luis@example.com");
        when(dto.password()).thenReturn("pass");
        when(dto.dni()).thenReturn("22222222");
        when(dto.nombre()).thenReturn("Luis");
        when(dto.apellido()).thenReturn("Perez");
        when(dto.edad()).thenReturn(40);
        when(dto.descripcionConductor()).thenReturn("Buen conductor");

        when(rolRepository.findByName(ERol.CONDUCTOR)).thenReturn(Optional.of(rolConductor));
        when(conductorRepository.existsByDni("22222222")).thenReturn(false);
        when(usuarioRepository.existsByEmail("luis@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded-pass");

        Usuario savedUsuario = new Usuario();
        savedUsuario.setIdUsuario(200);
        savedUsuario.setEmail("luis@example.com");

        UsuarioPerfilResponseDTO expectedDto = mock(UsuarioPerfilResponseDTO.class);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(savedUsuario);
        when(usuarioMapper.toUsuarioPerfilResponseDTO(savedUsuario)).thenReturn(expectedDto);

        // Act
        UsuarioPerfilResponseDTO result = usuarioService.registroConductor(dto);

        // Assert
        assertThat(result).isSameAs(expectedDto);
        verify(rolRepository).findByName(ERol.CONDUCTOR);
        verify(conductorRepository).existsByDni("22222222");
        verify(usuarioRepository).existsByEmail("luis@example.com");
        verify(passwordEncoder).encode("pass");
        verify(usuarioMapper).toUsuarioPerfilResponseDTO(savedUsuario);
    }

    @Test
    @DisplayName("CP04 - DNI Duplicado (Conductor)")
    void registroConductor_DniDuplicate_Throws() {
        // Arrange
        ConductorRequestDTO dto = mock(ConductorRequestDTO.class);
        when(dto.dni()).thenReturn("33333333");
        when(rolRepository.findByName(ERol.CONDUCTOR)).thenReturn(Optional.of(rolConductor));
        when(conductorRepository.existsByDni("33333333")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.registroConductor(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un conductor");

        verify(usuarioRepository, never()).save(any());
    }

    @DisplayName("CP05 - Evitar registro con email duplicado")
    @Test
    void CP08_registroPasajeroConEmailDuplicado() {
        // Arrange
        PasajeroRequestDTO dto = new PasajeroRequestDTO(
                "Rafael",
                "Chui",
                "test@example.com",
                "12345",
                "test@example.com",
                20,
                "Pasajero frecuente"
        );

        Rol rolMock = new Rol();
        rolMock.setIdRol(1);
        rolMock.setName(ERol.PASAJERO);
        when(rolRepository.findByName(ERol.PASAJERO)).thenReturn(Optional.of(rolMock));

        // Simula que ya existe un usuario con ese email
        when(usuarioRepository.existsByEmail(dto.email())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.registroPasajero(dto)
        );

        assertEquals("Ya existe un usuario con el email: test@example.com", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class)); // asegura que no se intenta guardar
    }

    // -----------------------
    // login tests
    // -----------------------
    @Test
    @DisplayName("CP06 - Login")
    void login_ValidCredentials_Success() {
        // Arrange
        LoginRequestDTO loginDto = mock(LoginRequestDTO.class);
        when(loginDto.email()).thenReturn("ana@example.com");
        when(loginDto.password()).thenReturn("pwd");

        Authentication auth = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(50);
        usuario.setEmail("ana@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getUsuario()).thenReturn(usuario);
        when(tokenProvider.createAccessToken(auth)).thenReturn("token-xyz");

        AuthResponseDTO expectedAuthResponse = mock(AuthResponseDTO.class);
        when(usuarioMapper.toAuthResponseDTO(usuario, "token-xyz")).thenReturn(expectedAuthResponse);

        // Act
        AuthResponseDTO result = usuarioService.login(loginDto);

        // Assert
        assertThat(result).isSameAs(expectedAuthResponse);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).createAccessToken(auth);
        verify(usuarioMapper).toAuthResponseDTO(usuario, "token-xyz");
    }

    @DisplayName("CP07 - Login con contraseña incorrecta")
    @Test
    void CP10_loginConContrasenaIncorrecta() {
        // Arrange
        LoginRequestDTO loginDTO = new LoginRequestDTO("test@example.com", "claveIncorrecta");

        // Simular que el AuthenticationManager lanza BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> usuarioService.login(loginDTO));

        // Verificar que no se haya intentado generar token
        verify(tokenProvider, never()).createAccessToken(any(Authentication.class));
    }

    // -----------------------
    // updateUsuarioPerfil tests
    // -----------------------
    @Test
    @DisplayName("CP08 - Actualizar Perfil")
    void updateUsuarioPerfil_ExistingUser_Success() {
        // Arrange
        Integer idUsuario = 10;
        UsuarioPerfilResponseDTO perfilRequestDTO = mock(UsuarioPerfilResponseDTO.class);

        Usuario existing = new Usuario();
        existing.setIdUsuario(idUsuario);
        existing.setEmail("old@example.com");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(existing));
        // applyPerfilUpdates no devuelve nada; solo verificamos que se invoque
        when(usuarioRepository.save(existing)).thenReturn(existing);

        UsuarioPerfilResponseDTO expectedDto = mock(UsuarioPerfilResponseDTO.class);
        when(usuarioMapper.toUsuarioPerfilResponseDTO(existing)).thenReturn(expectedDto);

        // Act
        UsuarioPerfilResponseDTO result = usuarioService.updateUsuarioPerfil(idUsuario, expectedDto);

        // Assert
        assertThat(result).isSameAs(expectedDto);
        verify(usuarioRepository).findById(idUsuario);
        verify(usuarioMapper).applyPerfilUpdates(any(UsuarioPerfilResponseDTO.class), eq(existing));
        verify(usuarioRepository).save(existing);
        verify(usuarioMapper).toUsuarioPerfilResponseDTO(existing);
    }

    @Test
    @DisplayName("CP09 - Actualizar usuario no existente")
    void getUsuarioPerfilById_UserNotFound_Throws() {
        // Arrange
        Integer idUsuario = 12345;
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.getUsuarioPerfilById(idUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    @DisplayName("updateUsuarioPerfil - lanza IllegalArgumentException cuando usuario no existe")
    void updateUsuarioPerfil_UserNotFound_Throws() {
        // Arrange
        Integer idUsuario = 999;
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.updateUsuarioPerfil(idUsuario, mock(UsuarioPerfilResponseDTO.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // -----------------------
    // getUsuarioPerfilById tests
    // -----------------------
    @Test
    @DisplayName("getUsuarioPerfilById - éxito")
    void getUsuarioPerfilById_ExistingUser_Success() {
        // Arrange
        Integer idUsuario = 7;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setEmail("user7@example.com");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        UsuarioPerfilResponseDTO expectedDto = mock(UsuarioPerfilResponseDTO.class);
        when(usuarioMapper.toUsuarioPerfilResponseDTO(usuario)).thenReturn(expectedDto);

        // Act
        UsuarioPerfilResponseDTO result = usuarioService.getUsuarioPerfilById(idUsuario);

        // Assert
        assertThat(result).isSameAs(expectedDto);
        verify(usuarioRepository).findById(idUsuario);
        verify(usuarioMapper).toUsuarioPerfilResponseDTO(usuario);
    }

}

 */
