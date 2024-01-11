package com.example.kechengsheji;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表示美容产品的信息的Java类。
 * 实现了Comparable接口，使得对象可以按照产品编号进行比较排序。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeautyProducts implements Comparable<BeautyProducts>{

    @ExcelProperty(value = "id")
    private String ProductID;//产品编号
    @ExcelProperty(value = "名称")
    private String ProductName;//产品名称
    @ExcelProperty(value = "净含量")
    private String NetWeight;//产品净含量
    @ExcelProperty(value = "描述")
    private String Efficacy;//产品描述
    @ExcelProperty(value = "价格")
    private String Price;//价格
    @ExcelProperty(value = "积分")
    private String Points;//产品积分
    @ExcelProperty(value = "库存")
    private String StockQuantity;//库存
    @ExcelProperty(value = "临期")
    private String ExpiryDate;//保质期至
    /**
     * 实现Comparable接口的比较方法，按照产品编号进行比较。
     * @param o 要比较的BeautyProducts对象
     * @return 负整数、零或正整数，表示当前对象小于、等于或大于指定对象
     */
    @Override
    public int compareTo(BeautyProducts o) {
        return CharSequence.compare(this.ProductID,o.ProductID);
    }
}
