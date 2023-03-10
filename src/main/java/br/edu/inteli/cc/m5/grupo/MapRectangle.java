package br.edu.inteli.cc.m5.grupo;

import java.util.ArrayList;
import java.util.List;




public class MapRectangle {


    public static void main(String[] args) {

        // Get the latitude and longitude of the upper left point
        double lat1 = Double.parseDouble(args[0]);
        double long1 = Double.parseDouble(args[1]);

        // Get the latitude and longitude of the lower right point
        double lat2 = Double.parseDouble(args[2]);
        double long2 = Double.parseDouble(args[3]);

        // Calculate the distance between the two points
        double distance = calculateDistance(lat1, long1, lat2, long2);
        System.out.println("The distance between the two points is " + distance + " kilometers.");

        // Map a rectangle using the two points
        double width = calculateDistance(lat1, long1, lat1, long2);
        double height = calculateDistance(lat1, long1, lat2, long1);
        System.out.println("The dimensions of the rectangle are " + width + " kilometers by " + height + " kilometers.");

        // Calculate the number of vertices needed for the width and height of the rectangle
        int numVerticesWidth = (int) Math.round(width / 0.12); // Each vertex is 0.12 km apart
        int numVerticesHeight = (int) Math.round(height / 0.12);

        // Create a 2D array to store the vertices
        Vertex[][] vertices = new Vertex[numVerticesWidth + 1][numVerticesHeight + 1];

        // Populate the 2D array with vertices at each grid point
        for (int i = 0; i <= numVerticesWidth; i++) {
            for (int j = 0; j <= numVerticesHeight; j++) {
                double lat = lat1 + (i * 0.0012); // Each vertex is 0.12 km apart, so we need to divide by 1000 to get km and multiply by 0.0012 to get the latitude offset
                double lon = long1 + (j * 0.0012); // Same as above but for longitude
                Vertex v = new Vertex(lat, lon);
                vertices[i][j] = v;
            }
        }

        // Connect the vertices by adding edges between adjacent vertices
        for (int i = 0; i < numVerticesWidth; i++) {
            for (int j = 0; j < numVerticesHeight; j++) {
                Vertex v1 = vertices[i][j];
                Vertex v2 = vertices[i+1][j];
                Vertex v3 = vertices[i][j+1];
                Vertex v4 = vertices[i+1][j+1];

                // Add edges between adjacent vertices
                v1.addEdge(v2, (v1.getWeight() + v2.getWeight()) / 2);
                v1.addEdge(v3, (v1.getWeight() + v3.getWeight()) / 2);
                v1.addEdge(v4, (v1.getWeight() + v4.getWeight()) / 2);
                v2.addEdge(v3, (v2.getWeight() + v3.getWeight()) / 2);
                v2.addEdge(v4, (v2.getWeight() + v4.getWeight()) / 2);
                v3.addEdge(v4, (v3.getWeight() + v4.getWeight()) / 2);
            }
        }

        for (int i = 0; i <= numVerticesWidth; i++) {
            for (int j = 0; j <= numVerticesHeight; j++) {
                Vertex v = vertices[i][j];
                System.out.println("Vertex at (" + v.getLatitude() + ", " + v.getLongitude() + ")");
                for (Edge e : v.getEdges()) {
                    System.out.println("    Edge to (" + e.getTo().getLatitude() + ", " + e.getTo().getLongitude() + ") with weight " + e.getWeight());
                }
            }
        }
    }
    
    // Method to create a vertex with a latitude, longitude, and weight
    public static Vertex createVertex(double latitude, double longitude, double weight) {
        Vertex v = new Vertex(latitude, longitude);
        v.setWeight(weight);
        return v;
    }
    
    // Method to connect two vertices with a weight that is the average of their weights
    public static void connectVertices(Vertex v1, Vertex v2) {
        double weight = (v1.getWeight() + v2.getWeight()) / 2;
        Edge e1 = new Edge(v2, weight);
        Edge e2 = new Edge(v1, weight);
        v1.addEdge(e1);
        v2.addEdge(e2);
    }

    //Method to calculate the distance between two points using the Haversine formula
    public static double calculateDistance(double lat1, double long1, double lat2, double long2) {

        double earthRadius = 6371; //in kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLong = Math.toRadians(long2-long1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        return distance;
    }

    // Method to add an edge between two vertices with a custom weight
    public static void addEdge(Vertex v1, Vertex v2, double weight) {
        Edge e1 = new Edge(v2, weight);
        Edge e2 = new Edge(v1, weight);
        v1.addEdge(e1);
        v2.addEdge(e2);
    }

}

class Vertex {
    private double latitude;
    private double longitude;
    private double weight;
    private List<Edge> edges;
    
    public Vertex(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new ArrayList<>();
    }
    
    public void addEdge(Vertex v2, double d) {
    }

    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public List<Edge> getEdges() {
        return edges;
    }
    
    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    
}

class Edge {
    private Vertex to;
    private double weight;

    public Edge(Vertex to, double weight) {
        this.to = to;
        this.weight = weight;
    }

    public Vertex getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

}