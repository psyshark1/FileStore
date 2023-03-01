package model;

import database.Dbconn;
import entity.UserData;
import props.Props;
import utils.SecurityUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.Properties;

public class ModelUserData{
    private final Dbconn db;
    private final Props props;
    private UserData user;
    private UserData chUser;
    private final String userTable;
    public static final String[] bE  = {"bE","bE1","bE2","bE3","bE4"};
    //public static final String mailLogin;
    //public static final String mailPass;

    public ModelUserData () throws SQLException, ClassNotFoundException, IOException {
        db = Dbconn.getInstance();
        props = Props.getInstance();
        userTable = props.getdbUserTable();
        //List<UserData> modelUserDataList = new ArrayList<>();
    }

    public UserData setUserData(String login, String pass) throws SQLException {
        //if (modelUserDataList.size() != 0) modelUserDataList.clear();
        String encPass = SecurityUtils.encPass(pass);
        StringBuilder sql = new StringBuilder("SELECT login, email, role, isActive FROM ").append(userTable).append(" WHERE login = '").append(login).append("' AND pass = '").append(encPass).append("'");

        db.checkConn();
        db.setStatmt();
        ResultSet recordset = db.Recordset(sql.toString());
        while (recordset.next()) {

            if (user == null) {
                user = new UserData();
            } else {
                user.destroy();
            }
            user.setLogin(recordset.getString("login"));
            user.setMail(recordset.getString("email"));
            user.setRole(recordset.getString("role"));
            user.setActive(recordset.getInt("isActive"));
            //model.add(user);
            //user = null;
        }
        db.closeRecordSet();
        db.closeStatement();
        db.closeConnection();
        recordset = null;

        sql.delete(0,sql.length());
        if (user != null && pass != null){
            db.checkConn();
            db.setStatmt();
            sql.append("UPDATE ").append(userTable).append(" SET lastLoginedDate = CURRENT_TIMESTAMP WHERE login = '").append(login).append("'");
            db.Exec(sql.toString());
            db.closeStatement();
            //db.closeConnection();
        }

        return user;
    }

    public UserData setUserData(String login) throws SQLException {
        //if (modelUserDataList.size() != 0) modelUserDataList.clear();

        db.checkConn();
        db.setStatmt();
        ResultSet recordset = db.Recordset("SELECT login, email, role, isActive FROM " + userTable + " WHERE login = '" + login + "'");
        while (recordset.next()) {

            if (chUser == null) {
                chUser = new UserData();
            } else {
                chUser.destroy();
            }
            chUser.setLogin(recordset.getString("login"));
            chUser.setRole(recordset.getString("role"));
            chUser.setActive(recordset.getInt("isActive"));
            //model.add(user);
            //user = null;
        }
        db.closeRecordSet();
        db.closeStatement();
        //db.closeConnection();
        recordset = null;

        return chUser;
    }

    public UserData resetPass (String login) throws SQLException, MessagingException {

        String mail = null;
        db.checkConn();
        db.setStatmt();
        ResultSet recordset = db.Recordset(
                "SELECT mail FROM OPENQUERY(ADSI, 'SELECT mail FROM ''LDAP://DC=org,DC=ru'' WHERE sAMAccountName = ''" + login + "''')");

        while (recordset.next()) {
            mail = recordset.getString("mail");
        }
        db.closeRecordSet();

        StringBuilder pass = new StringBuilder(SecurityUtils.setRandomPass());

        db.Exec("UPDATE "+ userTable +" SET pass = '"+ SecurityUtils.encPass(pass.toString()) + "' WHERE login = '"+ login +"'");
        db.closeStatement();
        //db.closeConnection();

        this.SendHTMLEmail(props.getEMail(), mail,"Пароль для входа",pass.toString(),props.getMailLogin(), props.getMailPass());

        pass.delete(0,pass.length()); pass = null;

        if (chUser == null) {
            chUser = new UserData();
        } else {
            chUser.destroy();
        }
        chUser.setMail(mail);

        return chUser;

    }

    public boolean checkLogin(String login, boolean LDAP) throws SQLException {

        db.checkConn();
        db.setStatmt();
        ResultSet recordset;

        if (LDAP) {
            recordset = db.Recordset("SELECT sAMAccountName FROM OPENQUERY(ADSI, 'SELECT sAMAccountName " +
                    "FROM ''LDAP://DC=org,DC=ru'' WHERE sAMAccountName = ''" + login + "''')");
        }else{
            recordset = db.Recordset("SELECT login FROM " + userTable + " WHERE login = '" + login + "'" );
        }

        //if (db.Exec(sql)) {
        while (recordset.next()) {
            db.closeStatement();
            //db.closeConnection();
            return true;
        }
        db.closeStatement();
        //db.closeConnection();
        return false;
    }

    public String addUser (String login, String role) throws SQLException, MessagingException {

        String mail = null;
        db.checkConn();
        db.setStatmt();
        ResultSet recordset = db.Recordset(
                "SELECT mail FROM OPENQUERY(ADSI, 'SELECT mail FROM ''LDAP://DC=org,DC=ru'' WHERE sAMAccountName = ''" + login + "''')");

        while (recordset.next()) {
            mail = recordset.getString("mail");
        }
        db.closeRecordSet();

        StringBuilder pass = new StringBuilder(SecurityUtils.setRandomPass());

        StringBuilder roleBuilder = new StringBuilder(role);
        for (String eB : bE) {
            roleBuilder.append(",").append(eB);
        }

        db.Exec("INSERT INTO " + userTable + " (login,pass,role,email,isActive,joinedDate) VALUES ('"+ login +"','"+ SecurityUtils.encPass(pass.toString()) +"','"+ roleBuilder.toString() +"','"+ mail +"',1,CURRENT_TIMESTAMP)");
        db.closeStatement();
        //db.closeConnection();

        this.SendHTMLEmail(props.getEMail(), mail,"Пароль для входа",pass.toString(), props.getMailLogin(), props.getMailPass());

        pass.delete(0,pass.length());

        return mail;
    }

    private void SendHTMLEmail (String from, String to, String subj,String HTMLmess, String login, String pass) throws MessagingException {

        Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "false");
        prop.put("mail.smtp.host", props.getSmtp_host());
        prop.put("mail.smtp.port", props.getSmtp_port());

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, pass);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subj);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(HTMLmess, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);

        try{
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String login, String accessRole, String[] roles,String block, String curUser) throws SQLException {

        StringBuilder sql = new StringBuilder("UPDATE " + userTable + " SET role = '");

        sql.append(accessRole);

        if (roles.length != 0) {
            for (String role : roles) {
                sql.append(",").append(role);
            }
        }

        if (block!=null) {
            if (block.equals("on")) {
                sql.append("',isActive = 0,");
            } else {
                sql.append("',isActive = 1,");
            }
        }else {
            sql.append("',isActive = 1,");
        }

        sql.append("lastModUser = '").append(curUser).append("' ");
        sql.append("WHERE login = '").append(login).append("'");

        db.checkConn();
        db.setStatmt();
        db.Exec(sql.toString());
        db.closeStatement();
        //db.closeConnection();
    }
}
