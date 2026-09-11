package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.DefaultPreparednessTasks
import com.example.data.local.PreparednessTaskDao
import com.example.data.local.PreparednessTaskEntity
import com.example.model.CivicActionItem
import com.example.model.EmergencyContact
import com.example.model.HyperlocalRisk
import com.example.model.IncidentReport
import com.example.model.OfficialAlert
import com.example.model.ReliefShelter
import com.example.model.RoadHazard
import com.example.model.SeverityLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DisasterRepository(
    private val taskDao: PreparednessTaskDao? = null
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var activeDao: PreparednessTaskDao? = taskDao

    private val _preparednessTasks = MutableStateFlow(DefaultPreparednessTasks.items)
    val preparednessTasks: StateFlow<List<PreparednessTaskEntity>> = _preparednessTasks.asStateFlow()

    init {
        if (taskDao != null) {
            observeDatabase(taskDao)
        }
    }

    fun initRoomDatabase(context: Context) {
        if (activeDao != null) return
        val db = AppDatabase.getDatabase(context)
        val dao = db.preparednessTaskDao()
        activeDao = dao
        observeDatabase(dao)
    }

    private fun observeDatabase(dao: PreparednessTaskDao) {
        scope.launch {
            val count = dao.getTotalCount()
            if (count == 0) {
                dao.insertAll(DefaultPreparednessTasks.items)
            }
            dao.getAllTasks().collect { dbTasks ->
                if (dbTasks.isNotEmpty()) {
                    _preparednessTasks.value = dbTasks
                }
            }
        }
    }

    private val _currentLocation = MutableStateFlow("Vijayawada North, AP")
    val currentLocation: StateFlow<String> = _currentLocation.asStateFlow()

    private val _isGpsCalibrated = MutableStateFlow(true)
    val isGpsCalibrated: StateFlow<Boolean> = _isGpsCalibrated.asStateFlow()

    private val _hyperlocalRisk = MutableStateFlow(
        HyperlocalRisk(
            riskLevel = SeverityLevel.MODERATE,
            locationName = "Krishna River Basin, Vijayawada North",
            elevationAmsl = 14,
            corridorDistanceMeters = 340,
            rainfallLast3hMm = 18,
            barrageDischargeCusecs = 42000,
            temperatureCelsius = 29,
            humidityPercent = 74,
            windKmh = 14,
            dewPointCelsius = 24,
            weatherCondition = "Overcast with intermittent drizzle"
        )
    )
    val hyperlocalRisk: StateFlow<HyperlocalRisk> = _hyperlocalRisk.asStateFlow()

    private val _officialAlerts = MutableStateFlow(
        listOf(
            OfficialAlert(
                id = "AP-DIS-902",
                severity = SeverityLevel.MODERATE,
                title = "Heavy Inflow Warning & Waterlogging Advisory",
                targetArea = "Prakasam Barrage & Low-lying Riverbank Wards, Vijayawada",
                issuedTime = "Today 08:30 AM IST",
                validTill = "20:00 IST",
                sourceAgency = "State Disaster Management Authority (SDMA) via SACHET",
                directive = "Stay alert along canal banks. Keep emergency phone charged. Avoid underpasses at Bhavanipuram. Divert via Highway Flyover."
            ),
            OfficialAlert(
                id = "WMO-CYC-04",
                severity = SeverityLevel.CRITICAL,
                title = "Tropical Cyclone Alert — Heavy Coastal Surge",
                targetArea = "Bay of Bengal Coastal Zone & Delta Rivers",
                issuedTime = "08:45 UTC",
                validTill = "Tomorrow 18:00 IST",
                sourceAgency = "NDMA Level-4 Stream / IMD SACHET",
                directive = "Fishermen advised strictly not to venture into sea. Coastal shelters active. Pre-position disaster relief battalions.",
                sustainedWind = "195 km/h",
                stormSurge = "3.5 - 5.2m",
                exposureCount = "420,000+ Coastal residents"
            ),
            OfficialAlert(
                id = "HYD-INUND-12",
                severity = SeverityLevel.HIGH,
                title = "Canal Runoff & Drainage Overflow Alert",
                targetArea = "Budameru Diversion Channel Corridor",
                issuedTime = "06:15 AM IST",
                validTill = "16:00 IST",
                sourceAgency = "Vijayawada Municipal Corporation & CWC Telemetry",
                directive = "Water pumps mobilized in Ward 14 and Ward 19. Move electronics and livestock to upper floors."
            )
        )
    )
    val officialAlerts: StateFlow<List<OfficialAlert>> = _officialAlerts.asStateFlow()

    private val _reliefShelters = MutableStateFlow(
        listOf(
            ReliefShelter(
                id = "shelter_1",
                name = "Bhavanipuram Community Relief Center",
                distanceKm = 1.2f,
                driveTimeMins = 4,
                walkTimeMins = 14,
                elevationMsl = 19,
                currentOccupancy = 142,
                maxCapacity = 350,
                generatorActive = true,
                fuelReserveHours = 48,
                provisions = listOf(
                    "Safe Drinking Water (RO)",
                    "15kVA Diesel Generator Active",
                    "Dry Rations & Fresh Milk",
                    "Medical Officer On Duty",
                    "Phone Charging Station (Solar + Gen)"
                ),
                auditedBy = "APSDMA Field Officer Rao",
                auditTime = "Today 07:00 AM",
                phoneContact = "0866-2410299"
            ),
            ReliefShelter(
                id = "shelter_2",
                name = "Municipal High School Emergency Camp",
                distanceKm = 2.1f,
                driveTimeMins = 7,
                walkTimeMins = 24,
                elevationMsl = 22,
                currentOccupancy = 84,
                maxCapacity = 200,
                generatorActive = true,
                fuelReserveHours = 36,
                provisions = listOf(
                    "Chlorinated Clean Water",
                    "Sanitation Supplies",
                    "Community Kitchen",
                    "First Aid Post"
                ),
                auditedBy = "Vijayawada Municipal Corp (GMC)",
                auditTime = "Today 08:15 AM",
                phoneContact = "0866-2428311"
            ),
            ReliefShelter(
                id = "shelter_3",
                name = "Government General Hospital (GGH) Shelter Wing",
                distanceKm = 2.8f,
                driveTimeMins = 9,
                walkTimeMins = 32,
                elevationMsl = 25,
                currentOccupancy = 210,
                maxCapacity = 500,
                generatorActive = true,
                fuelReserveHours = 72,
                provisions = listOf(
                    "24x7 Trauma & ICU Access",
                    "Emergency Power Grid",
                    "Pediatric & Maternal Care",
                    "Pure Water Tankers"
                ),
                auditedBy = "District Medical Health Officer",
                auditTime = "Today 06:30 AM",
                phoneContact = "0866-2577222"
            )
        )
    )
    val reliefShelters: StateFlow<List<ReliefShelter>> = _reliefShelters.asStateFlow()

    private val _roadHazards = MutableStateFlow(
        listOf(
            RoadHazard(
                id = "hz_1",
                hazardType = "Waterlogging",
                title = "35cm Waterlogged Road",
                location = "Near RTC Pandit Nehru Bus Station Underpass",
                timeAgo = "25 mins ago",
                waterDepthCm = 35,
                isPassable = false,
                verificationCount = 5
            ),
            RoadHazard(
                id = "hz_2",
                hazardType = "Fallen Tree",
                title = "Fallen Banyan Tree on Power Line",
                location = "Gandhi Road Corridor near Sub-Collector Office",
                timeAgo = "12 mins ago",
                waterDepthCm = null,
                isPassable = false,
                verificationCount = 3
            ),
            RoadHazard(
                id = "hz_3",
                hazardType = "Canal Overflow",
                title = "Western Main Canal Bank Spillage",
                location = "Kummaripalem Center Bund Road",
                timeAgo = "40 mins ago",
                waterDepthCm = 20,
                isPassable = true,
                verificationCount = 8
            )
        )
    )
    val roadHazards: StateFlow<List<RoadHazard>> = _roadHazards.asStateFlow()

    private val _incidentReports = MutableStateFlow(
        listOf(
            IncidentReport(
                id = "INC-9042",
                incidentType = "Severe Flooding",
                locationName = "Bhavanipuram PHC Approach Road, Ward 14",
                severity = SeverityLevel.CRITICAL,
                estimatedWaterDepth = "Knee deep (30-50 cm)",
                groundObservations = "Drainage overflowed near PHC gate. Ambulances cannot enter directly. Low-lying corridor holding water.",
                timestamp = "12 mins ago",
                status = "Under Review by NDRF / GMC",
                upvotes = 6,
                hasPhoto = true
            ),
            IncidentReport(
                id = "INC-9038",
                incidentType = "Fallen Tree / Pole",
                locationName = "Gandhi Road Corridor, Ward 11",
                severity = SeverityLevel.MODERATE,
                estimatedWaterDepth = "None",
                groundObservations = "Heavy branch brought down telephone lines. Electricity board staff notified.",
                timestamp = "32 mins ago",
                status = "Dispatched Unit #2",
                upvotes = 4,
                hasPhoto = false
            ),
            IncidentReport(
                id = "INC-9019",
                incidentType = "Waterlogging",
                locationName = "One Town Market Canal Crossing",
                severity = SeverityLevel.MODERATE,
                estimatedWaterDepth = "Ankle deep (~10 cm)",
                groundObservations = "Water pooling near vegetable stalls. Silt clogging storm gutter.",
                timestamp = "1 hour ago",
                status = "Action Taken",
                upvotes = 9,
                hasPhoto = true
            )
        )
    )
    val incidentReports: StateFlow<List<IncidentReport>> = _incidentReports.asStateFlow()

    private val _civicActions = MutableStateFlow(
        listOf(
            CivicActionItem(
                id = "act_1",
                title = "Prepare Emergency Go-Bag",
                subtitle = "Keep ID proofs, dry medicines, phone power bank, and torch accessible.",
                isDone = true
            ),
            CivicActionItem(
                id = "act_2",
                title = "Elevate Valuables & Switch Off Main Gas",
                subtitle = "Prevent electrical short-circuit if storm water breaches ground floor.",
                isDone = false
            ),
            CivicActionItem(
                id = "act_3",
                title = "Locate Nearest Verified Relief Shelter",
                subtitle = "Bhavanipuram Community Center (1.2 km, 19m elevation) is open.",
                isDone = false
            ),
            CivicActionItem(
                id = "act_4",
                title = "Keep Emergency Helplines Saved",
                subtitle = "112 (National), 1070 (SDMA), 0866-2474600 (Vijayawada Flood Control).",
                isDone = true
            )
        )
    )
    val civicActions: StateFlow<List<CivicActionItem>> = _civicActions.asStateFlow()

    val emergencyContacts = listOf(
        EmergencyContact("National Emergency Helpline", "112", "All-in-one Police, Fire & Medical dispatch"),
        EmergencyContact("State Disaster Control Room (SDMA)", "1070", "State disaster coordination & evacuation"),
        EmergencyContact("District Emergency Center (DDMA)", "1077", "District collectorate disaster response"),
        EmergencyContact("Vijayawada Municipal Flood Room", "0866-2474600", "De-watering pumps & localized civic alerts"),
        EmergencyContact("NDRF 10th Battalion Dispatch", "9711077372", "Flood rescue boats & rapid field teams"),
        EmergencyContact("Medical Emergency / Ambulance", "108", "Free government ambulance service")
    )

    fun updateLocation(newLoc: String) {
        _currentLocation.value = newLoc
        _isGpsCalibrated.value = true
    }

    fun addIncidentReport(report: IncidentReport) {
        _incidentReports.value = listOf(report) + _incidentReports.value
    }

    fun upvoteReport(reportId: String) {
        _incidentReports.value = _incidentReports.value.map {
            if (it.id == reportId) it.copy(upvotes = it.upvotes + 1) else it
        }
    }

    fun toggleCivicAction(actionId: String) {
        _civicActions.value = _civicActions.value.map {
            if (it.id == actionId) it.copy(isDone = !it.isDone) else it
        }
    }

    fun togglePreparednessTask(taskId: String) {
        val currentList = _preparednessTasks.value
        val target = currentList.find { it.id == taskId } ?: return
        val newStatus = !target.isCompleted

        // Immediately update memory stateflow for instantaneous UI feedback
        _preparednessTasks.value = currentList.map {
            if (it.id == taskId) it.copy(isCompleted = newStatus) else it
        }

        // Persist to Room SQLite offline database
        activeDao?.let { dao ->
            scope.launch {
                dao.updateTaskCompletion(taskId, newStatus)
            }
        }
    }

    fun setAllPreparednessTasks(completed: Boolean) {
        _preparednessTasks.value = _preparednessTasks.value.map {
            it.copy(isCompleted = completed)
        }
        activeDao?.let { dao ->
            scope.launch {
                dao.setAllCompleted(completed)
            }
        }
    }
}
