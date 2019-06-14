package com.example.moshitest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class Vehicle {
    abstract val model: String
    abstract val type : String
    abstract val tyres: List<Tyre>?
}

@JsonClass(generateAdapter = true)
data class Car(override val model: String,
               override val type: String,
               override val tyres: List<Tyre>?): Vehicle()

@JsonClass(generateAdapter = true)
data class Truck(override val model: String,
                 override val type: String,
                 override val tyres: List<Tyre>?): Vehicle()


@JsonClass(generateAdapter = true)
data class Tyre(val type: String)
//@JsonClass(generateAdapter = true)
//data class MRFTyre(override val type : String): Tyre()
//
//@JsonClass(generateAdapter = true)
//data class ApolloTyre(override val type : String): Tyre()