package com.aethernova.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity {
    private static final String CONTROLLER_URL = "https://mariotonygomez81-debug.github.io/aether-nova-mobile-controller/";
    private static final int CAMERA_PERMISSION_REQUEST = 701;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        FrameLayout root = new FrameLayout(this);
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        root.addView(webView, new FrameLayout.LayoutParams(-1, -1));

        Button scanButton = new Button(this);
        scanButton.setText("ESCANEAR QR");
        scanButton.setTextColor(Color.WHITE);
        scanButton.setTextSize(13);
        scanButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        scanButton.setAllCaps(false);
        scanButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(0, 145, 190)));
        scanButton.setOnClickListener(v -> requestCameraAndScan());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp(168), dp(48), Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        params.topMargin = dp(10);
        root.addView(scanButton, params);
        setContentView(root);
        webView.loadUrl(buildControllerUrl(getIntent()));
    }

    private int dp(int value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void requestCameraAndScan() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else startQrScan();
    }

    private void startQrScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Apuntá la cámara al QR de AETHER NOVA");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) startQrScan();
            else Toast.makeText(this, "Necesitamos permiso de cámara para escanear el QR.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String code = extractCode(result.getContents());
                if (code != null) {
                    String player = extractPlayer(result.getContents());
                    webView.loadUrl(CONTROLLER_URL + "?code=" + code + ("2".equals(player) ? "&player=2" : ""));
                    Toast.makeText(this, "Código detectado. Conectando joystick…", Toast.LENGTH_LONG).show();
                } else Toast.makeText(this, "El QR no contiene un código válido de AETHER NOVA.", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String extractCode(String raw) {
        try {
            String code = Uri.parse(raw).getQueryParameter("code");
            if (code != null && code.matches("(?i)[A-F0-9]{12}")) return code.toUpperCase();
        } catch (Exception ignored) { }
        return null;
    }

    private String extractPlayer(String raw) {
        try { return "2".equals(Uri.parse(raw).getQueryParameter("player")) ? "2" : "1"; }
        catch (Exception ignored) { return "1"; }
    }

    private String buildControllerUrl(Intent intent) {
        Uri data = intent != null ? intent.getData() : null;
        String code = data != null ? data.getQueryParameter("code") : null;
        String player = data != null ? data.getQueryParameter("player") : null;
        if (code != null && code.matches("(?i)[A-F0-9]{12}")) return CONTROLLER_URL + "?code=" + code.toUpperCase() + ("2".equals(player) ? "&player=2" : "");
        return CONTROLLER_URL;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (webView != null) webView.loadUrl(buildControllerUrl(intent));
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
