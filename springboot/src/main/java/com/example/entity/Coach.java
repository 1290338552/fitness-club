package com.example.entity;

/**
 * 教练实体类
 */
public class Coach extends Account {
    private String img;      // 教练图片
    private String position; // 教练职位（原name字段）
    private String price;    // 价格
    private Integer num;     // 可预约数量

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    
    // 为了兼容旧代码，保留原有的getter/setter别名
    public String getCoach() {
        return getName();
    }

    public void setCoach(String coach) {
        setName(coach);
    }
}

