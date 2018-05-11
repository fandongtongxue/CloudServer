package me.fandong.cloudserver.Model;

public class WingManProductModel {
    private String id;
    private String title;
    private String price;
    private String content;

    public WingManProductModel(String id, String title, String price, String content) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
