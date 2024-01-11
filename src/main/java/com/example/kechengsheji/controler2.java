package com.example.kechengsheji;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import javafx.stage.FileChooser;

import javax.swing.*;

public class controler2 {
    private  MembersDAO memberDao;

    @FXML
    private TextField Addname;

    @FXML
    private MenuItem edit;

    @FXML
    private DatePicker Adddate;

    @FXML
    private Button saveEditButton;

    @FXML
    private ToggleGroup sex;

    @FXML
    private TableColumn<Members, String> Gender;

    @FXML
    private TextField AddStoredValue;

    @FXML
    private TableColumn<Members, Integer> StoredValue;

    @FXML
    private MenuItem delete;

    @FXML
    private TableColumn<Members, Integer> MemberID;

    @FXML
    private Pagination pagetable;

    @FXML
    private TextField AddPoints;

    @FXML
    private TableColumn<Members, String> MembershipLevel;

    @FXML
    private TextField Addphone;

    @FXML
    private TableColumn<Members, String> PhoneNumber;

    @FXML
    private TableView<Members> tableview;

    @FXML
    private TableColumn<Members, String> MemberName;

    @FXML
    private RadioButton Addsex1;

    @FXML
    private TextField AddLevel;

    @FXML
    private TableColumn<Members, Integer> MembershipPoints;

    @FXML
    private TextField Addid;

    @FXML
    private RadioButton Addsex;

    @FXML
    private TableColumn<Members, Date> BirthDate;

    @FXML
    private Button addbutton;

    @FXML
    private Button button1;


