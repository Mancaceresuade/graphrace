package com.graphrace.graph_race.model;

public class Jugador {
    private String nombre;
    private String gameId;
    private int posicionActual;
    private boolean haGanado;

    public Jugador(String nombre, String gameId) {
        this.nombre = nombre;
        this.gameId = gameId;
        this.posicionActual = 0;
        this.haGanado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(int posicionActual) {
        this.posicionActual = posicionActual;
    }

    public boolean isHaGanado() {
        return haGanado;
    }

    public void setHaGanado(boolean haGanado) {
        this.haGanado = haGanado;
    }
}

