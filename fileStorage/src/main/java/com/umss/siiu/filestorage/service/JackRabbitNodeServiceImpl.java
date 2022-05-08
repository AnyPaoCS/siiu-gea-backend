/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.service;

import com.umss.siiu.core.exceptions.NotFoundException;
import com.umss.siiu.core.exceptions.RepositoryException;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.service.GenericServiceImpl;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.FileTypeCategory;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import com.umss.siiu.filestorage.repository.JackRabbitNodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jcr.Node;
import java.util.List;
import java.util.Optional;

@Service
public class JackRabbitNodeServiceImpl extends GenericServiceImpl<JackRabbitNode> implements JackRabbitNodeService {

    private final JackRabbitNodeRepository repository;

    public JackRabbitNodeServiceImpl(JackRabbitNodeRepository repository) {
        this.repository = repository;
    }

    @Override
    protected GenericRepository<JackRabbitNode> getRepository() {
        return repository;
    }

    @Override
    public JackRabbitNode createJackRabbitNode(Object owner, long fileTypeCode, boolean isFolder, String fileName,
                                               String description,
                                               Long ownerId, Node node, String parentPath, boolean flush) {
        try {
            var jackRabbitNode = new JackRabbitNode();
            var fileType = new FileType();
            fileType.setId(fileTypeCode);
            jackRabbitNode.setFileType(fileType);
            jackRabbitNode.setFolder(isFolder);
            jackRabbitNode.setOwnerClass(owner.getClass().getCanonicalName());
            jackRabbitNode.setOwnerId(ownerId);
            jackRabbitNode.setDescription(description);
            jackRabbitNode.setNodeId(node.getIdentifier());
            jackRabbitNode.setPath(node.getPath());
            jackRabbitNode.setFileName(fileName);
            jackRabbitNode.setParentPath(parentPath);
            jackRabbitNode.setPermanent(false);
            jackRabbitNode.setVerified(false);
            return flush ? saveAndFlush(jackRabbitNode) : save(jackRabbitNode);
        } catch (javax.jcr.RepositoryException e) {
            throw new RepositoryException("Error retrieving node information", e);
        }
    }

    @Override
    public List<JackRabbitNode> findByOwnerClassAndFileTypeIdAndOwnerId(Object owner, List<Long> fileTypesId,
                                                                        Long ownerId) {
        return repository.findByOwnerClassAndFileTypeIdInAndOwnerId(owner.getClass().getCanonicalName(), fileTypesId,
                ownerId);
    }

    @Override
    public List<JackRabbitNode> findByOwnerClassAndFileTypeIdsAndOwnerIds(Object owner, List<Long> fileTypeIds,
                                                                          List<Long> ownerIds) {
        return repository.findByOwnerClassAndFileTypeIdInAndOwnerIdIn(owner.getClass().getCanonicalName(), fileTypeIds,
                ownerIds);
    }

    @Override
    public List<JackRabbitNode> findByOwnerClassAndOwnerIdAndFileTypeIn(Object owner, List<FileType> fileTypes,
                                                                        Long ownerId) {
        return repository.findByOwnerClassAndOwnerIdAndFileTypeIn(owner.getClass().getCanonicalName(), ownerId,
                fileTypes);
    }

    @Override
    public List<JackRabbitNode> findByOwnerClassAndFileCategoryTypeAndOwnerId(Object owner, String category,
                                                                              Long ownerId) {
        return repository.findByOwnerClassAndFileTypeFileTypeCategoryAndOwnerId(owner.getClass().getCanonicalName(),
                FileTypeCategory.valueOf(category), ownerId);
    }

    @Override
    public List<JackRabbitNode> findByOwnerClassAndFileCategoryTypeAndOwnerIds(Object owner, String category,
                                                                               List<Long> ownerIds) {
        return repository.findByOwnerClassAndFileTypeFileTypeCategoryAndOwnerIdIn(owner.getClass().getCanonicalName(),
                FileTypeCategory.valueOf(category), ownerIds);
    }

    @Override
    public List<JackRabbitNode> findByOwnerClassAndOwnerId(Object owner, Long ownerId) {
        return repository.findByOwnerClassAndOwnerId(owner.getClass().getCanonicalName(), ownerId);
    }

    @Override
    public void updateNode(JackRabbitNodeDto jackRabbitNodeDto) {
        Optional<JackRabbitNode> optional = repository.findById(jackRabbitNodeDto.getId());
        if (optional.isPresent()) {
            var jackRabbitNode = optional.get();
            jackRabbitNode.getFileType().setId(jackRabbitNodeDto.getFileTypeId());
            jackRabbitNode.setDescription(jackRabbitNodeDto.getDescription());
            repository.save(jackRabbitNode);
        } else {
            throw new NotFoundException("File not found");
        }
    }

    @Override
    @Transactional
    public void deleteByPath(String path) {
        repository.deleteByPath(path);
    }

    @Override
    public JackRabbitNode findByFilePath(String filePath) {
        return repository.findByPath(filePath);
    }

    @Override
    public JackRabbitNode findByOwnerIdAndFileTypeId(Long ownerId, Long fileTypeId) {
        return repository.findByOwnerIdAndFileTypeId(ownerId, fileTypeId);
    }

    @Override
    public List<JackRabbitNode> findByOwnerId(Long ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}