    private int itempage=20;
    /**
     * 处理导出文件按钮点击事件的方法。
     * @param event 点击事件
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
            MembersDAO mmDao=new MembersDAO(connection);
            List<Members> list=mmDao.selectAll();
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
     * 处理新增按钮点击事件的方法。
     * @param event 点击事件
     */
    @FXML
    void Add(ActionEvent event) {
        String MemberID=Addid.getText();
        String MemberName = Addname.getText();
        String PhoneNumber = Addphone.getText();
        String MembershipLevel = AddLevel.getText();
        String StoredValue= AddStoredValue.getText();
        String MembershipPoints= AddPoints.getText();
        String Gender = Addsex.isSelected() ? "男" : "女";
        java.sql.Date birthDate = java.sql.Date.valueOf(Adddate.getValue());
        LocalDate localDate = birthDate.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String BirthDate = localDate.format(formatter);
        Members newmm=new Members(MemberID,MemberName,Gender,MembershipLevel,StoredValue,MembershipPoints,PhoneNumber,BirthDate);
        System.out.println(newmm);
        try (Connection connection=DruidDataSourceUtil.getConnection()){
            MembersDAO mmDao=new MembersDAO(connection);
            mmDao.insert(newmm);
        }catch (Exception e){
            e.printStackTrace();
        }
        clearInputFields();
        loadPageData(0);
    }
    /**
     * 处理返回按钮点击事件的方法。
     * @param event 点击事件
     */
    @FXML
    void Return(ActionEvent event){
        clearInputFields();
        tableview.refresh();
        loadPageData(pagetable.getCurrentPageIndex());
        button1.setVisible(true);
    }
    /**
     * 处理编辑按钮点击事件的方法。
     * @param event 点击事件
     */
    @FXML
    void EDIT(ActionEvent event) {
        Members selectedMember = tableview.getSelectionModel().getSelectedItem();

        if (selectedMember != null) {
            // 将选定的用户信息显示在左边输入区域
            Addid.setText(selectedMember.getMemberID());
            Addname.setText(selectedMember.getMemberName());
            String dateString =selectedMember.getBirthDate();
            // 指定日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 使用 DateTimeFormatter 将字符串解析为 LocalDate
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            Adddate.setValue(localDate);
            Addphone.setText(selectedMember.getPhoneNumber());
            AddStoredValue.setText(selectedMember.getStoredValue());;
            AddLevel.setText(selectedMember.getMembershipLevel());;
            AddPoints.setText(selectedMember.getMembershipPoints());
            Addid.setDisable(true);
            if ("男".equals(selectedMember.getGender())) {
                Addsex.setSelected(true);
                Addsex1.setSelected(false);
            } else if ("女".equals(selectedMember.getGender())) {
                Addsex.setSelected(false);
                Addsex1.setSelected(true);
            } else {
                Addsex.setSelected(false);
                Addsex1.setSelected(false);
            }
            saveEditButton.setVisible(true);
            addbutton.setVisible(false);
        } else {
            // 如果没有选择用户，显示警告或通知用户选择一个用户
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个用户以执行编辑操作。");
            alert.showAndWait();
        }
        button1.setVisible(true);
    }
    /**
     * 处理删除按钮点击事件的方法。
     *
     * @param event 删除按钮点击事件
     */
    @FXML
    void DELETE(ActionEvent event) {
        Members demember = tableview.getSelectionModel().getSelectedItem();
        if (demember != null) {
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                MembersDAO mmDao = new MembersDAO(connection);
                // 在数据库中删除选定的学生
                mmDao.delete(Integer.valueOf(demember.getMemberID()));
                // 重新加载当前页的数据
                loadPageData(pagetable.getCurrentPageIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选择学生，显示警告或通知用户选择一个学生
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个用户以执行删除操作。");
            alert.showAndWait();
        }
    }
    /**
     * 处理搜索按钮点击事件的方法。
     *
     * @param event 搜索按钮点击事件
     */
    @FXML
    void Search(ActionEvent event) {
        String searchId = Addid.getText().trim();
        String searchName = Addname.getText().trim();

        if (!searchId.isEmpty() && !searchName.isEmpty()) {
            // 同时输入了 ID 和姓名
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请只输入 ID 或姓名进行搜索。");
            alert.showAndWait();
        } else if (!searchId.isEmpty()) {
            // 执行 ID 搜索
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                MembersDAO mmDAO = new MembersDAO(connection);
                int memberId = Integer.parseInt(searchId);
                Members foundMember = mmDAO.select(memberId);

                if (foundMember != null) {
                    ObservableList<Members> searchResult = FXCollections.observableArrayList(foundMember);
                    updateTableView(searchResult);
                    button1.setVisible(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("搜索结果");
                    alert.setHeaderText(null);
                    alert.setContentText("未找到 ID 为：" + memberId + " 的成员");
                    alert.showAndWait();
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (!searchName.isEmpty()) {
            // 执行姓名搜索
            try (Connection connection = DruidDataSourceUtil.getConnection()) {
                MembersDAO mmDAO = new MembersDAO(connection);
                List<Members> searchResult = mmDAO.selectByCondition("MemberName='" + searchName + "'");
                ObservableList<Members> observableList = FXCollections.observableArrayList(searchResult);
                updateTableView(observableList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            button1.setVisible(false);
        } else {
            // 未输入 ID 或姓名
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入要搜索的 ID 或姓名。");
            alert.showAndWait();
        }
    }

    /**
     * 处理保存编辑按钮点击事件的方法。
     *
     * @param event 保存编辑按钮点击事件
     */
    @FXML
    void saveEdit(ActionEvent event) {
// 获取编辑后的信息
        String editedid=Addid.getText();
        String editedName = Addname.getText();
        String editedPhone = Addphone.getText();
        String editedLevel = AddLevel.getText();
        String editedStoreValue= AddStoredValue.getText();
        String editedPoints= AddPoints.getText();
        String editedSex = Addsex.isSelected() ? "男" : "女";

        // 更新选定用户的信息
        Members selectedMember = tableview.getSelectionModel().getSelectedItem();
        selectedMember.setMemberName(editedName);
        selectedMember.setMemberID(editedid);
        selectedMember.setPhoneNumber(editedPhone);
        selectedMember.setGender(editedSex);
        selectedMember.setMembershipLevel(editedLevel);
        selectedMember.setStoredValue(editedStoreValue);
        selectedMember.setMembershipPoints(editedPoints);
//         将 LocalDate 转换为 java.sql.Date
        java.sql.Date editedBirthday = java.sql.Date.valueOf(Adddate.getValue());
// 使用 SimpleDateFormat 将 java.sql.Date 转换为 String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(editedBirthday);
        selectedMember.setBirthDate(formattedDate);

        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            MembersDAO mmDao = new MembersDAO(connection);
            // 将更改保存到数据库
            mmDao.updateByid(selectedMember,editedid);
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
        Addid.setDisable(false);
        // 重新加载当前页的数据
        loadPageData(pagetable.getCurrentPageIndex());
    }

    //static List<Members> members = new ArrayList();
    /**
     * JavaFX 控制器初始化方法。在界面加载后自动调用此方法，用于初始化控制器的状态、设置事件监听器等。
     *
     * @throws Exception 如果在初始化过程中发生异常，将抛出异常
     */
    @FXML
    private void initialize() throws Exception{
// 使用用户数据更新TableView
        initializepage();
        tableview.setRowFactory(tv -> {
            TableRow<Members> row = new TableRow<>();
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
     * 初始化分页控件的方法。该方法用于设置分页控件的总页数和监听器，以便在页面切换时加载对应页的数据。
     */
    private void initializepage(){
        pagetable.setPageCount(CalculatepageCount());
        pagetable.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    /**
     * 计算并返回分页总数的方法。该方法通过连接数据库获取成员总记录数，然后根据每页显示的数量计算总页数。
     *
     * @return 分页总数
     */
    private int CalculatepageCount(){
        try (Connection connection=DruidDataSourceUtil.getConnection()){
            MembersDAO mmDao=new MembersDAO(connection);
            return(int)Math.ceil((double) mmDao.gettotalRecords()/itempage);

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 清空输入区域的方法。该方法用于将文本框、日期选择器和性别按钮组中的内容清空，以便用户输入新的成员信息。
     */
    private void clearInputFields() {
        // 清空文本框
        Addid.clear();
        Addname.clear();
        Addphone.clear();
        AddPoints.clear();
        AddLevel.clear();
        AddStoredValue.clear();
        // 清空日期选择器
        Adddate.setValue(null);
        // 清空性别按钮组
        sex.selectToggle(null);
    }
    /**
     * 加载指定页码的数据到表格视图的方法。该方法从数据库中获取分页数据并更新 JavaFX 表格视图，以显示成员信息。
     *
     * @param pageindex 要加载的页码，从0开始。
     */
    private void loadPageData(int pageindex) {
        try (Connection connection = DruidDataSourceUtil.getConnection()) {
            MembersDAO mmDAO = new MembersDAO(connection);
            List<Members> pageData = mmDAO.selectPaged(pageindex, itempage);
            updateTableView(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 更新 JavaFX 表格视图的方法。该方法接受成员列表作为参数，然后将成员信息设置到表格视图的相应列中。
     *
     * @param members 要在表格视图中显示的成员列表。
     */
    private void updateTableView(List<Members> members) {

        tableview.getItems().setAll(members);
// 为每列设置单元格值工厂，以从用户对象中获取属性值显示。
        MemberID.setCellValueFactory(new PropertyValueFactory<>
                ("MemberID"));
        MemberName.setCellValueFactory(new PropertyValueFactory<>
                ("MemberName"));
        Gender.setCellValueFactory(new PropertyValueFactory<>
                ("Gender"));
        MembershipLevel.setCellValueFactory(new PropertyValueFactory<>
                ("MembershipLevel"));
        StoredValue.setCellValueFactory(new PropertyValueFactory<>
                ("StoredValue"));
        MembershipPoints.setCellValueFactory(new PropertyValueFactory<>
                ("MembershipPoints"));
        PhoneNumber.setCellValueFactory(new PropertyValueFactory<>
                ("PhoneNumber"));
        BirthDate.setCellValueFactory(new PropertyValueFactory<>
                ("BirthDate"));
    }

}
