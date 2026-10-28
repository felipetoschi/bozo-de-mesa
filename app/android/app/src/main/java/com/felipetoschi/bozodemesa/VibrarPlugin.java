package com.felipetoschi.bozodemesa;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * Vibração do jogo (chacoalhar o copo, dados caindo, General).
 * "ms" vibra uma vez; "padrao" é como no navegador: [vibra, pausa, vibra, ...] em milissegundos.
 */
@CapacitorPlugin(name = "Vibrar")
public class VibrarPlugin extends Plugin {

    @PluginMethod
    public void vibrar(PluginCall call) {
        try {
            Vibrator v;
            if (Build.VERSION.SDK_INT >= 31) {
                VibratorManager vm = (VibratorManager) getContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                v = vm == null ? null : vm.getDefaultVibrator();
            } else {
                v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            if (v == null || !v.hasVibrator()) {
                call.resolve();
                return;
            }
            JSArray padrao = call.getArray("padrao");
            if (padrao != null && padrao.length() > 0) {
                // No Android o padrão começa com uma pausa: [pausa, vibra, pausa, vibra, ...]
                long[] tempos = new long[padrao.length() + 1];
                for (int i = 0; i < padrao.length(); i++) tempos[i + 1] = padrao.getLong(i);
                if (Build.VERSION.SDK_INT >= 26) v.vibrate(VibrationEffect.createWaveform(tempos, -1));
                else v.vibrate(tempos, -1);
            } else {
                long ms = call.getInt("ms", 20);
                if (Build.VERSION.SDK_INT >= 26) v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
                else v.vibrate(ms);
            }
            call.resolve();
        } catch (Exception e) {
            call.reject("Não deu para vibrar: " + e.getMessage());
        }
    }
}
