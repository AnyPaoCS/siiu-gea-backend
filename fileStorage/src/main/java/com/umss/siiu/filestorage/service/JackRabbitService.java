/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.service;

import com.umss.siiu.core.exceptions.MyException;
import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.filestorage.model.JackRabbitNode;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface JackRabbitService {
    void save();

    void checkin(Node node) throws RepositoryException;

    void checkout(Node node) throws RepositoryException;

    Node getRootNode();

    Node getNode(String path);

    Property getProperty(String path, String property);

    Node addBinaryFile(String nodeName, String parentPath, String file, String mimeType);

    Node addBinaryFileFromStream(String nodeName, String parentPath, InputStream stream, String mimeType);

    Node createBinaryNode(String nodeName, String parentPath, String file, String mimeType);

    Node createBinaryNodeFromStream(String nodeName, String parentPath, InputStream stream, String mimeType);

    void deleteNode(String nodeName, String parentPath);

    Node createFolderNode(String nodeName, String parentPath);

    Node updateFileNode(String nodeName, String parentPath, String file, String mimeType);

    Node updateFileNodeFromStream(String nodeName, String parentPath, InputStream stream, String mimeType);

//    Node updateFileNode(ModelBase<?> model, String args, JackRabbitNode jackRabbitNode);

    Binary getRabbitBinary(OutputStream outputStream) throws MyException ;

    Node getFileNode(String fileNodeName);

    String sanitizePath(String path);

    void downloadBinaryInfo(Node node, String downloadPath) throws RepositoryException, IOException;

    VersionHistory getVersionHistory(Node node) throws RepositoryException;

    VersionIterator getVersions(Node node) throws RepositoryException;

    void restore(Version version) throws RepositoryException;

    JackRabbitNode fetchNodeByOwner(ModelBase<?> owner, Long fileType);

}
