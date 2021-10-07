package com.hsikkk.androidbridgesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class MainActivity : AppCompatActivity() {
    private lateinit var  wv : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init webview
        findViewById<WebView>(R.id.webView).apply {
            visibility = View.VISIBLE
            WebView.setWebContentsDebuggingEnabled(true)
            val bridge = AndroidBridge()
            getSettings().setJavaScriptEnabled(true);
            getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            addJavascriptInterface(bridge, "Bridge");
            setWebChromeClient(WebChromeClient());
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    view?.loadUrl("javascript:AndroidFunction.resize(document.body.scrollHeight)");
                }
            }

            loadUrl("http://192.168.0.3:3000/");

            wv = this
        }

//        findViewById<Button>(R.id.button_blue).setOnClickListener {
//            findViewById<WebView>(R.id.webView).evaluateJavascript(WebFunction.CLICK_BLUE, null)
//        }
//
//        findViewById<Button>(R.id.button_red).setOnClickListener {
//            findViewById<WebView>(R.id.webView).loadUrl(WebFunction.CLICK_RED)
//        }
//
//        findViewById<Button>(R.id.button_send).setOnClickListener {
//            findViewById<WebView>(R.id.webView).loadUrl(String.format(WebFunction.CLICK_SEND, findViewById<EditText>(R.id.editText).text.toString()))
//        }
    }

    inner class AndroidBridge {
//        @JavascriptInterface
//        fun setResult(color : String, text : String) : String{
//            findViewById<TextView>(R.id.tv_result).setText(
//                "Color : $color\nText : $text"
//            )
//
//            return "1"
//        }

        @JavascriptInterface
        fun resize(height: Float) {
            val webViewHeight = height * resources.displayMetrics.density
            //webViewHeight is the actual height of the WebView in pixels as per device screen density
        }

        @JavascriptInterface
        fun postAction(callbackID: String, action: String, actionArgs: String){
            val jsonObject = JsonParser.parseString(actionArgs) as JsonObject

            if(action == "onSubmit"){
                runOnUiThread {
                    val a = mapOf<String, String>("color" to "blue", "message" to "hihi")
                    wv.apply {
                        evaluateJavascript(
                    "javascript:window.interface.nativeCallback(" +
                            "`${callbackID}`, " +
                            "false," +
                            "{color : '${jsonObject["color"].asString}', message : 'hi'}," +
                            "true)",
            null)
                    }
                }
            }
        }
    }

//    private object WebFunction {
//        const val CLICK_BLUE = "javascript:window.interface.clickBlue('abab')"
//        const val CLICK_RED = "javascript:clickRed()"
//        const val CLICK_SEND = "javascript:setText(\'%s\')"
//    }
}