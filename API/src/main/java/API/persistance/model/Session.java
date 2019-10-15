package API.persistance.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="ACCOUNT_SESSION_TRACKING")
public class Session {

    @Id
    @Column(name="session_token")
    private String sessionToken;
    @Column(name="account_id")
    private Integer accountId;
    @Column(name="refresh_token")
    private String refreshToken;
    @Column(name="request_number")
    private Integer requestNumber;
    @Column(name="session_start_time")
    private LocalDateTime sessionStartTime;
    @Column(name="session_last_used")
    private LocalDateTime sessionLastUsed;

    public Session(String sessionToken, Integer accountId,String refreshToken, Integer requestNumber, LocalDateTime sessionStartTime, LocalDateTime sessionLastUsed) {
        this.sessionToken = sessionToken;
        this.accountId = accountId;
        this.refreshToken = refreshToken;
        this.requestNumber = requestNumber;
        this.sessionStartTime = sessionStartTime;
        this.sessionLastUsed = sessionLastUsed;
    }

    public Session(){}

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(LocalDateTime sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public LocalDateTime getSessionLastUsed() {
        return sessionLastUsed;
    }

    public void setSessionLastUsed(LocalDateTime sessionLastUsed) {
        this.sessionLastUsed = sessionLastUsed;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionToken='" + sessionToken + '\'' +
                ", accountId=" + accountId +
                ", refreshToken='" + refreshToken + '\'' +
                ", requestNumber=" + requestNumber +
                ", sessionStartTime=" + sessionStartTime +
                ", sessionLastUsed=" + sessionLastUsed +
                '}';
    }
}
