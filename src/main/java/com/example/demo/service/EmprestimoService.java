package com.example.demo.service;

import com.example.demo.model.Emprestimo;
import com.example.demo.repository.EmprestimoRepository;

import java.time.LocalDate;

public class EmprestimoService {

    private final EmprestimoRepository repository;

    private static final int LIMITE_EMPRESTIMOS = 3;
    private static final int PRAZO_DIAS = 7;

    public EmprestimoService(EmprestimoRepository repository) {
        this.repository = repository;
    }

    public void realizarEmprestimo(Emprestimo emp) {

        // TC044 — usuário obrigatório
        if (emp.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário obrigatório");
        }

        // TC045 — livro obrigatório
        if (emp.getLivro() == null) {
            throw new IllegalArgumentException("Livro obrigatório");
        }

        // TC046 e TC047 — livro indisponível
        if (repository.existsLivroEmprestado(emp.getLivro())) {
            throw new IllegalArgumentException("Livro não disponível");
        }

        // TC050 — limite de empréstimos
        if (repository.countEmprestimosAtivosUsuario(emp.getUsuario()) >= LIMITE_EMPRESTIMOS) {
            throw new IllegalArgumentException("Usuário atingiu limite de empréstimos");
        }

        LocalDate hoje = LocalDate.now();

        // TC051 — data padrão
        if (emp.getDataEmprestimo() == null) {
            emp.setDataEmprestimo(hoje);
        }

        // TC052 — impedir futura
        if (emp.getDataEmprestimo().isAfter(hoje)) {
            throw new IllegalArgumentException("Data de empréstimo futura");
        }

        // TC055 — calcular devolução
        if (emp.getDataPrevistaDevolucao() == null) {
            emp.setDataPrevistaDevolucao(emp.getDataEmprestimo().plusDays(PRAZO_DIAS));
        }

        // TC053 e TC054 — validar devolução
        if (!emp.getDataPrevistaDevolucao().isAfter(emp.getDataEmprestimo())) {
            throw new IllegalArgumentException("Data de devolução inválida");
        }

        // TC056 — status inicial
        emp.setStatus("ATIVO");

        repository.save(emp);
    }

    // TC057 — atraso automático
    public void atualizarStatusAtrasados(Emprestimo emp) {
        if ("ATIVO".equals(emp.getStatus())
                && LocalDate.now().isAfter(emp.getDataPrevistaDevolucao())) {
            emp.setStatus("ATRASADO");
        }
    }
}