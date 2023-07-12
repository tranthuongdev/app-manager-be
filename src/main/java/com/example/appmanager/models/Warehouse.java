package com.example.appmanager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseId;

    private String warehouseName;

    public Warehouse(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}