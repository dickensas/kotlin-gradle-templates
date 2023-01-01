package com.zigma.service

import com.zigma.vo.EmployeeVO

interface NLayerService {
    fun findAll(): List<EmployeeVO?>?
}