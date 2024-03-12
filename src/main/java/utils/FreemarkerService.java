package utils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class FreemarkerService {

    private final Configuration conf = new Configuration(Configuration.VERSION_2_3_32);

    public FreemarkerService(String root) throws IOException {
        conf.setDirectoryForTemplateLoading(
                new File(ResourceOps.resourceUnsafe(root))
        );
    }

    public void render(String template, HashMap<String, Object> data, Writer w) {
        try {
            conf.getTemplate(template)
                    .process(data, w);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
