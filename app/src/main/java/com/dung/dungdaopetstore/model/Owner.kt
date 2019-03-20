package com.dung.dungdaopetstore.model

data class Owner(var username: String = "",
                 var petName: String = "",
                 var petGender: String = "",
                 var petWeight: Int = 0,
                 var petCategory: String = "",
                 var petImage: String = "",
                 var ownerID: String = "")