package com.example.demo.service;

import com.example.demo.model.Livro;

public class LivroService {

    public void cadastrar(Livro livro) {

        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        if (livro.getIsbn() == null || livro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }

        

    }
}