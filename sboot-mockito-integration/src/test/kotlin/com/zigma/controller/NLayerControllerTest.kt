package com.zigma.controller

import com.zigma.service.NLayerServiceImplTest
import com.zigma.service.impl.NLayerServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class NLayerControllerTest {

    private var service: NLayerServiceImplTest? = null

    private var mockMvc: MockMvc? = null

	@InjectMocks
    private var controller:NLayerController? = null

    @BeforeEach
    fun setup() {
		MockitoAnnotations.openMocks(this)
        service = NLayerServiceImplTest()
        service?.setup()
        
        ReflectionTestUtils.setField(controller!!, "service", service)

        this.mockMvc =  MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    fun findAll() {
        var result:MvcResult? = mockMvc?.perform(MockMvcRequestBuilders.get("/demo"))
            ?.andExpect(MockMvcResultMatchers.status().isOk())
            ?.andReturn();

        val content: String? = result?.getResponse()?.getContentAsString()
        println(content)
    }
}