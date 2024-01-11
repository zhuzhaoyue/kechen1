package com.example.kechengsheji;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * 控制 BeautyProducts 数据的 JavaFX 控制器类。
 */
public class controler3 {
    private BeautyProductsDAO beautyProductsDAO;

    @FXML
    private DatePicker AddExpiryDate;

    @FXML
    private TableColumn<BeautyProducts, Date> ExpiryDate;

    @FXML
    private TableColumn<BeautyProducts, String> ProductName;

    @FXML
    private MenuItem edit;

    @FXML
    private TableColumn<BeautyProducts, Integer> StockQuantity;

    @FXML
    private Button saveEditButton;

    @FXML
    private TextField AddProductID;

    @FXML
    private TableColumn<BeautyProducts, String> Efficacy;

    @FXML
    private TextField AddStockQuantity;

    @FXML
    private TableColumn<BeautyProducts, Integer> ProductID;

    @FXML
    private TableColumn<BeautyProducts, Integer> NetWeight;

    @FXML
    private MenuItem delete;

    @FXML
    private TextField AddNetWeight;

    @FXML
    private Pagination pagetable;

    @FXML
    private TableColumn<BeautyProducts, Integer> Points;

    @FXML
    private TextField AddPoints;

    @FXML
    private TextField AddPrice;

    @FXML
    private TableColumn<BeautyProducts, Integer> Price;

    @FXML
    private TextField AddEfficacy;

    @FXML
    private TextField AddProductName;

    @FXML
    private TableView<BeautyProducts> tableview;

    @FXML
    private Button addbutton;

    @FXML
    private Button button1;

