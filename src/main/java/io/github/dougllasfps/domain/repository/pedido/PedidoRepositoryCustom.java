package io.github.dougllasfps.domain.repository.pedido;

import io.github.dougllasfps.domain.repository.filter.PedidoFilter;
import io.github.dougllasfps.rest.dto.PedidoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepositoryCustom {

    Page<PedidoDTO> resumo(PedidoFilter filter, Pageable pageable);
}
