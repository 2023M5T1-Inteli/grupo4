package br.edu.inteli.cc.m5.grupo;

import java.util.Optional;

import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;

/**
 * Demonstra o uso da classe DtedDatabaseHandler.
 * 
 * Este programa consulta a base de dados geográfica do Rio, cujos arquivos estão
 * em src/main/resources. Como os arquivos de dados geográficos poder ser inconvenientemente
 * grandes para serem armazenados no Git, só foram colocados nesta pasta os arquivos de
 * uma única região somente para facilitar os testes.
 * 
 * Os arquivos de múltiplas regiões não devem ser colocados no controle de versão, e podem
 * ser acessados utilizando o método Initialize (em lugar de InitializeFromResources) da
 * classe DtedDatabaseHandler.
 * 
 */


public class AppDted {
    public static void main(String[] args) {

        /** RIO sample **/
        DtedDatabaseHandler dbRio = new DtedDatabaseHandler();
        boolean dbRioOk = dbRio.InitializeFromResources("dted/Rio");

        if (dbRioOk) {
            // P0
            // Lon = -42.489
            // Lat = -22.146
            Optional<Integer> ret0 = dbRio.QueryLatLonElevation(-43.46083676101143, -22.669875377228426);
            System.out.println("ret0: " + ret0);
            // P1
            // Lon = -42.488
            // Lat = -22.146
            Optional<Integer> ret1 = dbRio.QueryLatLonElevation(-42.488, -22.146);
            System.out.println("ret1: " + ret1);
            // P2
            // Lon = -42.487
            // Lat = -22.146
            Optional<Integer> ret2 = dbRio.QueryLatLonElevation(-42.487, -22.146);
            System.out.println("ret2: " + ret2);
            // P3
            // Lon = -42.486
            // Lat = -22.146
            Optional<Integer> ret3 = dbRio.QueryLatLonElevation(-42.486, -22.146);
            System.out.println("ret3: " + ret3);
            // P4
            // Lon = -42.485
            // Lat = -22.146
            Optional<Integer> ret4 = dbRio.QueryLatLonElevation(-42.485, -22.146);
            System.out.println("ret4: " + ret4);
            // P5
            // Lon = -42.489
            // Lat = -22.147
            Optional<Integer> ret5 = dbRio.QueryLatLonElevation(-42.489, -22.147);
            System.out.println("ret5: " + ret5);
            // P6
            // Lon = -42.488
            // Lat = -22.147
            Optional<Integer> ret6 = dbRio.QueryLatLonElevation(-42.488, -22.147);
            System.out.println("ret6: " + ret6);
            // P7
            // Lon = -42.487
            // Lat = -22.147
            Optional<Integer> ret7 = dbRio.QueryLatLonElevation(-42.487, -22.147);
            System.out.println("ret7: " + ret7);
            // P8
            // Lon = -42.486
            // Lat = -22.147
            Optional<Integer> ret8 = dbRio.QueryLatLonElevation(-42.486, -22.147);
            System.out.println("ret8: " + ret8);
            // P9
            // Lon = -42.485
            // Lat = -22.147
            Optional<Integer> ret9 = dbRio.QueryLatLonElevation(-42.485, -22.147);
            System.out.println("ret9: " + ret9);
            // P10
            // Lon = -42.489
            // Lat = -22.149
            Optional<Integer> ret10 = dbRio.QueryLatLonElevation(-42.489, -22.149);
            System.out.println("ret10: " + ret10);
            // P11
            // Lon = -42.488
            // Lat = -22.149
            Optional<Integer> ret11 = dbRio.QueryLatLonElevation(-42.488, -22.149);
            System.out.println("ret11: " + ret11);
            // P12
            // Lon = -42.487
            // Lat = -22.149
            Optional<Integer> ret12 = dbRio.QueryLatLonElevation(-42.487, -22.149);
            System.out.println("ret12: " + ret12);
            // P13
            // Lon = -42.486
            // Lat = -22.149
            Optional<Integer> ret13 = dbRio.QueryLatLonElevation(-42.486, -22.149);
            System.out.println("ret13: " + ret13);
            // P14
            // Lon = -42.485
            // Lat = -22.149
            Optional<Integer> ret14 = dbRio.QueryLatLonElevation(-42.485, -22.149);
            System.out.println("ret14: " + ret14);
            // P15
            // Lon = -42.489
            // Lat = -22.15
            Optional<Integer> ret15 = dbRio.QueryLatLonElevation(-42.489, -22.15);
            System.out.println("ret15: " + ret15);
            // P16
            // Lon = -42.488
            // Lat = -22.15
            Optional<Integer> ret16 = dbRio.QueryLatLonElevation(-42.488, -22.15);
            System.out.println("ret16: " + ret16);
            // P17
            // Lon = -42.487
            // Lat = -22.15
            Optional<Integer> ret17 = dbRio.QueryLatLonElevation(-42.487, -22.15);
            System.out.println("ret17: " + ret17);
            // P18
            // Lon = -42.486
            // Lat = -22.15
            Optional<Integer> ret18 = dbRio.QueryLatLonElevation(-42.486, -22.15);
            System.out.println("ret18: " + ret18);
            // P19
            // Lon = -42.485
            // Lat = -22.15
            Optional<Integer> ret19 = dbRio.QueryLatLonElevation(-42.485, -22.15);
            System.out.println("ret19: " + ret19);
            // P20
            // Lon = -42.489
            // Lat = -22.151
            Optional<Integer> ret20 = dbRio.QueryLatLonElevation(-42.489, -22.151);
            System.out.println("ret20: " + ret20);
            // P21
            // Lon = -42.488
            // Lat = -22.151
            Optional<Integer> ret21 = dbRio.QueryLatLonElevation(-42.488, -22.151);
            System.out.println("ret21: " + ret21);
            // P22
            // Lon = -42.487
            // Lat = -22.151
            Optional<Integer> ret22 = dbRio.QueryLatLonElevation(-42.487, -22.151);
            System.out.println("ret22: " + ret22);
            // P23
            // Lon = -42.486
            // Lat = -22.151
            Optional<Integer> ret23 = dbRio.QueryLatLonElevation(-42.486, -22.151);
            System.out.println("ret23: " + ret23);
            // P24
            // Lon = -42.485
            // Lat = -22.151
            Optional<Integer> ret24 = dbRio.QueryLatLonElevation(-42.485, -22.151);
            System.out.println("ret24: " + ret24);
        }
        
        else {
            System.out.println("Não foi possível abrir a base com dados do Rio!");
        }

        /** 
         * Yosemite sample
        DtedDatabaseHandler dbYosemite = new DtedDatabaseHandler();
        boolean dbYoseOk = dbYosemite.Initialize("C:/Users/Inteli/Documents/Databases/Yosemite");

        if (dbYoseOk) {
            // P5
            // Lon = -119.2138645743
            // Lat = 37.8072336631
            // Alt = 3525 (valor esperado)
            Optional<Integer> ret5 = dbYosemite.QueryLatLonElevation(-119.2138645743, 37.8072336631);
            System.out.println("ret5: " + ret5);
        }
        else {
            System.out.println("Não foi possível abrir a base com dados de Yosemite!");
        }
        **/
        
    }
}