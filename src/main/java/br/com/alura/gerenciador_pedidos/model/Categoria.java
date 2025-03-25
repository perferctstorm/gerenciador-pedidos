package br.com.alura.gerenciador_pedidos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Categoria {
    @Id
    private Long id;
    private String nome;
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Produto> produtos;

    public Categoria(){}
    public Categoria(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        produtos.forEach(p->p.setCategoria(this));
        this.produtos = produtos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
