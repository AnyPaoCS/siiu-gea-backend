/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.service;

import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.filestorage.dto.JackRabbitNodeDto;
import com.umss.siiu.filestorage.model.FileType;
import com.umss.siiu.filestorage.model.JackRabbitNode;

import javax.jcr.Node;
import java.util.List;

public interface JackRabbitNodeService extends GenericService<JackRabbitNode> {

    JackRabbitNode createJackRabbitNode(Object owner, long fileTypeCode, boolean isFolder, String fileName,
                                        String description, Long ownerId, Node node, String parenthPath, boolean flush);

    List<JackRabbitNode> findByOwnerClassAndFileTypeIdAndOwnerId(Object owner, List<Long> fileTypesId, Long ownerId);

    List<JackRabbitNode> findByOwnerClassAndOwnerIdAndFileTypeIn(Object owner, List<FileType> fileTypes, Long ownerId);

    List<JackRabbitNode> findByOwnerClassAndFileCategoryTypeAndOwnerId(Object owner, String category, Long ownerId);

    List<JackRabbitNode> findByOwnerClassAndFileCategoryTypeAndOwnerIds(Object owner, String category,
                                                                        List<Long> ownerIds);

    List<JackRabbitNode> findByOwnerClassAndOwnerId(Object owner, Long ownerId);

    void updateNode(JackRabbitNodeDto jackRabbitNodeDto);

    void deleteByPath(String path);

    List<JackRabbitNode> findByOwnerClassAndFileTypeIdsAndOwnerIds(Object owner, List<Long> fileTypeIds,
                                                                   List<Long> ownerIds);

    JackRabbitNode findByFilePath(String filePath);

    JackRabbitNode findByOwnerIdAndFileTypeId(Long ownerId, Long fileTypeId);

    List<JackRabbitNode> findByOwnerId(Long ownerId);

}
