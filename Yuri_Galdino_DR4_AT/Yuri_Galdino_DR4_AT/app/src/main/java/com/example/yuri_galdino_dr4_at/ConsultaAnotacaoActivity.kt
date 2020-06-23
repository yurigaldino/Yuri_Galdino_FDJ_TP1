package com.example.yuri_galdino_dr4_at

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.android.billingclient.api.*
import com.example.yuri_galdino_dr4_at.adapter.Anotacao
import com.example.yuri_galdino_dr4_at.adapter.RecyclerAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_consulta_anotacao.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ConsultaAnotacaoActivity : AppCompatActivity(),
    BillingClientStateListener,
    SkuDetailsResponseListener,
    PurchasesUpdatedListener,
    ConsumeResponseListener {

    private lateinit var clienteInApp: BillingClient
    private var currentSku = "android.test.purchased"
    private var mapSku = HashMap<String, SkuDetails>()

    lateinit var billingResult: BillingResult
    lateinit var purchases: MutableList<Purchase>

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_anotacao)

        //inicialixar admob
        MobileAds.initialize(this)
        val request = AdRequest.Builder().build()
        adView.loadAd(request)

        val sharedCripto = EncryptedSharedPreferences
            .create(
                "simpleFile",
                masterKeyAlias,
                this,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        val resultado = sharedCripto.getString("produto","consumido")
        Log.i("Compra Cons", resultado.toString())
        if(resultado.toString() == "comprado"){
            btn_comprar_premium.visibility = View.GONE
            adView.visibility = View.GONE

        }else{
            btn_comprar_premium.visibility = View.VISIBLE
            adView.visibility = View.VISIBLE
        }

        //billing
        clienteInApp = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        clienteInApp.startConnection(this)


//        checaSeJaComprou()
    }

    override fun onDestroy() {
        clienteInApp.endConnection()
        super.onDestroy()
    }

    override fun onBillingServiceDisconnected() {
        Log.d("COMPRA>>","Serviço InApp desconectado")
    }

    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        if(billingResult?.responseCode ==
            BillingClient.BillingResponseCode.OK){
            Log.d("COMPRA>>","Serviço InApp conectado")

            val skuList = arrayListOf(currentSku)
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(
                BillingClient.SkuType.INAPP)
            clienteInApp.querySkuDetailsAsync(params.build(), this)
        }
    }

    override fun onSkuDetailsResponse(billingResult: BillingResult?, skuDetailsList: MutableList<SkuDetails>?) {
        if(billingResult?.responseCode ==
            BillingClient.BillingResponseCode.OK){
            mapSku.clear()
            skuDetailsList?.forEach{
                    t ->
                mapSku[t.sku] = t

                val preco = t.price
                val descricao = t.description
                Log.d("COMPRA>>",
                    "Produto Disponivel ($preco): $descricao")
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode ==
            BillingClient.BillingResponseCode.OK &&
            purchases != null) {
            for (purchase in purchases) {
                GlobalScope.launch(Dispatchers.IO){
                    handlePurchase(purchase)
                }
            }
            Toast.makeText(this, "Versão comprada com sucesso", Toast.LENGTH_SHORT).show()
            adView.visibility = View.GONE
            //Shared Preferences que foi comprado

//            coloca_no_shared()

        } else if (billingResult.responseCode ==
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
//            Log.d("COMPRA>>","Produto já foi comprado")
            Toast.makeText(this, "Versão Premium já comprada.", Toast.LENGTH_SHORT).show()
            //Shared Preferences que foi comprado
        } else if (billingResult.responseCode ==
            BillingClient.BillingResponseCode.USER_CANCELED) {
//            Log.d("COMPRA>>","Usuário cancelou a compra")
            Toast.makeText(this, "Compra cancelada.", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("COMPRA>>",
                "Código de erro desconhecido: ${billingResult.responseCode}")
            Toast.makeText(this, "Erro1.", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED)
        {
            // Aqui acessaria a base e concederia o produto ao usuário
//            Log.d("COMPRA>>","Produto obtido com sucesso")
            val sharedCripto = EncryptedSharedPreferences
                .create(
                    "simpleFile",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            val editor = sharedCripto.edit()
            editor.putString("produto", "comprado")
            editor.apply()

            // Acknowledge -> Obrigatório para confirmação ao Google
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
//                Toast.makeText(this, "Versão comprada com sucesso", Toast.LENGTH_SHORT).show()
//                adView.visibility = View.GONE
                val ackPurchaseResult = withContext(Dispatchers.IO) {
                    clienteInApp.acknowledgePurchase(
                        acknowledgePurchaseParams.build())
                }
            }
        }
    }

    override fun onConsumeResponse(billingResult: BillingResult?, purchaseToken: String?) {
        if(billingResult?.responseCode==
            BillingClient.BillingResponseCode.OK) {
//            Log.d("COMPRA>>", "Produto Consumido")
            val sharedCripto = EncryptedSharedPreferences
                .create(
                    "simpleFile",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            val editor = sharedCripto.edit()
            editor.putString("produto", "consumido")
            Log.i("Compra Consumo","passei")
            editor.apply()
            Toast.makeText(this, "Compra refeita.", Toast.LENGTH_SHORT).show()
            btn_comprar_premium.visibility = View.VISIBLE
            adView.visibility = View.VISIBLE
        }
    }

    fun processGoogleInApp(view: View) {
        val skuDetails = mapSku[currentSku]
        val purchaseParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails).build()
        clienteInApp.launchBillingFlow(this,purchaseParams)
    }

    fun consumeGoogleInApp(view: View) {
        var compras = clienteInApp.queryPurchases(
            BillingClient.SkuType.INAPP)
        if( compras.purchasesList.size > 0 )
        {
            var purchase: Purchase = compras.purchasesList[0]
            var params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .setDeveloperPayload(purchase.developerPayload)
                .build()
            clienteInApp.consumeAsync(params,this)
        }
    }

//    fun coloca_no_shared(){
//        var pref = getPreferences(Context.MODE_PRIVATE)
//        val editor = pref.edit()
//
//        editor.putBoolean("COMPRA_SUCCESS", true)
//
//        editor.commit()
//    }

//    fun checaSeJaComprou(){
//
//        billingResult = BillingResult()
//
//        if (billingResult.responseCode ==
//            BillingClient.BillingResponseCode.OK &&
//            purchases != null){
//            adView.visibility = View.GONE
//        } else {
//            adView.visibility = View.VISIBLE
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()

        rcyVwAnots.adapter = RecyclerAdapter(updateRcy())
        rcyVwAnots.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateRcy(): List<Anotacao> {
        val location = File(this.filesDir.toURI())
        var nome = ""
        val files = location.listFiles()
        val d = mutableListOf<Anotacao>()

        files?.forEach {
            if ("$nome.txt" != it.name && "$nome.fig" != it.name) {
                nome = it.name.removeSuffix(".txt")
                nome = nome.removeSuffix(".fig")
                d.add(pegaInfo(nome))
            }
        }
        return d
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pegaInfo(prefix: String): Anotacao {
        var novo = prefix.removeSuffix(".fig")
        novo = novo.removeSuffix(".txt")
        val img: ByteArray = Criptografador().leituraDeImagem("$novo.fig", this)

        val info: String = Criptografador().leituraArquivoTexto("$novo.txt", this)[2]
        val bitmapp = BitmapFactory.decodeByteArray(img, 0, img.size)

        return Anotacao(
            prefix.split("(")[0],
            info,
            prefix.split("(")[1].removeSuffix(")"),
            bitmapp
        )
    }
}
