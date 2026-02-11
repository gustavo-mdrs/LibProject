package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String isbn;
    private String autor;
    private Integer anoPublicacao;
    private String editora;
    private Integer edicao;
    private Integer numeroPaginas;
    
    private boolean disponivel = true; 

    public Livro() {
    }

    public Livro(String titulo, String isbn, String autor, Integer anoPublicacao, String editora, Integer edicao, Integer numeroPaginas) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.edicao = edicao;
        this.numeroPaginas = numeroPaginas;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getEditora() {
        return editora;
    }

    public Integer getEdicao() {
        return edicao;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    
}