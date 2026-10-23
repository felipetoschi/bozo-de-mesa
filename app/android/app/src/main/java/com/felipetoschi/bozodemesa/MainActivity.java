package com.felipetoschi.bozodemesa;

import android.os.Bundle;
import android.view.WindowManager;
import androidx.activity.OnBackPressedCallback;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Plugin próprio para mandar o resultado (imagem ou texto) direto para o WhatsApp
        registerPlugin(CompartilharPlugin.class);
        super.onCreate(savedInstanceState);
        // Jogo de mesa: a tela não apaga enquanto o app está aberto
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Botão voltar: o jogo fecha a janela aberta ou pede um segundo toque; só então o app vai para o fundo
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getBridge() == null || getBridge().getWebView() == null) {
                    moveTaskToBack(true);
                    return;
                }
                getBridge().getWebView().evaluateJavascript(
                    "window.bozoVoltar ? window.bozoVoltar() : 'sair'",
                    resultado -> {
                        if (resultado != null && resultado.contains("sair")) moveTaskToBack(true);
                    }
                );
            }
        });
    }
}
