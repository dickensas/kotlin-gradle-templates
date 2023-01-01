package com.zigma.vo

import jakarta.persistence.*

@Entity
@Table(name = "employee")
class EmployeeVO {
    @Column(name = "name")
    var name: String? = null

    @Column(name = "salary")
    var salary: Int? = null

    @Id
    @Column(name = "id")
    var id: Int? = null
}