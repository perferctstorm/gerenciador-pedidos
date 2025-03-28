package br.com.alura.gerenciador_pedidos;

import br.com.alura.gerenciador_pedidos.model.Categoria;
import br.com.alura.gerenciador_pedidos.model.Produto;
import br.com.alura.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class GerenciadorPedidosApplication implements CommandLineRunner {
	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	FornecedorRepository fornecedorRepository;
	@Autowired
	PedidoRepository pedidoRepository;
	@Autowired
	ProdutoRepository produtoRepository;

	public static void main(String[] args) {
		SpringApplication.run(GerenciadorPedidosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Entrando na aplicação.");
		//var nome = "livro";
		//1 - Retorne todos os produtos com o nome exato fornecido.
		//List<Produto> produtos = produtoRepository.findByNomeIgnoreCase(nome);
		//--List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);

		//2 - Retorne todos os produtos associados a uma categoria específica.
		//List<Produto> produtos = produtoRepository.findByCategoria_Id(1);
		//System.out.println(String.format("Lista dos produtos cujo nome são semelhantes a %s:", nome));

		//3 - Retorne produtos com preço maior que o valor fornecido.
		Double preco = 10.;
		//List<Produto> produtos = produtoRepository.findByPrecoGreaterThan(preco);
		//System.out.println(String.format("Lista de produtos acima dos R$ %.2f.", preco));

		//4 - Retorne produtos com preço menor que o valor fornecido.
		//List<Produto> produtos = produtoRepository.findByPrecoLessThan(preco);
		//System.out.println(String.format("Produtos com preço abaixo de R$ %.2f: ", preco));

		//5 - Retorne produtos cujo nome contenha o termo especificado.
		//List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
		//System.out.println(String.format("Lista de produtos cujo nome contenha %s: ", nome)) ;
		//produtos.forEach(p->{
		//	System.out.println(String.format(" - %s, preço: %.2f.", p.getNome(), p.getPreco()));
		//});

		//6 - Retorne pedidos que ainda não possuem uma data de entrega.
		//List<Pedido> pedidos = pedidoRepository.findByDataIsNull();
		//System.out.println("Lista de pedidos sem data de entrega: ");

		//7 - Retorne pedidos com data de entrega preenchida.
		//List<Pedido> pedidos = pedidoRepository.findByDataIsNotNull();
		//System.out.println("Lista de pedidos com data de entrega: ");

		//8 - Retorne produtos de uma categoria ordenados pelo preço.
		//9 - Retorne produtos de uma categoria ordenados pelo preço de forma decrescente.
		//10 - Retorne a contagem de produtos em uma categoria específica.
		Long categoria_id = 2L;
		Optional<Categoria> categoria = categoriaRepository.findById(categoria_id);
		if(categoria.isPresent()) {
			//List<Produto> produtos = produtoRepository.findByCategoria_IdOrderByPreco(categoria_id);
			//List<Produto> produtos = produtoRepository.findByCategoriaOrderByPrecoDesc(categoria.get());
			Long totalProdutos = produtoRepository.countByCategoria(categoria.get());
			System.out.println(String.format("Total de produtos da categoria %s: %d.: ", categoria.get().getNome(), totalProdutos));
			//produtos.forEach(p -> System.out.println(
			//		String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));
		}else{
			System.out.println(String.format("Categoria %d não encontrada.", categoria_id));
		}

		System.out.println();
		Long totalProdutos = produtoRepository.countByPrecoGreaterThan(preco);
		System.out.println(String.format("Total de produtos cujo preço é maior que %,2.2f: %d.", preco, totalProdutos));

		System.out.println("");
		System.out.println("Os 3 produtos mais caros: ");
		List<Produto> produtos = produtoRepository.findTop2ByOrderByPrecoDesc();
		produtos.forEach(p -> System.out.println(
				String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));

		System.out.println("");
		System.out.println("Os 3 produtos mais baratos: ");
		produtos = produtoRepository.findTop2ByOrderByPreco();
		produtos.forEach(p -> System.out.println(
				String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));


		System.out.println();
		if(categoria.isPresent()) {
			produtos = produtoRepository.findTop1ByCategoriaOrderByPreco(categoria.get());
			System.out.println(String.format("Produtos mais baratos da categoria %s: ", categoria.get().getNome()));
			produtos.forEach(p -> System.out.println(
					String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));
		}else{
			System.out.println(String.format("Categoria %d não encontrada.", categoria_id));
		}
		System.out.println("\nSaindo na aplicação.");


/*
		// Criando categorias
		Categoria categoriaEletronicos = new Categoria(1L, "Eletrônicos");
		Categoria categoriaLivros = new Categoria(2L, "Livros");
		categoriaRepository.saveAll(List.of(categoriaEletronicos, categoriaLivros));

		// Criando fornecedores
		Fornecedor fornecedorTech = new Fornecedor("Tech Supplier");
		Fornecedor fornecedorLivros = new Fornecedor("Livraria Global");
		fornecedorRepository.saveAll(List.of(fornecedorTech, fornecedorLivros));

		// Criando produtos
		Produto produto1 = new Produto("Notebook", 3500.0, categoriaEletronicos, fornecedorTech);
		Produto produto2 = new Produto("Smartphone", 2500.0, categoriaEletronicos, fornecedorTech);
		Produto produto3 = new Produto("Livro de Java", 100.0, categoriaLivros, fornecedorLivros);
		produtoRepository.saveAll(List.of(produto1, produto2, produto3));

		// Criando pedidos e associando produtos
		Pedido pedido1 = new Pedido(1L, LocalDate.now());
		pedido1.setProdutos(List.of(produto1, produto3));
		Pedido pedido2 = new Pedido(2L, LocalDate.now().minusDays(1));
		pedido2.setProdutos(List.of(produto2));
		pedidoRepository.saveAll(List.of(pedido1, pedido2));

		System.out.println("Produtos na categoria Eletrônicos:");
		categoriaRepository.findById(1L).ifPresent(c-> c.getProdutos()
						.forEach(p-> System.out.println(String.format(" - %s",p.getNome()))));

		System.out.println("\nPedidos e seus produtos:");
		pedidoRepository.findAll().forEach(p->{
			System.out.println(String.format("Pedido %d:", p.getId()));
			p.getProdutos().forEach(pdto-> System.out.println(String.format(" - %s", pdto.getNome())));
		});

		System.out.println("\nProdutos e seus fornecedores:");
		produtoRepository.findAll().forEach(p->{
			System.out.println(String.format("Produto: %s, Fornecedor: %s.",
					p.getNome(), p.getFornecedor().getNome()));
		});
*/
	}
}
