package org.example.progetto_gestionale_cv_server.CV.DTOs.resp;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;

import java.util.List;

public class Get_All_cv_DTO {

    private List<BaseDTO> listaCV;
    private String message;

    //costrutt
    public Get_All_cv_DTO(List<BaseDTO> listacv, String msg) {
        this.listaCV = listacv;
        this.message = msg;
    }

    public List<BaseDTO> getListaCV() {
        return listaCV;
    }

    public void setListaCV(List<BaseDTO> listaCV) {
        this.listaCV = listaCV;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
