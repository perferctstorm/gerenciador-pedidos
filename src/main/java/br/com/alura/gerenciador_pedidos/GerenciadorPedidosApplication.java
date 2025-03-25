package br.com.alura.gerenciador_pedidos;

import br.com.alura.gerenciador_pedidos.model.Categoria;
import br.com.alura.gerenciador_pedidos.model.Fornecedor;
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

import java.time.LocalDate;
import java.util.List;

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
	}
}
