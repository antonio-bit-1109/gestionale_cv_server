package org.example.progetto_gestionale_cv_server.CV.DTOs.resp;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;

public class Cv_Msg_response {
    private BaseDTO cv;
    private String message;

    public Cv_Msg_response(BaseDTO cv, String msg) {
        this.cv = cv;
        this.message = msg;
    }

    public BaseDTO getCv() {
        return cv;
    }

    public void setCv(BaseDTO cv) {
        this.cv = cv;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
