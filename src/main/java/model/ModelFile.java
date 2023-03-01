package model;

import database.Dbconn;
import entity.FileDB;
import props.Props;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import webapptest.database.Dbconn;
//import webapptest.entities.User;
import java.sql.Blob;

public class ModelFile {

    private final Dbconn dbf;
    private final Props props;
    private FileDB file;

    private final List<FileDB> filemodel;

    public ModelFile() throws SQLException, ClassNotFoundException, IOException {
        dbf = Dbconn.getInstance();
        props = Props.getInstance();
        filemodel = new ArrayList<>();
    }

    public List<FileDB> fList(String BE, String code, StringBuilder from, StringBuilder to) throws SQLException, IOException {

        from.deleteCharAt(2).deleteCharAt(4).delete(4,6);
        to.deleteCharAt(2).deleteCharAt(4).delete(4,6);

        if (filemodel.size() != 0) filemodel.clear();
        dbf.checkConn();
        dbf.setStatmt();
        String sql;

        if (code.equals("")){
            sql = "SELECT fName, content FROM " + props.getDbFilesTable() + " WHERE " +
                    "be = (SELECT ID_BE FROM "+ props.getDbBETable() +" WHERE BE = '" + BE + "') AND dateFrom = '"+ from.toString() +"' AND dateTo = '"+ to.toString() +"'";
        }else {
            sql = "SELECT fName, content FROM " + props.getDbFilesTable() + " WHERE " +
                    "idCA = "+ code +" AND be = (SELECT ID_BE FROM "+ props.getDbBETable() +" WHERE BE = '" + BE + "') AND dateFrom = '"+ from.toString() +"' AND dateTo = '"+ to.toString() +"'";
        }

        ResultSet recordset = dbf.Recordset(sql);
        while (recordset.next()) {

            if (file == null) file = new FileDB();
            file.setContent(recordset.getBlob("content"));
            file.setFilename(recordset.getString("fName"));
            filemodel.add(file);
            file  = null;
        }
        dbf.closeRecordSet();
        dbf.closeStatement();
        //dbf.closeConnection();
        return filemodel;
    }

}
