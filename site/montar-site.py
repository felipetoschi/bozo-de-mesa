"""Monta o site do jogo (GitHub Pages) na pasta dist/:
- index.html: o jogo, com ícone e manifesto para "Adicionar à tela inicial";
- bozo-de-mesa.html: o jogo num arquivo só, com as fontes e os sons embutidos (para mandar a amigos);
- privacidade.html: a política de privacidade exigida pela Play Store.
Uso: python3 site/montar-site.py
"""
import base64
import datetime
import pathlib
import shutil
import subprocess
import zoneinfo

raiz = pathlib.Path(__file__).resolve().parent.parent
www, site, dist = raiz / "app" / "www", raiz / "site", raiz / "dist"

shutil.rmtree(dist, ignore_errors=True)
shutil.copytree(www, dist)
for nome in ["privacidade.html", "manifest.webmanifest", "icone-192.png", "icone-512.png", "apple-touch-icon.png"]:
    shutil.copy(site / nome, dist / nome)

jogo = (www / "index.html").read_text(encoding="utf-8")

# Versão: data e hora (de Brasília) da última mudança, igual à que aparece no app
quando = int(subprocess.run(["git", "log", "-1", "--format=%ct"], cwd=raiz, capture_output=True, text=True).stdout.strip() or 0)
hora = datetime.datetime.fromtimestamp(quando, zoneinfo.ZoneInfo("America/Sao_Paulo")).strftime("%d/%m/%Y %H:%M")
assert jogo.count("__VERSAO__") == 1
jogo = jogo.replace("__VERSAO__", f"Versão online · atualizada em {hora}")

# Site: ícone e manifesto (no app Android isso não é necessário, por isso entra só aqui)
cabeca = ('<link rel="manifest" href="manifest.webmanifest">'
          '<link rel="icon" href="icone-192.png">'
          '<link rel="apple-touch-icon" href="apple-touch-icon.png">'
          '<meta name="apple-mobile-web-app-capable" content="yes">')
assert jogo.count("<head>") == 1
(dist / "index.html").write_text(jogo.replace("<head>", "<head>" + cabeca, 1), encoding="utf-8")

# Arquivo único: troca cada fonte pelo conteúdo dela em base64
unico = jogo
for fonte in sorted((www / "fonts").glob("*.woff2")):
    ref = f"url(fonts/{fonte.name})"
    assert unico.count(ref) == 1, ref
    dados = base64.b64encode(fonte.read_bytes()).decode()
    unico = unico.replace(ref, f"url(data:font/woff2;base64,{dados})")
# ...e cada som de dado (pasta sons/), levando junto o aviso da licença deles
for som in sorted((www / "sons").glob("*.mp3")):
    ref = f"'sons/{som.name}'"
    assert unico.count(ref) == 1, ref
    dados = base64.b64encode(som.read_bytes()).decode()
    unico = unico.replace(ref, f"'data:audio/mpeg;base64,{dados}'")
aviso = (www / "sons" / "LEIAME.txt").read_text(encoding="utf-8").replace("--", "- -")
unico = unico.replace("</body>", f"<!--\n{aviso}-->\n</body>", 1)
(dist / "bozo-de-mesa.html").write_text(unico, encoding="utf-8")

print("Site montado em", dist)
