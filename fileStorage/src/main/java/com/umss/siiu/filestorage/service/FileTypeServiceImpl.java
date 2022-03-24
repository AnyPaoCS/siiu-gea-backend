package com.umss.siiu.filestorage.service;

import com.umss.siiu.bpmn.model.Job;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import com.umss.siiu.filestorage.repository.FileTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileTypeServiceImpl extends GenericServiceImpl<FileType> implements FileTypeService {

    private FileTypeRepository repository;
    private JackRabbitNodeService jackRabbitNodeService;

    public FileTypeServiceImpl(FileTypeRepository repository, JackRabbitNodeService jackRabbitNodeService) {
        this.repository = repository;
        this.jackRabbitNodeService = jackRabbitNodeService;
    }

    @Override
    public FileType findByAbbreviation(String abbreviation) {
        return repository.findByAbbreviation(abbreviation);
    }

    @Override
    public List<FileType> findByFileTypeCategory(FileTypeCategory fileTypeCategory) {
        return repository.findByFileTypeCategoryOrderByName(fileTypeCategory);
    }

    @Override
    public List<FileType> findByFileTypeCategory(String fileTypeCategory) {
        List<FileType> tags = repository.findByFileTypeCategoryOrderByName(FileTypeCategory.valueOf(fileTypeCategory));
        if (null != fileTypeCategory && fileTypeCategory.equals("MAPS")) {
            tags.add(findById(1L));
        }
        return tags;
    }

    @Override
    protected GenericRepository<FileType> getRepository() {
        return repository;
    }

    @Override
    public Set<FileType> getFileTypesByJobIdAndCategory(long jobId, FileTypeCategory fileTypeCategory) {
        // Getting all the file types removing duplicated values
        return jackRabbitNodeService
                .findByOwnerClassAndFileCategoryTypeAndOwnerId(new Job(), fileTypeCategory.toString(), jobId).stream()
                .map(JackRabbitNode::getFileType).collect(Collectors.toSet());
    }

}
