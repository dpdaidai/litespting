package top.dpdaidai.mn.context.suport;

import top.dpdaidai.mn.core.io.FileSystemResource;
import top.dpdaidai.mn.core.io.Resource;

/**
 * @Author chenpantao
 * @Date 9/2/21 3:24 PM
 * @Version 1.0
 */
public class FileSystemXmlApplicationContext extends AbstractApplicationContext {

    public FileSystemXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new FileSystemResource(path);
    }
}
