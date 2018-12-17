package me.fandong.cloudserver.Model;

public class FilePathModel {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FilePathModel{" +
                "filePath='" + filePath + '\'' +
                '}';
    }
}
