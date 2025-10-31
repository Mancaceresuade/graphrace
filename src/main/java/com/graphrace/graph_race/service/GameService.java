package com.graphrace.graph_race.service;

import com.graphrace.graph_race.model.Game;
import com.graphrace.graph_race.model.Jugador;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {
    private Map<String, Game> juegos;

    public GameService() {
        this.juegos = new HashMap<>();
    }

    public Game crearJuego(String algoritmo, String nombreAnfitrion) {
        String gameId = generarGameId();
        Game juego = new Game(gameId, algoritmo, nombreAnfitrion);
        
        // Agregar al anfitrión como jugador
        Jugador anfitrion = new Jugador(nombreAnfitrion, gameId);
        juego.agregarJugador(anfitrion);
        
        juegos.put(gameId, juego);
        return juego;
    }

    public Game unirseAJuego(String gameId, String nombreJugador) {
        Game juego = juegos.get(gameId);
        if (juego != null && !juego.isIniciado()) {
            Jugador jugador = new Jugador(nombreJugador, gameId);
            juego.agregarJugador(jugador);
            return juego;
        }
        return null;
    }

    public Game obtenerJuego(String gameId) {
        return juegos.get(gameId);
    }

    public void iniciarJuego(String gameId) {
        Game juego = juegos.get(gameId);
        if (juego != null) {
            juego.setIniciado(true);
        }
    }

    public boolean validarMovimiento(String gameId, String nombreJugador, String nodoSeleccionado) {
        Game juego = juegos.get(gameId);
        if (juego != null && juego.isIniciado()) {
            return juego.validarMovimiento(nombreJugador, nodoSeleccionado);
        }
        return false;
    }

    private String generarGameId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

