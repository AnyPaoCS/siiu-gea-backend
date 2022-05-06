/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.filestorage.service;

import com.umss.siiu.core.model.ModelBase;
import com.umss.siiu.filestorage.model.JackRabbitNode;
import com.umss.siiu.filestorage.util.StreamUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.commons.predicate.NtFilePredicate;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.*;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class JackRabbitServiceImpl implements JackRabbitService {
    private static final String ERROR_CREATING_NODE = "Error creating node";
    private static final String ERROR_UPDATING_NODE = "Error updating node";
    private static final String ERROR_DELETING_NODE = "Error deleting node";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jackrabbit.user}")
    private String user;

    @Value("${jackrabbit.password}")
    private String password;

    @Value("${jackrabbit.isStandAlone}")
    private boolean isStandAlone;

    @Value("${jackrabbit.standAlone.host}")
    private String host;

    @Value("${jackrabbit.standAlone.port}")
    private String port;

    private Session session;

    private JackRabbitNodeService jackRabbitNodeService;

    public JackRabbitServiceImpl(JackRabbitNodeService jackRabbitNodeService) {
        this.jackRabbitNodeService = jackRabbitNodeService;
    }

    @PostConstruct
    private Session login() {
        if (null == session) {
            Repository repository;
            try {
                if (isStandAlone) {
                    repository = new URLRemoteRepository(String.format("%s:%s/rmi", host, port));
                } else {
                    repository = JcrUtils.getRepository();
                }
                signIntoRepository(repository);
            } catch (MalformedURLException | RepositoryException e) {
                throw new IllegalStateException("Content repository is not available.");
            }
        }
        return session;
    }

    private void signIntoRepository(Repository repository) {
        final Credentials credentials = new SimpleCredentials(user, password.toCharArray());
        try {
            session = repository.login(credentials);
            String userId = session.getUserID();
            String name = repository.getDescriptor(Repository.REP_NAME_DESC);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Logged in as %s to a %s repository.", userId, name));
            }
        } catch (RepositoryException e) {
            throw new IllegalStateException("The provided credentials are incorrect.");
        }
    }

    @PreDestroy
    private Session release() {
        if (session != null && session.isLive()) {
            session.logout();
        }
        session = null;
        return null;
    }

    @Override
    public void save() {
        try {
            session.save();
        } catch (RepositoryException e) {
            throw new IllegalStateException("Error saving session", e);
        }
    }

    @Override
    public void checkin(Node node) throws RepositoryException {
        VersionManager vManager = session.getWorkspace().getVersionManager();
        vManager.checkin(node.getPath());
    }

    @Override
    public void checkout(Node node) throws RepositoryException {
        VersionManager vManager = session.getWorkspace().getVersionManager();
        vManager.checkout(node.getPath());
    }

    @Override
    public Node getRootNode() {
        try {
            login();
            return session.getRootNode();
        } catch (RepositoryException e) {
            try {
                session = null;
                login();
                return session.getRootNode();
            } catch (Exception ex) {
                throw new IllegalStateException("The session is not active.");
            }
        }
    }

    /**
     * Returns a node given a path
     *
     * @param path a string that represents a path of a graph where the path is
     *             build with pattern: node and /. ie: node1/node11/node12 where
     *             node11 is the name of the child node of node1
     * @return a Node given a path.
     */
    @Override
    public Node getNode(String path) {
        if (!StringUtils.hasText(path)) {
            return getRootNode();
        }
        try {
            return getRootNode().getNode(path);
        } catch (RepositoryException e) {
            throw new IllegalStateException("The node path is invalid.");
        }
    }

    @Override
    public Property getProperty(String path, String property) {
        final Node node = getNode(path);
        try {
            return node.getProperty(property);
        } catch (RepositoryException e) {
            throw new IllegalStateException("Property is invalid.");
        }
    }

    @Override
    public Node addBinaryFile(String nodeName, String parentPath, String file, String mimeType) {
        return createBinaryNode(nodeName, parentPath, file, mimeType);
    }

    @Override
    public Node addBinaryFileFromStream(String nodeName, String parentPath, InputStream stream, String mimeType) {
        return createBinaryNodeFromStream(nodeName, parentPath, stream, mimeType);
    }

    @Override
    public Node createBinaryNode(String nodeName, String parentPath, String file, String mimeType) {
        try (InputStream stream = new FileInputStream(file)) {
            return createBinaryNodeFromStream(nodeName, parentPath, stream, mimeType);
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_CREATING_NODE, e);
        }
    }

    @Override
    public Node createBinaryNodeFromStream(String nodeName, String parentPath, InputStream stream, String mimeType) {
        try {
            Node content;
            Binary binary;
            Node parentNode = getNode(parentPath);
            Node fileNode = parentNode.addNode(nodeName, NtFilePredicate.NT_FILE);
            fileNode.addMixin(JcrConstants.MIX_VERSIONABLE);
            content = fileNode.addNode(JcrConstants.JCR_CONTENT, NtFilePredicate.NT_RESOURCE);
            content.addMixin(JcrConstants.MIX_REFERENCEABLE);
            binary = session.getValueFactory().createBinary(stream);
            content.setProperty(JcrConstants.JCR_DATA, binary);
            content.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
            content.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
            session.save();
            checkin(fileNode);
            return fileNode;
        } catch (RepositoryException e) {
            throw new IllegalStateException(ERROR_CREATING_NODE, e);
        }
    }

    @Override
    public void deleteNode(String nodeName, String parentPath) {
        try {
            Node parentNode = getNode(parentPath);
            Node fileNode = parentNode.getNode(nodeName);
            fileNode.remove();
            session.save();
        } catch (RepositoryException e) {
            throw new IllegalStateException(ERROR_DELETING_NODE, e);
        }
    }

    @Override
    public Node createFolderNode(String nodeName, String parentPath) {
        try {
            Node parentNode = getNode(parentPath);
            if (parentNode.hasNode(nodeName)) {
                throw new IllegalStateException(String.format("The node: %s already exists", nodeName));
            }
            Node node = parentNode.addNode(nodeName);
            session.save();
            return node;
        } catch (RepositoryException e) {
            throw new IllegalStateException(ERROR_CREATING_NODE, e);
        }
    }

    @Override
    public Node updateFileNode(String nodeName, String parentPath, String file, String mimeType) {
        try (InputStream stream = new FileInputStream(file)) {
            return updateFileNodeFromStream(nodeName, parentPath, stream, mimeType);
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_UPDATING_NODE, e);
        }
    }

    @Override
    public Node updateFileNodeFromStream(String nodeName, String parentPath, InputStream file, String mimeType) {
        try {
            Node content;
            Binary binary;
            Node fileNode = getNode(parentPath).getNode(nodeName);
            checkout(fileNode);
            content = fileNode.getNode(JcrConstants.JCR_CONTENT);
            binary = session.getValueFactory().createBinary(file);
            content.setProperty(JcrConstants.JCR_DATA, binary);
            content.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
            session.save();
            checkin(fileNode);
            return fileNode;
        } catch (RepositoryException e) {
            throw new IllegalStateException(ERROR_UPDATING_NODE, e);
        }
    }

    @Override
    public Binary getRabbitBinary(OutputStream outputStream) {
        try {
            return session.getValueFactory().createBinary(StreamUtils
                    .toInputStream(outputStream));
        } catch (RepositoryException e) {
            logger.error("Error creating binary from outputStream");
        }
        return null;
    }

    private InputStream obtainFileFromNode(JackRabbitNode jackRabbitNode) {
        try {
            final Node node = getFileNode(jackRabbitNode.getPath());
            final Property property = node.getProperty(JcrConstants.JCR_DATA);
            final Binary bin = property.getBinary();
            return bin.getStream();
        } catch (Exception e) {
            throw new IllegalStateException("The file was not found", e);
        }
    }

    @Override
    public Node getFileNode(String fileNodeName) {
        String path = sanitizePath(fileNodeName);
        try {
            return getRootNode().getNode(path).getNode(Node.JCR_CONTENT);
        } catch (RepositoryException e) {
            throw new IllegalStateException("File repository error.", e);
        }
    }

    @Override
    public String sanitizePath(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

    @Override
    public void downloadBinaryInfo(Node node, String downloadPath) throws RepositoryException, IOException {
        final Binary bin = node.getProperty(JcrConstants.JCR_DATA).getBinary();
        InputStream stream = bin.getStream();
        final byte[] content = IOUtils.toByteArray(stream);
        File file = new File(downloadPath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }
    }

    @Override
    public VersionHistory getVersionHistory(Node node) throws RepositoryException {
        VersionManager versionManager = session.getWorkspace().getVersionManager();
        return versionManager.getVersionHistory(node.getPath());
    }

    @Override
    public VersionIterator getVersions(Node node) throws RepositoryException {
        return getVersionHistory(node).getAllVersions();
    }

    @Override
    public void restore(Version version) throws RepositoryException {
        VersionManager versionManager = session.getWorkspace().getVersionManager();
        versionManager.restore(version, true);
    }

    @Override
    public JackRabbitNode fetchNodeByOwner(ModelBase<?> owner, Long fileType) {
        return fetchFileNode(jackRabbitNodeService.findByOwnerClassAndFileTypeIdAndOwnerId(owner,
                Arrays.asList(fileType), owner.getId()));
    }

    private JackRabbitNode fetchFileNode(List<JackRabbitNode> nodes) {
        if (nodes.iterator().hasNext()) {
            return nodes.iterator().next();
        }
        throw new IllegalStateException("There are no templates");
    }
}
