package com.example.kechengsheji;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * SalesDAO 类实现了 JDBC 接口，用于对销售信息进行数据库操作。
 */
public class SalesDAO implements JDBC<Sales,Integer>{
    private final String insert = "INSERT INTO sales (SaleID, ProductID, MemberID, SaleDate, QuantitySold, TotalAmount) VALUES (?, ?, ?, ?, ?, ?)";
    private final String update1 ="UPDATE sales SET ProductID=?,MemberID=? ,SaleDate=?, QuantitySold=? ,TotalAmount=? WHERE SaleID=?";
    private final String delete="DELETE FROM sales WHERE SaleID=?";
    /**
     * 构造 SalesDAO 对象。
     *
     * @param conn 数据库连接
     */
    public SalesDAO(Connection conn){

    }
    /**
     * 向数据库中插入销售信息。
     *
     * @param entity 包含销售信息的 Sales 对象
     * @throws SQLException 如果 SQL 异常发生
     */
    @Override
    public void insert(Sales entity) throws SQLException {
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, entity.getSaleID());
            preparedStatement.setString(2, entity.getProductID());
            preparedStatement.setString(3, entity.getMemberID());
            preparedStatement.setString(4, entity.getSaleDate());
            preparedStatement.setString(5, entity.getQuantitySold());
            preparedStatement.setString(6, entity.getTotalAmount());
            preparedStatement.executeUpdate();
        }
    }
    /**
     * 根据销售单号更新数据库中的销售信息。
     *
     * @param entity      包含更新后销售信息的 Sales 对象
     * @param primaryKey  销售单号，用于定位要更新的记录
     * @throws SQLException 如果 SQL 异常发生
     */
    @Override
    public void updateByid(Sales entity, String primaryKey) throws SQLException {
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(update1)){
            preparedStatement.setString(1, entity.getProductID());
            preparedStatement.setString(2, entity.getMemberID());
            preparedStatement.setString(3, entity.getSaleDate());
            preparedStatement.setString(4, entity.getQuantitySold());
            preparedStatement.setString(5, entity.getTotalAmount());
            preparedStatement.setString(6, primaryKey);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByCondition(String condition, Object[] params, Sales entity) throws SQLException {

    }
    /**
     * 根据销售单号从数据库中删除销售信息。
     *
     * @param primaryKey  销售单号，用于定位要删除的记录
     * @throws SQLException 如果 SQL 异常发生
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
     * 根据销售单号从数据库中检索销售信息。
     *
     * @param primaryKey  销售单号，用于定位要检索的记录
     * @return 代表销售信息的 Sales 对象，如果未找到则返回 null
     * @throws SQLException 如果 SQL 异常发生
     */
    @Override
    public Sales select(Integer primaryKey) throws SQLException {
        Sales sl=null;
        String select1="SELECT * FROM sales WHERE SalesID = ?";
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(select1)){
            preparedStatement.setInt(1,primaryKey);
            System.out.println("Executing SQL: " + preparedStatement.toString());
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    sl=mapResultSetToEntity(resultSet);
                }
            }
        }
        return sl;
    }
    /**
     * 从数据库中检索所有销售信息。
     *
     * @return 包含所有销售信息的列表
     * @throws SQLException 如果 SQL 异常发生
     */
    @Override
    public List<Sales> selectAll() throws SQLException {

        List<Sales> sl = new ArrayList<>();
        String select2 = "SELECT * FROM sales";
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select2)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sl.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return sl;
    }
    /**
     * 根据条件从数据库中检索销售信息。
     *
     * @param conditions 查询条件的字符串表示
     * @return 符合条件的销售信息列表
     * @throws SQLException 如果 SQL 异常发生
     */
    @Override
    public List<Sales> selectByCondition(String conditions) throws SQLException {
        List<Sales> sl = new ArrayList<>();

        String query = "SELECT * FROM beautyproducts WHERE " + conditions;

        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Sales sl1 = new Sales(
                        resultSet.getString("SaleID"),
                        resultSet.getString("ProductID"),
                        resultSet.getString("MemberID"),
                        resultSet.getString("SaleDate"),
                        resultSet.getString("QuantitySold"),
                        resultSet.getString("TotalAmount")
                );
                sl.add(sl1);
            }
        }

        return sl;
    }

    /**
     * 从数据库中分页检索销售信息。
     *
     * @param page     要检索的页码，从0开始
     * @param pagesize 每页的记录数
     * @return 分页后的销售信息列表
     * @throws SQLException 如果 SQL 异常发生
     */
    @Override
    public List<Sales> selectPaged(int page, int pagesize) throws SQLException {
        List<Sales> sl = new ArrayList<>();
        String select4 = "SELECT * FROM sales LIMIT ?,?";
        try(Connection connection=DruidDataSourceUtil.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(select4)) {
            preparedStatement.setInt(1,page*pagesize);
            preparedStatement.setInt(2,pagesize);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    sl.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return sl;
    }
    /**
     * 获取销售信息的总记录数。
     *
     * @return 销售信息的总记录数
     * @throws Exception 如果发生异常
     */
    @Override
    public int gettotalRecords() throws Exception {
        String sql="SELECT COUNT(*) FROM sales";
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
     * 将数据库查询结果映射为 Sales 对象。
     *
     * @param resultSet 查询结果集
     * @return Sales 对象
     * @throws SQLException 如果 SQL 异常发生
     */
    private Sales mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Sales sl = new Sales(resultSet.getString("SaleID"),
                resultSet.getString("ProductID"),
                resultSet.getString("MemberID"),
                resultSet.getString("SaleDate"),
                resultSet.getString("QuantitySold"),
                resultSet.getString("TotalAmount"));
        sl.setSaleID(resultSet.getString("SaleID"));
        sl.setProductID(resultSet.getString("ProductID"));
        sl.setMemberID(resultSet.getString("MemberID"));
        sl.setSaleDate(resultSet.getString("SaleDate"));
        sl.setQuantitySold(resultSet.getString("QuantitySold"));
        sl.setTotalAmount(resultSet.getString("TotalAmount"));
        return sl;
    }
    /**
     * 根据产品号查询产品价格。
     *
     * @param productID 产品号
     * @return 产品价格
     * @throws SQLException 如果 SQL 异常发生
     */
    public double getPriceByProductID(String productID) throws SQLException {
        double price = 0.0;
        String query = "SELECT Price FROM beautyproducts WHERE ProductID = ?";
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    price = resultSet.getDouble("Price");
                }
            }
        }
        return price;
    }
}
