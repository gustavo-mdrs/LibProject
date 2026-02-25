package com.example.demo.service;

import com.example.demo.model.Livro;
import com.example.demo.repository.LivroRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Year;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    
    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    private Livro livroValido;

    @BeforeEach
    void setUp() {
        lenient().when(livroRepository.existsByIsbn(anyString()))
            .thenReturn(false);
        
        livroValido = new Livro(
                "Livro Teste",
                "1234567890123",
                "Autor Teste",
                2020,
                "Editora Teste",
                1,
                100
        );
    }

    // TC001
    @Test
    void deveCadastrarLivroComSucesso() {
        assertDoesNotThrow(() -> livroService.cadastrar(livroValido));
    }

    // ===== CAMPOS OBRIGATÓRIOS =====

    @Test
    void naoDevePermitirTituloVazio() {
        livroValido.setTitulo("");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirIsbnVazio() {
        livroValido.setIsbn("");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirAutorVazio() {
        livroValido.setAutor("");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirAnoNulo() {
        livroValido.setAnoPublicacao(null);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirEditoraVazia() {
        livroValido.setEditora("");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirEdicaoNula() {
        livroValido.setEdicao(null);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirNumeroPaginasNulo() {
        livroValido.setNumeroPaginas(null);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    // ===== TAMANHO =====

    @Test
    void naoDevePermitirTituloMaiorQue255() {
        livroValido.setTitulo("A".repeat(256));
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirAutorMenorQue3() {
        livroValido.setAutor("AB");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirAutorMaiorQue255() {
        livroValido.setAutor("A".repeat(256));
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirEditoraMenorQue3() {
        livroValido.setEditora("AB");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirEditoraMaiorQue255() {
        livroValido.setEditora("A".repeat(256));
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    // ===== ISBN =====

    @Test
    void naoDevePermitirIsbnComMenosDe13Digitos() {
        livroValido.setIsbn("123456789012");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirIsbnComMaisDe13Digitos() {
        livroValido.setIsbn("12345678901234");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirIsbnComLetras() {
        livroValido.setIsbn("12345678901AB");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirIsbnDuplicado() {

        when(livroRepository.existsByIsbn("1234567890123"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));

        verify(livroRepository)
                .existsByIsbn("1234567890123");
    }

    // ===== ANO =====

    @Test
    void naoDevePermitirAnoMaiorQueAtual() {
        livroValido.setAnoPublicacao(Year.now().getValue() + 1);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    // ===== VALORES NUMÉRICOS =====

    @Test
    void naoDevePermitirEdicaoMenorOuIgualZero() {
        livroValido.setEdicao(0);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }

    @Test
    void naoDevePermitirNumeroPaginasMenorOuIgualZero() {
        livroValido.setNumeroPaginas(0);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livroValido));
    }
}