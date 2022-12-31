package com.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@SpringBootApplication(exclude = [JdbcTemplateAutoConfiguration::class])
@EnableTransactionManagement
@ComponentScan("com.example")
class Application {
	@Autowired
	var env: Environment? = null

	@Bean
	fun dataSource(): DataSource? {
		val dataSource = DriverManagerDataSource()
		dataSource.setDriverClassName(env!!.getProperty("spring.datasource.driverClassName")!!)
		dataSource.setUrl(env!!.getProperty("spring.datasource.url")!!)
		return dataSource
	}

	@Bean
	fun dataSourceTransactionManager(dataSource: DataSource): DataSourceTransactionManager? {
		return DataSourceTransactionManager(dataSource)
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
