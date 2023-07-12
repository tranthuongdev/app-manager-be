package com.example.appmanager.repository;

import com.example.appmanager.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query(value = "SELECT * FROM devices WHERE warehouse_id = ?", nativeQuery = true)
    public List<Device> listDeviceByWarehouse(Long warehouseId);
}
