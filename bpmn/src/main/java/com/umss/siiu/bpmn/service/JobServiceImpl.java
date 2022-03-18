/**
 * @author: Edson A. Terceros T.
 */
package com.umss.siiu.bpmn.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.bpmn.repository.JobRepository;
import com.umss.siiu.bpmn.repository.spceifications.JobSpecificationsBuilder;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JobServiceImpl extends GenericServiceImpl<Job> implements JobService {

    private static final String ENGAGEMENT_LETTER_ABBREVIATION = "EL";
    private static final String JOB_QUERY_FILTER = "jobQueryFilter";
    private static final String LOWER_DATE = "lowerDate";
    private static final String NONE_FILE_TYPE_ABBREVIATION = "None";
    private static final String CORRESPONDENCE = "Correspondence";
    private static final String OTHER = "Other";
    private static final int ROOT_DEEPTH = 3;
    private static final String PATH_SEPARATOR = "/";
    private static final int EFFECTIVE_PATH_ORDINAL = 4;
    private static final String DATE_ENTERED = "date_entered";
    private static final String JOB_NAME = "name";
    private static final String STAR_LITERAL = "star";
    private JobRepository repository;

    private ConfigurationService configurationService;
    private JobBpmService jobBpmService;
    //todo typo
    @Value("${maecre.batchZise:5}")
    private Integer batchZise;

    public JobServiceImpl(JobRepository repository,
            ConfigurationService configurationService,
            @Lazy JobBpmService jobBpmService) {
        this.repository = repository;
        this.configurationService = configurationService;
        this.jobBpmService = jobBpmService;
    }


    private Date getDateFromString(String dateEntered) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result = null;
        try {
            result = dateFormat.parse(dateEntered);
        } catch (ParseException e) {
            logger.error("Error parsing job entered date", e);
        }
        return result;
    }

    @Override
    public GenericRepository<Job> getRepository() {
        return repository;
    }

    @Override
    public Page<Job> findAll(String filter, Pageable pageable) {
        return repository.findAll(getQueryFrom(filter), pageable);
    }

    @Override
    protected Specification<Job> getQueryFrom(String filter) {
        return new JobSpecificationsBuilder().build(filter);
    }

    @Override
    public Job findById(Long id) {
        Job job = super.findById(id);
        job.setJobBpm(jobBpmService.findByJobId(job.getId()));
        return job;
    }

}
