package top.dpdaidai.mn.core.io;

import top.dpdaidai.mn.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author chenpantao
 * @Date 9/2/21 11:25 AM
 * @Version 1.0
 */
public class ClassPathResource implements Resource {

    private String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    public InputStream getInputStream() throws IOException {
        InputStream resourceAsStream = this.classLoader.getResourceAsStream(path);
        if (resourceAsStream == null) {
            throw new FileNotFoundException(path + " cannot be opened");
        }
        return resourceAsStream;
    }

    public String getDescription() {
        return this.path;
    }
}
