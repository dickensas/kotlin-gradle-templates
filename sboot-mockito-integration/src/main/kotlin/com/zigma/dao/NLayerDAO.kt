package com.zigma.dao

import com.zigma.vo.EmployeeVO

interface NLayerDAO {
    fun findAll(): List<EmployeeVO?>?
}