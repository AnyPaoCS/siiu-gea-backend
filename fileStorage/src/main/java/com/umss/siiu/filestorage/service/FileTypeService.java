package com.umss.siiu.filestorage.service;

import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;

import java.util.List;
import java.util.Set;

public interface FileTypeService extends GenericService<FileType> {

    FileType findByAbbreviation(String abbreviation);

    List<FileType> findByFileTypeCategory(FileTypeCategory fileTypeCategory);

    List<FileType> findByFileTypeCategory(String fileTypeCategory);

    Set<FileType> getFileTypesByJobIdAndCategory(long jobId, FileTypeCategory fileTypeCategory);
}
