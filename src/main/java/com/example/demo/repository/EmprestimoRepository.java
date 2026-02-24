package com.example.demo.repository;

import com.example.demo.model.Emprestimo;

import java.util.ArrayList;
import java.util.List;

public class EmprestimoRepository {

    private final List<Emprestimo> base = new ArrayList<>();

    public void save(Emprestimo e) {
        base.add(e);
    }

    public boolean existsEmprestimoAtivoByUsuario(Object usuario) {
        return base.stream()
                .anyMatch(e -> e.getUsuario().equals(usuario)
                        && "ATIVO".equals(e.getStatus()));
    }

    public boolean existsLivroEmprestado(Object livro) {
        return base.stream()
                .anyMatch(e -> e.getLivro().equals(livro)
                        && "ATIVO".equals(e.getStatus()));
    }

    public long countEmprestimosAtivosUsuario(Object usuario) {
        return base.stream()
                .filter(e -> e.getUsuario().equals(usuario)
                        && "ATIVO".equals(e.getStatus()))
                .count();
    }
}
