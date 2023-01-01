package com.zigma.service
import com.zigma.dao.NLayerDAOImplTest
import com.zigma.dao.impl.NLayerDAOImpl
import com.zigma.service.impl.NLayerServiceImpl
import com.zigma.vo.EmployeeVO
import org.springframework.test.util.ReflectionTestUtils
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class NLayerServiceImplTest: NLayerService {

    private var dao:NLayerDAOImplTest? = null
	
    @InjectMocks
    private var service:NLayerServiceImpl? = null

    fun setup() {
		MockitoAnnotations.openMocks(this);
        dao = NLayerDAOImplTest()
        dao?.setup()
		
		ReflectionTestUtils.setField(service!!, "dao", dao)
    }

    override fun findAll(): List<EmployeeVO?>? {
        return service?.findAll()
    }
}