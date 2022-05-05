package com.umss.siiu.core.repository.specifications;

import com.umss.siiu.core.model.ModelBase;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class GenericSpecificationsBuilder<E extends ModelBase> {

    protected static final String BLANK = "";
    protected static final String ID = "id";
    protected static final String SPACER = ":";
    protected static final String SEPARATOR = ",";

    protected static final String FILTER_PATTERN = "(\\w+)(:gt:|:gte:|:lt:|:lte:|:eq:)((\\w+)|(\\w+ \\w+))" + SEPARATOR;

    protected final List<SearchCriteria> params;

    public GenericSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public GenericSpecificationsBuilder(String filter) {
        params = new ArrayList<>();
        this.getQueryOf(filter);
    }

    public GenericSpecificationsBuilder<E> with(SearchCriteria criteria) {
        if (criteria != null) {
            params.add(criteria);
        }
        return this;
    }

    public GenericSpecificationsBuilder<E> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<E> build() {
        if (params.isEmpty()) {
            return null;
        }
        return buildQuery();
    }

    public Specification<E> build(String filter) {
        return getQueryOf(filter);
    }

    protected Specification<E> getQueryOf(String filter) {
        if (filter != null) {
            mapSearchCriteria(filter);
        }
        return buildQuery();
    }

    private void mapSearchCriteria(String filter) {
        var pattern = Pattern.compile(FILTER_PATTERN, Pattern.UNICODE_CHARACTER_CLASS);
        var matcher = pattern.matcher(filter + SEPARATOR);
        while (matcher.find()) {
            with(matcher.group(1), matcher.group(2).replace(SPACER, BLANK), matcher.group(3));
        }
    }

    protected Specification<E> buildQuery() {
        List<Specification<E>> specs = getSpecification();
        if (!specs.isEmpty()) {
            return extractSpecificationOf(specs);
        }
        return new NoFilterSpecification<>(); // Returns all values
    }

    protected Specification<E> extractSpecificationOf(List<Specification<E>> specs) {
        Specification<E> result = specs.get(0);
        for (var i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }

    protected List<Specification<E>> getSpecification() {
        return params.stream().map(this::parseSearchCriteria).collect(Collectors.toList());
    }

    protected Specification<E> parseSearchCriteria(SearchCriteria criteria) {
        return new GenericSpecification<>(criteria);
    }
}
