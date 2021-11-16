package com.umss.siiu.core.repository.specifications;

import com.umss.siiu.core.model.ModelBase;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@SuppressWarnings("rawtypes")
public class NoFilterSpecification<E extends ModelBase> implements Specification<E> {

    private static final long serialVersionUID = 1L;

    private static final String ID = "id";

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.equal(root.get(ID), root.get(ID));
    }
}
