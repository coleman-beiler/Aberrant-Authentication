package API;

import javax.persistence.*;

@Entity
@Table(name="ACCOUNT_LOGIN_CREDENTIALS")
public class User {

    @Id
    @Column(name="account_id")
    private String user_id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="salt")
    private String salt;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
