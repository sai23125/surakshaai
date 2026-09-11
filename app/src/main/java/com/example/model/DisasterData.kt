package com.example.model

enum class Language(val code: String, val displayName: String, val nativeName: String, val flag: String) {
    EN("en", "English", "English", "🇮🇳"),
    TE("te", "Telugu", "తెలుగు", "🇮🇳"),
    HI("hi", "Hindi", "हिन्दी", "🇮🇳")
}

enum class AppRole(val title: String, val subtitle: String, val badge: String) {
    CITIZEN("Citizen", "INDIVIDUAL & FAMILY", "100% Free Public Safety"),
    ORG("Organization / Civic Responder", "CIVIC TEAMS & NGOS", "Govt ID or NGO Reg. Required")
}

enum class SeverityLevel(val label: String, val colorHex: Long) {
    ADVISORY("Advisory", 0xFF069669),
    MODERATE("Moderate Advisory", 0xFFD97706),
    HIGH("High Warning", 0xFFEA580C),
    CRITICAL("Red Alert / Emergency", 0xFFBA1A1A)
}

data class OfficialAlert(
    val id: String,
    val severity: SeverityLevel,
    val title: String,
    val targetArea: String,
    val issuedTime: String,
    val validTill: String,
    val sourceAgency: String,
    val directive: String,
    val sustainedWind: String? = null,
    val stormSurge: String? = null,
    val exposureCount: String? = null
)

data class HyperlocalRisk(
    val riskLevel: SeverityLevel,
    val locationName: String,
    val elevationAmsl: Int,
    val corridorDistanceMeters: Int,
    val rainfallLast3hMm: Int,
    val barrageDischargeCusecs: Int,
    val temperatureCelsius: Int,
    val humidityPercent: Int,
    val windKmh: Int,
    val dewPointCelsius: Int,
    val weatherCondition: String
)

data class ReliefShelter(
    val id: String,
    val name: String,
    val distanceKm: Float,
    val driveTimeMins: Int,
    val walkTimeMins: Int,
    val elevationMsl: Int,
    val currentOccupancy: Int,
    val maxCapacity: Int,
    val generatorActive: Boolean,
    val fuelReserveHours: Int,
    val provisions: List<String>,
    val auditedBy: String,
    val auditTime: String,
    val phoneContact: String
)

data class RoadHazard(
    val id: String,
    val hazardType: String,
    val title: String,
    val location: String,
    val timeAgo: String,
    val waterDepthCm: Int? = null,
    val isPassable: Boolean,
    val verificationCount: Int
)

data class IncidentReport(
    val id: String,
    val incidentType: String,
    val locationName: String,
    val severity: SeverityLevel,
    val estimatedWaterDepth: String,
    val groundObservations: String,
    val timestamp: String,
    val status: String,
    val upvotes: Int = 1,
    val hasPhoto: Boolean = false
)

data class EmergencyContact(
    val name: String,
    val number: String,
    val description: String
)

data class UserCoordinates(
    val latitude: Double = 16.5186,
    val longitude: Double = 80.6195,
    val accuracyMeters: Float = 4.2f,
    val address: String? = "Bhavanipuram, Ward 14, Vijayawada North",
    val isLiveGps: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

data class CivicActionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    var isDone: Boolean = false
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: String,
    val confidenceScore: Int? = null,
    val actionDirective: String? = null,
    val verifiedShelterName: String? = null
)
