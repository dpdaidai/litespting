package top.dpdaidai.mn.test.v1;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author chenpantao
 * @Date 9/2/21 11:30 AM
 * @Version 1.0
 */
public class ResourceTest {

    @Test
    public void testClassPathResource() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("petstore-v1.xml");

        InputStream inputStream = null;
        try {
            inputStream = classPathResource.getInputStream();
            Assert.assertNotNull(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Test
    public void testFileSystemResource() throws IOException {
        FileSystemResource fileSystemResource = new FileSystemResource("./src/test/resource/petstore-v1.xml");

        InputStream inputStream = null;
        try {
            inputStream = fileSystemResource.getInputStream();
            Assert.assertNotNull(inputStream);
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
