package ru.plumsoftware.weatherforecast.data.remote.dto.weatherapi

import com.google.gson.annotations.SerializedName


data class Forecast (

  @SerializedName("forecastday" ) var forecastday : ArrayList<Forecastday> = arrayListOf<Forecastday>(Forecastday())

)