package com.graphrace.graph_race.controller;

import com.graphrace.graph_race.dto.WebSocketMessage;
import com.graphrace.graph_race.model.Game;
import com.graphrace.graph_race.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/joinGame")
    @SendTo("/topic/game/{gameId}")
    public WebSocketMessage joinGame(WebSocketMessage message) {
        String gameId = message.getGameId();
        String nombreJugador = message.getNombreJugador();

        Game juego = gameService.unirseAJuego(gameId, nombreJugador);
        
        WebSocketMessage response = new WebSocketMessage("playerJoined");
        response.setGameId(gameId);
        response.setNombreJugador(nombreJugador);
        
        if (juego != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("jugadores", juego.getJugadores().keySet());
            response.setData(data);
        } else {
            response.setMensaje("No se pudo unir al juego");
        }

        messagingTemplate.convertAndSend("/topic/game/" + gameId, response);
        return response;
    }

    @MessageMapping("/startGame")
    @SendTo("/topic/game/{gameId}")
    public WebSocketMessage startGame(WebSocketMessage message) {
        String gameId = message.getGameId();
        
        gameService.iniciarJuego(gameId);
        Game juego = gameService.obtenerJuego(gameId);
        
        WebSocketMessage response = new WebSocketMessage("gameStarted");
        response.setGameId(gameId);
        
        if (juego != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("grafo", juego.getGrafo());
            data.put("recorridoCorrecto", juego.getRecorridoCorrecto());
            data.put("algoritmo", juego.getAlgoritmo());
            response.setData(data);
        }

        messagingTemplate.convertAndSend("/topic/game/" + gameId, response);
        return response;
    }

    @MessageMapping("/move")
    public void move(WebSocketMessage message) {
        String gameId = message.getGameId();
        String nombreJugador = message.getNombreJugador();
        String nodoSeleccionado = message.getNodoSeleccionado();

        boolean esCorrecto = gameService.validarMovimiento(gameId, nombreJugador, nodoSeleccionado);
        Game juego = gameService.obtenerJuego(gameId);

        WebSocketMessage response = new WebSocketMessage();
        response.setGameId(gameId);
        response.setNombreJugador(nombreJugador);
        response.setNodoSeleccionado(nodoSeleccionado);

        if (esCorrecto) {
            if (juego != null && juego.getGanador() != null) {
                // Hay un ganador
                response.setType("winner");
                response.setMensaje("¡" + juego.getGanador() + " ha ganado!");
                
                Map<String, Object> data = new HashMap<>();
                data.put("ganador", juego.getGanador());
                response.setData(data);
            } else {
                // Movimiento correcto pero no ganó aún
                response.setType("moveCorrect");
                response.setMensaje("Movimiento correcto");
                
                if (juego != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("progreso", juego.getProgresoJugadores());
                    response.setData(data);
                }
            }
        } else {
            // Movimiento incorrecto
            response.setType("moveIncorrect");
            response.setMensaje("Movimiento incorrecto");
        }

        messagingTemplate.convertAndSend("/topic/game/" + gameId, response);
    }
}

