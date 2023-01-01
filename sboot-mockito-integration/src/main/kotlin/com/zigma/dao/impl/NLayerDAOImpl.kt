package com.zigma.dao.impl

import com.zigma.dao.NLayerDAO
import com.zigma.vo.EmployeeVO
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class NLayerDAOImpl : NLayerDAO {
    @Autowired
    var entityManager: EntityManager? = null
    override fun findAll(): List<EmployeeVO?>? {
        return entityManager?.createQuery("select e from EmployeeVO e", EmployeeVO::class.java)?.getResultList()
    }
}