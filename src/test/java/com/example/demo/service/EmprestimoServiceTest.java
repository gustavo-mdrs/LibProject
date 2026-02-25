package com.example.demo.service;

import com.example.demo.model.Emprestimo;
import com.example.demo.model.Livro;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository repository;

    @InjectMocks
    private EmprestimoService service;

    private Usuario usuario;
    private Livro livro;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        livro = new Livro();
    }

    // TC044
    @Test
    void naoPermitirEmprestimoSemUsuario() {
        Emprestimo e = new Emprestimo();
        e.setLivro(livro);

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC045
    @Test
    void naoPermitirEmprestimoSemLivro() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC046 e TC047
    @Test
    void naoPermitirLivroIndisponivel() {
        when(repository.existsLivroEmprestado(livro)).thenReturn(true);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC050
    @Test
    void impedirQuandoUltrapassarLimite() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(3L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC051
    @Test
    void dataEmprestimoDeveSerHoje() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(0L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);

        service.realizarEmprestimo(e);

        assertEquals(LocalDate.now(), e.getDataEmprestimo());
    }

    // TC052
    @Test
    void impedirDataEmprestimoFutura() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(0L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);
        e.setDataEmprestimo(LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC053 e TC054
    @Test
    void impedirDataDevolucaoInvalida() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(0L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);
        e.setDataEmprestimo(LocalDate.now());
        e.setDataPrevistaDevolucao(LocalDate.now());

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC055
    @Test
    void deveCalcularDataDevolucaoAutomaticamente() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(0L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);

        service.realizarEmprestimo(e);

        assertNotNull(e.getDataPrevistaDevolucao());
    }

    // TC056
    @Test
    void statusInicialDeveSerAtivo() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(0L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);

        service.realizarEmprestimo(e);

        assertEquals("ATIVO", e.getStatus());
    }

    // TC057
    @Test
    void deveMudarParaAtrasado() {
        Emprestimo e = new Emprestimo();
        e.setStatus("ATIVO");
        e.setDataPrevistaDevolucao(LocalDate.now().minusDays(1));

        service.atualizarStatusAtrasados(e);

        assertEquals("ATRASADO", e.getStatus());
    }

    // Verifica save chamado (boa prática Mockito)
    @Test
    void deveSalvarEmprestimoQuandoValido() {
        when(repository.existsLivroEmprestado(any())).thenReturn(false);
        when(repository.countEmprestimosAtivosUsuario(usuario)).thenReturn(0L);

        Emprestimo e = new Emprestimo();
        e.setUsuario(usuario);
        e.setLivro(livro);

        service.realizarEmprestimo(e);

        verify(repository, times(1)).save(e);
    }
}
