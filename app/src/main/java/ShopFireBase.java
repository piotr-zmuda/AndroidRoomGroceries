public class ShopFireBase {
    String name;
    String desc;
    Integer range;

    public ShopFireBase(String name, String desc, Integer range){
        this.name=name;
        this.desc=desc;
        this.range=range;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRange() {
        return range;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}
