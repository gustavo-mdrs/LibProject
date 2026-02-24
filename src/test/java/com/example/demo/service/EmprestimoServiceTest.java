package com.example.demo.service;

import com.example.demo.model.Emprestimo;
import com.example.demo.model.Livro;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    private EmprestimoService service;
    private EmprestimoRepository repository;

    @BeforeEach
    void setup() {
        repository = new EmprestimoRepository();
        service = new EmprestimoService(repository);
    }

    // TC044
    @Test
    void naoPermitirEmprestimoSemUsuario() {
        Emprestimo e = new Emprestimo();
        e.setLivro(new Livro());

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC045
    @Test
    void naoPermitirEmprestimoSemLivro() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC046 e TC047
    @Test
    void naoPermitirLivroJaEmprestado() {
        Usuario u = new Usuario();
        Livro l = new Livro();

        Emprestimo primeiro = new Emprestimo();
        primeiro.setUsuario(u);
        primeiro.setLivro(l);
        service.realizarEmprestimo(primeiro);

        Emprestimo segundo = new Emprestimo();
        segundo.setUsuario(new Usuario());
        segundo.setLivro(l);

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(segundo));
    }

    // TC050
    @Test
    void impedirQuandoUltrapassarLimiteEmprestimos() {
        Usuario u = new Usuario();

        for (int i = 0; i < 3; i++) {
            Emprestimo e = new Emprestimo();
            e.setUsuario(u);
            e.setLivro(new Livro());
            service.realizarEmprestimo(e);
        }

        Emprestimo extra = new Emprestimo();
        extra.setUsuario(u);
        extra.setLivro(new Livro());

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(extra));
    }

    // TC051
    @Test
    void dataEmprestimoDeveSerHoje() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());

        service.realizarEmprestimo(e);

        assertEquals(LocalDate.now(), e.getDataEmprestimo());
    }

    // TC052
    @Test
    void impedirDataEmprestimoFutura() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());
        e.setDataEmprestimo(LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC053 e TC054
    @Test
    void impedirDataDevolucaoInvalida() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());
        e.setDataEmprestimo(LocalDate.now());
        e.setDataPrevistaDevolucao(LocalDate.now()); // inválida

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    // TC055
    @Test
    void deveCalcularDataDevolucaoAutomaticamente() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());

        service.realizarEmprestimo(e);

        assertNotNull(e.getDataPrevistaDevolucao());
    }

    // TC056
    @Test
    void statusInicialDeveSerAtivo() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());

        service.realizarEmprestimo(e);

        assertEquals("ATIVO", e.getStatus());
    }

    // TC057
    @Test
    void deveMudarParaAtrasado() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());
        e.setDataEmprestimo(LocalDate.now().minusDays(10));
        e.setDataPrevistaDevolucao(LocalDate.now().minusDays(1));

        service.realizarEmprestimo(e);
        service.atualizarStatusAtrasados(e);

        assertEquals("ATRASADO", e.getStatus());
    }

    // TC058
    @Test
    void impedirEmprestimoDuplicadoMesmoLivroMesmoUsuario() {
        Usuario u = new Usuario();
        Livro l = new Livro();

        Emprestimo e1 = new Emprestimo();
        e1.setUsuario(u);
        e1.setLivro(l);
        service.realizarEmprestimo(e1);

        Emprestimo e2 = new Emprestimo();
        e2.setUsuario(u);
        e2.setLivro(l);

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e2));
    }
}
