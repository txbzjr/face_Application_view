package com.example.jr.face_application_view;
import java.util.List;
public class Img_Json_Bean {
    private int count;
    private int IsEmpty;
    private List<FileInfoBean> File_Info;
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getIsEmpty() {
        return IsEmpty;
    }
    public void setIsEmpty(int IsEmpty) {
        this.IsEmpty = IsEmpty;
    }
    List<FileInfoBean> getFile_Info() { return File_Info; }
    public void setFile_Info(List<FileInfoBean> File_Info) {
        this.File_Info = File_Info;
    }
    public static class FileInfoBean {
        private String file_address;
        private String file_time;
        public String getFile_address() {
            return file_address;
        }
        public void setFile_address(String file_address) {
            this.file_address = file_address;
        }
        public String getFile_time() {
            return file_time;
        }
        public void setFile_time(String file_time) {
            this.file_time = file_time;
        }
    }
}
