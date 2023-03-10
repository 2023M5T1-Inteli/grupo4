package br.edu.inteli.cc.m5.grupo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.event.PrintJobListener;

import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;
import br.edu.inteli.cc.m5.grupo.Intermediary_Points;
import br.edu.inteli.cc.m5.grupo.LatLon;

public class Grid {
    public double LatRegInit;
    public double LonRegInit;
    public double LatRegEnd;
    public double LonRegEnd;

    public Grid(double LatRegInit, double LonRegInit, double LatRegEnd, double LonRegEnd) {
        this.LatRegInit = LatRegInit;
        this.LatRegInit = LonRegInit;
        this.LatRegInit = LatRegEnd;
        this.LatRegInit = LonRegEnd;

        plotNodes();
    }

    private void plotNodes(){
        List<LatLon> topLine = Intermediary_Points.calculateIntermediaryPoints(LatRegInit, LonRegInit, LatRegEnd, LonRegInit);
        List<LatLon> leftLine = Intermediary_Points.calculateIntermediaryPoints(LatRegInit, LonRegInit, LatRegInit, LonRegEnd);

        int gridLength = topLine.size();
        int gridHeight = leftLine.size();

        LatLon[][] gridNodes = new LatLon[gridHeight][gridLength];

        for(int j = 0; j < gridLength; j++){
            gridNodes[0][j] = topLine.get(j);
        }

        for(int i = 1; i < gridHeight; i++){
            gridNodes[i][0] = leftLine.get(i);
        }

        for(int i = 1; i < gridHeight; i++){
            for(int j = 1; j < gridLength; j++){
                gridNodes[i][j] = new LatLon(gridNodes[0][j].latitude, gridNodes[i][0].longitude);
            }
        }
        
        System.out.println("[" + gridNodes[0][0].latitude + "] [" + gridNodes[0][0].longitude + "]");
    }

    public static void main(String[] args) {

        Grid malha = new Grid(-22.5889042043, -43.4855748, -22.359194448201, -42.5794347619519);

    }
}
