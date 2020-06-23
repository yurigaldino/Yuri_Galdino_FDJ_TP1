package com.example.yuri_galdino_dr4_at

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_nova_anotacao.*
import java.io.ByteArrayOutputStream
import java.util.*

class NovaAnotacaoActivity : AppCompatActivity() {

    private val PICK_FROM_GALLERY = 1

    private var imagemByteArray: ByteArray? = null

    private var latitude_var: String = ""
    private var longitude_var: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_anotacao)

        //Vê permissões e dá uma resposta
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                ), 10
            )
        } else {
            leituraDeCoordernadas()
        }

        //Adicao de uma imagem
        btn_adicionar_imagem.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            if (intent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(intent, PICK_FROM_GALLERY)
            }
        }

        //Processo de inserir uma anotação
        btn_fazer_anot.setOnClickListener {

            val titulo = inputTitulo.text.toString()
            val text = inputTexto.text.toString()
            val data = SimpleDateFormat("dd_MM_yyyy").format(Date())

            //Segunda checagem de permissões para tentar fazer com que o user aceite, se não aceitou na primeira
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                //Pedido de permissão
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 10
                )
            } else {
                if (imagemByteArray == null || text.isEmpty() || titulo.isEmpty()) {
                    Toast.makeText(this,
                        "Preencha todos os campo e insira uma imagem.", Toast.LENGTH_SHORT).show()
                } else {
                    //Ver se lat lon deu bom (AQUI OCORRE O BUG DE SEMPRE NÃO SER POSSÍVEL NA PRIMEIRA VEZ QUE ENTRA NO APP)
                    if (latitude_var.isEmpty() || longitude_var.isEmpty()) { Toast.makeText(this,
                            "Erro ao ler latitude e longitude, tente novamente.", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {

                        //arquivo em si
                        val nomeDoArquivo = "${titulo.toLowerCase(Locale.ROOT)}(${data})"

                        val DADOS = "$nomeDoArquivo.txt"
                        val IMAGEM = "$nomeDoArquivo.fig"

                        //Inserção de dados no internal storage
                        Criptografador().armazenarArquivo(DADOS, this, listOf(latitude_var, longitude_var, text))
                        Toast.makeText(this, "Anotação armazenada com sucesso.", Toast.LENGTH_SHORT).show()

                        Criptografador().armazenadorDeArquivoByte(IMAGEM, this, imagemByteArray!!)
                        Toast.makeText(this, "Imagem armazenada com sucesso.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Não é possível realizar esta ação ser a permissão do uso de localização.", Toast.LENGTH_LONG).show()
            }
        }
    }

    //onde se altera para bitmap (e trocar imagem)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_FROM_GALLERY -> {
                val uri: Uri? = data?.data
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                imageView3.setImageBitmap(bitmap)

                val img = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, img)
                val byteArray = img.toByteArray()
                imagemByteArray = byteArray
            }
        }
    }
    private fun leituraDeCoordernadas() {
        val locationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )
        val isNetworkEnabled = locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

        if (!isGPSEnabled && !isNetworkEnabled) {
        } else {
            if (isGPSEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permission", "Security Exception")
                }
            } else if (isNetworkEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permission", "Security Exception")
                }
            }
        }
    }

    private val locationListener: LocationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                latitude_var = "${location.latitude}"
                longitude_var = "${location.longitude}"
            }

            override fun onStatusChanged(
                provider: String, status: Int, extras: Bundle
            ) {
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
}
