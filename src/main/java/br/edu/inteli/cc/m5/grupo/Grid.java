package br.edu.inteli.cc.m5.grupo;

// importações necessárias para utilização de alguns tipos abstratos de dados
import java.util.Optional;
import java.util.LinkedList;
//import br.edu.inteli.cc.m5.grupo.Nodes;
import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;
import br.edu.inteli.cc.m5.geo.Area;
import scala.collection.immutable.List;

public class Grid {

    // atributos da classe Grid
    private double latRegInit;
    private double lonRegInit;
    private double latRegEnd;
    private double lonRegEnd;
    private double height;
    private double length;
    private int lengthNodes;
    private int heightNodes;
    private LinkedList<Nodes> grid;

    // Constructor: um método que se roda 1 única vez e é responsável por receber parâmetros para a instância dos obejtos  e atribui-los aos atributos criados a cima
    // caso qualquer latitude ou longitude esteja fora do intervalo [-90, 90] e [-180, 180], respectivamente, o código retorna erro, pois não existem latitudes fora desses intervalos
    public Grid(double latRegInit, double lonRegInit, double latRegEnd, double lonRegEnd){
        
        if(latRegInit > 90 || latRegInit <  -90 || latRegEnd > 90 || latRegEnd <  -90 || lonRegInit > 180 || lonRegInit <  -180 || lonRegEnd > 180 || lonRegEnd <  -180){
            throw new IllegalArgumentException("Please input valid coordinates");
        }

        this.latRegInit = latRegInit;
        this.lonRegInit = lonRegInit;
        this.latRegEnd = latRegEnd;
        this.lonRegEnd = lonRegEnd;
        this.grid = new LinkedList<Nodes>();
        plotNodes();
        plotEdges();
    }

    // Método responsável por plotar todos os nós da malha, mas por enquanto sem as arestas
    private void plotNodes(){

        // numero de identificação de cada nó
        int id = 0;

        // latitude, longitude e elevação de cada nó no momento atual da iteração
        double currentLat = latRegInit;
        double currentLon = lonRegInit;

        // Dependendo do caso indicado pelo método checkCases() ele trata a criação da grid de uma forma diferente
        switch (checkCases()){

            case 0:
                // so adiciona 120m na longitude
                length = calculateDistance(lonRegInit, latRegInit, lonRegEnd, latRegEnd);
                lengthNodes = (int) (length)/120;

                for(int i = 0; i < lengthNodes; i++){
                    grid.add(new Nodes(id++, currentLon, currentLat));

                    currentLon = addLongitude(currentLon, currentLat);
                }
                break;

            case 1:
                //so diminui 120m na longitude
                length = calculateDistance(lonRegEnd, latRegEnd, lonRegInit, latRegInit);
                lengthNodes = (int) (length)/120;

                for(int i = 0; i < lengthNodes; i++){
                    grid.add(new Nodes(id++, currentLon, currentLat));

                    currentLon = subtractLongitude(currentLon, currentLat);
                }

                break;
            
            case 2:
                //para cada j adiciona 120m na longitude
                //para cada i diminui 120m na latitude
                length = calculateDistance(lonRegInit, latRegInit, lonRegEnd, latRegInit);
                height = calculateDistance(lonRegInit, latRegInit, lonRegInit, latRegEnd);

                lengthNodes = (int) (length)/120;
                heightNodes = (int) (height)/120;

                for(int i = 0; i < heightNodes; i++){
                    for(int j = 0; j < lengthNodes; j++){
                        grid.add(new Nodes(id++, currentLon, currentLat));

                        currentLon = addLongitude(currentLon, currentLat);
                    }
                    currentLat = subtractLatitude(currentLat);
                    currentLon = lonRegInit;
                }

                break;

            case 3:
                //para cada j adiciona 120m na longitude
                //para cada i adiciona 120m na latitude

                length = calculateDistance(lonRegInit, latRegEnd, lonRegEnd, latRegEnd);
                height = calculateDistance(lonRegInit, latRegEnd, lonRegInit, latRegInit);
                
                lengthNodes = (int) (length)/120;
                heightNodes = (int) (height)/120;

                for(int i = 0; i < heightNodes; i++){
                    for(int j = 0; j < lengthNodes; j++){
                        grid.add(new Nodes(id++, currentLon, currentLat));
                        currentLon = addLongitude(currentLon, currentLat);
                    }
                    currentLat = addLatitude(currentLat);
                    currentLon = lonRegInit;
                }

                break;

            case 4:
                //para cada j diminui 120m na longitude
                //para cada i diminui 120m na latitude
                length = calculateDistance(lonRegEnd, latRegInit, lonRegInit, latRegInit);
                height = calculateDistance(lonRegEnd, latRegInit, lonRegEnd, latRegEnd);
                
                lengthNodes = (int) (length)/120;
                heightNodes = (int) (height)/120;
                
                for(int i = 0; i < heightNodes; i++){
                    for(int j = 0; j < lengthNodes; j++){
                        grid.add(new Nodes(id++, currentLon, currentLat));

                        currentLon = subtractLongitude(currentLon, currentLat);
                    }
                    currentLat = subtractLatitude(currentLat);
                    currentLon = lonRegInit;
                }

                break;

            case 5:
                //para cada j diminui 120m na longitude
                //para cada i adiciona 120m na latitude

                length = calculateDistance(lonRegEnd, latRegEnd, lonRegInit, latRegEnd);
                height = calculateDistance(lonRegEnd, latRegEnd, lonRegEnd, latRegInit);
                
                lengthNodes = (int) (length)/120;
                heightNodes = (int) (height)/120;

                for(int i = 0; i < heightNodes; i++){
                    for(int j = 0; j < lengthNodes; j++){
                        grid.add(new Nodes(id++, currentLon, currentLat));
                        currentLon = subtractLongitude(currentLon, currentLat);
                    }
                    currentLat = addLatitude(currentLat);
                    currentLon = lonRegInit;
                }

                break;

            case 6:
                // devolve erro
                throw new IllegalArgumentException("Please input a valid area");
        }
    }

