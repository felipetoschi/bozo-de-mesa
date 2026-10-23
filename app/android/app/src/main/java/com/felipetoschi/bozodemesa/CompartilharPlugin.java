package com.felipetoschi.bozodemesa;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import androidx.core.content.FileProvider;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Compartilha o resultado das partidas (imagem e/ou texto).
 * Com "package" (ex.: com.whatsapp) abre direto naquele app; se ele não estiver instalado, mostra a lista de apps.
 */
@CapacitorPlugin(name = "Compartilhar")
public class CompartilharPlugin extends Plugin {

    @PluginMethod
    public void enviar(PluginCall call) {
        String base64 = call.getString("base64");
        String text = call.getString("text");
        String pkg = call.getString("package");
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (base64 != null) {
                File dir = new File(getContext().getCacheDir(), "resultados");
                dir.mkdirs();
                File file = new File(dir, "bozo-resultado.png");
                try (FileOutputStream out = new FileOutputStream(file)) {
                    out.write(Base64.decode(base64, Base64.DEFAULT));
                }
                Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", file);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setClipData(ClipData.newRawUri("", uri));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setType("text/plain");
            }
            if (text != null) intent.putExtra(Intent.EXTRA_TEXT, text);
            if (pkg != null) {
                intent.setPackage(pkg);
                try {
                    getActivity().startActivity(intent);
                    call.resolve();
                    return;
                } catch (ActivityNotFoundException e) {
                    intent.setPackage(null);
                }
            }
            getActivity().startActivity(Intent.createChooser(intent, "Compartilhar resultado"));
            call.resolve();
        } catch (Exception e) {
            call.reject("Não deu para compartilhar: " + e.getMessage());
        }
    }
}
