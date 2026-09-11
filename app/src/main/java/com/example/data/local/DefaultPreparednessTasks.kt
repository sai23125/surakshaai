package com.example.data.local

object DefaultPreparednessTasks {
    val items: List<PreparednessTaskEntity> = listOf(
        PreparednessTaskEntity(
            id = "prep_go_bag",
            title = "Pack 72-Hour Emergency Go-Bag",
            description = "Pack high-lumen waterproof torch, spare batteries, hand-crank radio, high-capacity power bank, and multi-tool.",
            category = "GO_BAG",
            categoryLabel = "Go-Bag",
            isCompleted = true,
            priority = "CRITICAL",
            sortOrder = 1
        ),
        PreparednessTaskEntity(
            id = "prep_water",
            title = "Store 3 Liters / Person / Day Clean Water",
            description = "Keep sealed, chlorination-treated drinking water elevated above potential floor flood water breach levels.",
            category = "GO_BAG",
            categoryLabel = "Go-Bag",
            isCompleted = true,
            priority = "CRITICAL",
            sortOrder = 2
        ),
        PreparednessTaskEntity(
            id = "prep_documents",
            title = "Waterproof Seal Identification & Land Deeds",
            description = "Place Aadhaar cards, Ration Card, property papers, insurance, and doctor prescriptions in double zip-seal waterproof pouches.",
            category = "DOCUMENTS",
            categoryLabel = "Documents",
            isCompleted = false,
            priority = "HIGH",
            sortOrder = 3
        ),
        PreparednessTaskEntity(
            id = "prep_gas_cylinder",
            title = "Turn Off LPG Regulator & Main Valve",
            description = "Disconnect gas cylinder regulator and secure cylinder upright to prevent gas leaks if floodwaters enter the kitchen.",
            category = "UTILITIES",
            categoryLabel = "Utilities",
            isCompleted = false,
            priority = "CRITICAL",
            sortOrder = 4
        ),
        PreparednessTaskEntity(
            id = "prep_electric_mcb",
            title = "Trip Main Circuit Breaker (MCB) Before Flooding",
            description = "Shut down main electricity distribution panel to avoid electrocution and transformer back-feeding while water rises.",
            category = "UTILITIES",
            categoryLabel = "Utilities",
            isCompleted = false,
            priority = "HIGH",
            sortOrder = 5
        ),
        PreparednessTaskEntity(
            id = "prep_shelter_route",
            title = "Identify Nearest High-Elevation Refuge",
            description = "Confirm elevated route to Bhavanipuram Community Center or Ward 14 Relief Shelter (19m elevation) avoiding low canal underpasses.",
            category = "EVACUATION",
            categoryLabel = "Evacuation",
            isCompleted = true,
            priority = "CRITICAL",
            sortOrder = 6
        ),
        PreparednessTaskEntity(
            id = "prep_medications",
            title = "Pack 7-Day Chronic & Emergency Medication",
            description = "Assemble insulin coolers, BP tablets, inhalers, water-purification chlorine tablets, and antiseptic wound dressings.",
            category = "MEDICAL",
            categoryLabel = "Medical",
            isCompleted = false,
            priority = "HIGH",
            sortOrder = 7
        ),
        PreparednessTaskEntity(
            id = "prep_phone_charge",
            title = "Fully Charge Devices & Save Emergency 112 / 1070",
            description = "Save emergency response helpline numbers on physical note and ensure phones have power saver mode configured.",
            category = "COMMUNICATION",
            categoryLabel = "Communication",
            isCompleted = true,
            priority = "HIGH",
            sortOrder = 8
        ),
        PreparednessTaskEntity(
            id = "prep_elevate_valuables",
            title = "Elevate Ground-Floor Electronics & Provisions",
            description = "Shift rice sacks, electronics, inverter batteries, and mattress bedding to mezzanine lofts or higher floors.",
            category = "HOME_SAFETY",
            categoryLabel = "Home",
            isCompleted = false,
            priority = "RECOMMENDED",
            sortOrder = 9
        ),
        PreparednessTaskEntity(
            id = "prep_pets",
            title = "Unchain Domestic Animals & Pets",
            description = "Never leave livestock or pets tethered in ground pens where rising waters can trap them; ensure they have open egress.",
            category = "EVACUATION",
            categoryLabel = "Evacuation",
            isCompleted = false,
            priority = "RECOMMENDED",
            sortOrder = 10
        )
    )
}
