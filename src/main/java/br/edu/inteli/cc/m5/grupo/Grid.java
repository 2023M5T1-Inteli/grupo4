package br.edu.inteli.cc.m5.grupo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;
import br.edu.inteli.cc.m5.grupo.Intermediary_Points;
import br.edu.inteli.cc.m5.grupo.LatLon;

public class Grid {
    public double LatRegInit;
    public double LonRegInit;
    public double LatRegEnd;
    public double LonRegEnd;

    public Grid(double LatRegInit, double LonRegInit, double LatRegEnd, double LonRegEnd){
        this.LatRegInit = LatRegInit;
        this.LonRegInit = LonRegInit;
        this.LatRegEnd = LatRegEnd;
        this.LonRegEnd = LonRegEnd;

        postNodes();
    }

    private void postNodes(){
        List<LatLon> topLine = Intermediary_Points.calculateIntermediaryPoints(LatRegInit, LonRegInit, LatRegEnd, LonRegInit);
        List<LatLon> bottomLine = Intermediary_Points.calculateIntermediaryPoints(LatRegEnd, LonRegInit, LatRegEnd, LonRegEnd);
    
        int gridLength = topLine.size();
        double[][] getLatLon = new double[gridLength][4];
        LatLon currentPoint;
    
        for(int i = 0; i < gridLength; i++){
    
            currentPoint = topLine.get(i);
            getLatLon[i][0] = currentPoint.latitude;
            getLatLon[i][1] = currentPoint.longitude;
    
            currentPoint = bottomLine.get(i);
            getLatLon[i][2] = currentPoint.latitude;
            getLatLon[i][3] = currentPoint.longitude;
    
        }
    
        for(int j = 0; j < gridLength; j++){
            List<LatLon> intermediaryPoints = Intermediary_Points.calculateIntermediaryPoints(getLatLon[j][0], getLatLon[j][1], getLatLon[j][2], getLatLon[j][3]);
            for (LatLon point : intermediaryPoints) {
                System.out.println("[" + point.latitude + "] [" + point.longitude + "]");
            }
        }
    }
    
    public static void main(String[] args) {

        Grid malha = new Grid(-22.5889042043, -43.4855748, -22.359194448201, -42.5794347619519);
        
    }
}
