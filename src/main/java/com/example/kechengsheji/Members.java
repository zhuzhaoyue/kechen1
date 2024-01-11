package com.example.kechengsheji;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
/**
 * 会员信息类，实现了 Comparable 接口用于比较。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Members implements Comparable<Members>{
    @ExcelProperty(value = "id")
    private String MemberID;//会员号
    @ExcelProperty(value = "姓名")
    private String MemberName;//会员姓名
    @ExcelProperty(value = "性别")
    private String Gender;//性别
    @ExcelProperty(value = "等级")
    private String MembershipLevel;//会员等级
    @ExcelProperty(value = "储值")
    private String StoredValue;//储值
    @ExcelProperty(value = "积分")
    private String MembershipPoints;//会员积分
    @ExcelProperty(value = "电话")
    private String PhoneNumber;//电话
    @ExcelProperty(value = "生日")
    private String BirthDate;
    /**
     * 实现 Comparable 接口的 compareTo 方法，按会员号升序排序。
     */
    @Override
    public int compareTo(Members o) {
        return CharSequence.compare(this.MemberID,o.MemberID);
    }
}
