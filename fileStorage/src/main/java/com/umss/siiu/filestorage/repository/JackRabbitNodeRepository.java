package com.umss.siiu.filestorage.repository;

import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.model.JackRabbitNode;

import java.util.List;

public interface JackRabbitNodeRepository extends GenericRepository<JackRabbitNode> {

    List<JackRabbitNode> findByOwnerClassAndOwnerIdAndFileTypeIn(String canonicalName, Long ownerId,
            List<FileType> fileTypes);

    List<JackRabbitNode> findByOwnerClassAndFileTypeIdInAndOwnerId(String canonicalName, List<Long> fileTypesId,
            Long ownerId);

    List<JackRabbitNode> findByOwnerClassAndFileTypeIdInAndOwnerIdIn(String canonicalName, List<Long> fileTypeIds,
            List<Long> ownerIds);

    List<JackRabbitNode> findByOwnerClassAndFileTypeFileTypeCategoryAndOwnerId(String canonicalName,
            FileTypeCategory category,
            Long ownerId);

    List<JackRabbitNode> findByOwnerClassAndFileTypeFileTypeCategoryAndOwnerIdIn(String canonicalName,
            FileTypeCategory category,
            List<Long> ownerIds);

    List<JackRabbitNode> findByOwnerClassAndOwnerId(String canonicalName, Long ownerId);

    void deleteByPath(String path);

    JackRabbitNode findByPath(String path);
}
