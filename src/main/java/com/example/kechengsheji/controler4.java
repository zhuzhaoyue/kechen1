package com.example.kechengsheji;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.BreakClear;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * 控制销售界面的FXML控制器。
 */
public class controler4 {

    private SalesDAO salesDAO;

    @FXML
    private TableView<Sales> tableview;

    @FXML
    private TableColumn<Sales, String> SaleID;

    @FXML
    private TableColumn<Sales, String> ProductID;

    @FXML
    private TableColumn<Sales, String> MemberID;

    @FXML
    private TableColumn<Sales, Date> SaleDate;

    @FXML
    private TableColumn<Sales, String> QuantitySold;

    @FXML
    private TableColumn<Sales, String> TotalAmount;

    @FXML
    private Pagination pagetable;

    @FXML
    private DatePicker AddSaleDate;

    @FXML
    private TextField AddSaleID;

    @FXML
    private TextField AddProductID;

    @FXML
    private TextField AddMemberID;

    @FXML
    private TextField AddQuantitySold;

    @FXML
    private Button addbutton;

    @FXML
    private Button NULL;

    private int itempage=15;
    /**
     * 导出数据的方法。根据用户选择的文件类型，使用不同的工具类导出数据。
     *
     * @param event 导出按钮的 ActionEvent。
     */
    @FXML
    void Importing(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        fileChooser.setInitialDirectory(new File("C:\\Users\\49462\\Desktop"));
        //选择信息
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)", "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")));
        // 显示文件保存对话框
        File selectedFile = fileChooser.showSaveDialog(null);

