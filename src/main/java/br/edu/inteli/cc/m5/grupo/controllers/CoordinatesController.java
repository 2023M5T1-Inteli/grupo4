package br.edu.inteli.cc.m5.grupo.controllers;
import java.util.List;

import br.edu.inteli.cc.m5.grupo.entities.Coordinates;
import br.edu.inteli.cc.m5.grupo.entities.NodeEntity;
import br.edu.inteli.cc.m5.grupo.repositories.NodeRepository;
import br.edu.inteli.cc.m5.grupo.AStar;
import br.edu.inteli.cc.m5.grupo.Grid;
import br.edu.inteli.cc.m5.grupo.Nodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/coordinates")
public class CoordinatesController { 
    
    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping("/Data")
    public List<NodeEntity> listAllCoordinates() {
        return nodeRepository.findAll();
    }
     
    @PostMapping("/process")
    public Coordinates enviarDados(@RequestBody Coordinates newCoord) {

        System.out.println("lat_str: " + newCoord.getLat_str());
        System.out.println("lon_str: " + newCoord.getLon_str());
        System.out.println("lat_end: " + newCoord.getLat_end());
        System.out.println("lon_end: " + newCoord.getLon_end());
        System.out.println("mbr_lat_str: " + newCoord.getMbr_lat_str());
        System.out.println("mbr_lon_str: " + newCoord.getMbr_lon_str());
        System.out.println("mbr_lat_end: " + newCoord.getMbr_lat_end());
        System.out.println("mbr_lon_end: " + newCoord.getMbr_lon_end());



        Grid grid = new Grid(newCoord.getMbr_lat_str(), newCoord.getMbr_lon_str(), newCoord.getMbr_lat_end(), newCoord.getMbr_lon_end());

        double lowestStr = 0;
        double lowestEnd = 0;

        double strLat = 0;
        double strLon = 0;
        double endLat = 0;
        double endLon = 0;
                
        System.out.println(newCoord.getLat_str());
        System.out.println(newCoord.getLon_str());
        System.out.println(newCoord.getLat_end());
        System.out.println(newCoord.getLon_end());


        for (Nodes node : grid.getGrid()) {
        
            double nodeLat = node.getLat();
            double nodeLon = node.getLon();
        
            double strLatDiff = Math.toRadians(nodeLat) - Math.toRadians(newCoord.getLat_str());
            double strLonDiff = Math.toRadians(nodeLon) - Math.toRadians(newCoord.getLon_str());
            double endLatDiff = Math.toRadians(nodeLat) - Math.toRadians(newCoord.getLat_end());
            double endLonDiff = Math.toRadians(nodeLon) - Math.toRadians(newCoord.getLon_end());
        
            double strDistance = 2 * 6371.0 * Math.asin(Math.sqrt(Math.pow(Math.sin(strLatDiff / 2), 2) + Math.cos(Math.toRadians(newCoord.getLat_str())) * Math.cos(Math.toRadians(nodeLat)) * Math.pow(Math.sin(strLonDiff / 2), 2)));

            double endDistance = 2 * 6371.0 * Math.asin(Math.sqrt(Math.pow(Math.sin(endLatDiff / 2), 2) + Math.cos(Math.toRadians(newCoord.getLat_end())) * Math.cos(Math.toRadians(nodeLat)) * Math.pow(Math.sin(endLonDiff / 2), 2)));
        
            if (strDistance < lowestStr || lowestStr == 0) {
                System.out.println(node);
                lowestStr = strDistance;
                strLat = nodeLat;
                strLon = nodeLon;
            } 
                    
            if (endDistance < lowestEnd || lowestEnd == 0) {
                System.out.println(node);
                lowestEnd = endDistance;
                endLat = nodeLat;
                endLon = nodeLon;
            }
        }
        
        System.out.println(lowestEnd);
        System.out.println(lowestStr);

        AStar aStar = new AStar();

        List<Nodes> path = aStar.findPath(grid, strLat, strLon, endLat, endLon);

        NodeEntity nodeEntityParent = null;
        for (Nodes node : path){
            double lat = node.getLat();
            double lon = node.getLon();
            int elevation = node.getElevation();
            Nodes parent = node.getParent();

            NodeEntity nodeEntity = new NodeEntity(lat, lon, elevation);
            if (parent == null) {
                nodeEntityParent = nodeEntity;
            }
            else {
                nodeEntity.addParent(nodeEntityParent);
                nodeEntityParent = nodeEntity;
            }
            nodeRepository.save(nodeEntity);
        }
        // chame um método que execute o algoritmo usando as informações extraídas do formulário HTML

        return newCoord;
    }     


}