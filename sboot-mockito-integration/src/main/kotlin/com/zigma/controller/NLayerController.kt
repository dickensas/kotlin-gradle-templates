package com.zigma.controller;

import com.zigma.service.NLayerService
import com.zigma.vo.EmployeeVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class NLayerController {
    @Autowired
    var service: NLayerService? = null

    @GetMapping("/demo")
    fun index():List<EmployeeVO?>? {
        return service?.findAll()
    }
}
