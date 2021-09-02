package top.dpdaidai.mn.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author chenpantao
 * @Date 9/2/21 11:22 AM
 * @Version 1.0
 */
public class FileSystemResource implements Resource {

    private final String path;
    private final File file;

    public FileSystemResource(String path) {
        this.path = path;
        this.file = new File(path);
    }


    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";
    }
}
