package br.com.alura.gerenciador_pedidos.repository;

import br.com.alura.gerenciador_pedidos.model.Categoria;
import br.com.alura.gerenciador_pedidos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeIgnoreCase(String nome);
    List<Produto> findByCategoria_Id(Integer id);
    List<Produto> findByPrecoGreaterThan(Double preco);
    List<Produto> findByPrecoLessThan(Double preco);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByCategoria_IdOrderByPreco(Long id);
    List<Produto> findByCategoriaOrderByPrecoDesc(Categoria categoria);
    Long countByCategoria(Categoria categoria);
    Long countByPrecoGreaterThan(Double preco);
    List<Produto> findTop2ByOrderByPrecoDesc();
    List<Produto> findTop2ByOrderByPreco();
    List<Produto> findTop1ByCategoriaOrderByPreco(Categoria categoria);
}
