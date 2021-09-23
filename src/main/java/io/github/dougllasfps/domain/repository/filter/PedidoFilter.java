package io.github.dougllasfps.domain.repository.filter;

import io.github.dougllasfps.rest.dto.ItemPedidoDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PedidoFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer codigoCliente;
    private String nomeCliente;
    private BigDecimal total;
    private List<ItemPedidoDTO> items;


}
