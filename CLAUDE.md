# Bozó de Mesa — contexto para o Claude

Jogo de dados de bar (bozó) feito em HTML/CSS/JS puro, empacotado como app Android com Capacitor.
Repositório público `felipetoschi/bozo-de-mesa`; link do jogo: https://felipetoschi.github.io/bozo-de-mesa/
O dono do projeto fala português; responda em português, de forma simples (ele não é programador).

## Onde está cada coisa

| O quê | Onde |
|---|---|
| **O jogo inteiro (único arquivo que se edita)** | `app/www/index.html` |
| Fontes locais (funciona sem internet) | `app/www/fonts/` |
| Sons gravados de dados (feltro e batidas; licença MIT do pacote `@3d-dice/dice-box-threejs`, ver `LEIAME.txt`) | `app/www/sons/` — o site embute no arquivo único |
| Versão mais nova publicada (plugin nativo `Versao`) e aviso de app de volta à tela | `.../VersaoPlugin.java`; `MainActivity.onResume` → `window.bozoVoltou()` |
| Vibração (plugin nativo `Vibrar`, permissão VIBRATE) | `.../VibrarPlugin.java`; no JS `vibrar(ms)` ou `vibrar([vibra,pausa,...])` |
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
- **Casa anulada**: quando **todos** marcam a mesma casa com os **mesmos pontos** (ex.: todos Quadrada 30), ela é riscada e não soma. Pontos diferentes (35 de boca × 30) **não** anulam; corte (0) nunca anula.
- **Cortar** (não "riscar") é o nome de marcar 0 numa casa: botão "Cortar Full", resumo "2 cortes", opção "cortar" no marcador.
- **Modo jogo: sem desfazer** — errou a marcação, é erro do jogador. **Modo marcador: pode corrigir/apagar.**
- **Cortar pede confirmação** (modo jogo, pedido do dono): o 1º toque em "Cortar X" deixa o botão vermelho ("✓ Cortar", `#mark.ask`, `cutT`) e mostra ao lado "✕ Cancelar" (`#cutNo`, dentro de `.markRow`); "✓ Cortar" corta, Cancelar desiste; sem resposta em 6 s, volta ao normal. Trocar de casa ou lançar também cancela.
- Ao sair Full/Seguida/Quadrada/General: brilho discreto + chance exata daquele lançamento.
- O botão discreto "teste" (menu com "Forçar general", que saía General normal, nunca de boca) foi usado e **retirado** a pedido do dono; se ele pedir de novo, ver o histórico do git (commit "volta o botão de teste").
- **Dupla** (botão ao lado de 2/3/4, só no modo jogo, só pessoas): pergunta quantas duplas (2 a 4), uma cartela por dupla ("Ana e Bia"). Na vez da dupla o 1º joga (até 3 lançamentos, baixo etc.) e aperta "Guardar jogo"; o 2º joga; aparecem os dois jogos lado a lado e escolhe-se qual marcar (o jogo guardado pode ser marcado sem o 2º lançar). De boca vale para cada jogo; General de boca de qualquer um encerra e a dupla vence. Estado: `G.duo`, `G.half` (0/1), `G.saved` {dice,boca}, `G.pick` (0 guardado, 1 atual).

## Funcionalidades prontas

