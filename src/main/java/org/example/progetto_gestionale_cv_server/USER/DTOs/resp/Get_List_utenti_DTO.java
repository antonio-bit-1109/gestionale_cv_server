package org.example.progetto_gestionale_cv_server.USER.DTOs.resp;

import java.util.List;

public class Get_List_utenti_DTO {

    private List<Get_Utente_DTO> listaUtenti;
    private String msg;

    //costrutt
    public Get_List_utenti_DTO(String msg, List<Get_Utente_DTO> listaUtenti) {
        this.listaUtenti = listaUtenti;
        this.msg = msg;
    }

    public List<Get_Utente_DTO> getListaUtenti() {
        return listaUtenti;
    }

    public void setListaUtenti(List<Get_Utente_DTO> listaUtenti) {
        this.listaUtenti = listaUtenti;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
