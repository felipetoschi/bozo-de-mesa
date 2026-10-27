# Bozó de Mesa — contexto para o Claude

Jogo de dados de bar (bozó) feito em HTML/CSS/JS puro, empacotado como app Android com Capacitor.
Repositório público `felipetoschi/bozo-de-mesa`; link do jogo: https://felipetoschi.github.io/bozo-de-mesa/
O dono do projeto fala português; responda em português, de forma simples (ele não é programador).

## Onde está cada coisa

| O quê | Onde |
|---|---|
| **O jogo inteiro (único arquivo que se edita)** | `app/www/index.html` |
| Fontes locais (funciona sem internet) | `app/www/fonts/` |
| Projeto Android (Capacitor 8, API 36) | `app/android/` — a cópia do jogo dentro dele é gerada, não editar |
| Plugin nativo de compartilhar (imagem/texto direto no WhatsApp) | `app/android/app/src/main/java/com/felipetoschi/bozodemesa/CompartilharPlugin.java` |
| Botão voltar, tela sempre acesa | `.../MainActivity.java` (chama `window.bozoVoltar()` do jogo) |
| Ícone e abertura (fontes das imagens) | `app/assets/` → `npm run icons` dentro de `app/` |
| Compilação do app | `.github/workflows/android.yml`: APK de teste sempre; com os segredos `KEYSTORE_BASE64` e `KEYSTORE_PASSWORD`, também APK e AAB assinados; **todo push no main (mudança em `app/`) cria sozinho uma versão em Releases** (link de download para os amigos); rodando à mão com "publicar" também |
| Site (GitHub Pages) | `.github/workflows/site.yml` + `site/montar-site.py`: publica o jogo, o arquivo único `bozo-de-mesa.html` (fontes embutidas) e `privacidade.html` |
| Material da Play Store, textos, política de privacidade | `loja/` |
| Passo a passo de publicação | `PUBLICAR.md` |

O app não carrega o `@capacitor/core` no JS: para falar com o Android use `window.Capacitor.nativePromise('Plugin','metodo',{...})`; `inApp` diz se está no app.

## Regras do jogo já confirmadas pelo dono (não mudar sem perguntar)

- Até 3 lançamentos; guardar dados; marcar exige tocar na casa **e** apertar Marcar.
- **Pedir baixo**: só a partir do 2º lançamento, vale para o próximo lançamento; os dados que caem valem a face de baixo (7 − valor); dados guardados não viram; dá para cancelar antes de lançar.
- **De boca** = os 5 dados lançados juntos, sem guardar nenhum (no 1º lançamento ou no 2º/3º): Full 15, Seguida 25, Quadrada 35; normal: 10/20/30; General 40; **General de boca (em qualquer lançamento) encerra e vence na hora**. General também vale como Quadrada.
- General (qualquer um, também no marcador) solta fogos de artifício simples na tela (faíscas em HTML/CSS + "GENERAL!" grande, virado para quem fez a jogada na mesa do tablet).
- **Casa anulada**: quando **todos** marcam a mesma casa com os **mesmos pontos** (ex.: todos Quadrada 30), ela é riscada e não soma. Pontos diferentes (35 de boca × 30) **não** anulam; risco (0) nunca anula.
- **Modo jogo: sem desfazer** — errou a marcação, é erro do jogador. **Modo marcador: pode corrigir/apagar.**
- Ao sair Full/Seguida/Quadrada/General: brilho discreto + chance exata daquele lançamento.
- Botão discreto **"teste"** na partida (modo jogo, embaixo do copo) abre o menu de teste do dono. Por enquanto só "Forçar general no próximo lançamento": sai General normal (40), **nunca de boca** (não encerra). Novas opções de teste entram nesse menu.
- **Dupla** (botão ao lado de 2/3/4, só no modo jogo, só pessoas): pergunta quantas duplas (2 a 4), uma cartela por dupla ("Ana e Bia"). Na vez da dupla o 1º joga (até 3 lançamentos, baixo etc.) e aperta "Guardar jogo"; o 2º joga; aparecem os dois jogos lado a lado e escolhe-se qual marcar (o jogo guardado pode ser marcado sem o 2º lançar). De boca vale para cada jogo; General de boca de qualquer um encerra e a dupla vence. Estado: `G.duo`, `G.half` (0/1), `G.saved` {dice,boca}, `G.pick` (0 guardado, 1 atual).

## Funcionalidades prontas

