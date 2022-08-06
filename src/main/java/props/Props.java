package props;

import java.io.IOException;

import java.util.Properties;

public class Props {
    /*
    * Класс с данными для отправки почты
    * */
    private static volatile Props instance;
    private static String smtp_host;
    private static String smtp_port;
    private static String mailLogin;
    private static String mailPass;
    private static String email;
    private static String temp_folder;
    private static String dbUserTable;
    private static String dbFilesTable;
    private static String dbBETable;


    public Props() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("../config/props.properties"));

        smtp_host = props.getProperty("smtp_host");
        smtp_port = props.getProperty("smtp_port");
        mailLogin = props.getProperty("mailLogin");
        mailPass = props.getProperty("mailPass");
        email = props.getProperty("email");
        temp_folder = props.getProperty("temp_folder");
        dbUserTable = props.getProperty("dbUserTable");
        dbFilesTable = props.getProperty("dbFilesTable");
        dbBETable = props.getProperty("dbBETable");
    }

    public static Props getInstance() throws IOException {
        if (instance == null) {
            synchronized (Props.class){
                if (instance == null) {
                    instance = new Props();
                }
            }
        }
        return instance;
    }

    public String getSmtp_host(){
        return smtp_host;
    }

    public String getSmtp_port(){
        return smtp_port;
    }

    public String getMailLogin(){
        return mailLogin;
    }

    public String getMailPass(){
        return mailPass;
    }

    public String getEMail(){
        return email;
    }

    public String getTempFolder(){
        return temp_folder;
    }

    public String getDbFilesTable(){
        return dbFilesTable;
    }

    public String getdbUserTable(){
        return dbUserTable;
    }

    public String getDbBETable(){
        return dbBETable;
    }
}
