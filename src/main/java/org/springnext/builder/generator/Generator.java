package org.springnext.builder.generator;

import java.util.HashMap;
import java.util.Map;

import org.springnext.builder.entity.Column;
import org.springnext.builder.entity.Field;
import org.springnext.builder.entity.JavaFile;
import org.springnext.builder.entity.Method;
import org.springnext.builder.entity.Parameter;
import org.springnext.builder.entity.ProjectInfo;
import org.springnext.builder.entity.Table;
import org.springnext.builder.utils.StringUtils;

/**
 * 生成器抽象类.
 *
 * @author HyDe
 */
public abstract class Generator {
    /**
     * 数据库表
     */
    protected Table table;
    
    protected ProjectInfo projectInfo;

    /**
     * <strong>Description:</strong>
     * <pre>
     * 构造初始化生成器.
     * </pre>
     *
     * @param table 表
     */
    public Generator(Table table,ProjectInfo projectInfo) {
        // 赋值
        this.table = table;
        this.projectInfo = projectInfo;
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 获取驼峰样式的表格名.
     * </pre>
     *
     * @return {@code java.lang.String} - 表格名
     */
    protected String getTableName() {
        // 返回大驼峰样式的表格名
        return StringUtils.toCamelCase(table.getName().replaceAll(projectInfo.getTablePrefix(), ""), true);
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 获取驼峰样式的列名.
     * </pre>
     *
     * @param column 列对象
     * @return {@code java.lang.String} - 列名
     */
    protected String getColumnName(Column column) {
        // 返回小驼峰样式的列名
        return StringUtils.toCamelCase(column.getName(), false);
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 为属性生成文档注释.
     * </pre>
     *
     * @param field    属性
     * @param document 文档注释
     */
    protected void generateFieldDocument(Field field, String document) {
        // 只有文档注释不为空是才添加
        if (StringUtils.isNotEmpty(document)) {
            field.addDocument("/**");
            // 将注释的前后空格去掉再添加
            field.addDocument(" * " + document.trim());
            field.addDocument(" */");
        }
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 添加方法的文档注释.
     * </pre>
     *
     * @param method    方法
     * @param statement 文档注释
     * @param map       属性的文档注释集合
     */
    protected void generateMethodDocument(Method method, String statement, Map<String, String> map) {
        method.addDocument("/**");
        method.addDocument(" * " + statement);
        // 遍历方法中的属性
        for (Parameter parameter : method.getParameters()) {
            // 获取属性的名字
            String parameterName = parameter.getName();
            // 添加对属性的注释
            method.addDocument(" * @param " + parameterName + " " + map.get(parameterName));
        }
        method.addDocument(" */");
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 添加文件文档注释.
     * </pre>
     *
     * @param javaFile java文件
     * @param document 文档注释
     */
    protected void generateFileDocument(JavaFile javaFile, String document) {
        javaFile.addDocument("/**");
        // 如果注释不为空
        if (StringUtils.isNotEmpty(document)) {
            // 添加去掉前后空格的注释
            javaFile.addDocument(" * " + document.trim());
            javaFile.addDocument(" * ");
        }
        // 添加文件编写作者
        javaFile.addDocument(" * @author "+projectInfo.getAuthor());
        javaFile.addDocument(" */");
    }

    /**
     * <strong>Description:</strong>
     * <pre>
     * 生成代码.
     * </pre>
     *
     * @return {@code java.lang.Object} - 生成的代码字符串
     */
    protected abstract Object generate();

    /**
     * <strong>Description:</strong>
     * <pre>
     * 生成注释(这个方法不通用,只是为了减少冗余代码而抽取出来的).
     * </pre>
     *
     * @param method    方法
     * @param methodDoc 方法的文档注释
     * @param paramName 参数名
     * @param paramDoc  参数的文档注释
     */
    protected void generateDocument(Method method, String methodDoc, String paramName, String paramDoc) {
        // 新建一个映射对象
        Map<String, String> map = new HashMap<>();
        // 将参数名和参数的注释映射关联起来
        map.put(paramName, paramDoc);
        // 添加方法的文档注释
        generateMethodDocument(method, methodDoc, map);
    }

}