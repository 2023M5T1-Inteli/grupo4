package br.edu.inteli.cc.m5.grupo;

import java.util.*;
import br.edu.inteli.cc.m5.grupo.Nodes;
import br.edu.inteli.cc.m5.grupo.Grid;
import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;

// Abaixo, estão as bibliotecas necessárias para a conexão com o Neo4J
import org.neo4j.driver.Session;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.AuthTokens;

public class AStar {

    public List<Nodes> findPath(Grid grid, double latStart, double lonStart, double latEnd, double lonEnd){
        // Calcula a distância de cada nó até o fim
        boolean gotStart = false;
        boolean gotEnd = false;
        Nodes start = null;
        Nodes end = null;
        LinkedList<Nodes> trueGrid = grid.getGrid();
        for (Nodes node : trueGrid){
            if (node.getLat() == latStart && node.getLon() == lonStart && !gotStart){
                start = node;
                gotStart = true;
            }
            if (node.getLat() == latEnd && node.getLon() == lonEnd && !gotEnd){
                end = node;
                gotEnd = true;
            }
            if (gotStart && gotEnd){
                break;
            }
        }
        if(start == null || end == null){
            System.out.println();
            return null;
        }
        for (Nodes node : trueGrid){
            node.setHScore(heuristic(node, end));
        }
        // Cria uma fila de nós ainda não visitados que são organizados pelo fScore (mais informações em fScore em Node)
        // e uma de visitados para não retornar a eles e entrar num loop.
        PriorityQueue<Nodes> unvisitedList = new PriorityQueue<Nodes>((node1, node2) -> Double.compare(node1.getFScore(), node2.getFScore()));
        HashSet<Nodes> visitedList = new HashSet<Nodes>();
        unvisitedList.add(start);
        start.setGScore(0.0);
        while (!unvisitedList.isEmpty()){ // Irá procurar uma rota enquanto não tiver mais nós para visitar.
            Nodes current = unvisitedList.remove();
            if (current.equals(end)){ // Se encontrar o nó final, retorne o caminho.
                return createPath(current);
            }
            visitedList.add(current);
            for (int ID : current.getEdges().keySet()){ // Como não encontrou o nó final olhe os vizinhos.
                Nodes neighbor = grid.getGrid().get(ID);
                if (visitedList.contains(neighbor)){
                    continue;
                }
                // Calcula o gScore dos próximos caminhos.
                double tentativeGScore = current.getGScore() + current.getEdges().get(neighbor.getID());

                if (tentativeGScore < neighbor.getGScore()) {
                    neighbor.setGScore(tentativeGScore);
                    neighbor.setParent(current);
                }
                if (!unvisitedList.contains(neighbor)) {
                    unvisitedList.add(neighbor);
                }
            }
        }
        return null;
    }

    // Cria o caminho depois o inverte, pois a rota é feita do caminho final e vai voltando até chegar o caminho inicial.
    private static List<Nodes> createPath(Nodes current){
        List<Nodes> path = new ArrayList<>();
        while (current != null){
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);

        sendToNeo4J(path);

        return path;
    }

    private static void sendToNeo4J(List<Nodes> path){

        CRUD_Neo4J neo4j = new CRUD_Neo4J();
        neo4j.connect("bolt://localhost:7687", "neo4j", "12345678");

        int aux = 0;
        int lastNodeID = 0;
        HashMap<Integer, Double> lastNodeNeighbor = new HashMap<>();
        for(Nodes node : path){
            neo4j.createNode(node.getID(), node.getLat(), node.getLon(), node.getElevation());
            if (aux != 0){
                neo4j.createRelationship(lastNodeID, node.getID(), lastNodeNeighbor.get(node.getID()));
            }
            lastNodeID = node.getID();
            lastNodeNeighbor = node.getEdges();
            aux++;
        }
        neo4j.disconnect();
    }

public class Neo4JConnector {
    private Driver driver;
    private Session session;

    // Método responsável por conectar o terminal com o localhost do Neo4J
    public void connect(String uri, String user, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        this.session = driver.session();
    }

    // Método responsável por enviar os dados do List path para o Neo4J
    public void sendToNeo4J(List<Nodes> path) {
        // Lógica para enviar dados para o Neo4J usando driver e session
    }

    // Método responsável por desconectar o terminal
    public void disconnect() {
        session.close();
        driver.close();
    }
}

    private static Double heuristic(Nodes node, Nodes goal){
        // Raio da Terra
        double r = 6371.0;
        
        //  Latitude e longitude inicial
        double x1 = Math.toRadians(node.getLat());
        double y1 = Math.toRadians(node.getLon());
        
        // Latitude e longitude do destino
        double x2 = Math.toRadians(goal.getLat());
        double y2 = Math.toRadians(goal.getLon());
        
        // Fórmula de haversine para calcular a distancia total
        
        // Ponto horizontal
        return (2 * r * Math.asin(Math.sqrt(Math.pow(Math.sin((x2 - x1) / 2), 2) + Math.cos(x1) * Math.cos(x2) * Math.pow(Math.sin((y2 - y1) / 2), 2))));
    }

    public static void main(String[] args){
        // Instancia do objeto example
        CRUD_Neo4J example = new CRUD_Neo4J();

        // Conectando o objeto no localhost do Neo4J
        example.connect("bolt://localhost:7687", "neo4j", "12345678");

        Scanner scanner = new Scanner(System.in);
    }  
}