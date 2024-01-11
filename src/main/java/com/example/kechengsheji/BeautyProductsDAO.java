package com.example.kechengsheji;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO(Data Access Object)类，用于操作BeautyProducts表的数据访问操作。
 * 实现了JDBC接口，通过JDBC实现与数据库的交互。
 */
public class BeautyProductsDAO implements JDBC<BeautyProducts,Integer>{
    private final String insert = "INSERT INTO beautyproducts (ProductID, ProductName, NetWeight, Efficacy, Price, Points, StockQuantity, ExpiryDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String update1 ="UPDATE beautyproducts SET ProductName=?,NetWeight=? ,Efficacy=?, Price=? ,Points=?,StockQuantity=?,ExpiryDate=? WHERE ProductID=?";
    private final String delete="DELETE FROM beautyproducts WHERE ProductID=?";

    /**
     * 构造函数，接受一个数据库连接对象作为参数。
     * @param conn 数据库连接对象
     */
    public BeautyProductsDAO(Connection conn) {

    }

    /**
     * 将BeautyProducts对象插入数据库。
     * @param entity 要插入的BeautyProducts对象
     * @throws SQLException SQL异常
     */
    @Override
    public void insert(BeautyProducts entity) throws SQLException {
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, entity.getProductID());
            preparedStatement.setString(2, entity.getProductName());
            preparedStatement.setString(3, entity.getNetWeight());
            preparedStatement.setString(4, entity.getEfficacy());
            preparedStatement.setString(5, entity.getPrice());
            preparedStatement.setString(6, entity.getPoints());
            preparedStatement.setString(7, entity.getStockQuantity());
            preparedStatement.setString(8, entity.getExpiryDate());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * 根据主键更新BeautyProducts对象。
     * @param entity 要更新的BeautyProducts对象
     * @param primaryKey 主键值
     * @throws SQLException SQL异常
     */
    @Override
    public void updateByid(BeautyProducts entity, String primaryKey) throws SQLException {
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(update1)){
            preparedStatement.setString(1, entity.getProductName());
            preparedStatement.setString(2, entity.getNetWeight());
            preparedStatement.setString(3, entity.getEfficacy());
            preparedStatement.setString(4, entity.getPrice());
            preparedStatement.setString(5, entity.getPoints());
            preparedStatement.setString(6, entity.getStockQuantity());
            preparedStatement.setString(7, entity.getExpiryDate());
            preparedStatement.setString(8, primaryKey);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByCondition(String condition, Object[] params, BeautyProducts entity) throws SQLException {

    }
    /**
     * 根据主键删除对应的BeautyProducts记录。
     * @param primaryKey 要删除的记录的主键值
     * @throws SQLException SQL异常
     */
    @Override
    public void delete(Integer primaryKey) throws SQLException {
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(delete)){
            preparedStatement.setInt(1,primaryKey);
            preparedStatement.executeUpdate();
        }
    }


    @Override
    public void insertBatch(List entities) throws SQLException {

    }


    @Override
    public void deleteBatch(List primaryKeys) throws SQLException {

    }

    @Override
    public void deleteByCondition(String condition, Object[] params) throws SQLException {

    }

    /**
     * 根据主键查询BeautyProducts对象。
     * @param primaryKey 主键值
     * @return 查询到的BeautyProducts对象，如果不存在则为null
     * @throws SQLException SQL异常
     */
    @Override
    public BeautyProducts select(Integer primaryKey) throws SQLException {
        BeautyProducts bp=null;
        String select1="SELECT * FROM beautyproducts WHERE ProductID = ?";
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(select1)){
            preparedStatement.setInt(1,primaryKey);
            System.out.println("Executing SQL: " + preparedStatement.toString());
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    bp=mapResultSetToEntity(resultSet);
                }
            }
        }
        return bp;
    }
    /**
     * 查询所有BeautyProducts对象。
     * @return 包含所有BeautyProducts对象的List
     * @throws SQLException SQL异常
     */
    @Override
    public List<BeautyProducts> selectAll() throws SQLException {

        List<BeautyProducts> bp = new ArrayList<>();
        String select2 = "SELECT * FROM beautyproducts";
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select2)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bp.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return bp;
    }
    /**
     * 根据条件查询BeautyProducts对象列表。
     * @param conditions 查询条件，例如："Price > 50 AND StockQuantity > 0"
     * @return 包含满足条件的BeautyProducts对象的List
     * @throws SQLException SQL异常
     */
    @Override
    public List<BeautyProducts> selectByCondition(String conditions) throws SQLException {
        List<BeautyProducts> bp = new ArrayList<>();

        String query = "SELECT * FROM beautyproducts WHERE " + conditions;

        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                BeautyProducts bp1 = new BeautyProducts(
                        resultSet.getString("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("NetWeight"),
                        resultSet.getString("Efficacy"),
                        resultSet.getString("Price"),
                        resultSet.getString("Points"),
                        resultSet.getString("StockQuantity"),
                        resultSet.getString("ExpiryDate")
                );
                bp.add(bp1);
            }
        }

        return bp;
    }

    /**
     * 分页查询BeautyProducts对象列表。
     * @param page 页码，从0开始
     * @param pagesize 每页的记录数
     * @return 包含分页结果的BeautyProducts对象的List
     * @throws SQLException SQL异常
     */
    @Override
    public List<BeautyProducts> selectPaged(int page, int pagesize) throws SQLException {
        List<BeautyProducts> bp = new ArrayList<>();
        String select4 = "SELECT * FROM beautyproducts LIMIT ?,?";
        try(Connection connection=DruidDataSourceUtil.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(select4)) {
            preparedStatement.setInt(1,page*pagesize);
            preparedStatement.setInt(2,pagesize);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    bp.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return bp;
    }
    /**
     * 获取BeautyProducts表的总记录数。
     * @return BeautyProducts表的总记录数
     * @throws SQLException SQL异常
     */
    @Override
    public int gettotalRecords() throws Exception {
        String sql="SELECT COUNT(*) FROM beautyproducts";
        try(Connection connection=DruidDataSourceUtil.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getInt(1);
            }
        }
        return 0;
    }
    /**
     * 将数据库查询结果集映射为BeautyProducts对象。
     * @param resultSet 数据库查询结果集
     * @return 映射后的BeautyProducts对象
     * @throws SQLException SQL异常
     */
    private BeautyProducts mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        BeautyProducts bp = new BeautyProducts(resultSet.getString("ProductID"),
                resultSet.getString("ProductName"),
                resultSet.getString("NetWeight"),
                resultSet.getString("Efficacy"),
                resultSet.getString("Price"),
                resultSet.getString("Points"),
                resultSet.getString("StockQuantity"),
                resultSet.getString("ExpiryDate"));
        bp.setProductID(resultSet.getString("ProductID"));
        bp.setProductName(resultSet.getString("ProductName"));
        bp.setNetWeight(resultSet.getString("NetWeight"));
        bp.setEfficacy(resultSet.getString("Efficacy"));
        bp.setPrice(resultSet.getString("Price"));
        bp.setPoints(resultSet.getString("Points"));
        bp.setStockQuantity(resultSet.getString("StockQuantity"));
        bp.setExpiryDate(resultSet.getString("ExpiryDate"));
        return bp;
    }
}