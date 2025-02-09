package org.example.progetto_gestionale_cv_server.utility.Responses;

public class TokenResponse {

    private String Token;
    private String msg;

    //costrutt
    public TokenResponse(String token, String msg) {
        setMsg(msg);
        setToken(token);
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
