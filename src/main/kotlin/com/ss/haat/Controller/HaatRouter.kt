package com.ss.haat.Controller

import com.ss.haat.Data.*
import com.ss.haat.Interface.Animal_Image_Repository
import com.ss.haat.Interface.Animal_Repository
import com.ss.haat.Interface.User_Repository
import com.ss.haat.Service.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/haat")
class HaatController(val userRepo:User_Repository, val animalRepo:Animal_Repository, val imageRepo:Animal_Image_Repository)
{
    @Autowired
    lateinit var fileStorage: FileStorage

    @PostMapping("/register")
    fun register(@RequestBody reg:register):ret
    {
        var num = userRepo.getByPhone(reg.phone)

        return try
        {
            if (num!!.name!!.isEmpty())
            {
                var kt = ret("1")
                return kt
            }
            else
            {
                var kt = ret("User already exists")
                return kt
            }
        }
        catch (e: Exception)
        {
            var tk = LocalDateTime.now().toString()+reg.phone
            tk = tk.replace(",","")
            tk = tk.replace(".","")
            tk = tk.replace(":","")
            tk = tk.replace("-","")
            var k = User(0,reg.name,reg.phone,reg.password,reg.address,0,0,tk)
            userRepo.save(k)
            var kt = ret(tk)
            return kt
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody lgn:login) : ret
    {
        var k = userRepo.getByPhone(lgn.phone)

        return try
        {
            if (k!!.phone!!.isEmpty())
            {
                var k = ret("user dosent exist")
                return k
            }
            else
            {
                if (lgn.phone==k.phone && lgn.password==k.password)
                {
                    var s = userRepo.findById(k.id_user!!)
                    if (s.isPresent)
                    {
                        var data = s.get()
                        var token = token(lgn.phone!!)
                        data.token = token
                        userRepo.save(data)
                        var k = ret(token)
                        return k
                    }
                    else
                    {
                        var k = ret("user dosent exist")
                        return k
                    }
                }
                else
                {
                    var k = ret("password didnt match")
                    return k
                }
            }
        }
        catch (e:Exception)
        {
            if (lgn.phone==k!!.phone && lgn.password==k.password)
            {
                val s = userRepo.findById(k.id_user!!)
                return if (s.isPresent)
                {
                    var data = s.get()
                    var token = token(lgn.phone!!)
                    data.token = token
                    userRepo.save(data)
                    var k = ret(token)
                    k
                }
                else
                {
                    var k = ret("password didnt match")
                    k
                }
            }
            else
            {
                var k = ret("user dosent exist")
                return k
            }
        }
    }

    @GetMapping("/{test}")
    fun test(@PathVariable test:String):ret
    {
        var k = token_varify(test)
        var kt = ret(k.toString())
        return kt
    }

    @PostMapping("/img/{test}")
    fun uploadMultipartFile(@RequestParam("uploadfile") file: MultipartFile, model: Model,@PathVariable test:String): String {
        //Appsession.name = test

        var tk = LocalDateTime.now().toString()
        tk = tk.replace(",","")
        tk = tk.replace(".","")
        tk = tk.replace(":","")
        tk = tk.replace("-","")

        var test1 = test+"_"+tk+".jpg"
        var k1 = Animal_Image(0,test.toLong(),test1)
        imageRepo.save(k1)

        fileStorage.store(file, test1)
        model.addAttribute("message", "File uploaded successfully! -> filename = " + file.originalFilename)
        return test1
    }

    @PostMapping("/animals/{type}")
    fun animals(@RequestBody ter:ret,@PathVariable type:String): MutableList<Animal>
    {
        return if (token_varify(ter.name!!))
        {
            if (type=="goru"||type=="sagol")
            {
                animalRepo.getAnimalType(type)
            }
            else
            {
                animalRepo.findAll()
            }
        }
        else
        {
            Collections.emptyList()
        }
    }

    @PostMapping("/product/{id}")
    fun product(@RequestBody ter:ret,@PathVariable id:String): MutableList<Animal>
    {
        return if (token_varify(ter.name!!))
        {
            animalRepo.getByAnimal(id.toLong())
        }
        else
        {
            Collections.emptyList()
        }
    }

    @PostMapping("/image/{id}")
    fun image(@RequestBody ter:ret, @PathVariable id:String): MutableList<Animal_Image>
    {
        return if (token_varify(ter.name!!))
        {
            imageRepo.getByImage(id.toLong())
        }
        else
        {
            Collections.emptyList()
        }
    }

    @GetMapping("/get/{get}")
    fun get(@PathVariable get:String):String
    {
        return get
    }

    @GetMapping("/getall")
    fun getall(): MutableList<User>
    {
        return userRepo.findAll()
    }


    @PostMapping("/animal/{animal_name}/{animal_des}/{animal_price_new}/{animal_price_old}/{animal_type}/{animal_contact}/{animal_owner}")
    fun animal_post(
            @RequestParam("uploadfile") file: MultipartFile,
            model: Model,
            @PathVariable animal_name:String,
            @PathVariable animal_des:String,
            @PathVariable animal_price_new:String,
            @PathVariable animal_price_old:String,
            @PathVariable animal_type:String,
            @PathVariable animal_contact:String,
            @PathVariable animal_owner:String): String
    {
        //Appsession.name = token

//        var k1 = Animal(0,animal_name,animal_des,animal_price_new,animal_price_old,animal_type,"",animal_owner.toLong(),animal_contact)
//        var k2 = animalRepo.save(k1)


        var tk = LocalDateTime.now().toString()
        tk = tk.replace(",","")
        tk = tk.replace(".","")
        tk = tk.replace(":","")
        tk = tk.replace("-","")

        var k1 = Animal(0,animal_name,animal_des,animal_price_new,animal_price_old,animal_type,tk+".jpg",animal_owner.toLong(),animal_contact)
        var k2 = animalRepo.save(k1)
        tk = k1.id_animal.toString()+"_"+tk+".jpg"
        var name = tk

        var k3 = Animal_Image(0,k2.id_animal,name)
        imageRepo.save(k3)


        fileStorage.store(file,name)
        model.addAttribute("message", "File uploaded successfully! -> filename = " + file.originalFilename)
        return name
    }

    fun token_varify(token:String):Boolean
    {
        val k = userRepo.getByToken(token)

        try {
            return  !k!!.phone.toString().isEmpty()
        }catch (e:Exception)
        {
            return false
        }
    }

    fun token(number:String):String
    {
        var tk = LocalDateTime.now().toString()
        tk = tk.replace(",","")
        tk = tk.replace(".","")
        tk = tk.replace(":","")
        tk = tk.replace("-","")
        tk = tk+number+tk
        return tk
    }

}