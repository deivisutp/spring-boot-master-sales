package io.github.dougllasfps.service.impl;

import io.github.dougllasfps.domain.entity.Pedido;
import io.github.dougllasfps.domain.repository.filter.PedidoFilter;
import io.github.dougllasfps.domain.repository.pedido.PedidoRepositoryCustom;
import io.github.dougllasfps.rest.dto.PedidoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PedidoRepositoryImpl implements PedidoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<PedidoDTO> resumo(PedidoFilter filter, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PedidoDTO> criteria = builder.createQuery(PedidoDTO.class);
        Root<Pedido> pedido = criteria.from(Pedido.class);
        From<?, ?> cliente = pedido.join("cliente_id", JoinType.INNER);

        criteria.select(builder.construct(PedidoDTO.class,
            pedido.get("cliente"), pedido.get("total"), pedido.get("itens")));


        Predicate[] where = criarRestricoes(filter, builder, pedido, cliente);

        Sort sort = pageable.getSort();
        if (sort != null) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            criteria.orderBy(order.isAscending() ? builder.asc(pedido.get(property))
                                : builder.desc(pedido.get(property)));
        }

        criteria.where(where);
        TypedQuery<PedidoDTO> query = em.createQuery(criteria);
        return new PageImpl<>(query.getResultList(), pageable, total(filter));

    }

    private Predicate[] criarRestricoes(PedidoFilter filter, CriteriaBuilder builder, Root<Pedido> root, From<?, ?> join) {
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(filter.getCodigoCliente())) {
            predicates.add(
                    builder.equal(root.get("cliente_id"),filter.getCodigoCliente())
            );
        }

        if (!StringUtils.isEmpty(filter.getNomeCliente())) {
            predicates.add(
              builder.like(builder.lower(join.get("nome")), "%" + filter.getNomeCliente().toLowerCase(Locale.ROOT) + "%"));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Long total(PedidoFilter filter) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pedido> root = criteria.from(Pedido.class);
        From<?, ?> clienteJoin = root.join("cliente_id", JoinType.INNER);

        Predicate[] predicates = criarRestricoes(filter, builder, root, clienteJoin);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return em.createQuery(criteria).getSingleResult();
    }
}
