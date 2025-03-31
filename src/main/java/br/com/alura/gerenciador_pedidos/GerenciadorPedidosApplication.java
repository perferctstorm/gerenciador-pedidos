package br.com.alura.gerenciador_pedidos;

import br.com.alura.gerenciador_pedidos.model.Categoria;
import br.com.alura.gerenciador_pedidos.model.Pedido;
import br.com.alura.gerenciador_pedidos.model.Produto;
import br.com.alura.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.gerenciador_pedidos.repository.FornecedorRepository;
import br.com.alura.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		//1 - Crie uma consulta que retorne os produtos com preço maior que um valor
		Double valor = 3000.;
		List<Produto> produtos = produtoRepository.produtosPrecoMaiorQue(valor);
		System.out.println("1 - Crie uma consulta que retorne os produtos com preço maior que um valor");
		System.out.println(String.format("Produtos cujo valor é maior que %,2.2f", valor));
		produtos.forEach(p-> System.out.println(String.format("Produto: %s, valor: %,3.2f",p.getNome(), p.getPreco())));
		System.out.println("");

		System.out.println("2 - Crie uma consulta que retorne os produtos ordenados pelo preço crescente.");
		produtos = produtoRepository.produtosPorPreco(Sort.by("preco"));
		System.out.println("Produtos ordanados pelo preço: ");
		produtos.forEach(p-> System.out.println(String.format("Produto: %s, valor: %,3.2f",p.getNome(), p.getPreco())));
		System.out.println();

		System.out.println("3 - Crie uma consulta que retorne os produtos ordenados pelo preço decrescente.");
		produtos = produtoRepository.produtosPorPreco(Sort.by("preco").descending());
		System.out.println("Produtos ordanados pelo preço decrescente: ");
		produtos.forEach(p-> System.out.println(String.format("Produto: %s, valor: %,3.2f",p.getNome(), p.getPreco())));
		System.out.println();

		System.out.println("4 - Crie uma consulta que retorne os produtos que comecem com uma letra específica.");
		String starts="N";
		System.out.println(String.format("Produtos que começam com : ", starts));
		produtos = produtoRepository.produtosComecamPor(starts);
		produtos.forEach(p-> System.out.println(String.format("Produto: %s, valor: %,3.2f",p.getNome(), p.getPreco())));
		System.out.println();

		System.out.println("5 - Crie uma consulta que retorne os pedidos feitos entre duas datas.");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate inicio = LocalDate.parse("24/03/2025", formatter);
		LocalDate fim = inicio;//.plusDays(1L);

		System.out.println(String.format("Peidos feitos entre %s e %s: ", inicio.format(formatter), fim.format(formatter)));
		List<Pedido> pedidos = pedidoRepository.pedidosEntreDatas(inicio, fim);
		pedidos.forEach(p-> System.out.println(String.format("Pedido: %d, valor: %s.", p.getId(), p.getData().format(formatter))));
		System.out.println();

		System.out.println("6 - Crie uma consulta que retorne a média de preços dos produtos.");
		Object media = produtoRepository.mediaPrecoProduto();
		System.out.println(String.format("Média do preço dos produtos: %,3.2f.", media));
		System.out.println();

		System.out.println("7 - Crie uma consulta que retorne o preço máximo de um produto em uma categoria");
		Long categoria_id=1L;
		Optional<Categoria> c = categoriaRepository.findById(categoria_id);
		if(c.isPresent()) {
			Object valorMaximo = produtoRepository.valorMáximoProdutoPorCategoria(c.get());
			System.out.println(String.format("O maior valor  de um produto da categoria %s é %,3.2f.", c.get().getNome(), valorMaximo));
		}else{
			System.out.println("Categoria não encontrada.");
		}
		System.out.println();

		System.out.println("8 - Crie uma consulta para contar o número de produtos por categoria.");
		List<Object[]> totais = produtoRepository.totalProdutosPorCategoria();
		System.out.println("Total de produtos por categoria: ");
		totais.forEach(o-> System.out.println(String.format("Categoria %s tem %d produtos.",o[1], o[0])));
		System.out.println();

		System.out.println("9 - Crie uma consulta para filtrar categorias com mais de 10 produtos.");
		Long totalProdutosMaior = 1L;
		totais = produtoRepository.totalProdutosPorCategoriaMaiorQue(totalProdutosMaior);
		System.out.println("Total de produtos por categoria maior que: ");
		totais.forEach(o-> System.out.println(String.format("Categoria %s tem %d produtos.",o[1], o[0])));
		System.out.println();


		System.out.println("10 - Crie uma consulta para retornar os produtos filtrados por nome ou por categoria.");
		String nome="java";
		produtos = produtoRepository.produtosPorNomeOuCategoria(nome, c.get());
		System.out.println(String.format("Produtos por nome %s ou categoria %s: ", nome, c.get().getNome()));
		produtos.forEach(p-> System.out.println(String.format("Produto: %s, categoria: %s.",
				p.getNome(),c.get().getNome())));
		System.out.println();

		System.out.println("11 - Crie uma consulta nativa para buscar os cinco produtos mais caros");
		Long limit = 2L;
		System.out.println(String.format("Top %d produto(s) mai(s) caro(s): ", limit));
		produtos = produtoRepository.produtosMaisCarosSQLNativo(limit);
		produtos.forEach(p-> System.out.println(String.format("Produto: %s, preço: %,3.2f.",
				p.getNome(), p.getPreco())));
		System.out.println();

		//System.out.println("Entrando na aplicação.");
		//var nome = "livro";
		//1 - Retorne todos os produtos com o nome exato fornecido.
		//List<Produto> produtos = produtoRepository.findByNomeIgnoreCase(nome);
		//--List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);

		//2 - Retorne todos os produtos associados a uma categoria específica.
		//List<Produto> produtos = produtoRepository.findByCategoria_Id(1);
		//System.out.println(String.format("Lista dos produtos cujo nome são semelhantes a %s:", nome));

		//3 - Retorne produtos com preço maior que o valor fornecido.
		//Double preco = 10.;
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
		//Long categoria_id = 2L;
		//Optional<Categoria> categoria = categoriaRepository.findById(categoria_id);
		//if(categoria.isPresent()) {
			//List<Produto> produtos = produtoRepository.findByCategoria_IdOrderByPreco(categoria_id);
			//List<Produto> produtos = produtoRepository.findByCategoriaOrderByPrecoDesc(categoria.get());
			//Long totalProdutos = produtoRepository.countByCategoria(categoria.get());
			//System.out.println(String.format("Total de produtos da categoria %s: %d.: ", categoria.get().getNome(), totalProdutos));
			//produtos.forEach(p -> System.out.println(
			//		String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));
		//}else{
		//	System.out.println(String.format("Categoria %d não encontrada.", categoria_id));
		//}

		//System.out.println();
		//Long totalProdutos = produtoRepository.countByPrecoGreaterThan(preco);
		//System.out.println(String.format("Total de produtos cujo preço é maior que %,2.2f: %d.", preco, totalProdutos));

		//System.out.println("");
		//System.out.println("Os 3 produtos mais caros: ");
		//List<Produto> produtos = produtoRepository.findTop2ByOrderByPrecoDesc();
		//produtos.forEach(p -> System.out.println(
		//		String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));

		//System.out.println("");
		//System.out.println("Os 3 produtos mais baratos: ");
		//produtos = produtoRepository.findTop2ByOrderByPreco();
		//produtos.forEach(p -> System.out.println(
		//		String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));

		//System.out.println();
		//if(categoria.isPresent()) {
		//	produtos = produtoRepository.findTop1ByCategoriaOrderByPreco(categoria.get());
		//	System.out.println(String.format("Produtos mais baratos da categoria %s: ", categoria.get().getNome()));
		//	produtos.forEach(p -> System.out.println(
		//			String.format(" - Produto %s, preco: %,3.2f.", p.getNome(),	p.getPreco())));
		//}else{
		//	System.out.println(String.format("Categoria %d não encontrada.", categoria_id));
		//}
		//System.out.println("\nSaindo na aplicação.");
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
