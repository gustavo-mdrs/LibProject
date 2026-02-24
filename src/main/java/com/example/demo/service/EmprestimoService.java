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

        // TC044
        if (emp.getUsuario() == null) {
            throw new IllegalArgumentException("Usuário obrigatório");
        }

        // TC045
        if (emp.getLivro() == null) {
            throw new IllegalArgumentException("Livro obrigatório");
        }

        // TC046 e TC047
        if (repository.existsLivroEmprestado(emp.getLivro())) {
            throw new IllegalArgumentException("Livro não disponível");
        }

        // TC050
        if (repository.countEmprestimosAtivosUsuario(emp.getUsuario()) >= LIMITE_EMPRESTIMOS) {
            throw new IllegalArgumentException("Usuário atingiu limite de empréstimos");
        }

        LocalDate hoje = LocalDate.now();

        // TC051
        if (emp.getDataEmprestimo() == null) {
            emp.setDataEmprestimo(hoje);
        }

        // TC052
        if (emp.getDataEmprestimo().isAfter(hoje)) {
            throw new IllegalArgumentException("Data de empréstimo futura");
        }

        // TC055 — cálculo automático
        if (emp.getDataPrevistaDevolucao() == null) {
            emp.setDataPrevistaDevolucao(emp.getDataEmprestimo().plusDays(PRAZO_DIAS));
        }

        // TC053 e TC054
        if (!emp.getDataPrevistaDevolucao().isAfter(emp.getDataEmprestimo())) {
            throw new IllegalArgumentException("Data de devolução inválida");
        }

        // TC056
        emp.setStatus("ATIVO");

        repository.save(emp);
    }

    // TC057
    public void atualizarStatusAtrasados(Emprestimo emp) {
        if ("ATIVO".equals(emp.getStatus())
                && LocalDate.now().isAfter(emp.getDataPrevistaDevolucao())) {
            emp.setStatus("ATRASADO");
        }
    }
}
