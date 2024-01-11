package com.example.kechengsheji;

import java.sql.SQLException;
import java.util.List;
/**
 * 通用 JDBC 接口，定义了常见的数据库操作方法。
 *
 * @param <T> 实体类型，表示数据库表的实体类
 * @param <K> 主键类型，表示数据库表的主键类型
 */
public interface JDBC<T,K> {
    /**
     * 插入单个实体记录。
     *
     * @param entity 要插入的实体对象
     * @throws SQLException 数据库操作异常
     */
    void insert(T entity) throws SQLException;
    /**
     * 批量插入实体记录。
     *
     * @param entities 要插入的实体对象列表
     * @throws SQLException 数据库操作异常
     */
    void insertBatch(List<T> entities) throws SQLException;
    /**
     * 根据主键更新实体记录。
     *
     * @param entity     包含更新数据的实体对象
     * @param primaryKey 主键值
     * @throws SQLException 数据库操作异常
     */
    void updateByid(T entity, String primaryKey) throws SQLException;
    /**
     * 根据条件和参数更新实体记录。
     *
     * @param condition 更新条件
     * @param params    更新参数
     * @param entity    包含更新数据的实体对象
     * @throws SQLException 数据库操作异常
     */
    void updateByCondition(String condition, Object[]params, T entity) throws SQLException;
    /**
     * 根据主键删除实体记录。
     *
     * @param primaryKey 主键值
     * @throws SQLException 数据库操作异常
     */
    void delete(K primaryKey)throws SQLException;
    /**
     * 批量删除实体记录。
     *
     * @param primaryKeys 主键值列表
     * @throws SQLException 数据库操作异常
     */
    void deleteBatch(List<K> primaryKeys) throws  SQLException;
    /**
     * 根据条件和参数删除实体记录。
     *
     * @param condition 删除条件
     * @param params    删除参数
     * @throws SQLException 数据库操作异常
     */
    void deleteByCondition(String condition,Object[]params) throws SQLException;
    /**
     * 根据主键查询单个实体记录。
     *
     * @param primaryKey 主键值
     * @return 查询到的实体对象
     * @throws SQLException 数据库操作异常
     */
    T select(K primaryKey) throws SQLException;
    /**
     * 查询所有实体记录。
     *
     * @return 所有实体对象的列表
     * @throws SQLException 数据库操作异常
     */
    List<T> selectAll() throws SQLException;
    /**
     * 根据条件查询实体记录。
     *
     * @param conditions 查询条件
     * @return 查询到的实体对象列表
     * @throws SQLException 数据库操作异常
     */
    List<T> selectByCondition(String conditions) throws SQLException;
    /**
     * 分页查询实体记录。
     *
     * @param page     查询页码，从0开始
     * @param pagesize 每页记录数
     * @return 查询到的实体对象列表
     * @throws SQLException 数据库操作异常
     */
    List<T> selectPaged(int page, int pagesize)throws SQLException;
    /**
     * 获取表中总记录数。
     *
     * @return 表中总记录数
     * @throws Exception 数据库操作异常
     */
    int gettotalRecords() throws Exception;
}

