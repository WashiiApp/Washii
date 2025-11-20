# 🚀 Guia Prático e Didático de Git e GitHub (COMPLETO)

## 1. 🛠️ Configuração e Inspeção

| Ação | Comando | O que faz (Conceito) |
| :--- | :--- | :--- |
| **Criar Repositório** | `git init` | Diz ao Git: "Comece a rastrear esta pasta!" |
| **Conectar ao GitHub** | `git remote add origin [URL]` | Define o endereço do seu repositório remoto (só na primeira vez). |
| **Verificar Status** | `git status` | Pergunta ao Git: "O que mudou? O que está pronto?" |
| **Ver Branches** | `git branch` | Lista todas as branches locais e mostra em qual você está (`*`). |
| **Ver Histórico** | `git log` | Vê o histórico detalhado de commits (IDs, autor, data e mensagem). |
| **Configurar Identidade** | `git config --global user.name "Seu Nome"` | Define quem está fazendo o commit (sua assinatura). |

---

## 2. 💾 Salvando e Sincronizando seu Trabalho (O Ciclo Essencial)

| Passo | Comando | Função Didática |
| :--- | :--- | :--- |
| **1. Adicionar** | `git add .` | **"Coloque tudo o que alterei no carrinho de compras."** (Prepara os arquivos para a versão). |
| **2. Confirmar** | `git commit -m "Nova feature: Adiciona busca por produtos"` | **"Feche o carrinho e salve esta versão com uma nota descritiva."** (Cria o ponto de salvamento/versão). |
| **3. Enviar (Padrão)** | `git push` | **"Envie os pontos de salvamento (commits) para o GitHub."** (Sincroniza sua branch atual). |
| **4. Enviar (1ª Vez)** | `git push --set-upstream origin [nome_da_branch]` | **"Envie esta branch nova e configure para onde ela deve ir no futuro."** (Comando usado ao subir uma branch nova pela primeira vez). |
| **5. Receber** | `git pull` | **"Baixe código novo do GitHub para sua máquina local."** (Sempre antes de começar a trabalhar!) |

---

## 3. 🌳 Branches: Desenvolvimento Paralelo

Uma **Branch** é como um *universo paralelo* onde você pode desenvolver recursos sem quebrar o código principal.

| Ação | Comando | Conceito |
| :--- | :--- | :--- |
| **Criar e Mudar** | `git checkout -b feature/login master` | **"Crie um novo universo** (`feature/login`) **baseado no universo principal** (`master`) **e entre nele."** |
| **Mudar de Branch** | `git checkout master` | **"Volte para o universo principal."** |
| **Unir Universos** | `git merge feature/login` | **"Traga as novidades (testadas!) do universo paralelo para este universo."** |
| **Apagar Branch Local** | `git branch -d [nome_da_branch]` | Limpa branches que já foram unidas (merged). |

---

## 4. 🛡️ Reversão e Segurança

| Ação | Comando | Função |
| :--- | :--- | :--- |
| **Ver IDs (Geral)** | `git reflog` | Vê o "Diário de Bordo" (todos os IDs de operações recentes). |
| **Reverter Código** | `git reset --hard [ID_do_commit]` | **Volta no tempo!** Desfaz todas as mudanças locais até o ponto salvo. |
| **Ignorar Arquivos** | Crie o arquivo `.gitignore` | Impede que o Git rastreie arquivos sensíveis ou temporários. |

### 🤝 Pull Request (PR)

* **O que é:** Uma **Solicitação Formal de Aprovação** para que seu código seja revisado por um colega antes de ser unido ao projeto principal (feito na interface web do GitHub).

---

# 🏷️ Convenções Profissionais para Nomeação de Branches

*Este guia utiliza prefixos (baseado no Gitflow) para classificar o tipo de trabalho que a branch representa, facilitando a revisão de código e a organização do histórico.*

## 1. ⚙️ Branches Principais (Permanentes)

São as branches de longa duração que representam a linha de tempo do projeto.

| Prefixo | Nome Comum | Propósito |
| :--- | :--- | :--- |
| N/A | `main` ou `master` | Contém o código **em produção**, sempre estável e pronto para ir ao ar. |
| N/A | `develop` | Contém o histórico de desenvolvimento mais recente. É o ponto de integração para novas *features*. |

## 2. ✨ Branches de Funcionalidades e Correções (Temporárias)

São criadas a partir de `develop` e unidas de volta após a conclusão.

| Prefixo | Tipo de Mudança | Exemplo de Nomeação |
| :--- | :--- | :--- |
| **`feature/`** | Adiciona **nova funcionalidade** visível ao usuário. | `feature/cadastro-de-usuarios` |
| **`fix/`** | Corrige **bug** não crítico ou problema detectado no ciclo de desenvolvimento. | `fix/validacao-de-formulario` |
| **`hotfix/`** | Corrige **bug crítico** na branch de produção (`main`). | `hotfix/erro-de-pagamento-urgente` |

## 3. 🛠️ Branches Estruturais e de Manutenção

Usadas para melhorias internas, organização e tarefas de infraestrutura.

| Prefixo | Tipo de Mudança | Exemplo de Nomeação |
| :--- | :--- | :--- |
| **`chore/`** (Recomendado) | **Tarefas de bastidores** (infraestrutura, ambiente, organização de pastas, dependências, etc.). Ideal para organizar a estrutura do projeto. | `chore/estrutura-de-pastas` |
| **`refactor/`** | **Reorganização interna do código** para melhorar a arquitetura, sem mudar o comportamento externo. | `refactor/separar-componentes-auth` |
| **`docs/`** | Adição ou alteração de **documentação** (ex: README, guias de contribuição). | `docs/adicionar-guia-api` |
| **`test/`** | Adiciona ou refatora testes unitários ou de integração, sem alterar código de produção. | `test/adicionar-testes-login` |

## 📝 Regras de Nomeação

1.  **Use Prefixos:** Inicie a branch com um prefixo (`feature/`, `chore/`, etc.) seguido de uma barra (`/`).
2.  **Use Hífen:** Use hífens (`-`) para separar palavras no nome descritivo.
3.  **Seja Descritivo:** O nome deve ser curto, mas informar o objetivo da branch.
4.  **Use Letras Minúsculas:** Evite letras maiúsculas ou caracteres especiais.