package com.graphrace.graph_race.controller;

import com.graphrace.graph_race.model.Game;
import com.graphrace.graph_race.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public String index() {
        return "redirect:/crear.html";
    }


    @PostMapping("/api/crear-partida")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> crearPartida(@RequestBody Map<String, String> request) {
        String nombreAnfitrion = request.get("nombreAnfitrion");
        String algoritmo = request.get("algoritmo");

        if (nombreAnfitrion == null || nombreAnfitrion.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "El nombre del anfitrión es requerido");
            return ResponseEntity.badRequest().body(error);
        }

        if (algoritmo == null || (!algoritmo.equals("DFS") && !algoritmo.equals("BFS"))) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "El algoritmo debe ser DFS o BFS");
            return ResponseEntity.badRequest().body(error);
        }

        Game juego = gameService.crearJuego(algoritmo, nombreAnfitrion);
        
        Map<String, Object> response = new HashMap<>();
        response.put("gameId", juego.getGameId());
        response.put("link", "/ingresar.html?gameId=" + juego.getGameId());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/juego/{gameId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerJuego(@PathVariable String gameId) {
        Game juego = gameService.obtenerJuego(gameId);
        
        if (juego == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Juego no encontrado");
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("gameId", juego.getGameId());
        response.put("algoritmo", juego.getAlgoritmo());
        response.put("nombreAnfitrion", juego.getNombreAnfitrion());
        response.put("iniciado", juego.isIniciado());
        response.put("jugadores", juego.getJugadores().keySet());
        
        if (juego.isIniciado()) {
            response.put("grafo", juego.getGrafo());
            response.put("recorridoCorrecto", juego.getRecorridoCorrecto());
        }

        return ResponseEntity.ok(response);
    }
}

