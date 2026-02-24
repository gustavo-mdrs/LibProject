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

    @BeforeEach
    void setup() {
        service = new EmprestimoService(new EmprestimoRepository());
    }

    @Test
    void TC044_naoPermitirSemUsuario() {
        Emprestimo e = new Emprestimo();
        e.setLivro(new Livro());

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    @Test
    void TC045_naoPermitirSemLivro() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());

        assertThrows(IllegalArgumentException.class,
                () -> service.realizarEmprestimo(e));
    }

    @Test
    void TC051_dataEmprestimoHoje() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());

        service.realizarEmprestimo(e);

        assertEquals(LocalDate.now(), e.getDataEmprestimo());
    }

    @Test
    void TC056_statusInicialAtivo() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());

        service.realizarEmprestimo(e);

        assertEquals("ATIVO", e.getStatus());
    }

    @Test
    void TC057_mudaParaAtrasado() {
        Emprestimo e = new Emprestimo();
        e.setUsuario(new Usuario());
        e.setLivro(new Livro());
        e.setDataEmprestimo(LocalDate.now().minusDays(10));
        e.setDataPrevistaDevolucao(LocalDate.now().minusDays(1));

        service.realizarEmprestimo(e);
        service.atualizarStatusAtrasados(e);

        assertEquals("ATRASADO", e.getStatus());
    }
}
