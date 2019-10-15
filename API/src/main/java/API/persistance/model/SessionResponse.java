package API.persistance.model;

public class SessionResponse {

    private Boolean answer;
    private String refreshToken;

    public SessionResponse(){}

    public SessionResponse(Boolean answer, String refreshToken) {
        this.answer = answer;
        this.refreshToken = refreshToken;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "SessionResponse{" +
                "answer=" + answer +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
