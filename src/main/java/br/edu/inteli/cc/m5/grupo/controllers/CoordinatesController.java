package br.edu.inteli.cc.m5.grupo.controllers;
import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;

import br.edu.inteli.cc.m5.grupo.entities.Coordinates;
import br.edu.inteli.cc.m5.grupo.entities.NodeEntity;
import br.edu.inteli.cc.m5.grupo.repositories.NodeRepository;
import br.edu.inteli.cc.m5.grupo.AStar;
import br.edu.inteli.cc.m5.grupo.Grid;
import br.edu.inteli.cc.m5.grupo.Nodes;
import br.edu.inteli.cc.m5.dted.DtedDatabaseHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.server.ResponseStatusException;
// import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/coordinates")
public class CoordinatesController { 
    
    @Autowired
    private NodeRepository nodeRepository;


    /**
     * This method handles a GET request for retrieving all coordinates.
     * @return a List of all coordinates stored in the database.
     */
    @GetMapping("/Data")
    public List<NodeEntity> listAllCoordinates() {
        return nodeRepository.findAll();
    }
    

    /**
     * This method handles a POST request for creating a new coordinate.
     * @param Coordinates the Coordinate object to store.
     * @return the newly stored Coordinate object.
     */
     
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
        AStar aStar = new AStar();
        List<Nodes> path = aStar.findPath(grid, newCoord.getLat_str(), newCoord.getLon_str(), newCoord.getLat_end(), newCoord.getLon_end());

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
            

            System.out.println(node);
        }
        // chame um método que execute o algoritmo usando as informações extraídas do formulário HTML

        return newCoord;
    }     


}