        try {
            Connection connection=DruidDataSourceUtil.getConnection();
            SalesDAO slDao=new SalesDAO(connection);
            List<Sales> list=slDao.selectAll();
            //根据不同类型使用不同的工具类导出
            if (selectedFile == null)
            {return ;}
            else if(selectedFile.getName().contains(".txt")){
                System.out.println(selectedFile.getPath());
//                FileIOTool.writeFile(list,selectedFile.getPath());
            }else if(selectedFile.getName().contains(".data")){
                System.out.println(selectedFile.getPath());
//                ObjectIOTool.wirteObject(list,selectedFile.getPath());
            }else if(selectedFile.getName().contains(".xlsx")){
                System.out.println(selectedFile.getPath());
                easyExcelTool.writeExcel(list,selectedFile.getPath());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 添加新的销售记录。
     *
     * @param event 事件对象
     */
    @FXML
    void Add(ActionEvent event) {
        try {
            // 获取界面输入的销售数据
            String saleID = AddSaleID.getText();
            String productID = AddProductID.getText();
            String memberID = AddMemberID.getText();
            //String ExpiryDate= AddExpiryDate.getText();
            java.sql.Date SaleDate = java.sql.Date.valueOf(AddSaleDate.getValue());
            LocalDate expiryDate = SaleDate.toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String saleDate = expiryDate.format(formatter);
            String quantitySold = AddQuantitySold.getText();

            // 根据 QuantitySold 和 Price 计算 TotalAmount
            double price = salesDAO.getPriceByProductID(productID);
            double totalAmount = Double.parseDouble(quantitySold) * price;

            // 创建 Sales 对象
            Sales newSale = new Sales(saleID, productID, memberID, saleDate, quantitySold, String.valueOf(totalAmount));
            System.out.println((newSale));

            // 将销售数据插入数据库
            insertSale(newSale);

            // 刷新表格数据和分页控件
            tableview.refresh();
            initializepage();

            // 清除输入框内容
            clearInputFields();
        } catch (SQLException e) {
            e.printStackTrace(); // 处理异常
        }
    }
    /**
     * 向数据库插入销售记录。
     *
     * @param newSale 要插入的销售记录
     * @throws SQLException SQL异常
     */
    private void insertSale(Sales newSale) throws SQLException {
        salesDAO.insert(newSale);
    }
    /**
     * 编辑选定的销售记录。
     *
     * @param event 事件对象
     */
    @FXML
    void EDIT(ActionEvent event) {
        Sales selectedsl = tableview.getSelectionModel().getSelectedItem();

        if (selectedsl != null) {
            // 将选定的商单显示在右
            AddSaleID.setText(selectedsl.getSaleID());
            AddProductID.setText(selectedsl.getProductID());
            AddMemberID.setText(selectedsl.getMemberID());
            String dateString =selectedsl.getSaleDate();
            // 指定日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 使用 DateTimeFormatter 将字符串解析为 LocalDate
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            AddSaleDate.setValue(localDate);
            AddQuantitySold.setText(selectedsl.getQuantitySold());;
            AddSaleID.setDisable(true);
            addbutton.setVisible(false);
        } else {
            // 如果没有选择商单，显示警告或通知用户选择一个商单
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个商单以执行编辑操作。");
            alert.showAndWait();
        }
    }
    /**
     * 清空输入区域。
     *
     * @param event 事件对象
     */
    @FXML
    void Null(ActionEvent event){
        clearInputFields();
        AddSaleID.setDisable(false);
        addbutton.setVisible(true);
    }
    /**
     * 删除选定的销售记录。
     *
     * @param event 事件对象
     */
    @FXML
    void DELETE(ActionEvent event) {
        Sales slbp = tableview.getSelectionModel().getSelectedItem();
        if (slbp != null) {
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                SalesDAO slDao = new SalesDAO(connection);
                // 在数据库中删除选定的商单
                slDao.delete(Integer.valueOf(slbp.getSaleID()));
                // 重新加载当前页的数据
                loadPageData(pagetable.getCurrentPageIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选择商单，显示警告或通知用户选择一个商单
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个商单。");
            alert.showAndWait();
        }
    }
    /**
     * 保存编辑后的销售记录。
     *
     * @param event 事件对象
     */
    @FXML
    void saveEdit(ActionEvent event) {
// 获取编辑后的信息
        String editedSaleID=AddSaleID.getText();
        String editedProductID = AddProductID.getText();
        String editedMemberID = AddMemberID.getText();
        String editedQuantitySold= AddQuantitySold.getText();

        // 更新选定商单的信息
        Sales selectedSl = tableview.getSelectionModel().getSelectedItem();
        selectedSl.setSaleID(editedSaleID);
        selectedSl.setProductID(editedProductID);
        selectedSl.setMemberID(editedMemberID);
        selectedSl.setQuantitySold(editedQuantitySold);
//         将 LocalDate 转换为 java.sql.Date
        java.sql.Date editedBirthday = java.sql.Date.valueOf(AddSaleDate.getValue());
// 使用 SimpleDateFormat 将 java.sql.Date 转换为 String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(editedBirthday);
        selectedSl.setSaleDate(formattedDate);
        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            SalesDAO slDao = new SalesDAO(connection);
            // 将更改保存到数据库
            slDao.updateByid(selectedSl,editedSaleID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 隐藏 "保存编辑" 按钮，显示 "新增数据" 按钮
        addbutton.setVisible(true);
        // 刷新表格视图
        tableview.refresh();
        // 清空输入区域
        clearInputFields();
        AddProductID.setDisable(false);
        // 重新加载当前页的数据
        loadPageData(pagetable.getCurrentPageIndex());
    }

    /**
     * 初始化FXML控制器。
     *
     * @throws Exception 异常
     */
    @FXML
    private void initialize() throws Exception{
        // 在这里初始化 salesDAO
        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            salesDAO = new SalesDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace(); // 适当处理异常
        }
// 使用用户数据更新TableView
        initializepage();
        tableview.setRowFactory(tv -> {
            TableRow<Sales> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // 处理双击事件（调用 EDIT 方法）
                    EDIT(new ActionEvent());
                }
            });
            return row;
        });
        loadPageData(0);
    }
    /**
     * 初始化分页控件。
     */
    private void initializepage(){
        pagetable.setPageCount(CalculatepageCount());
        pagetable.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    /**
     * 计算总页数。
     *
     * @return 总页数
     */
    private int CalculatepageCount(){
        try (Connection connection=DruidDataSourceUtil.getConnection()){
            SalesDAO slDao=new SalesDAO(connection);
            return(int)Math.ceil((double) slDao.gettotalRecords()/itempage);

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 清空输入区域。
     */
    private void clearInputFields() {
        // 清空文本框
        AddSaleID.clear();
        AddProductID.clear();
        AddMemberID.clear();
        AddQuantitySold.clear();
        // 清空日期选择器
        AddSaleDate.setValue(null);
    }
    /**
     * 加载分页数据。
     *
     * @param pageindex 当前页索引
     */
    private void loadPageData(int pageindex) {
        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            SalesDAO slDAO = new SalesDAO(connection);
            List<Sales> pageData = slDAO.selectPaged(pageindex, itempage);
            updateTableView(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 更新表格视图。
     *
     * @param sales 包含要在表格中显示的销售记录信息的列表
     */
    private void updateTableView(List<Sales> sales) {

        tableview.getItems().setAll(sales);
// 为每列设置单元格值工厂，以从用户对象中获取属性值显示。
        SaleID.setCellValueFactory(new PropertyValueFactory<>
                ("SaleID"));
        ProductID.setCellValueFactory(new PropertyValueFactory<>
                ("ProductID"));
        MemberID.setCellValueFactory(new PropertyValueFactory<>
                ("MemberID"));
        SaleDate.setCellValueFactory(new PropertyValueFactory<>
                ("SaleDate"));
        QuantitySold.setCellValueFactory(new PropertyValueFactory<>
                ("QuantitySold"));
        TotalAmount.setCellValueFactory(new PropertyValueFactory<>
                ("TotalAmount"));
    }

}
