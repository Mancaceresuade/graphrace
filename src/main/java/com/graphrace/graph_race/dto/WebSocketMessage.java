package com.graphrace.graph_race.dto;

public class WebSocketMessage {
    private String type; // joinGame, startGame, move, winner
    private String gameId;
    private String nombreJugador;
    private String nodoSeleccionado;
    private String algoritmo;
    private String mensaje;
    private Object data;

    public WebSocketMessage() {
    }

    public WebSocketMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    public void setNodoSeleccionado(String nodoSeleccionado) {
        this.nodoSeleccionado = nodoSeleccionado;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

