package com.example.appmanager.controllers;

import com.example.appmanager.models.Device;
import com.example.appmanager.models.User;
import com.example.appmanager.models.Warehouse;
import com.example.appmanager.payload.request.DeviceRequest;
import com.example.appmanager.repository.DeviceRepository;
import com.example.appmanager.repository.UserRepository;
import com.example.appmanager.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserRepository userRepository;
    @PutMapping("/update-warehouse/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable("id") Long id, @RequestBody Warehouse warehouse) {
        Optional<Warehouse> warehouseData = warehouseRepository.findById(id);
        if(warehouseData.isPresent()){
            Warehouse _warehouse = warehouseData.get();
            _warehouse.setWarehouseName(warehouse.getWarehouseName());
            return new ResponseEntity<>(warehouseRepository.save(_warehouse), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-warehouse/{id}")
    public ResponseEntity<HttpStatus> deleteWarehouse(@PathVariable("id") Long id) {
        try {
            warehouseRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-warehouse")
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse){
        try{
            Warehouse _warehouse = warehouseRepository.save(new Warehouse(warehouse.getWarehouseName()));
            return new ResponseEntity<>(_warehouse, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-device")
    public ResponseEntity<Device> createDevice(@RequestBody DeviceRequest deviceRequest) {
        try {
            Warehouse warehouse = warehouseRepository.findById(deviceRequest.getWarehouseId())
                    .orElse(null);

            if (warehouse == null) {
                return ResponseEntity.notFound().build();
            }

            Device device = new Device();
            device.setName(deviceRequest.getName());
            device.setWarehouse(warehouse);

            deviceRepository.save(device);

            return ResponseEntity.ok(device);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete-device/{deviceId}")
    public ResponseEntity<String> deleteDevice(@PathVariable Long deviceId) {
        try {
            deviceRepository.deleteById(deviceId);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting device");
        }
    }
    @PutMapping("/update-device/{deviceId}")
    public ResponseEntity<String> updateDevice(@PathVariable Long deviceId, @RequestBody DeviceRequest deviceRequest) {
        try {
            Device existingDevice = deviceRepository.findById(deviceId)
                    .orElse(null);

            if (existingDevice == null) {
                return ResponseEntity.notFound().build();
            }

            Warehouse warehouse = warehouseRepository.findById(deviceRequest.getWarehouseId())
                    .orElse(null);

            if (warehouse == null) {
                return ResponseEntity.notFound().build();
            }

            existingDevice.setName(deviceRequest.getName());
            existingDevice.setWarehouse(warehouse);

            deviceRepository.save(existingDevice);

            return ResponseEntity.ok("Device updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating device");
        }
    }
    @GetMapping("/get-all-user")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            user.setId(userId);
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}
