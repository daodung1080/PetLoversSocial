package com.dung.dungdaopetstore.model

data class Animal(var id: String = "",
                  var name: String = "",
                  var gender: String = "",
                  var price: Double = 0.0,
                  var amount: Int = 0,
                  var image: String = "",
                  var confirm: Boolean = true,
                  var seller: String = "")