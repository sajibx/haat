package com.ss.haat.Service

import com.ss.haat.Data.Appsession
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.stream.Stream

@Service
class FileStorageImpl : FileStorage {

    val rootLocation = Paths.get("filestorage")

    override fun store(file: MultipartFile, name:String) {
//        var tk = LocalDateTime.now().toString()
//        tk = tk.replace(",","")
//        tk = tk.replace(".","")
//        tk = tk.replace(":","")
//        tk = Appsession.name+tk.replace("-","")+".jpg"
//        Appsession.file = tk
        Files.copy(file.inputStream, this.rootLocation.resolve(name))//file.originalFilename))
    }

    override fun loadFile(filename: String): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }

    override fun deleteAll() {
        //FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(this.rootLocation, 1)
                .filter { path -> !path.equals(this.rootLocation) }
                .map(this.rootLocation::relativize)
    }

}