package cl.duoc.pichangapp.data.repository

import cl.duoc.pichangapp.data.model.*
import cl.duoc.pichangapp.data.remote.EventApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventApi: EventApi
) {
    suspend fun getEvents(lat: Double, lng: Double): List<EventDto> = eventApi.getEvents(lat, lng)
    
    suspend fun getEventById(id: Int): EventDto = eventApi.getEventById(id)
    
    suspend fun createEvent(request: CreateEventRequest): EventDto = eventApi.createEvent(request)
    
    suspend fun joinEvent(id: Int): Response<Unit> = eventApi.joinEvent(id)
    
    suspend fun checkIn(id: Int, lat: Double, lng: Double): Response<Unit> = eventApi.checkIn(id, EventCheckInRequest(lat, lng))
    
    suspend fun getRegistrations(id: Int): List<EventRegistrationDto> = eventApi.getRegistrations(id)
    
    suspend fun markAttendance(id: Int, userId: Int, attended: Boolean): Response<Unit> = eventApi.markAttendance(id, AttendanceRequest(userId, attended))
    
    suspend fun finishEvent(id: Int): Response<Unit> = eventApi.finishEvent(id)
    
    suspend fun getMyEvents(): List<EventDto> = eventApi.getMyEvents()
    
    suspend fun getOrganizingEvents(): List<EventDto> = eventApi.getOrganizingEvents()
    
    suspend fun leaveEvent(id: Int): retrofit2.Response<Unit> = eventApi.leaveEvent(id)

    suspend fun getUserById(id: Int): UserDto = eventApi.getUserById(id)
}
