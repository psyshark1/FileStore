package entity;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class FileDB {
    /*
    * Модель выгружаемых из БД файлов
    * */
    private Blob content;
    private String fname;

    public FileDB(){}

    public void setContent (Blob bCnt) throws SQLException, IOException {

        this.content = bCnt;
    }
    public void setFilename(String str){
        this.fname = str;
    }

    public Blob getContent(){return this.content;}
    public String getFilename(){return this.fname;}
}
