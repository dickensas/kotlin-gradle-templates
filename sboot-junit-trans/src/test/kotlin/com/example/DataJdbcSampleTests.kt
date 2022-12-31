package com.example

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import javax.sql.DataSource


@DataJdbcTest(excludeAutoConfiguration = [
    DataSourceTransactionManagerAutoConfiguration::class,
    DataSourceAutoConfiguration::class,
    HibernateJpaAutoConfiguration::class,
    JdbcTemplateAutoConfiguration::class
])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DataJdbcSampleTests {
    @Autowired
    private var jdbcTemplate: JdbcTemplate? = null

    @Autowired
    private val datsSource: DataSource? = null

    @Autowired
    private val transactionManager: PlatformTransactionManager? = null

    private var transactionTemplate: TransactionTemplate? = null

    @BeforeEach
    fun setUp() {
        transactionTemplate = TransactionTemplate(transactionManager!!)
        jdbcTemplate = JdbcTemplate(datsSource!!)
    }

    class MyClassBackNoReturn : TransactionCallbackWithoutResult {
        private var jdbcTemplate: JdbcTemplate? = null
        constructor(jdbcTemplate: JdbcTemplate?){
            this.jdbcTemplate = jdbcTemplate
        }
        override fun doInTransactionWithoutResult(status: TransactionStatus) {
            jdbcTemplate!!.execute("delete from employee")
            jdbcTemplate!!.execute("insert into employee (id, name) values (1, 'John')")
        }
    }

    @Test
    fun testStuff() {
        transactionTemplate!!.execute( MyClassBackNoReturn(jdbcTemplate) )
        val employees = jdbcTemplate!!.queryForList("select id, name from employee")
        assertThat(employees).hasSize(1)
    }
}