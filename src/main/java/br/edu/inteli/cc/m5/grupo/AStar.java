package br.edu.inteli.cc.m5.grupo;

import java.util.*;

public class AStar {

    public AStar(){

    }

    public LinkedList<Node> findPath(List<Node> grid, Node start, Node end){
        for (Node node : grid){
            node.set_hScore(heuristic(node, end));
        }
        PriorityQueue<Node> unvisitedList = new PriorityQueue<Node>((node1, node2) -> Double.compare(node1.get_fScore(), node2.get_fScore()));
        HashSet<Node> visitedList = new HashSet<Node>();
        unvisitedList.add(start);
        while (!unvisitedList.isEmpty()){
            Node current = unvisitedList.remove();
            if (current.equals(end)){
                return createPath(current);
            }
            visitedList.add(current);
            for (Node neighbor : current.get_neighbors()){
                if (visitedList.contains(neighbor)){
                    continue;
                }
                double tentativeGScore = current.get_gScore() + current.get_distance(neighbor);

                if (tentativeGScore < neighbor.get_gScore()) {
                    neighbor.set_gScore(tentativeGScore);
                    neighbor.set_parent(current);
                }
                if (!unvisitedList.contains(neighbor)) {
                    unvisitedList.add(neighbor);
                }

                neighbor.set_gScore(heuristic(neighbor, start));
                neighbor.set_parent(current);
            }
        }
        return null;
    }


    private static LinkedList<Node> createPath(Node current){
        LinkedList<Node> path = new LinkedList<Node>();
        while (current != null){
            path.add(current);
            current = current.get_parent();
        }
        return path;
    }

    private static Double heuristic(Node node, Node goal){
        return Math.sqrt(Math.pow(node.get_lon() - goal.get_lon(), 2) + Math.pow(node.get_lat() - goal.get_lat(), 2));
    }
    
    public static void main(String[] args){

    // Create some nodes
    Node a = new Node("A", 0.0, 0.0);
    Node b = new Node("B", 1.0, 0.0);
    Node c = new Node("C", 2.0, 0.0);
    Node d = new Node("D", 0.0, 1.0);
    Node e = new Node("E", 1.0, 1.0);
    Node f = new Node("F", 2.0, 1.0);
    Node g = new Node("G", 0.0, 2.0);
    Node h = new Node("H", 1.0, 2.0);
    Node i = new Node("I", 2.0, 2.0);

    // Create some edges
    a.addNeighbor(f);
    b.addNeighbor(c);
    c.addNeighbor(i);
    d.addNeighbor(b);
    b.addNeighbor(e);
    c.addNeighbor(d);
    e.addNeighbor(b);
    b.addNeighbor(i);
    g.addNeighbor(h);
    a.addNeighbor(b);
    f.addNeighbor(c);
    a.addNeighbor(b);
    b.addNeighbor(c);
    i.addNeighbor(g);

    // Create the grid
    List<Node> grid = new ArrayList<Node>();
    grid.add(a);
    grid.add(b);
    grid.add(c);
    grid.add(d);
    grid.add(e);
    grid.add(f);
    grid.add(g);
    grid.add(h);
    grid.add(i);

    // Create the AStar object
    AStar astar = new AStar();

    // Test the findPath method
    LinkedList<Node> path = astar.findPath(grid, a, d);

    // Print the path
    for (Node node : path){
        System.out.println(node.getName());
    }
}  
}