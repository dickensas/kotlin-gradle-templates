package com.zigma.dao

import com.zigma.dao.impl.NLayerDAOImpl
import com.zigma.vo.EmployeeVO
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.test.util.ReflectionTestUtils

class NLayerDAOImplTest: NLayerDAO {
    @Mock
    var entityManager: EntityManager? = null

    @Mock
    var typedQuery: TypedQuery<EmployeeVO>? = null

    @InjectMocks
    var dao: NLayerDAOImpl? = null

    fun setup() {
        MockitoAnnotations.openMocks(this)
        val lst = ArrayList<EmployeeVO>()

        val x = EmployeeVO()
        x.id = 1
        x.name = "ram"
        x.salary = 10000
        lst.add(x)

        `when`(typedQuery?.getResultList()).thenReturn(lst)
        `when`(entityManager?.createQuery("select e from EmployeeVO e", EmployeeVO::class.java)).thenReturn(typedQuery)
		
		ReflectionTestUtils.setField(dao!!, "entityManager", entityManager)
    }

    override fun findAll(): List<EmployeeVO?>? {
        return dao?.findAll()
    }

}