    public LinkedList<Nodes> getGrid(){
        return grid;
    }

    // O objetivo desse código é verificar quais vizinhos o nó possui,
    // calcular o peso entre o nó e seu vizinho(1), e adicionar no nó
    // quais são seus vizinhos e qual é o peso da aretsa que os conecta (2).
    // (Todos os IFs seguem a mesma lógica).
    // - Jonas
    private void plotEdges(){
        for(Nodes node : grid){
            int nodeID = node.getID();
            if (nodeID + lengthNodes < (lengthNodes * heightNodes)){ // Verifica se existe um vizinho embaixo.
                int neighborNodeID = nodeID + lengthNodes; // Vê qual é o ID do seu vizinho.
                Nodes neighborNode = grid.get(neighborNodeID); // Busca o nó vizinho.
                double weight = calculateNeighborWeight(node, neighborNode); // (1)
                Edge edge = new Edge(neighborNodeID, weight); // Cria a aresta que relaciona os dois nós.
                node.addNeighbor(edge); // (2)
            };
            if ((nodeID + 1) % lengthNodes != 0){ // Verifica se existe um vizinho na direita.
                int neighborNodeID = nodeID + 1;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };
            if (nodeID - lengthNodes >= 0){ // Verifica se existe um vizinho acima.
                int neighborNodeID = nodeID - lengthNodes;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };
            if (nodeID % lengthNodes != 0){ // Verifica se existe um vizinho na esquerda.
                int neighborNodeID = nodeID - 1;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };
            if (nodeID + lengthNodes + 1 < (lengthNodes * heightNodes) && (nodeID + 1) % lengthNodes != 0) { // Verifica se existe um vizinho na diagonal inferior direita.
                int neighborNodeID = nodeID + lengthNodes + 1;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };
            if (nodeID + lengthNodes - 1 < (lengthNodes * heightNodes) && nodeID % lengthNodes != 0) { // Verifica se existe um vizinho na diagonal inferior esquerda.
                int neighborNodeID = nodeID + lengthNodes - 1;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };
            if (nodeID - lengthNodes + 1 >= 0 && (nodeID + 1) % lengthNodes != 0) { // Verifica se existe um vizinho na diagonal superior direita.
                int neighborNodeID = nodeID - lengthNodes + 1;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };
            if (nodeID - lengthNodes - 1 >= 0 && nodeID % lengthNodes != 0) { // Verifica se existe um vizinho na diagonal superior esquerda.
                int neighborNodeID = nodeID - lengthNodes - 1;
                Nodes neighborNode = grid.get(neighborNodeID);
                double weight = calculateNeighborWeight(node, neighborNode);
                Edge edge = new Edge(neighborNodeID, weight);
                node.addNeighbor(edge);
            };            
        }
    }

    // Método que calcula o peso de cada aresta baseado no modulo da diferença de (altura + distancia)/2
    // por fim é dividido por 2 visto que estamos dando uma importancia de 50% para a altura (visibilidade) a 50%
    private double calculateNeighborWeight(Nodes node1, Nodes node2){
        double distance;

        if (node2.getLat() == node1.getLat() || node2.getLon() == node1.getLon()){
            distance = 120; //se a latitude ou a longitude for a mesma, significa que os pontos estão um do lado do outro, portanto são vetices adjacentes de um mesmo quadrado
        } 
        else {
            distance = 120 * Math.sqrt(2); // ja se for diferente, significa que são vértices opostas e sua distancia é representada pela diagonal do quadrado
        }

        return (Math.abs(node2.getElevation() - node1.getElevation()) + distance)/2;
    }

