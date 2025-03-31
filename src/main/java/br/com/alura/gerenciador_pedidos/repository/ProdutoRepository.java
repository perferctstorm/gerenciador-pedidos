package br.com.alura.gerenciador_pedidos.repository;

import br.com.alura.gerenciador_pedidos.model.Categoria;
import br.com.alura.gerenciador_pedidos.model.Produto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    //Derived Queries
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

    //JPQL
    @Query("SELECT p FROM Produto p WHERE p.preco >= :valor")
    List<Produto> produtosPrecoMaiorQue(Double valor);
    @Query("SELECT p FROM Produto p")
    List<Produto> produtosPorPreco(Sort sort);
    @Query("SELECT p FROM Produto p WHERE p.nome ilike :comeco%")
    List<Produto> produtosComecamPor(String comeco);

    @Query("SELECT AVG(p.preco) FROM Produto p")
    Object mediaPrecoProduto();

    @Query("SELECT MAX(p.preco) FROM Produto p WHERE p.categoria= :categoria")
    Object valorMáximoProdutoPorCategoria(Categoria categoria);

    @Query("""
            SELECT COUNT(p.id), c.nome
            FROM Produto p INNER JOIN p.categoria c 
            GROUP BY c.nome
            """)
    List<Object[]> totalProdutosPorCategoria();

    @Query("""
            SELECT COUNT(p.id), c.nome
            FROM Produto p INNER JOIN p.categoria c 
            GROUP BY c.nome
            HAVING COUNT(p.id) > :contProdutos
            """)
    List<Object[]> totalProdutosPorCategoriaMaiorQue(Long contProdutos);

    @Query("SELECT p FROM Produto p WHERE p.nome ilike %:nome% OR p.categoria=:categoria")
    List<Produto> produtosPorNomeOuCategoria(String nome, Categoria categoria);

    @Query(value = "select * from produto order by valor desc limit :limit", nativeQuery = true)
    List<Produto> produtosMaisCarosSQLNativo(Long limit);
}
