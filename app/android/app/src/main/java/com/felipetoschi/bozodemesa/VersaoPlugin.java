package com.felipetoschi.bozodemesa;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Descobre a versão mais nova do app publicada em Releases.
 * Pergunta à página github.com/.../releases/latest (que redireciona para .../releases/tag/v1.0.N):
 * ao contrário da API do GitHub, essa página não tem limite de consultas por hora.
 */
@CapacitorPlugin(name = "Versao")
public class VersaoPlugin extends Plugin {

    @PluginMethod
    public void ultima(PluginCall call) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL("https://github.com/felipetoschi/bozo-de-mesa/releases/latest").openConnection();
            con.setInstanceFollowRedirects(false);
            con.setConnectTimeout(8000);
            con.setReadTimeout(8000);
            con.setRequestMethod("HEAD");
            con.setUseCaches(false);
            con.getResponseCode();
            String destino = con.getHeaderField("Location");
            String tag = destino == null ? "" : destino.substring(destino.lastIndexOf('/') + 1);
            if (!tag.startsWith("v")) {
                call.reject("Resposta inesperada do GitHub");
                return;
            }
            JSObject ret = new JSObject();
            ret.put("tag", tag);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Não deu para conferir: " + e.getMessage());
        } finally {
            if (con != null) con.disconnect();
        }
    }
}