    private int checkCases(){

        // o ponto inicial vem antes do final, mas estão na mesma latitude
        if(latRegInit < latRegEnd && lonRegInit == lonRegEnd) return 0;

        // o ponto inicial vem depois do final, mas estão na mesma latitude
        else if(latRegInit > latRegEnd && lonRegInit == lonRegEnd) return 1;

        // o ponto inicial vem antes do final e está mais em cima
        else if(latRegInit < latRegEnd && lonRegInit > lonRegEnd) return 2;

        // o ponto inicial vem antes do final e está mais embaixo
        else if(latRegInit < latRegEnd && lonRegInit < lonRegEnd) return 3;

        //o ponto inicial vem depois do final e está mais em cima
        else if(latRegInit > latRegEnd && lonRegInit > lonRegEnd) return 4;

        // o ponto inicial vem depois do final e está mais embaixo
        else if(latRegInit > latRegEnd && lonRegInit < lonRegEnd) return 5;

        // os 2 pontos são sobrepostos e por isso não é possível criar uma área com isso
        else return 6;
    }

    // Método que calcula a distancia entre 2 pontos pela fórmula de Haversine
    private double calculateDistance(double longitude1, double latitude1, double longitude2, double latitude2){
        double totalDistance = 2 * 6371 * Math.asin(Math.sqrt(Math.pow(Math.sin((latitude2 - latitude1) / 2), 2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.pow(Math.sin((longitude2 - longitude1) / 2), 2)));
        return totalDistance;
    }

    // Método que descobre uma nova latitude que se encontra 120m pra baixo
    private double subtractLatitude(double latitude){
        // constante de conversão de latitudes pra metros
        double metersPerDegree = 110574.0;

        // converte latitude para metros
        double meters = metersPerDegree * latitude;

        // diminui 120m nos metros convertidos
        double newMeters = meters - 120;

        // retorna os novos metros para uma latitude denovo
        double newLatitude = newMeters / metersPerDegree;

        // retorna a nova latitude
        return newLatitude;
    }

    // Método que descobre uma nova latitude que se encontra 120m pra cima
    private double addLatitude(double latitude){
        // constante de conversão de latitudes pra metros
        double metersPerDegree = 110574.0;

        // converte latitude para metros
        double meters = metersPerDegree * latitude;

        // adiciona 120m nos metros convertidos
        double newMeters = meters + 120;

        // retorna os novos metros para uma latitude denovo
        double newLatitude = newMeters / metersPerDegree;

        // retorna a nova latitude
        return newLatitude;
    }

    // Método que descobre uma nova longitude que se encontra 120m pra tras
    private double subtractLongitude(double longitude, double latitude){
        // constante de conversão de longitudes pra metros
        double metersPerDegree = 111321.0;

        // converte a longitude para metros
        double meters = Math.cos(Math.toRadians(latitude)) * metersPerDegree * longitude;

        // diminui 120m na longitude convertida em m
        double newMeters = meters - 120;

        // converte denovo os metros para longitude
        double newLongitude = newMeters / (Math.cos(Math.toRadians(latitude)) * metersPerDegree);

        // retorna a nova longitude
        return newLongitude;
    }

    // Método que descobre uma nova longitude que se encontra 120m pra frente
    private double addLongitude(double longitude, double latitude){
        // constante de conversão de longitudes pra metros
        double metersPerDegree = 111321.0;

        // converte a longitude para metros
        double meters = Math.cos(Math.toRadians(latitude)) * metersPerDegree * longitude;

        // adiciona 120m na longitude convertida em matros
        double newMeters = meters + 120;

        // converte denovo os metros para longitude
        double newLongitude = newMeters / (Math.cos(Math.toRadians(latitude)) * metersPerDegree);

        // retorna a nova longitude
        return newLongitude;
    }

    // public static void main(String[] args) {
    //     Grid grid = new Grid(-22.5889042043, -45.172953, -22.905374, -44.5794347619519);
    //     AStar aStar = new AStar();
    //     java.util.List<Nodes> path = aStar.findPath(grid, -22.5889042043, -45.172953, -22.572625513106775, -45.193966081084056);
    //     int a = 0;

    //     for(Nodes node : path){
    //         System.out.println(node);
    //         a++;
    //     }    
    //     System.out.println(a);
    // }
}