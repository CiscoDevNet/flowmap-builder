package com.appdynamics.tools.util;

import org.slf4j.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class DefaultJarPathResolver {
    public Logger logger;

    public DefaultJarPathResolver() {
        logger = new SysOutMinimalLogger();
    }

    public DefaultJarPathResolver(Logger logger) {
        this.logger = logger;
    }

    public File resolveDirectory(Class clazz) {
        File installDir = null;
        try {
            ProtectionDomain pd = clazz.getProtectionDomain();
            if (pd != null) {
                CodeSource cs = pd.getCodeSource();
                if (cs != null) {
                    URL url = cs.getLocation();
                    if (url != null) {
                        String path = URLDecoder.decode(url.getFile(), "UTF-8");
                        if(path.contains("!")){
                            path = path.substring(0, path.indexOf("!"));
                        }
                        path = CsStringUtils.trimLeading(path, "file:");
                        if (path.startsWith("/")) {
                            path = "/" + CsStringUtils.trimLeading(path, "/");
                        }
                        logger.info("Attempting to resolve the path " + path);
                        File dir = new File(path).getParentFile();
                        if (dir.exists()) {
                            installDir = dir;
                        } else {
                            logger.error("Install dir resolved to " + dir.getAbsolutePath() + ", however it doesnt exist.");
                        }
                    }
                } else {
                    logger.warn("Cannot resolve path for the class {} since CodeSource is null", clazz.getName());
                }

            }
        } catch (Exception e) {
            logger.error("Error while resolving the Install Dir", e);
        }
        if (installDir != null) {
            logger.info("Install dir resolved to " + installDir.getAbsolutePath());
            return installDir;
        } else {
            File workDir = new File("");
            logger.info("Failed to resolve install dir, returning current work dir" + workDir.getAbsolutePath());
            return workDir;
        }
    }

    public File getFile(String path, Class clazz) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        if (file.exists()) {
            return new File(path);
        }

        File installDir = resolveDirectory(clazz);
        if (installDir != null) {
            logger.debug("The install directory is resolved to {}", installDir.getAbsolutePath());
            file = new File(installDir, path);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public File getFile(String path, File installDir) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        if (file.exists()) {
            return new File(path);
        }

        if (installDir != null) {
            logger.debug("The install directory is resolved to {}", installDir.getAbsolutePath());
            file = new File(installDir, path);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    public File getFile(String path, String parent) {
        return null;
    }
}