package me.fandong.cloudserver.Model;

public class QiniuFileModel {
	private String fileName;
	private String fileSize;
	private String fileType;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Override
	public String toString() {
		return "QiniuFileModel{" +
				"fileName='" + fileName + '\'' +
				", fileSize='" + fileSize + '\'' +
				", fileType='" + fileType + '\'' +
				'}';
	}
}
