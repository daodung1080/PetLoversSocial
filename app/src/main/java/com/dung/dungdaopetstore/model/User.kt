package com.dung.dungdaopetstore.model

data class User(var fullname: String = "",
                var username: String = "",
                var password: String = "",
                var image: String = "",
                var money: Int = 0,
                var bannedTime: Int = 1,
                var tradeTime: Int = 1,
                var phoneNumber: String = "",
                var username_password:String = "",
                var ban: Boolean = true)