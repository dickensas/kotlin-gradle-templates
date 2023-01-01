package com.zigma.service.impl

import com.zigma.dao.NLayerDAO
import com.zigma.service.NLayerService
import com.zigma.vo.EmployeeVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NLayerServiceImpl : NLayerService {

    @Autowired
    var dao:NLayerDAO? = null

    override fun findAll(): List<EmployeeVO?>? {
        return dao?.findAll()
    }
}