# Como distribuir e publicar o Bozó de Mesa

Há três jeitos de levar o jogo para as pessoas, do mais simples ao mais completo:

| Jeito | Para quem | O que fazer |
|---|---|---|
| **Link do jogo** | qualquer celular, tablet ou computador (inclusive iPhone) | mandar https://felipetoschi.github.io/bozo-de-mesa/ |
| **APK** | amigos com Android | mandar o link https://github.com/felipetoschi/bozo-de-mesa/releases/latest (passo 2) |
| **Play Store** | qualquer pessoa | passos 3 a 7 |

Tudo o que dá para preparar já está pronto neste repositório:

| O quê | Onde |
|---|---|
| Jogo (funciona sem internet) | `app/www/` |
| Projeto Android | `app/android/` |
| Compilação automática no GitHub | `.github/workflows/android.yml` |
| Ícone 512×512, imagem de destaque 1024×500 e capturas de tela | `loja/` |
| Textos da loja | `loja/textos-da-loja.md` |
| Política de privacidade | https://felipetoschi.github.io/bozo-de-mesa/privacidade.html (arquivo `site/privacidade.html`) |
| Link do jogo e arquivo único para download | https://felipetoschi.github.io/bozo-de-mesa/ e `.../bozo-de-mesa.html` |

A chave de assinatura **não** está no repositório. Ela foi entregue separada (arquivos `bozo-upload.jks` e `LEIA-CHAVE.txt`). Guarde em lugar seguro.

---

## Passo 0 — Ligar o link do jogo (só uma vez)

No repositório: **Settings → Pages → Build and deployment → Source: GitHub Actions**. Depois, em **Actions → Site (link do jogo) → Run workflow**. Em 1 minuto o link https://felipetoschi.github.io/bozo-de-mesa/ funciona, e daí em diante ele se atualiza sozinho a cada mudança no jogo.

## Passo 1 — Testar no seu celular

1. No GitHub, abra a aba **Actions** do repositório e clique na compilação mais recente de **App Android** (com ✅ verde).
2. Lá embaixo, em **Artifacts**, baixe `bozo-de-mesa-teste-N`. Vem um `.zip` com o arquivo `app-debug.apk`.
3. Passe o `.apk` para o celular Android e abra. O Android vai pedir para permitir "instalar apps desconhecidos": permita só para isso.

## Passo 2 — Colocar a chave no GitHub (só uma vez)

No repositório: **Settings → Secrets and variables → Actions → New repository secret**. Crie dois segredos:

| Nome | Valor |
|---|---|
| `KEYSTORE_BASE64` | todo o conteúdo do arquivo `bozo-upload.jks.base64.txt` |
| `KEYSTORE_PASSWORD` | a senha que está no `LEIA-CHAVE.txt` |

Depois, em **Actions → App Android → Run workflow**, marque **"Publicar o APK assinado em Releases"** e clique em **Run workflow**. Em uns 3 minutos:

- aparece uma versão nova em **Releases**, com o `bozo-de-mesa.apk` assinado. O link https://github.com/felipetoschi/bozo-de-mesa/releases/latest sempre aponta para a mais recente: é ele que você manda para os amigos. Com a mesma chave, as atualizações instalam por cima **sem perder o placar**;
- em **Artifacts** fica também o `bozo-de-mesa-play-store-N`, com o arquivo **`.aab`** que vai para a Play Store.

## Passo 3 — Criar a conta no Google Play Console

1. Acesse play.google.com/console e crie uma conta de desenvolvedor **pessoal**. A taxa é de US$ 25, paga uma única vez.
2. O Google pede para verificar sua identidade (documento) e um celular. Pode levar alguns dias.

## Passo 4 — Política de privacidade

Já está publicada junto com o link do jogo (depois do passo 0): https://felipetoschi.github.io/bozo-de-mesa/privacidade.html. É esse endereço que vai no Play Console. O contato dela aponta para as *Issues* do repositório, sem expor seu e-mail.

## Passo 5 — Criar o app no Play Console

1. **Criar app**: nome "Bozó de Mesa", idioma português (Brasil), tipo **Jogo**, **Gratuito**.
2. **Página principal da loja**: cole os textos de `loja/textos-da-loja.md` e envie:
   - ícone: `loja/icone-512.png`;
   - recurso gráfico: `loja/destaque-1024x500.png`;
   - capturas de celular: `loja/capturas/celular-*.png`;
   - capturas de tablet de 7": `loja/capturas/tablet-7-1.png`;
   - capturas de tablet de 10": `loja/capturas/tablet-10-*.png`.
3. **Conteúdo do app** (menu "Política"), com estas respostas:
   - Política de privacidade: o link do passo 4.
   - Anúncios: **não** tem anúncios.
   - Acesso ao app: todo o app funciona sem login.
   - Classificação do conteúdo: categoria **Jogos**. Responda **não** para violência, linguagem, drogas etc. Em **jogos de azar**, responda que **não** há apostas, dinheiro real nem prêmios: é só um jogo de dados com pontos.
   - Público-alvo: **13 anos ou mais**. Marcar idades menores coloca o app nas regras de apps para crianças, que exigem mais coisas.
   - Segurança dos dados: o app **não coleta nem compartilha** dados.
   - App governamental, financeiro, de saúde etc.: **não**.

## Passo 6 — Teste fechado (obrigatório para contas pessoais novas)

Contas pessoais novas precisam de um teste fechado com **pelo menos 12 testadores** durante **14 dias seguidos** antes de poder publicar para todo mundo.

1. Em **Testar e lançar → Testes → Teste fechado**, crie uma faixa e envie o `.aab` do passo 2.
2. Adicione os e-mails (conta Google) de 12 ou mais pessoas: família, amigos, quem joga com você.
3. Mande o link de participação para eles. Cada um precisa aceitar e instalar o app pela Play Store.
4. Depois de 14 dias, peça o **acesso à produção** no painel do Play Console.

## Passo 7 — Produção

Em **Produção → Criar nova versão**, envie o `.aab` mais recente e mande para revisão. A revisão costuma levar de alguns dias a uma semana.

---

## Para lançar uma atualização

1. Altere o jogo em `app/www/index.html` e envie para o GitHub. O **link do jogo** se atualiza sozinho.
2. Para os amigos com o APK: **Actions → App Android → Run workflow** com "Publicar" marcado. Eles baixam do mesmo link e instalam por cima.
3. Para a Play Store: pegue o novo `.aab` (versão maior, automático) em Artifacts e envie como nova versão no Play Console.

> As regras da Play Store mudam com frequência (teste com 12 pessoas, versão mínima do Android etc.). Se algo no Play Console estiver diferente do que está escrito aqui, siga o que o Play Console pedir.
