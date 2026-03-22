package com.assignment.zaveproject.ui.home

import com.assignment.zaveproject.domain.model.Store

object MockStoreData {

    val stores = listOf(

        // Grocery
        Store("1", "Reliance Smart", "Sector 62", 28.6280, 77.3721, "grocery", 0.5, 4.2, null),
        Store(
            "2",
            "More Supermarket",
            "Block B Market",
            28.6265,
            77.3700,
            "grocery",
            0.8,
            4.0,
            null
        ),
        Store("3", "Spencer's", "City Center", 28.6258, 77.3712, "grocery", 1.4, 4.4, null),

        // Pharmacy
        Store("4", "Apollo Pharmacy", "Main Road", 28.6272, 77.3740, "pharmacy", 0.6, 4.3, null),
        Store("5", "MedPlus", "Sector 63", 28.6290, 77.3730, "pharmacy", 1.1, 4.1, null),
        Store("6", "Guardian Pharmacy", "Sector 61", 28.6302, 77.3698, "pharmacy", 1.8, 4.0, null),

        // Restaurant
        Store("7", "Haldiram's", "Sector Market", 28.6289, 77.3751, "restaurant", 1.0, 4.5, null),
        Store("8", "Bikanervala", "City Walk", 28.6242, 77.3729, "restaurant", 2.3, 4.3, null),
        Store(
            "9",
            "Domino's Pizza",
            "Sector 62 Road",
            28.6275,
            77.3689,
            "restaurant",
            1.7,
            4.1,
            null
        ),

        // Cafe
        Store("10", "Cafe Coffee Day", "Mall Area", 28.6283, 77.3708, "cafe", 0.9, 4.0, null),
        Store("11", "Starbucks", "Business Park", 28.6298, 77.3742, "cafe", 1.3, 4.6, null),
        Store("12", "Third Wave Coffee", "Tech Hub", 28.6260, 77.3690, "cafe", 2.0, 4.4, null),

        // Electronics
        Store("13", "Croma", "Sector Market", 28.6305, 77.3715, "electronics", 1.2, 4.2, null),
        Store(
            "14",
            "Reliance Digital",
            "City Center",
            28.6249,
            77.3737,
            "electronics",
            2.6,
            4.1,
            null
        ),
        Store("15", "Vijay Sales", "Main Road", 28.6270, 77.3762, "electronics", 3.0, 4.3, null)
    )
}