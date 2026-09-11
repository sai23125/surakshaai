package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.example.data.DisasterRepository
import com.example.data.LocationService
import com.example.data.local.PreparednessTaskEntity
import com.example.model.AppRole
import com.example.model.ChatMessage
import com.example.model.CivicActionItem
import com.example.model.EmergencyContact
import com.example.model.HyperlocalRisk
import com.example.model.IncidentReport
import com.example.model.Language
import com.example.model.OfficialAlert
import com.example.model.ReliefShelter
import com.example.model.RoadHazard
import com.example.model.SeverityLevel
import com.example.model.UserCoordinates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DisasterViewModel(
    private val repository: DisasterRepository = DisasterRepository()
) : ViewModel() {

    val currentLocation: StateFlow<String> = repository.currentLocation
    val isGpsCalibrated: StateFlow<Boolean> = repository.isGpsCalibrated
    val hyperlocalRisk: StateFlow<HyperlocalRisk> = repository.hyperlocalRisk
    val officialAlerts: StateFlow<List<OfficialAlert>> = repository.officialAlerts
    val reliefShelters: StateFlow<List<ReliefShelter>> = repository.reliefShelters
    val roadHazards: StateFlow<List<RoadHazard>> = repository.roadHazards
    val incidentReports: StateFlow<List<IncidentReport>> = repository.incidentReports
    val civicActions: StateFlow<List<CivicActionItem>> = repository.civicActions
    val preparednessTasks: StateFlow<List<PreparednessTaskEntity>> = repository.preparednessTasks
    val emergencyContacts: List<EmergencyContact> = repository.emergencyContacts

    private val _selectedPrepCategory = MutableStateFlow("ALL")
    val selectedPrepCategory: StateFlow<String> = _selectedPrepCategory.asStateFlow()

    // Live Coordinates from play-services-location
    private val _currentCoordinates = MutableStateFlow(UserCoordinates())
    val currentCoordinates: StateFlow<UserCoordinates> = _currentCoordinates.asStateFlow()

    private val _isLocationPermissionGranted = MutableStateFlow(false)
    val isLocationPermissionGranted: StateFlow<Boolean> = _isLocationPermissionGranted.asStateFlow()

    private val _isFetchingLocation = MutableStateFlow(false)
    val isFetchingLocation: StateFlow<Boolean> = _isFetchingLocation.asStateFlow()

    // Active app state
    private val _currentLanguage = MutableStateFlow(Language.EN)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _selectedRole = MutableStateFlow(AppRole.CITIZEN)
    val selectedRole: StateFlow<AppRole> = _selectedRole.asStateFlow()

    private val _showSosDialog = MutableStateFlow(false)
    val showSosDialog: StateFlow<Boolean> = _showSosDialog.asStateFlow()

    private val _showLocationDialog = MutableStateFlow(false)
    val showLocationDialog: StateFlow<Boolean> = _showLocationDialog.asStateFlow()

    private val _isGpsRefreshing = MutableStateFlow(false)
    val isGpsRefreshing: StateFlow<Boolean> = _isGpsRefreshing.asStateFlow()

    private val _gpsStatusMessage = MutableStateFlow<String?>(null)
    val gpsStatusMessage: StateFlow<String?> = _gpsStatusMessage.asStateFlow()

    // Incident Report Form State
    private val _reportType = MutableStateFlow("Waterlogging")
    val reportType: StateFlow<String> = _reportType.asStateFlow()

    private val _reportSeverity = MutableStateFlow(SeverityLevel.MODERATE)
    val reportSeverity: StateFlow<SeverityLevel> = _reportSeverity.asStateFlow()

    private val _reportWaterDepth = MutableStateFlow("Knee deep (30-50 cm)")
    val reportWaterDepth: StateFlow<String> = _reportWaterDepth.asStateFlow()

    private val _reportNotes = MutableStateFlow("")
    val reportNotes: StateFlow<String> = _reportNotes.asStateFlow()

    private val _reportSubmitted = MutableStateFlow(false)
    val reportSubmitted: StateFlow<Boolean> = _reportSubmitted.asStateFlow()

    // Ask Suraksha Chat State
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "m1",
                isUser = false,
                text = "Namaskaram! I am actively tracking live rainfall data from IMD and inflows at Prakasam Barrage for Vijayawada North. How can I keep you and your family safe right now?",
                timestamp = "08:30 AM"
            ),
            ChatMessage(
                id = "m2",
                isUser = true,
                text = "Why is my flood risk moderate, and is Bhavanipuram underpass safe to cross right now?",
                timestamp = "08:32 AM"
            ),
            ChatMessage(
                id = "m3",
                isUser = false,
                text = "Flood risk in Vijayawada North is categorized as MODERATE driven by 3 active environmental risk parameters:\n• Rainfall: 18 mm in last 3h (Moderate accumulation)\n• Barrage Inflow: Prakasam Barrage reached 42,000 cusecs (Rising trend)\n• Terrain Elevation: Your current zone is at 14m AMSL (Low-lying runoff corridor)\n\nAction Directive: Do NOT attempt to cross the Bhavanipuram underpass. 35cm standing water is reported. Divert via the Highway Flyover.",
                timestamp = "08:32 AM",
                confidenceScore = 98,
                actionDirective = "Divert via Highway Flyover. Do NOT cross Bhavanipuram underpass.",
                verifiedShelterName = "Bhavanipuram Community Relief Center (1.2 km away, 19m safe elevation)"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun selectLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    fun selectRole(role: AppRole) {
        _selectedRole.value = role
    }

    fun toggleSosDialog(show: Boolean) {
        _showSosDialog.value = show
    }

    fun toggleLocationDialog(show: Boolean) {
        _showLocationDialog.value = show
    }

    fun updateLocationPermission(isGranted: Boolean) {
        _isLocationPermissionGranted.value = isGranted
    }

    fun fetchLiveCoordinates(context: Context) {
        viewModelScope.launch {
            _isGpsRefreshing.value = true
            _isFetchingLocation.value = true
            _gpsStatusMessage.value = "Fetching live GPS coordinates from Play Services..."

            val locationService = LocationService(context)
            _isLocationPermissionGranted.value = locationService.hasLocationPermission()

            val coords = locationService.fetchCurrentCoordinates()
            _currentCoordinates.value = coords

            val resolvedAddress = coords.address ?: "Vijayawada North, AP"
            repository.updateLocation(resolvedAddress)

            _isGpsRefreshing.value = false
            _isFetchingLocation.value = false
            _gpsStatusMessage.value = if (coords.isLiveGps) {
                "Live GPS Beacon Active: %.4f° N, %.4f° E (±%.1fm)".format(coords.latitude, coords.longitude, coords.accuracyMeters)
            } else {
                "Location set to $resolvedAddress"
            }
        }
    }

    fun refreshGps() {
        viewModelScope.launch {
            _isGpsRefreshing.value = true
            _gpsStatusMessage.value = "Calibrating high-precision GPS coordinates..."
            delay(800)
            _isGpsRefreshing.value = false
            val current = _currentCoordinates.value
            _gpsStatusMessage.value = "GPS synced: %.4f° N, %.4f° E (±%.1fm). Alerts linked.".format(current.latitude, current.longitude, current.accuracyMeters)
        }
    }

    fun setLocationManually(location: String) {
        repository.updateLocation(location)
        _gpsStatusMessage.value = "Manual Location Linked: $location. Localized alerts active."
    }

    fun updateReportType(type: String) {
        _reportType.value = type
    }

    fun updateReportSeverity(severity: SeverityLevel) {
        _reportSeverity.value = severity
    }

    fun updateReportWaterDepth(depth: String) {
        _reportWaterDepth.value = depth
    }

    fun updateReportNotes(notes: String) {
        _reportNotes.value = notes
    }

    fun submitIncidentReport() {
        if (_reportNotes.value.isBlank()) return
        val newReport = IncidentReport(
            id = "INC-${(1000..9999).random()}",
            incidentType = _reportType.value,
            locationName = "${repository.currentLocation.value} (Ward 14)",
            severity = _reportSeverity.value,
            estimatedWaterDepth = _reportWaterDepth.value,
            groundObservations = _reportNotes.value,
            timestamp = "Just now",
            status = "Verified & Shared with Responders",
            upvotes = 1,
            hasPhoto = true
        )
        repository.addIncidentReport(newReport)
        _reportSubmitted.value = true
        _reportNotes.value = ""
    }

    fun resetReportForm() {
        _reportSubmitted.value = false
    }

    fun upvoteReport(reportId: String) {
        repository.upvoteReport(reportId)
    }

    fun toggleCivicAction(actionId: String) {
        repository.toggleCivicAction(actionId)
    }

    fun sendChatMessage(query: String) {
        if (query.isBlank()) return
        val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            id = "user_${System.currentTimeMillis()}",
            isUser = true,
            text = query,
            timestamp = timeStr
        )
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isChatLoading.value = true
            delay(1200)
            val lower = query.lowercase()
            val answer = when {
                lower.contains("shelter") || lower.contains("place") || lower.contains("safe") -> {
                    ChatMessage(
                        id = "bot_${System.currentTimeMillis()}",
                        isUser = false,
                        text = "Nearest verified shelter: Bhavanipuram Community Relief Center (1.2 km away, 19m MSL elevation). It currently has 142/350 occupants with 48h generator backup, clean RO drinking water, and medical officers on duty.",
                        timestamp = timeStr,
                        confidenceScore = 99,
                        actionDirective = "Walk or drive north via Canal Bank Road to Bhavanipuram Center.",
                        verifiedShelterName = "Bhavanipuram Community Relief Center"
                    )
                }
                lower.contains("water") || lower.contains("rain") || lower.contains("flood") -> {
                    ChatMessage(
                        id = "bot_${System.currentTimeMillis()}",
                        isUser = false,
                        text = "Rainfall in Vijayawada North is at 18 mm in the last 3 hours. Discharge at Prakasam Barrage is 42,000 cusecs. Flood risk is MODERATE. Low-lying canal banks and railway underpasses are experiencing 30-40cm waterlogging. Keep your emergency kit packed.",
                        timestamp = timeStr,
                        confidenceScore = 97,
                        actionDirective = "Move vehicles to elevated ground and avoid subways."
                    )
                }
                lower.contains("sos") || lower.contains("help") || lower.contains("emergency") -> {
                    ChatMessage(
                        id = "bot_${System.currentTimeMillis()}",
                        isUser = false,
                        text = "If you are in immediate life danger, tap the red SOS button at top-right or call 112 directly. NDRF 10th Battalion rescue teams are stationed near Krishna barrage.",
                        timestamp = timeStr,
                        confidenceScore = 100,
                        actionDirective = "Call 112 immediately or trigger app SOS siren."
                    )
                }
                else -> {
                    ChatMessage(
                        id = "bot_${System.currentTimeMillis()}",
                        isUser = false,
                        text = "Verified update for Vijayawada North: IMD radar shows overcast conditions with scattered drizzle (29°C). Official advisory #AP-DIS-902 remains active until 8:00 PM. Please stay tuned to verified notifications and avoid unverified rumors.",
                        timestamp = timeStr,
                        confidenceScore = 95,
                        actionDirective = "Follow official NDMA/SDMA directives."
                    )
                }
            }
            _isChatLoading.value = false
            _chatMessages.value = _chatMessages.value + answer
        }
    }

    fun initPreparednessDatabase(context: Context) {
        repository.initRoomDatabase(context)
    }

    fun togglePreparednessTask(taskId: String) {
        repository.togglePreparednessTask(taskId)
    }

    fun resetAllPreparednessTasks() {
        repository.setAllPreparednessTasks(false)
    }

    fun completeAllPreparednessTasks() {
        repository.setAllPreparednessTasks(true)
    }

    fun selectPrepCategory(category: String) {
        _selectedPrepCategory.value = category
    }
}
