package com.umss.siiu.filestorage.repository;

import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;

import java.util.List;

public interface FileTypeRepository extends GenericRepository<FileType> {
    FileType findByAbbreviation(String abbreviation);

    List<FileType> findByFileTypeCategoryOrderByName(FileTypeCategory fileTypeCategory);
}
