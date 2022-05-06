package com.umss.siiu.bpmn.repository.spceifications;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.model.processes.ResourceInstance;
import com.umss.siiu.core.model.Employee;
import com.umss.siiu.core.repository.specifications.GenericSpecification;
import com.umss.siiu.core.repository.specifications.GenericSpecificationsBuilder;
import com.umss.siiu.core.repository.specifications.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class JobSpecificationsBuilder extends GenericSpecificationsBuilder<Job> {

    private static final String FIRSTNAME_FILTER = "firstName";
    private static final String LASTNAME_FILTER = "lastName";
    private static final String STATUS_FILTER = "status";
    private static final String PRIORITY_FILTER = "priority";

    private static final String JOB_BPM_KEY = "jobBpm";
    private static final String PROCESS_KEY = "processInstance";
    private static final String TASKS_KEY = "taskInstances";
    private static final String RESOURCES_KEY = "resourceInstances";
    private static final String EMPLOYEE_KEY = "employee";

    @Override
    protected Specification<Job> parseSearchCriteria(SearchCriteria criteria) {
        switch (criteria.getKey()) {
            case FIRSTNAME_FILTER:
                return joinAssignee(FIRSTNAME_FILTER, getSearchCriteriaValue(criteria));
            case LASTNAME_FILTER:
                return joinAssignee(LASTNAME_FILTER, getSearchCriteriaValue(criteria));
            case STATUS_FILTER:
                return joinBPM(STATUS_FILTER, getSearchCriteriaValue(criteria));
            case PRIORITY_FILTER:
                return joinBPM(PRIORITY_FILTER, getSearchCriteriaValue(criteria));
            default:
                return new GenericSpecification<>(criteria);

        }
    }

    private String getSearchCriteriaValue(SearchCriteria criteria) {
        try {
            return (String) criteria.getValue();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Cannot process specification", e);
        }
    }

    private static Specification<Job> joinAssignee(String type, String value) {
        return new Specification<Job>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);
                Join<ResourceInstance, Employee> resourcesEmployee = getJoinedTable(root);
                return builder.like(resourcesEmployee.<String>get(type), "%" + value + "%");
            }

            private Join<ResourceInstance, Employee> getJoinedTable(Root<Job> root) {
                return root.join(JOB_BPM_KEY).join(PROCESS_KEY).join(TASKS_KEY).join(RESOURCES_KEY).join(EMPLOYEE_KEY);
            }
        };
    }

    private static Specification<Job> joinBPM(String type, String value) {
        return new Specification<Job>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);
                Join<ResourceInstance, Employee> resourcesEmployee = getJoinedTable(root);
                return builder.like(resourcesEmployee.<String>get(type), "%" + value + "%");
            }

            private Join<ResourceInstance, Employee> getJoinedTable(Root<Job> root) {
                return root.join(JOB_BPM_KEY);
            }
        };
    }
}
