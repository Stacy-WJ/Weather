package com.lifes.carry.weather.db;

import org.litepal.crud.DataSupport;

/**
 * @author Administrator
 * @time 2018-02-25 22:32
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Province extends DataSupport{
//    LitePal 的每个实体类  都需要继承 datasupport类
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getprovinceCode() {
        return provinceCode;
    }

    public void setprovinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
