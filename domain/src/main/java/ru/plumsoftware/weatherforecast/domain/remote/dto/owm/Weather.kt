package ru.plumsoftware.weatherforecast.domain.remote.dto.owm

interface Weather {
    var id: Int?
    var main: String?
    var description: String?
    var icon: String?
}