package com.dung.dungdaopetstore.model

data class Order(var username: String = "",
                 var pet: String = "",
                 var amount: Int = 0,
                 var totalPrice: Int = 0,
                 var idOrder: String = "",
                 var petImage: String = "")