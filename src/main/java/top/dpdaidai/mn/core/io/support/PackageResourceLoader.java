package top.dpdaidai.mn.core.io.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.dpdaidai.mn.core.io.FileSystemResource;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.util.Assert;
import top.dpdaidai.mn.util.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author chenpantao
 * @Date 9/14/21 9:26 PM
 * @Version 1.0
 */
public class PackageResourceLoader {

    private static final Log logger = LogFactory.getLog(PackageResourceLoader.class);

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader, "ResourceLoader must not be null");
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public Resource[] getResources(String basePackage) {
        Assert.notNull(basePackage, "basePackage  must not be null");
        //将类路径转换为文件路径
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);
        ClassLoader classLoader = getClassLoader();

        //类加载器加载资源文件 , url 的值已经是target内的.class文件了
        URL url = classLoader.getResource(location);
        //根目录
        File rootDir = new File(url.getFile());

        Set<File> matchingFiles = retrieveMatchingFiles(rootDir);
        Resource[] resources = new Resource[matchingFiles.size()];

        int i = 0;
        for (File matchingFile : matchingFiles) {
            resources[i++] = new FileSystemResource(matchingFile);
        }

        return resources;
    }

    protected Set<File> retrieveMatchingFiles(File rootDir) {

        //文件夹不存在
        if (!rootDir.exists()) {
            // Silently skip non-existing directories.
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
            }
            return Collections.emptySet();
        }
        //不是一个文件夹
        if (!rootDir.isDirectory()) {
            // Complain louder if it exists but is no directory.
            if (logger.isWarnEnabled()) {
                logger.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
            }
            return Collections.emptySet();
        }
        //文件夹不可读
        if (!rootDir.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() +
                        "] because the application is not allowed to read the directory");
            }
            return Collections.emptySet();
        }

        Set<File> result = new LinkedHashSet<File>(8);
        doRetrieveMatchingFiles(rootDir, result);
        return result;

    }

    protected void doRetrieveMatchingFiles(File dir, Set<File> result) {

        File[] dirContents = dir.listFiles();

        if (dirContents == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
            }
            return;
        }

        for (File dirContent : dirContents) {
            if (dirContent.isDirectory()) {
                if (dirContent.canRead()) {
                    doRetrieveMatchingFiles(dirContent, result);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() +
                                "] because the application is not allowed to read the directory");
                    }
                }
            } else {
                result.add(dirContent);
            }
        }

    }

}
