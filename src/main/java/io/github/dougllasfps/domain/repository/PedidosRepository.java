package io.github.dougllasfps.domain.repository;

import io.github.dougllasfps.domain.entity.Cliente;
import io.github.dougllasfps.domain.entity.Pedido;
import io.github.dougllasfps.domain.repository.pedido.PedidoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PedidosRepository extends JpaRepository<Pedido, Integer>, PedidoRepositoryCustom {

    List<Pedido> findByCliente(Cliente cliente);

    @Query(" select p from Pedido p left join fetch p.itens where p.id = :id ")
    Optional<Pedido> findByIdFetchItens(@Param("id") Integer id);
}
