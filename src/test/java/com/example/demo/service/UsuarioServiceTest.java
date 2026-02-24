package com.example.demo.service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    private UsuarioService service;

    @BeforeEach
    void setup() {
        service = new UsuarioService(repository);
    }

    @Test
    @DisplayName("TC022 - Cadastro realizado com sucesso")
    void tc022_sucesso() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "pedro@mail.com", "81999887766", LocalDate.of(2000, 10, 10));
        assertDoesNotThrow(() -> service.cadastrarUsuario(u));
        verify(repository, times(1)).save(u);
    }

    @Test
    @DisplayName("TC023 - Nome vazio")
    void tc023_nomeVazio() {
        Usuario u = new Usuario("", "111-222-333-12", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC024 - Nome < 3 caracteres")
    void tc024_nomeCurto() {
        Usuario u = new Usuario("Pe", "111-222-333-12", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC025 - Nome > 255 caracteres")
    void tc025_nomeLongo() {
        String nomeGigante = "A".repeat(256);
        Usuario u = new Usuario(nomeGigante, "111-222-333-12", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC026 - Nome com números")
    void tc026_nomeComNumeros() {
        Usuario u = new Usuario("Pedro5123", "111-222-333-12", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC027 - Nome com apenas espaços")
    void tc027_nomeEspacos() {
        Usuario u = new Usuario("             ", "111-222-333-12", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC028 - CPF vazio")
    void tc028_cpfVazio() {
        Usuario u = new Usuario("Pedro", "", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC029 - Letras no CPF")
    void tc029_cpfComLetras() {
        Usuario u = new Usuario("Pedro", "111-abc-333-12", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC030 - CPF com tamanho inválido")
    void tc030_cpfTamanhoInvalido() {
        Usuario u = new Usuario("Pedro", "111-222-333-124312", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC031 - CPF duplicado")
    void tc031_cpfDuplicado() {
        when(repository.existsByCpf("111-222-333-12")).thenReturn(true);
        Usuario u = new Usuario("Cleber", "111-222-333-12", "cleber@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC032 - Email vazio")
    void tc032_emailVazio() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC033 - Email sem @")
    void tc033_emailInvalido() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "pedro", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC034 - Email duplicado")
    void tc034_emailDuplicado() {
        when(repository.existsByEmail("pedro@mail.com")).thenReturn(true);
        Usuario u = new Usuario("Cleber", "111-222-333-99", "pedro@mail.com", "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC035 - Email > 255 caracteres")
    void tc035_emailLongo() {
        String emailLongo = "a".repeat(250) + "@mail.com";
        Usuario u = new Usuario("Pedro", "111-222-333-12", emailLongo, "81999887766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC036 - Telefone vazio")
    void tc036_telefoneVazio() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC037 - Telefone com letras")
    void tc037_telefoneLetras() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "819998877frt", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC038 - Telefone > 11 dígitos")
    void tc038_telefoneLongo() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "81999887766000", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC039 - Telefone < 10 dígitos")
    void tc039_telefoneCurto() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "81999", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC040 - Telefone com caracteres especiais")
    void tc040_telefoneEspecial() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "81999#*7766", LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC041 - Data nascimento vazia")
    void tc041_dataVazia() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "81999887766", null);
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC042 - Data nascimento futura")
    void tc042_dataFutura() {
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "81999887766", LocalDate.of(3010, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }

    @Test
    @DisplayName("TC043 - Data nascimento com letras")
    void tc043_dataLetras() {
        // No Java, se usarmos LocalDate, o erro ocorre antes ou o campo fica null. 
        // Validamos aqui que se a data for nula (devido a erro de conversão), o sistema bloqueia.
        Usuario u = new Usuario("Pedro", "111-222-333-12", "p@m.com", "81999887766", null);
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarUsuario(u));
    }
}