- Jogo no app (2–4 jogadores, pessoas e/ou computador) e **Só marcador** (2–8 jogadores, dados de verdade): ranking ao vivo, Finalizar partida a qualquer momento, Zerar partida.
- Layout: tablet = mesa com cartelas em volta (≤4 jogadores); celular ou marcador com >4 = modo compacto (ranking/nomes escolhem a cartela). Tudo testado de 320×568 até 1280×800.
- Resumo no fim da partida; placar de vitórias e histórico mantidos **com os mesmos jogadores até "Zerar placar"/"Zerar tudo"**.
- Salva tudo: reabrir volta na partida, no resumo ou na tela inicial.
- Exportar: imagem do resultado (WhatsApp direto no app), texto, planilha .csv (navegador). Lembrete de exportar antes de zerar.
- Versão visível na tela inicial e nas Regras: `const VERSAO='__VERSAO__'` é trocado na compilação — app: "Versão 1.0.N · atualizada em dd/mm/aaaa hh:mm" (N = número da compilação, igual ao de Releases); site: "Versão online · atualizada em ...". A data/hora é a do último commit (horário de Brasília), então app e site atualizados mostram a mesma.
- Cartela minimalista "papel e caneta" (linhas riscadas à mão, letra Caveat).

## Situação da versão 1.0

- ✅ GitHub Pages ligado: jogo, `privacidade.html` e `bozo-de-mesa.html` no ar.
- ✅ Segredos da chave criados; APK assinado publicado em Releases (`v1.0.4` foi a primeira). O workflow confere a chave antes de assinar e diz qual segredo está errado.
- Versão nova para os amigos sai sozinha quando a mudança entra no main (o dono quer sempre pronto para o app). Link fixo: https://github.com/felipetoschi/bozo-de-mesa/releases/latest
- O app tem a permissão INTERNET só para isso (sem ela o aviso dava "sem internet"; a política de privacidade explica). O app confere ao abrir (API do GitHub, `releases/latest`) se há versão maior que a dele e avisa na tela inicial/Regras: "Tem versão nova! Toque para baixar" (baixa o APK), "App atualizado" ou "Não deu para conferir". No site não aparece.

Falta decidir/fazer (só se o dono pedir):
1. **Play Store** (ver `PUBLICAR.md`, passos 3 a 7). Recomendação já dada: publicar grátis e sem anúncios primeiro; anúncio intersticial no fim da partida só numa atualização futura.
2. Opcional: capturas da loja atualizadas (sem o marcador e a exportação).

O projeto começou num branch do repositório de perfil `felipetoschi/felipetoschi` (`ccr-ee8ee3ac-gefrkk`); este repositório é a continuação com histórico novo.

## Como trabalhar neste projeto

- Teste no navegador com Playwright (Chromium em `/opt/pw-browsers/chromium`): várias telas (celular e tablet, em pé e deitado), 2–8 jogadores, modo jogo e marcador; conferir que nada se sobrepõe nem rola.
- Depois de enviar, conferir a compilação em Actions (o APK sai em Artifacts).
- Versão "arquivo único" e site: `python3 site/montar-site.py` gera tudo em `dist/` (ignorado pelo git); o GitHub Pages publica sozinho.
- **Fluxo combinado com o dono:** mudança pronta e testada → commit e push no branch → abrir PR → fazer o merge (ele pediu para já deixar pronto para o app) → o push no main publica sozinho o site e a versão nova do app em Releases → conferir que a compilação deu certo e dizer ao dono o número da versão (ex.: 1.0.27) e o link https://github.com/felipetoschi/bozo-de-mesa/releases/latest. Se o PR anterior já foi juntado, recomeçar o branch a partir do main.
- Quem já tem o app instala a versão nova por cima pelo link (o placar não se perde, mesma chave); o próprio app avisa na tela inicial quando sai versão nova (a partir da 1.0.27, que tem a permissão de internet). Quem instalou o APK de teste (Artifacts) precisa desinstalar antes.
- Mudança em regra do jogo: perguntar ao dono quando houver mais de um jeito de entender (ex.: "de boca" foi confirmado como "5 dados lançados juntos").
- Pente fino de layout feito (v1.0.31): 15 tamanhos de 320×568 a 1366×1024, todas as telas e menus, 0 sobreposição/corte. Celular deitado: resumo/exportar em duas colunas, quadro de pontos com 4 por linha, nomes em duas colunas. Só a tela inicial rola um pouco com muitos nomes (formulário, aceitável).
- Sempre explicar ao dono o que mudou em linguagem simples.
