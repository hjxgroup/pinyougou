package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//饼状图实体类
public class ChartResult implements Serializable {
    private List<Map<String,String>> seriesData;
    private Map<String,String> selected;

    public List<Map<String,String>> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<Map<String,String>> seriesData) {
        this.seriesData = seriesData;
    }

    public Map<String, String> getSelected() {
        return selected;
    }

    public void setSelected(Map<String, String> selected) {
        this.selected = selected;
    }
}
