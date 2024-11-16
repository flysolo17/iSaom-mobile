package com.ketchupzzz.isaom.models.sections

import java.util.Date

//export interface ISection {
//    id: string;
//    name: string;
//    teacher: string;
//    createdAt: Date;
//    updatedAt: Date;
//}

data class Sections(
    val id : String ? = null,
    val name : String ? = null,
    val active : Boolean ? = false,
    val createdAt : Date ? = null,
    val updatedAt : Date ? = null
)