package com.example.yuri_galdino_dr4_at

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "FirebaseEmailPassword"

    private var mAuth: FirebaseAuth? = null

    private var latitude_var: String = ""
    private var longitude_var: String = ""

    companion object {
        val baseURL = "https://api.exchangeratesapi.io/latest?base=USD"
    }

    class chamadaREST : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            val url = URL(baseURL)
            val conn = url.openConnection() as HttpsURLConnection
            var linha: String?
            var resultado = ""
            conn.requestMethod = "GET"
            conn.connect()
            val entrada = BufferedReader(
                InputStreamReader(conn.inputStream)
            )
            while (entrada.readLine().also { linha = it } != null) {
                resultado += linha
            }
            entrada.close()
            return resultado
        }
        override fun onPostExecute(resultado: String?) {
            val valorJSON = JSONObject(resultado)
            val valorDolar = valorJSON.getJSONObject("rates")
                .getDouble("BRL")
            Log.d("RESTful>>", "US$ 1.00 vale R$ $valorDolar")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_email_sign_in.setOnClickListener(this)
        btn_email_create_account.setOnClickListener(this)
        btn_sign_out.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()

        btn_nova_anot.setOnClickListener {
            val intent = Intent(this, NovaAnotacaoActivity::class.java)
            startActivity(intent)
        }
        btn_consulta.setOnClickListener {
            val intent = Intent(this, ConsultaAnotacaoActivity::class.java)
            startActivity(intent)
        }
    }

    fun processREST(view: View) {
        chamadaREST().execute()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    override fun onClick(view: View?) {
        val i = view!!.id

        if (i == R.id.btn_email_create_account) {
            createAccount(edtEmail.text.toString(), edtPassword.text.toString())
        } else if (i == R.id.btn_email_sign_in) {
            signIn(edtEmail.text.toString(), edtPassword.text.toString())
        } else if (i == R.id.btn_sign_out) {
            signOut()
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.e(TAG, "createAccount:" + email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e(TAG, "createAccount: Success!")

                        // update UI with the signed-in user's information
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        Log.e(TAG, "createAccount: Fail!", task.exception)
                        Toast.makeText(applicationContext, "Autenticação falhou!", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
    }

    private fun signIn(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e(TAG, "signIn: Success!")

                        // update UI with the signed-in user's information
                        val user = mAuth!!.getCurrentUser()
                        updateUI(user)
                        if (ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissions(
                                arrayOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ), 10
                            )
                        } else {
                            leituraDeCoordernadas()
                        }
                    } else {
                        Log.e(TAG, "signIn: Fail!", task.exception)
                        Toast.makeText(applicationContext, "Autenticação falhou!", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    if (!task.isSuccessful) {
                        tvStatus.text = "Autenticação falhou!"
                    }
                }
    }

    private fun signOut() {
        mAuth!!.signOut()
        updateUI(null)
    }

    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Coloque um endereço de email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Insira uma senha.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Senha curta demais, insira pelo menos 6 dígitos.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            tvStatus.text = "User Email: " + user.email + "(verified: " + user.isEmailVerified + ")"
            tvDetail.text = "Firebase User ID: " + user.uid

            txtVwAt.visibility = View.GONE
            email_password_buttons.visibility = View.GONE
            email_password_fields.visibility = View.GONE
            relative_layout_cinza.visibility = View.GONE
            //layout com o botao de sign out
            btn_sign_out.visibility = View.VISIBLE
            botoes_tp.visibility = View.VISIBLE

//            btn_verify_email.isEnabled = !user.isEmailVerified
        } else {
            tvStatus.text = "Signed Out"
            tvDetail.text = null

            email_password_buttons.visibility = View.VISIBLE
            email_password_fields.visibility = View.VISIBLE
            relative_layout_cinza.visibility = View.VISIBLE
            botoes_tp.visibility = View.GONE
            btn_sign_out.visibility = View.GONE
            txtVwAt.visibility = View.VISIBLE
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
            Log.d("Permissao", "Ative os serviços necessários")
        } else {
            if (isGPSEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            } else if (isNetworkEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
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
