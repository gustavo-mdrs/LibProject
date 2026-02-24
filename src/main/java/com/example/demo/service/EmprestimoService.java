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

        // TC048 — usuário inativo
        if (!"ATIVO".equals(emp.getUsuario().getStatus())) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        // TC049 — usuário com atraso
        if (emp.getUsuario().isPossuiAtraso()) {
            throw new IllegalArgumentException("Usuário com atraso");
        }

        // TC045 — livro obrigatório
        if (emp.getLivro() == null) {
            throw new IllegalArgumentException("Livro obrigatório");
        }

        // TC046 e TC047 — livro indisponível ou já emprestado
        if (repository.existsLivroEmprestado(emp.getLivro())) {
            throw new IllegalArgumentException("Livro não disponível");
        }

        // TC058 — empréstimo duplicado (mesmo usuário + mesmo livro)
        if (repository.existsEmprestimoAtivoByUsuario(emp.getUsuario())
                && repository.existsLivroEmprestado(emp.getLivro())) {
            throw new IllegalArgumentException("Empréstimo duplicado");
        }

        // TC050 — limite de empréstimos
        if (repository.countEmprestimosAtivosUsuario(emp.getUsuario()) >= LIMITE_EMPRESTIMOS) {
            throw new IllegalArgumentException("Usuário atingiu limite de empréstimos");
        }

        LocalDate hoje = LocalDate.now();

        // TC051 — data de empréstimo padrão = hoje
        if (emp.getDataEmprestimo() == null) {
            emp.setDataEmprestimo(hoje);
        }

        // TC052 — impedir data futura
        if (emp.getDataEmprestimo().isAfter(hoje)) {
            throw new IllegalArgumentException("Data de empréstimo futura");
        }

        // TC055 — cálculo automático da devolução
        if (emp.getDataPrevistaDevolucao() == null) {
            emp.setDataPrevistaDevolucao(emp.getDataEmprestimo().plusDays(PRAZO_DIAS));
        }

        // TC053 e TC054 — validar data de devolução
        if (!emp.getDataPrevistaDevolucao().isAfter(emp.getDataEmprestimo())) {
            throw new IllegalArgumentException("Data de devolução inválida");
        }

        // TC056 — status inicial
        emp.setStatus("ATIVO");

        repository.save(emp);
    }

    // TC057 — atualizar atrasados
    public void atualizarStatusAtrasados(Emprestimo emp) {
        if ("ATIVO".equals(emp.getStatus())
                && LocalDate.now().isAfter(emp.getDataPrevistaDevolucao())) {
            emp.setStatus("ATRASADO");
        }
    }
}
