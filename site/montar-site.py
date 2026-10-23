"""Monta o site do jogo (GitHub Pages) na pasta dist/:
- index.html: o jogo, com ícone e manifesto para "Adicionar à tela inicial";
- bozo-de-mesa.html: o jogo num arquivo só, com as fontes embutidas (para mandar a amigos);
- privacidade.html: a política de privacidade exigida pela Play Store.
Uso: python3 site/montar-site.py
"""
import base64
import pathlib
import shutil

raiz = pathlib.Path(__file__).resolve().parent.parent
www, site, dist = raiz / "app" / "www", raiz / "site", raiz / "dist"

shutil.rmtree(dist, ignore_errors=True)
shutil.copytree(www, dist)
for nome in ["privacidade.html", "manifest.webmanifest", "icone-192.png", "icone-512.png", "apple-touch-icon.png"]:
    shutil.copy(site / nome, dist / nome)

jogo = (www / "index.html").read_text(encoding="utf-8")

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
(dist / "bozo-de-mesa.html").write_text(unico, encoding="utf-8")

print("Site montado em", dist)
