package br.com.alura.gerenciador_pedidos.repository;

import br.com.alura.gerenciador_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    //Derived Queries
    List<Pedido> findByDataIsNull();
    List<Pedido> findByDataIsNotNull();

    //JPQL Queries
    @Query("SELECT p FROM Pedido p WHERE p.data between :inicio and :fim")
    List<Pedido> pedidosEntreDatas(LocalDate inicio, LocalDate fim);
}
