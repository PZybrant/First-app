package SQL;

import javax.persistence.*;


/**
 * Created by $(USER) on $(DATE)
 */
@Entity
public class LoginData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "login_id")
    private int loginId;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "loginData", fetch = FetchType.LAZY)
    private Forwarder forwarder;

    private LoginData() {
    }
//----------------------------------------------------------------------------------------------------------------------
    public LoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }
//----------------------------------------------------------------------------------------------------------------------
    public int getId() {
        return loginId;
    }
//----------------------------------------------------------------------------------------------------------------------
    public String getLogin() {
        return login;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setLogin(String login) {
        this.login = login;
    }
//----------------------------------------------------------------------------------------------------------------------
    public String getPassword() {
        return password;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setPassword(String password) {
        this.password = password;
    }
//----------------------------------------------------------------------------------------------------------------------
    public Forwarder getUser() {
        return forwarder;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setUser(Forwarder user) {
        this.forwarder = user;
    }
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "LoginData{" +
                "loginId=" + loginId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

//----------------------------------------------------------------------------------------------------------------------
}
