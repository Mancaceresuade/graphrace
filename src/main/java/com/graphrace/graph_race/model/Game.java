package com.graphrace.graph_race.model;

import java.util.*;

public class Game {
    private String gameId;
    private String algoritmo; // DFS o BFS
    private String nombreAnfitrion;
    private Map<String, Jugador> jugadores;
    private List<String> recorridoCorrecto;
    private Map<String, Integer> progresoJugadores; // nombreJugador -> índice en recorridoCorrecto
    private boolean iniciado;
    private String ganador;
    private Map<String, List<String>> grafo; // Representación del grafo

    public Game(String gameId, String algoritmo, String nombreAnfitrion) {
        this.gameId = gameId;
        this.algoritmo = algoritmo;
        this.nombreAnfitrion = nombreAnfitrion;
        this.jugadores = new HashMap<>();
        this.recorridoCorrecto = new ArrayList<>();
        this.progresoJugadores = new HashMap<>();
        this.iniciado = false;
        this.ganador = null;
        this.grafo = new HashMap<>();
        
        // Inicializar un grafo simple de ejemplo
        inicializarGrafoEjemplo();
        calcularRecorridoCorrecto();
    }

    private void inicializarGrafoEjemplo() {
        // Grafo simple: A -> B -> C -> D -> E
        // Con algunas conexiones adicionales
        grafo.put("A", Arrays.asList("B", "C"));
        grafo.put("B", Arrays.asList("C", "D"));
        grafo.put("C", Arrays.asList("D"));
        grafo.put("D", Arrays.asList("E"));
        grafo.put("E", new ArrayList<>());
    }

    private void calcularRecorridoCorrecto() {
        String origen = "A";
        String destino = "E";
        
        if ("DFS".equals(algoritmo)) {
            recorridoCorrecto = dfs(origen, destino);
        } else { // BFS
            recorridoCorrecto = bfs(origen, destino);
        }
    }

    private List<String> dfs(String origen, String destino) {
        Set<String> visitados = new HashSet<>();
        List<String> camino = new ArrayList<>();
        dfsRecursivo(origen, destino, visitados, camino);
        return camino;
    }

    private boolean dfsRecursivo(String actual, String destino, Set<String> visitados, List<String> camino) {
        visitados.add(actual);
        camino.add(actual);
        
        if (actual.equals(destino)) {
            return true;
        }
        
        List<String> adyacentes = grafo.getOrDefault(actual, new ArrayList<>());
        for (String vecino : adyacentes) {
            if (!visitados.contains(vecino)) {
                if (dfsRecursivo(vecino, destino, visitados, camino)) {
                    return true;
                }
            }
        }
        
        camino.remove(camino.size() - 1);
        return false;
    }

    private List<String> bfs(String origen, String destino) {
        Queue<String> cola = new LinkedList<>();
        Map<String, String> padre = new HashMap<>();
        Set<String> visitados = new HashSet<>();
        
        cola.offer(origen);
        visitados.add(origen);
        padre.put(origen, null);
        
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            
            if (actual.equals(destino)) {
                // Reconstruir camino
                List<String> camino = new ArrayList<>();
                String nodo = destino;
                while (nodo != null) {
                    camino.add(0, nodo);
                    nodo = padre.get(nodo);
                }
                return camino;
            }
            
            List<String> adyacentes = grafo.getOrDefault(actual, new ArrayList<>());
            for (String vecino : adyacentes) {
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    padre.put(vecino, actual);
                    cola.offer(vecino);
                }
            }
        }
        
        return new ArrayList<>(); // No hay camino
    }

    public void agregarJugador(Jugador jugador) {
        jugadores.put(jugador.getNombre(), jugador);
        progresoJugadores.put(jugador.getNombre(), 0);
    }

    public boolean validarMovimiento(String nombreJugador, String nodoSeleccionado) {
        Integer progresoActual = progresoJugadores.get(nombreJugador);
        if (progresoActual == null || recorridoCorrecto.isEmpty()) {
            return false;
        }
        
        if (progresoActual < recorridoCorrecto.size() - 1) {
            String siguienteNodoEsperado = recorridoCorrecto.get(progresoActual + 1);
            if (siguienteNodoEsperado.equals(nodoSeleccionado)) {
                progresoJugadores.put(nombreJugador, progresoActual + 1);
                Jugador jugador = jugadores.get(nombreJugador);
                if (jugador != null) {
                    jugador.setPosicionActual(progresoActual + 1);
                }
                
                // Verificar si ganó
                if (progresoActual + 1 == recorridoCorrecto.size() - 1) {
                    ganador = nombreJugador;
                    if (jugador != null) {
                        jugador.setHaGanado(true);
                    }
                    return true; // Movimiento correcto y ganó
                }
                return true; // Movimiento correcto pero no ganó aún
            }
        }
        return false; // Movimiento incorrecto
    }

    public Map<String, List<String>> getGrafo() {
        return grafo;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public String getNombreAnfitrion() {
        return nombreAnfitrion;
    }

    public void setNombreAnfitrion(String nombreAnfitrion) {
        this.nombreAnfitrion = nombreAnfitrion;
    }

    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Map<String, Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<String> getRecorridoCorrecto() {
        return recorridoCorrecto;
    }

    public void setRecorridoCorrecto(List<String> recorridoCorrecto) {
        this.recorridoCorrecto = recorridoCorrecto;
    }

    public Map<String, Integer> getProgresoJugadores() {
        return progresoJugadores;
    }

    public void setProgresoJugadores(Map<String, Integer> progresoJugadores) {
        this.progresoJugadores = progresoJugadores;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }
}

