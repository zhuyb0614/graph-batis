package org.zhuyb.graphbatis.cg.ext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.TableConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author zhuyb
 */
@Slf4j
public abstract class FreemarkerExtGenerator implements ExtGenerator {
    private String templateDirPath = new File("./src/main/resources/ftl").getAbsolutePath();

    protected void writFile(Template template, Object dataModel, String fileName) {
        Writer out = null;
        try {
            out = new FileWriter(fileName);
            template.process(dataModel, out);
            log.info("write {} success ", fileName);
        } catch (IOException | TemplateException e) {
            log.error("write {} error", fileName, e);
            return;
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log.error("close output error", e);
                }
            }
        }
    }

    @Nullable
    protected Template getTemplate(String templateName) throws IOException {
        Configuration conf = getConfiguration();
        // 加载模板
        try {
            return conf.getTemplate(templateName);
        } catch (IOException e) {
            log.error("load template file {} error", templateName, e);
            throw e;
        }
    }

    @NotNull
    protected Configuration getConfiguration() throws IOException {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_30);
        //加载模板文件(模板的路径)
        try {
            conf.setDirectoryForTemplateLoading(new File(templateDirPath));
        } catch (IOException e) {
            log.error("load template dir {} error", templateDirPath, e);
            throw e;
        }
        return conf;
    }

    protected String getObjectName(IntrospectedTable introspectedTable) {
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        return tableConfiguration.getDomainObjectName();
    }
}
