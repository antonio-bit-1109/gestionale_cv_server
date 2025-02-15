package org.example.progetto_gestionale_cv_server.USER.DTOs.resp;

public class Resp_get_utente_DTO {

    private String msg;
    private Get_Utente_DTO getUtenteDto;

    public Resp_get_utente_DTO(String msg, Get_Utente_DTO dto) {
        this.msg = msg;
        this.getUtenteDto = dto;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Get_Utente_DTO getGetUtenteDto() {
        return getUtenteDto;
    }

    public void setGetUtenteDto(Get_Utente_DTO getUtenteDto) {
        this.getUtenteDto = getUtenteDto;
    }
}
