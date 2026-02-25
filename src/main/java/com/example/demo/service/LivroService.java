package com.example.demo.service;

import com.example.demo.model.Livro;
import com.example.demo.repository.LivroRepository;

import java.time.Year;

public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public void cadastrar(Livro livro) {

        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        // ===== CAMPOS OBRIGATÓRIOS =====

        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        if (livro.getIsbn() == null || livro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }

        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }

        if (livro.getAnoPublicacao() == null) {
            throw new IllegalArgumentException("Ano de publicação é obrigatório");
        }

        if (livro.getEditora() == null || livro.getEditora().isBlank()) {
            throw new IllegalArgumentException("Editora é obrigatória");
        }

        if (livro.getEdicao() == null) {
            throw new IllegalArgumentException("Edição é obrigatória");
        }

        if (livro.getNumeroPaginas() == null) {
            throw new IllegalArgumentException("Número de páginas é obrigatório");
        }

        // ===== VALIDAÇÕES DE TAMANHO =====

        if (livro.getTitulo().length() > 255) {
            throw new IllegalArgumentException("Título deve ter no máximo 255 caracteres");
        }

        if (livro.getAutor().length() < 3 || livro.getAutor().length() > 255) {
            throw new IllegalArgumentException("Autor deve ter entre 3 e 255 caracteres");
        }

        if (livro.getEditora().length() < 3 || livro.getEditora().length() > 255) {
            throw new IllegalArgumentException("Editora deve ter entre 3 e 255 caracteres");
        }

        // ===== ISBN =====

        if (!livro.getIsbn().matches("\\d{13}")) {
            throw new IllegalArgumentException("ISBN deve conter exatamente 13 dígitos numéricos");
        }

        if (livroRepository.existsByIsbn(livro.getIsbn())) {
            throw new IllegalArgumentException("Já existe livro com esse ISBN");
        }

        // ===== ANO =====

        int anoAtual = Year.now().getValue();

        if (livro.getAnoPublicacao() > anoAtual) {
            throw new IllegalArgumentException("Ano de publicação não pode ser maior que o ano atual");
        }

        // ===== VALORES NUMÉRICOS =====

        if (livro.getEdicao() <= 0) {
            throw new IllegalArgumentException("Edição deve ser maior que zero");
        }

        if (livro.getNumeroPaginas() <= 0) {
            throw new IllegalArgumentException("Número de páginas deve ser maior que zero");
        }

        // Se passou por tudo, cadastro é válido
    }
}