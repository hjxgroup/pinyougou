package entity;

import java.io.Serializable;
//销售统计
public class MorrisData implements Serializable{
    //日期
    private String period;
    //销售量
    private String licensed;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLicensed() {
        return licensed;
    }

    public void setLicensed(String licensed) {
        this.licensed = licensed;
    }
}
