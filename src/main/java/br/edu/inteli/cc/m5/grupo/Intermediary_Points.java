package br.edu.inteli.cc.m5.grupo;

// Bibliotecas que serão utilizadas ao longo do código
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;

// Classe principal
public class Intermediary_Points {

    private static final double DISTANCE_BETWEEN_POINTS = 0.12;

    public static List<LatLon> calculateIntermediaryPoints(double startLat, double startLon, double endLat, double endLon) {
        
        // Haversine formula
        List<LatLon> intermediaryPoints = new ArrayList<>();

        // Principais atributos pra instancia do objeto
        double distanceBetweenLat = endLat - startLat;
        double distanceBetweenLon = endLon - startLon;

        // Raio da Terra
        double r = 6371.0;

        //  Latitude e longitude inicial
        double x1 = Math.toRadians(startLat);
        double y1 = Math.toRadians(startLon);

        // Latitude e longitude do destino
        double x2 = Math.toRadians(endLat);
        double y2 = Math.toRadians(endLon);

        // Fórmula de haversine para calcular a distancia total
        double totalDistance = 2 * r * Math.asin(Math.sqrt(Math.pow(Math.sin((x2 - x1) / 2), 2) + Math.cos(x1) * Math.cos(x2) * Math.pow(Math.sin((y2 - y1) / 2), 2)));

        System.out.println(totalDistance + " Kilometers");

        double numPoints = Math.ceil(totalDistance / DISTANCE_BETWEEN_POINTS);

        double latStep = distanceBetweenLat / numPoints;
        double lonStep = distanceBetweenLon / numPoints;

        DtedDatabaseHandler dbHandler = new DtedDatabaseHandler();

        boolean dbHandlerInitialized = dbHandler.InitializeFromResources("dted/Rio");

        if (!dbHandlerInitialized) {
            System.out.println("Failed to initialize DtedDatabaseHandler");
            return intermediaryPoints;
        }

        double currentLat = startLat;
        double currentLon = startLon;

        intermediaryPoints.add(new LatLon(currentLat, currentLon));

        System.out.println(numPoints);

        for (int i = 0; i < numPoints - 1; i++) {
            currentLat += latStep;
            currentLon += lonStep;
            Optional<Integer> elevation = dbHandler.QueryLatLonElevation(currentLon, currentLat);
            if (!elevation.isPresent()) {
                System.out.println("Failed to retrieve elevation for point (" + currentLat + ", " + currentLon + ")");
                break;
            }
            intermediaryPoints.add(new LatLon(currentLat, currentLon, elevation.get()));
        }

        intermediaryPoints.add(new LatLon(endLat, endLon));

        return intermediaryPoints;
    }

    // Método main que executa os códigos
    public static void main(String[] args) {
        
        // Exemplo dado algumas coordenadas
        List<LatLon> intermediaryPoints = calculateIntermediaryPoints(-22.5889042043, -43.4855748, -22.359194448201, -42.5794347619519);

        for (LatLon point : intermediaryPoints) {
            System.out.println(point.toString());
        }
    }
}

// Classe que guarda os atributos de latitude e longitude
class LatLon {
    public double latitude;
    public double longitude;
    public int elevation;
    //public int id = 0;
    //public List<LatLon> neighbors;

    // Constructor indicando como o objeto vai ser instanciado caso seja dado apenas latitude e longitude
    public LatLon(double latitude, double longitude) {
        this(latitude, longitude, 0);
    }

    // Outro constructor para caso a elevação seja dada tambem, alem da latitude e longitude
    public LatLon(double latitude, double longitude, int elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    // Método que formata o objeto para o formato de string
    public String toString() {
        return "LatLon: (" + latitude + ", " + longitude + ", " + elevation + ")";
    }
}