    private int itempage=20;
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
            BeautyProductsDAO bpDao=new BeautyProductsDAO(connection);
            List<BeautyProducts> list=bpDao.selectAll();
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
     * 新增 BeautyProducts 数据的方法。从用户输入区域获取数据，创建新的 BeautyProducts 对象，
     * 将其插入数据库，然后清空输入区域并重新加载当前页的数据。
     *
     * @param event 新增按钮的 ActionEvent。
     */
    @FXML
    void Add(ActionEvent event) {
        String ProductID=AddProductID.getText();
        String ProductName = AddProductName.getText();
        String NetWeight = AddNetWeight.getText();
        String Efficacy = AddEfficacy.getText();
        String Price= AddPrice.getText();
        String Points= AddPoints.getText();
        String StockQuantity= AddStockQuantity.getText();
        //String ExpiryDate= AddExpiryDate.getText();
        java.sql.Date birthDate = java.sql.Date.valueOf(AddExpiryDate.getValue());
        LocalDate expiryDate = birthDate.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String ExpiryDate = expiryDate.format(formatter);
        BeautyProducts newbp=new BeautyProducts(ProductID,ProductName,NetWeight,Efficacy,Price,Points,StockQuantity,ExpiryDate);
        System.out.println(newbp);
        try (Connection connection=DruidDataSourceUtil.getConnection()){
            BeautyProductsDAO bpDao=new BeautyProductsDAO(connection);
            bpDao.insert(newbp);
        }catch (Exception e){
            e.printStackTrace();
        }
        clearInputFields();
        loadPageData(0);
    }
    /**
     * 编辑 BeautyProducts 数据的方法。从表格视图中选定的项获取数据，
     * 将数据显示在输入区域，并将编辑按钮显示为保存编辑按钮。
     *
     * @param event 编辑按钮的 ActionEvent。
     */
    @FXML
    void EDIT(ActionEvent event) {
        BeautyProducts selectedBp = tableview.getSelectionModel().getSelectedItem();

        if (selectedBp != null) {
            // 将选定的美容品信息显示在左边输入区域
            AddProductID.setText(selectedBp.getProductID());
            AddProductName.setText(selectedBp.getProductName());
            AddNetWeight.setText(selectedBp.getNetWeight());
            AddEfficacy.setText(selectedBp.getEfficacy());
            AddPrice.setText(selectedBp.getPrice());;
            AddPoints.setText(selectedBp.getPoints());;
            AddStockQuantity.setText(selectedBp.getStockQuantity());
            AddProductID.setDisable(true);
            String dateString =selectedBp.getExpiryDate();
            // 指定日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 使用 DateTimeFormatter 将字符串解析为 LocalDate
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            AddExpiryDate.setValue(localDate);
            saveEditButton.setVisible(true);
            addbutton.setVisible(false);
        } else {
            // 如果没有选择美容品，显示警告或通知用户选择一个美容品
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个美容品以执行编辑操作。");
            alert.showAndWait();
        }
        button1.setVisible(true);
    }
    /**
     * 删除 BeautyProducts 数据的方法。从表格视图中选定的项获取数据，
     * 将其从数据库中删除，然后重新加载当前页的数据。
     *
     * @param event 删除按钮的 ActionEvent。
     */
    @FXML
    void DELETE(ActionEvent event) {
        BeautyProducts debp = tableview.getSelectionModel().getSelectedItem();
        if (debp != null) {
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                BeautyProductsDAO bpDao = new BeautyProductsDAO(connection);
                // 在数据库中删除选定的美容品
                bpDao.delete(Integer.valueOf(debp.getProductID()));
                // 重新加载当前页的数据
                loadPageData(pagetable.getCurrentPageIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选择美容品，显示警告或通知用户选择一个美容品
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个美容品以执行删除操作。");
            alert.showAndWait();
        }
    }
    /**
     * 处理基于用户输入的搜索操作。
     *
     * @param event 由搜索按钮触发的 ActionEvent。
     */
    @FXML
    void Search(ActionEvent event) {
        String searchId = AddProductID.getText().trim();
        String searchName = AddProductName.getText().trim();

        if (!searchId.isEmpty() && !searchName.isEmpty()) {
            // 同时输入了 ID 和 name
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请只输入 ID 或名字进行搜索。");
            alert.showAndWait();
        } else if (!searchId.isEmpty()) {
            // 执行 ID 搜索
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                BeautyProductsDAO bpDAO = new BeautyProductsDAO(connection);
                int bpId = Integer.parseInt(searchId);
                BeautyProducts foundbp = bpDAO.select(bpId);

                if (foundbp != null) {
                    ObservableList<BeautyProducts> searchResult = FXCollections.observableArrayList(foundbp);
                    updateTableView(searchResult);
                    button1.setVisible(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("搜索结果");
                    alert.setHeaderText(null);
                    alert.setContentText("未找到 ID 为：" + bpId + " 的成员");
                    alert.showAndWait();
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (!searchName.isEmpty()) {
            // 执行姓名搜索
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                BeautyProductsDAO bpDAO = new BeautyProductsDAO(connection);
                List<BeautyProducts> searchResult = bpDAO.selectByCondition("ProductName='" + searchName + "'");
                ObservableList<BeautyProducts> observableList = FXCollections.observableArrayList(searchResult);
                updateTableView(observableList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            button1.setVisible(false);
        } else {
            // 未输入 ID 或名字
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入要搜索的 ID 或名字。");
            alert.showAndWait();
        }
    }
    /**
     * 处理用户点击返回按钮的操作，用于清空输入区域、刷新表格视图并加载当前页的数据。
     *
     * @param event 由返回按钮触发的 ActionEvent。
     */
    @FXML
    void Return(ActionEvent event){
        clearInputFields();
        tableview.refresh();
        loadPageData(pagetable.getCurrentPageIndex());
        button1.setVisible(true);
    }
    /**
     * 处理用户点击保存编辑按钮的操作，用于获取编辑后的信息并更新数据库中选定用户的信息。
     *
     * @param event 由保存编辑按钮触发的 ActionEvent。
     */
    @FXML
    void saveEdit(ActionEvent event) {
// 获取编辑后的信息
        String editedid=AddProductID.getText();
        String editedName = AddProductName.getText();
        String editedNetWeight = AddNetWeight.getText();
        String editedEfficacy = AddEfficacy.getText();
        String editedPrice= AddPrice.getText();
        String editedPoints= AddPoints.getText();
        String editedStockQuantity= AddStockQuantity.getText();
        // 更新选定用户的信息
        BeautyProducts selectedBp = tableview.getSelectionModel().getSelectedItem();
        selectedBp.setProductName(editedName);
        selectedBp.setProductID(editedid);
        selectedBp.setNetWeight(editedNetWeight);
        selectedBp.setEfficacy(editedEfficacy);
        selectedBp.setPrice(editedPrice);
        selectedBp.setPoints(editedPoints);
        selectedBp.setStockQuantity(editedStockQuantity);
//         将 LocalDate 转换为 java.sql.Date
        java.sql.Date editedBirthday = java.sql.Date.valueOf(AddExpiryDate.getValue());
// 使用 SimpleDateFormat 将 java.sql.Date 转换为 String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(editedBirthday);
        selectedBp.setExpiryDate(formattedDate);

        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            BeautyProductsDAO bpDao = new BeautyProductsDAO(connection);
            // 将更改保存到数据库
            bpDao.updateByid(selectedBp,editedid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 隐藏 "保存编辑" 按钮，显示 "新增数据" 按钮
        saveEditButton.setVisible(false);
        addbutton.setVisible(true);
        // 刷新表格视图
        tableview.refresh();
        // 清空输入区域
        clearInputFields();
        AddProductID.setDisable(false);
        // 重新加载当前页的数据
        loadPageData(pagetable.getCurrentPageIndex());
    }

//    static List<BeautyProducts> beautyProducts = new ArrayList();
    /**
     * 初始化方法，在FXML文件加载后自动执行。用于初始化界面和数据。
     * @throws Exception 可能抛出的异常。
     */
    @FXML
    private void initialize() throws Exception{
// 使用用户数据更新TableView
        initializepage();
        tableview.setRowFactory(tv -> {
            TableRow<BeautyProducts> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // 处理双击事件（调用 EDIT 方法）
                    EDIT(new ActionEvent());
                }
            });
            return row;
        });
        loadPageData(0);
        saveEditButton.setVisible(false);
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
            BeautyProductsDAO bpDao=new BeautyProductsDAO(connection);
            return(int)Math.ceil((double) bpDao.gettotalRecords()/itempage);

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 清空输入区域的文本框和日期选择器。
     */
    private void clearInputFields() {
        // 清空文本框
        AddProductID.clear();
        AddProductName.clear();
        AddNetWeight.clear();
        AddEfficacy.clear();
        AddPrice.clear();
        AddPoints.clear();
        AddStockQuantity.clear();
        // 清空日期选择器
        AddExpiryDate.setValue(null);
    }
    /**
     * 根据给定的页面索引加载表格视图的数据。
     *
     * @param pageindex 要加载的页面索引
     */
    private void loadPageData(int pageindex) {
        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            BeautyProductsDAO bpDAO = new BeautyProductsDAO(connection);
            List<BeautyProducts> pageData = bpDAO.selectPaged(pageindex, itempage);
            updateTableView(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 根据提供的美容产品列表更新表格视图。
     *
     * @param beautyproducts 包含要在表格中显示的美容产品信息的列表
     */
    private void updateTableView(List<BeautyProducts> beautyproducts) {

        tableview.getItems().setAll(beautyproducts);
// 为每列设置单元格值工厂，以从用户对象中获取属性值显示。
        ProductID.setCellValueFactory(new PropertyValueFactory<>
                ("ProductID"));
        ProductName.setCellValueFactory(new PropertyValueFactory<>
                ("ProductName"));
        NetWeight.setCellValueFactory(new PropertyValueFactory<>
                ("NetWeight"));
        Efficacy.setCellValueFactory(new PropertyValueFactory<>
                ("Efficacy"));
        Price.setCellValueFactory(new PropertyValueFactory<>
                ("Price"));
        Points.setCellValueFactory(new PropertyValueFactory<>
                ("Points"));
        StockQuantity.setCellValueFactory(new PropertyValueFactory<>
                ("StockQuantity"));
        ExpiryDate.setCellValueFactory(new PropertyValueFactory<>
                ("ExpiryDate"));
    }

}