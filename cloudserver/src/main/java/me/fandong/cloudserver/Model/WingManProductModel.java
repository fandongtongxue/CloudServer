package me.fandong.cloudserver.Model;

public class WingManProductModel {
    private int id;
    private String title;
    private String price;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WingManProductModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
