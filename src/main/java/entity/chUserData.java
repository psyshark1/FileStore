package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class chUserData {
    /*
    * Модель изменяемого пользователя
    * */
    private static volatile chUserData instance;
    private String login;
    private List<String> role = new ArrayList<>();
    private String mail;
    private int active;

    public static chUserData getInstance() {
        if (instance == null) {
            synchronized (UserData.class){
                if (instance == null) {
                    instance = new chUserData();
                }
            }
        }
        return instance;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(String role) {
        this.role = Arrays.asList(role.split(","));
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public List<String> getRole() {
        return role;
    }

    public int getActive() {
        return active;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        chUserData userData = (chUserData) o;

        if (this.login != null){
            if (!this.login.equals(userData.login)) return false;
        }else {
            if (userData.login != null) return false;
        }

        if (this.role != null){
            if (!this.role.equals(userData.role)) return false;
        }else {
            if (userData.role != null) return false;
        }

        if (this.mail != null){
            return this.mail.equals(userData.mail);
        }else { return false;}

    }

    @Override
    public int hashCode(){
        int result = 0;
        if (this.login != null) {
            result = this.login.hashCode();
        }

        if (this.mail != null) {
            result = result + this.mail.hashCode();
        }

        if (this.role != null) {
            result = 31 * result + this.login.hashCode();
        }
        return result;
    }

    public void destroy(){
        this.login = null;
        this.role = null;
        this.mail = null;
    }

}
