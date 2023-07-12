package com.example.appmanager.controllers;

import com.example.appmanager.models.Device;
import com.example.appmanager.models.Warehouse;
import com.example.appmanager.repository.DeviceRepository;
import com.example.appmanager.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @GetMapping("/get-all-warehouse")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Warehouse>> getAllWarehouses(){
        try{
            List<Warehouse> warehouses = new ArrayList<>();
            warehouseRepository.findAll().forEach(warehouses::add);
            if(warehouses.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(warehouses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-warehouse-by-id/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable("id") Long id){
        Optional<Warehouse> warehouseData = warehouseRepository.findById(id);
        if(warehouseData.isPresent()){
            return new ResponseEntity<>(warehouseData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-device-by-warehouse")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Device>> getAllDeviceByWarehouse(@RequestParam(required = false) Long id){
        try{
            List<Device> devices = new ArrayList<>();
            if(id == null)
                deviceRepository.findAll().forEach(devices::add);
            else
                deviceRepository.listDeviceByWarehouse(id).forEach(devices::add);
            if(devices.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(devices, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
