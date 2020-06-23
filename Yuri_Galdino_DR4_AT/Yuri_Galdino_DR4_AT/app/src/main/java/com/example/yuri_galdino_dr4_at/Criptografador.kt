package com.example.yuri_galdino_dr4_at

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.*

class Criptografador {

    private fun TrataEncryptedFile (nome: String, context: Context): EncryptedFile {
        val masterKeyAlias: String =
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val file: File =
            File(context.filesDir, nome)
        return EncryptedFile.Builder(
            file,
            context,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        )
            .build()
    }

    fun armazenadorDeArquivoByte(nome: String, context: Context, img: ByteArray) {
        val encryptedOut: FileOutputStream =
            TrataEncryptedFile(nome, context).openFileOutput()
        encryptedOut.write(img)
        encryptedOut.close()
    }

    fun armazenarArquivo(nome: String, context: Context, texto: List<String>) {
        val encryptedOut: FileOutputStream =
            TrataEncryptedFile(nome, context).openFileOutput()
        val printWriter = PrintWriter(encryptedOut)
        texto.forEach {
            printWriter.println(it)
        }
        printWriter.flush()
        encryptedOut.close()
    }

    fun leituraDeImagem(nome: String, context: Context): ByteArray {

        val encryptedIn: FileInputStream = TrataEncryptedFile(nome, context).openFileInput()
        return ByteArrayInputStream(encryptedIn.readBytes()).readBytes()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun leituraArquivoTexto(nome: String, context: Context): List<String> {

        val encryptedIn: FileInputStream = TrataEncryptedFile(nome, context).openFileInput()
        val line = BufferedReader(InputStreamReader(encryptedIn)).lines()
        val resultado = mutableListOf<String>()

        line.forEach { resultado.add(it)
        }
        encryptedIn.close()
        return resultado
    }
}