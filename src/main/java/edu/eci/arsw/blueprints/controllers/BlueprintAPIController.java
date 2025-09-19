/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.Set;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author hcadavid
 */

@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    private BlueprintsServices blueprintsServices;

    @GetMapping
    public ResponseEntity<?> getAllBlueprints() {
        try {
            Set<Blueprint> data = blueprintsServices.getAllBlueprints();
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al obtener los planos", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsbyAuthor(@PathVariable("author") String author) {
        try {
            Set<Blueprint> data = blueprintsServices.getBlueprintsByAuthor(author);
            if (data == null || data.isEmpty()) {
                return new ResponseEntity<>("No se encontraron planos para: " + author, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al obtener los planos", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}/{name}")
    public ResponseEntity<?> getBlueprints(@PathVariable("author") String author,@PathVariable("name") String name) {
        try {
            Blueprint data = blueprintsServices.getBlueprint(author, name);
            if (data == null ) {
                return new ResponseEntity<>("No se encontraron planos para: " + author, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al obtener los planos", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> postBlueprint(@RequestBody Blueprint blueprint){
        try {
            blueprintsServices.addNewBlueprint(blueprint);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al crear el plano",HttpStatus.FORBIDDEN);
        }

    }



}
