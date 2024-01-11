package com.example.kechengsheji;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
/**
 * Sales 类是表示销售信息的数据模型。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sales implements Comparable<Sales>{
    @ExcelProperty(value = "单号")
    private String SaleID;//单号
    @ExcelProperty(value = "产品号")
    private String ProductID;//产品号
    @ExcelProperty(value = "会员号")
    private String MemberID;//会员号
    @ExcelProperty(value = "销售日期")
    private String SaleDate;//销售日期
    @ExcelProperty(value = "销售量")
    private String QuantitySold;//销售数量
    @ExcelProperty(value = "销售额")
    private String TotalAmount;//销售总额
    /**
     * 实现 Comparable 接口的 compareTo 方法，用于比较 Sales 对象的 SaleID 字段。
     *
     * @param o 要比较的另一个 Sales 对象
     * @return 返回比较结果
     */
    @Override
    public int compareTo(Sales o) {
        return CharSequence.compare(this.SaleID,o.SaleID);
    }
}
