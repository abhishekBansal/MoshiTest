package com.example.moshitest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class Vehicle {
    abstract val model: String?
    abstract val type : VehicleType
    abstract val tyres: List<Tyre>?
}

@JsonClass(generateAdapter = true)
data class Car(override val model: String?,
               override val type: VehicleType,
               override val tyres: List<Tyre>?): Vehicle()

@JsonClass(generateAdapter = true)
data class Truck(override val model: String?,
                 override val type: VehicleType,
                 override val tyres: List<Tyre>?): Vehicle()


enum class VehicleType {
    @Json(name = "car")
    CAR,

    @Json(name = "truck")
    TRUCK
}

@JsonClass(generateAdapter = true)
data class Tyre(val type: String)

@JsonClass(generateAdapter = true)
data class MapTest(val vehicleTypes: Map<VehicleType2, Int>)

enum class VehicleType2 {
    @Json(name ="type1")
    TYPE1,

    @Json(name ="type2")
    TYPE2,

    @Json(name ="type3")
    TYPE3,

    @Json(name ="type4")
    TYPE4,
}