- **Níveis do computador** (`G.botLevel`, padrão Médio; partidas antigas = Difícil): **Fácil** guarda o número que mais aparece, às vezes marca sem tentar de novo, marca o que dá mais ponto na hora (~75 pts/partida); **Médio** = planejador com 40 simulações, peso 0,5 nas casas valiosas e enganos aleatórios (~99); **Difícil** = planejador completo, 150 simulações (~112). **Bot rápido** (`G.botFast`): pausas ×0,35 e chacoalhar mais curto (vez em ~3,7 s em vez de ~10,5 s). Escolhidos na tela de jogadores (`#botOpts`, aparece quando há bot); preferência em `localStorage` `bozo-bot`.
- Computador se chama só **"Bot"** (com 🤖 na frente; com mais de um, Bot 1, Bot 2...); o nome não se edita. Partidas salvas antigas são renomeadas ao abrir.
- **Abertura (`#menu`)**: logo, desenho do copo derramando os dados (tocar chacoalha; 5 iguais solta fogos) e três botões grandes: 🤖 Jogar contra o computador, 👥 Jogar com amigos, 📝 Só marcador; embaixo Regras e Som. "▶️ Continuar partida" aparece em cima quando há partida pela metade (fica amarelo; senão o amarelo é "contra o computador"). Variável `flow` = 'bot' | 'amigos' | 'marcador'.
- **Tela de jogadores (`#setup`)**: "← Voltar", quantos (bots 1–3 / jogadores 2–4 + Dupla / marcador 2–8) e os nomes em **plaquinhas de papel em volta de uma mesinha**, no mesmo lugar da mesa do tablet (`cellsOf`), com o número da ordem de jogar. Em "com amigos" cada lugar tem "🤖 pôr bot". Botão voltar do Android na tela de jogadores volta para a abertura. Celular deitado: controles à esquerda, mesinha à direita.
- Os campos de nome vêm **sempre em branco** (aparece "Jogador 1", "Jogador 2"...); o último nome digitado não fica salvo (`autocomplete=off`). Guarda só o jeito de jogar, o número de jogadores e quem é computador. O placar de vitórias segue com "Próxima partida" (ou digitando os mesmos nomes).
- Jogo no app (2–4 jogadores, pessoas e/ou computador) e **Só marcador** (2–8 jogadores, dados de verdade): Finalizar partida a qualquer momento, Zerar partida.
- **Marcador: uma pessoa só marca** (pedido do dono, v1.0.53): no tablet com 2–4 jogadores as cartelas em volta ficam viradas para cada dono **só para ver** (`seatHTML(p,i,ro)`, sem toque); no meio, virada para quem marca (embaixo), fica a **cartela grande** (`#mkCard`) da vez ou de quem foi tocado na lista (corrigir), com a lista e os botões ao lado (`live[data-shape=side]`, deitado) ou embaixo (`stack`, em pé); `fitScorer` escolhe. Tablet pequeno sem espaço: `mini` (lista em 2 colunas; tocar no nome abre a cartela num quadro, `openSheet`). O quadro de pontos fica sempre reto (para quem marca). Com 5–8 jogadores (e no celular) continua o modo compacto.
- **Marcador com vez** (pedido do dono): marcou uma casa nova, a vez passa sozinha para o próximo com casa vazia (`nextOpen`) e no celular aparece a cartela dele; corrigir/apagar não muda a vez. A lista fica **na ordem da jogada** (sem 👑 nem 1º/2º, para não confundir), com 🎲 e borda dourada em quem joga; a cartela da vez brilha.
- Layout: tablet = mesa com cartelas em volta (≤4 jogadores); celular ou marcador com >4 = modo compacto (ranking/nomes escolhem a cartela). Tudo testado de 320×568 até 1366×1024.
- **Tela fixa** (pedido do dono): a página nunca rola nem "puxa" (`html,body` com `overflow:hidden`, `overscroll-behavior:none`); `.mesa` tem a altura exata da tela e as telas preenchem com flex (sem `100vh`). Abertura e jogadores encolhem com `zoom` (`fitScreen`) se não couberem; só Regras/resumo/exportar rolam por dentro.
- **Copo sempre à direita dos dados** (pedido do dono) e **copo e dados sempre abaixo da cartela** (ergonomia): celular em pé = placar / cartela (encostada embaixo) / baixo, lançamentos e copo à direita / dados / Marcar / botões; celular deitado = coluna à esquerda com placar e botões, à direita cartela em cima e embaixo dados | baixo e Marcar | copo.
- **Tablet, jogo no app** (`#game.tpanel`): o painel do copo (`#spinWrap`, dados de 64px, copo grande à direita de quem joga) muda de lugar a cada vez e fica entre a cartela de quem joga e a beirada da mesa, virado para ele. Tem dois formatos (`data-shape` `long` = baixo e comprido, `block` = mais quadrado). No meio da mesa (`#meio`) ficam o "Vez de" e o **placar ao vivo** (2 colunas), virados para quem joga, em linha (`row`) ou empilhados (`col`). `fitTable(pos)` testa os formatos e calcula o tamanho das cartelas para caber com o copo em qualquer lado (não muda de uma vez para outra), escolhendo entre laterais na altura toda ou cima/baixo na largura toda (classe `wide`). Não usar a classe `mid` fora da cartela (é das casas do meio). **Sozinho contra o computador** (≤1 pessoa): nada vira — computadores em cima, a pessoa embaixo e o copo sempre debaixo da cartela dela. Marcador no tablet continua como antes (ranking no meio, cartelas viradas).
- Resumo no fim da partida; placar de vitórias e histórico mantidos **com os mesmos jogadores até "Zerar placar"/"Zerar tudo"**.
- Salva tudo: reabrir volta na partida, no resumo ou na abertura (com "Continuar partida" se ficou pela metade).
- Exportar: imagem do resultado (WhatsApp direto no app), texto, planilha .csv (navegador). Lembrete de exportar antes de zerar.
- Versão visível na tela inicial e nas Regras: `const VERSAO='__VERSAO__'` é trocado na compilação — app: "Versão 1.0.N · atualizada em dd/mm/aaaa hh:mm" (N = número da compilação, igual ao de Releases); site: "Versão online · atualizada em ...". A data/hora é a do último commit (horário de Brasília), então app e site atualizados mostram a mesma.
- Cartela minimalista "papel e caneta" (linhas riscadas à mão, letra Caveat). Nomes das casas mais escuros (`#6f604c`) e maiores no tablet (`--cw*.045`, números `*.095`) e no celular (10,5px).
- Mesa verde lisa (gradiente). Uma textura de feltro felpudo foi testada na 1.0.45 e **o dono não gostou** — não voltar com ela. **Copo de couro** (`--couro`: SVG com bordas grossas, costuras e grão; usado no `.cup` do jogo e no `.artcup` da abertura). Filtros SVG precisam de `color-interpolation-filters='sRGB'` (senão as cores clareiam).
- **Fim da partida**: botão "🏠 Voltar ao início" (vai para a abertura); na barra do jogo o 🏠 se chama "Início".
- **Sons gravados e vibração**: copo chacoalhando = batidas de dados (`toc`); dados caindo = feltro com quiques (`landSound`); guardar dado = toc. **Marcar = lápis riscando o papel** (`markSound(cut)`: chiado de grafite feito pelo app em 2–3 traços; cortar = risco comprido + cruzado; o dono **não gostou** desse som sintetizado e quer um som gravado de lápis anotando; os sites de sons estão bloqueados nas sessões — pedir o arquivo ao dono, ex.: enviado ao repositório). Fanfarra continua sintetizada. Sem os arquivos (ex.: abrindo por file://) volta aos sons sintetizados. Vibra ao chacoalhar, ao cair, ao guardar dado, ao marcar e no General (só jogadas de pessoas). Botão "📳 Vibração" na abertura liga/desliga (`bozo-vibra`).

## Situação da versão 1.0

- ✅ GitHub Pages ligado: jogo, `privacidade.html` e `bozo-de-mesa.html` no ar.
- ✅ Segredos da chave criados; APK assinado publicado em Releases (`v1.0.4` foi a primeira). O workflow confere a chave antes de assinar e diz qual segredo está errado.
- Versão nova para os amigos sai sozinha quando a mudança entra no main (o dono quer sempre pronto para o app). Link fixo: https://github.com/felipetoschi/bozo-de-mesa/releases/latest
- O app tem a permissão INTERNET só para isso (sem ela o aviso dava "sem internet"; a política de privacidade explica). O app confere ao abrir **e toda vez que volta para a tela** (o Android não fecha o app: `MainActivity.onResume` chama `window.bozoVoltou()`; intervalo mínimo de 30 s) se há versão maior que a dele. Fonte no app: plugin nativo `Versao` (`VersaoPlugin.java`, lê o redirecionamento de `github.com/.../releases/latest` → `/tag/v1.0.N`, **sem limite de consultas**); reserva: API do GitHub (60/h por IP, estoura em rede de celular). A maior versão vista fica em `localStorage` `bozo-ultima`: sem internet, continua exigindo a atualização. Download pelo link fixo `releases/latest/download/bozo-de-mesa.apk`. **Atualização obrigatória** (pedido do dono): com versão nova aparece o aviso `#updGate` por cima de tudo, que não fecha (voltar do Android sai do app), com "⬇️ Baixar e instalar". Sem internet ou com a mesma versão, joga normal ("App atualizado" / "Não deu para conferir"). No site não aparece.

Falta decidir/fazer (só se o dono pedir): a lista combinada com o dono está em **`MELHORIAS.md`** (próximos passos em ordem; marcar `[x]` e anotar a versão quando ficar pronto). Inclui:
1. **Play Store** (ver `PUBLICAR.md`, passos 3 a 7). Recomendação já dada: publicar grátis e sem anúncios primeiro; anúncio intersticial no fim da partida só numa atualização futura.
2. Capturas da loja atualizadas (tela inicial nova, marcador, exportação).

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
