package com.ss.haat.Data

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_user: Long? = null,
        val name:String? = null,
        val phone:String? = null,
        val password:String? = null,
        val address:String? = null,
        val order_state_one:Long = 0,
        val order_state_two:Long = 0,
        var token:String? = null
)

@Entity
class Animal(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_animal: Long? = null,
        val animal_name:String? = null,
        val animal_des:String? = null,
        val animal_price_new:String? = null,
        val animal_price_old:String? = null,
        val animal_type:String? = null,
        val animal_pic:String? = null,
        val animal_owner:Long? = null,
        val animal_contact:String? = null
)

@Entity
class Animal_Image(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) val id_animal_image: Long? = null,
        val animal_id:Long? = null,
        val image_url:String? = null
)