package com.hsikkk.androidbridgesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
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
        }

        findViewById<Button>(R.id.button_blue).setOnClickListener {
            findViewById<WebView>(R.id.webView).loadUrl(WebFunction.CLICK_BLUE)
        }

        findViewById<Button>(R.id.button_red).setOnClickListener {
            findViewById<WebView>(R.id.webView).loadUrl(WebFunction.CLICK_RED)
        }

        findViewById<Button>(R.id.button_send).setOnClickListener {
            findViewById<WebView>(R.id.webView).loadUrl(String.format(WebFunction.CLICK_SEND, findViewById<EditText>(R.id.editText).text.toString()))
        }
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun setResult(color : String, text : String) {
            findViewById<TextView>(R.id.tv_result).setText(
                "Color : $color\nText : $text"
            )
        }

        @JavascriptInterface
        fun resize(height: Float) {
            val webViewHeight = height * resources.displayMetrics.density
            //webViewHeight is the actual height of the WebView in pixels as per device screen density
        }
    }

    private object WebFunction {
        const val CLICK_BLUE = "javascript:clickBlue()"
        const val CLICK_RED = "javascript:clickRed()"
        const val CLICK_SEND = "javascript:setText(\'%s\')"
    }
}