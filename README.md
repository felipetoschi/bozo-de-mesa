# 🎲 Bozó de Mesa

O jogo de dados de bar para jogar em volta do tablet ou passando o celular de mão em mão — ou só para marcar os pontos quando o jogo é com copo e dados de verdade.

**▶️ Jogar agora:** https://felipetoschi.github.io/bozo-de-mesa/
(no celular, use "Adicionar à tela inicial" para ganhar um ícone como de app)

**📱 App Android:** baixe o `bozo-de-mesa.apk` da [versão mais recente](https://github.com/felipetoschi/bozo-de-mesa/releases/latest) e abra no celular.

## O que tem

- **Jogar no app**: 2 a 4 jogadores (pessoas e/ou computador), copo e dados na tela, cartelas viradas para cada lado da mesa, "pedir baixo", casa anulada, chance de cada jogada.
- **Só marcador**: 2 a 8 jogadores com dados de verdade; ranking ao vivo, finalizar partida a qualquer momento.
- Resumo de cada partida e placar de vitórias que continua até zerar.
- Exportar o resultado em imagem (direto no WhatsApp pelo app), texto ou planilha.
- Funciona sem internet e volta de onde parou.

## Para quem vai mexer no código

| O quê | Onde |
|---|---|
| O jogo (um arquivo só) | `app/www/index.html` |
| App Android (Capacitor) | `app/android/` |
| Site (link do jogo, arquivo único, política de privacidade) | `site/` |
| Compilação automática | `.github/workflows/` |
| Material da Play Store | `loja/` e [`PUBLICAR.md`](PUBLICAR.md) |

Cada alteração enviada ao GitHub gera sozinha o APK (Actions → App Android → Artifacts) e atualiza o link do jogo.

[Política de privacidade](https://felipetoschi.github.io/bozo-de-mesa/privacidade.html) — o jogo não coleta nenhum dado.
