package com.example.kechengsheji;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 会员信息数据访问对象，实现了 JDBC 接口，用于对会员信息进行数据库操作。
 */
public class MembersDAO implements JDBC<Members,Integer> {

    private final String insert = "INSERT INTO members (MemberID, MemberName, Gender, MembershipLevel, StoredValue, MembershipPoints, PhoneNumber, BirthDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String update1 ="UPDATE members SET MemberName=?,Gender=? ,MembershipLevel=?, StoredValue=? ,MembershipPoints=?,PhoneNumber=?,BirthDate=? WHERE MemberID=?";
    private final String delete="DELETE FROM members WHERE MemberID=?";

    public MembersDAO(Connection conn) {

    }
    /**
     * 将会员信息插入数据库。
     *
     * @param entity 要插入的会员对象
     * @throws SQLException 如果插入操作发生 SQL 异常
     */
    @Override
    public void insert(Members entity) throws SQLException {
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, entity.getMemberID());
            preparedStatement.setString(2, entity.getMemberName());
            preparedStatement.setString(3, entity.getGender());
            preparedStatement.setString(4, entity.getMembershipLevel());
            preparedStatement.setString(5, entity.getStoredValue());
            preparedStatement.setString(6, entity.getMembershipPoints());
            preparedStatement.setString(7, entity.getPhoneNumber());
            preparedStatement.setString(8, entity.getBirthDate());
            preparedStatement.executeUpdate();
        }
    }
    /**
     * 根据会员ID更新会员信息。
     *
     * @param entity      包含更新信息的会员对象
     * @param primaryKey  要更新的会员ID
     * @throws SQLException 如果更新操作发生 SQL 异常
     */
    @Override
    public void updateByid(Members entity, String primaryKey) throws SQLException {
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(update1)){
            preparedStatement.setString(1, entity.getMemberName());
            preparedStatement.setString(2, entity.getGender());
            preparedStatement.setString(3, entity.getMembershipLevel());
            preparedStatement.setString(4, entity.getStoredValue());
            preparedStatement.setString(5, entity.getMembershipPoints());
            preparedStatement.setString(6, entity.getPhoneNumber());
            preparedStatement.setString(7, entity.getBirthDate());
            preparedStatement.setString(8, primaryKey);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByCondition(String condition, Object[] params, Members entity) throws SQLException {

    }
    /**
     * 根据会员ID删除会员信息。
     *
     * @param primaryKey 要删除的会员ID
     * @throws SQLException 如果删除操作发生 SQL 异常
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
     * 根据会员ID查询会员信息。
     *
     * @param primaryKey 要查询的会员ID
     * @return 查询到的会员信息，如果未找到则返回 null
     * @throws SQLException 如果查询操作发生 SQL 异常
     */
    @Override
    public Members select(Integer primaryKey) throws SQLException {
        Members mm=null;
        String select1="SELECT * FROM members WHERE MemberID = ?";
        try (Connection connection=DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(select1)){
            preparedStatement.setInt(1,primaryKey);
            System.out.println("Executing SQL: " + preparedStatement.toString());
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    mm=mapResultSetToEntity(resultSet);
                }
            }
        }
        return mm;
    }
    /**
     * 查询所有会员信息。
     *
     * @return 包含所有会员信息的列表
     * @throws SQLException 如果查询操作发生 SQL 异常
     */
    @Override
    public List<Members> selectAll() throws SQLException {

        List<Members> member = new ArrayList<>();
        String select2 = "SELECT * FROM members";
        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select2)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    member.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return member;
    }
    /**
     * 根据指定条件查询会员信息。
     *
     * @param conditions 查询条件的 SQL 表达式，例如 "MembershipLevel='Gold'"。
     * @return 符合条件的会员信息列表
     * @throws SQLException 如果查询操作发生 SQL 异常
     */
    @Override
    public List<Members> selectByCondition(String conditions) throws SQLException {
        List<Members> member = new ArrayList<>();

        String query = "SELECT * FROM members WHERE " + conditions;

        try (Connection connection = DruidDataSourceUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Members member1 = new Members(
                        resultSet.getString("MemberID"),
                        resultSet.getString("MemberName"),
                        resultSet.getString("Gender"),
                        resultSet.getString("MembershipLevel"),
                        resultSet.getString("StoredValue"),
                        resultSet.getString("MembershipPoints"),
                        resultSet.getString("PhoneNumber"),
                        resultSet.getString("BirthDate")
                );
                member.add(member1);
            }
        }

        return member;
    }

    /**
     * 分页查询会员信息。
     *
     * @param page     请求的页数，从 0 开始计数
     * @param pagesize 每页的记录数
     * @return 当前页的会员信息列表
     * @throws SQLException 如果查询操作发生 SQL 异常
     */
    @Override
    public List<Members> selectPaged(int page, int pagesize) throws SQLException {
        List<Members> mm = new ArrayList<>();
        String select4 = "SELECT * FROM members LIMIT ?,?";
        try(Connection connection=DruidDataSourceUtil.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(select4)) {
            preparedStatement.setInt(1,page*pagesize);
            preparedStatement.setInt(2,pagesize);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    mm.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return mm;
    }
    /**
     * 获取会员总记录数。
     *
     * @return 会员总记录数
     * @throws Exception 如果获取总记录数操作发生异常
     */
    @Override
    public int gettotalRecords() throws Exception {
        String sql="SELECT COUNT(*) FROM members";
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
     * 将数据库查询结果集映射为 Members 实体对象。
     *
     * @param resultSet 包含查询结果的 ResultSet 对象
     * @return 映射后的 Members 实体对象
     * @throws SQLException 如果映射过程中发生 SQL 异常
     */
    private Members mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Members member = new Members(resultSet.getString("MemberID"),
                resultSet.getString("MemberName"),
                resultSet.getString("Gender"),
                resultSet.getString("MembershipLevel"),
                resultSet.getString("StoredValue"),
                resultSet.getString("MembershipPoints"),
                resultSet.getString("PhoneNumber"),
                resultSet.getString("BirthDate"));
        member.setMemberID(resultSet.getString("MemberID"));
        member.setMemberName(resultSet.getString("MemberName"));
        member.setGender(resultSet.getString("Gender"));
        member.setMembershipLevel(resultSet.getString("MembershipLevel"));
        member.setStoredValue(resultSet.getString("StoredValue"));
        member.setMembershipPoints(resultSet.getString("MembershipPoints"));
        member.setPhoneNumber(resultSet.getString("PhoneNumber"));
        member.setBirthDate(resultSet.getString("BirthDate"));
        return member;
    }
}

