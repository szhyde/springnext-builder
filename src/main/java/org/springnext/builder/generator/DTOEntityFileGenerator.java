package org.springnext.builder.generator;

import java.io.File;
import java.util.List;

import org.springnext.builder.entity.Class;
import org.springnext.builder.entity.ProjectInfo;
import org.springnext.builder.entity.Table;
import org.springnext.builder.utils.FileUtils;
import org.springnext.builder.utils.StringUtils;

/**
 * Hibernate持久层代码生成器.
 *
 * @author 李程鹏
 */
public class DTOEntityFileGenerator {
    /**
     * 表格集合
     */
    private List<Table> tables;

    /**
     * <strong>Description:</strong>
     * <pre>
     * 构造实例化生成器.
     * </pre>
     *
     * @param tables 表格集合
     */
    public DTOEntityFileGenerator(List<Table> tables) {
        // 赋值
        this.tables = tables;
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 生成代码.
     * </pre>
     */
    public void generate(ProjectInfo projectInfo) throws Exception {
        // 遍历表格集合
        for (Table table : tables) {
            // 生成实体类文件
            generatePO(table,projectInfo);
        }
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 根据表格对象生成实体类文件.
     * </pre>
     *
     * @param table 表格对象
     */
    private void generatePO(Table table,ProjectInfo projectInfo) throws Exception {
        // 获取生成的实体类
        Class entity = new POJOGenerator(table,projectInfo).generate();
        
        String filePath = projectInfo.getAbsoluteModelNamePackagePath()+"dto"+File.separator;
        // 根据实体类内容生成文件
        FileUtils.generateFile(filePath, StringUtils.getJavaFileName(entity), entity.toString(0));
    }

}