package com.ocean.uploadpdfimageapp;

public class RvItemModel {

    private String fileTittle;
    private String filePath;
    private Boolean fileType;

    public RvItemModel(String fileTittle, String filePath, Boolean fileType) {
        this.fileTittle = fileTittle;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public String getFileTittle() {
        return fileTittle;
    }

    public void setFileTittle(String fileTittle) {
        this.fileTittle = fileTittle;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getFileType() {
        return fileType;
    }

    public void setFileType(Boolean fileType) {
        this.fileType = fileType;
    }
}
