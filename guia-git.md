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
| **1. Adicionar Tudo** | `git add .` | **"Coloque tudo o que alterei no carrinho de compras."** (Prepara os arquivos para a versão). |
| **1.5. Adicionar Item**| `git add nome-arquivo`| Adicionar um arquivo específico|
| **2. Confirmar** | `git commit -m "Nova feature: Adiciona busca por produtos"` | **"Feche o carrinho e salve esta versão com uma nota descritiva."** (Cria o ponto de salvamento/versão). |
| **3. Enviar (Padrão)** | `git push` | **"Envie os pontos de salvamento (commits) para o GitHub."** (Sincroniza sua branch atual). |
| **4. Enviar (1ª Vez)** | `git push -u origin [nome_da_branch]` | **"Envie esta branch nova e configure para onde ela deve ir no futuro."** (Comando usado ao subir uma branch nova pela primeira vez). |
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

# 🏷️ Convenções de Branches (Cheatsheet)

## 📌 Principais

| Nome | Função | Vida Útil |
| :--- | :--- | :--- |
| `main` / `master` | Produção (código no ar). | Permanente |
| `develop` | Integração (código mais recente/testado). | Permanente |

## ✨ Desenvolvimento (Temporárias)

| Prefixo | Propósito | Exemplo |
| :--- | :--- | :--- |
| **`feature/`** | Nova funcionalidade. | `feature/carrinho-de-compras` |
| **`fix/`** | Correção de bug no desenvolvimento. | `fix/layout-mobile` |
| **`hotfix/`** | Correção urgente na produção (`main`). | `hotfix/erro-de-api` |

## 🛠️ Manutenção e Estrutura (Temporárias)

| Prefixo | Propósito | Exemplo |
| :--- | :--- | :--- |
| **`chore/`** | Tarefas de infraestrutura, organização de pastas, dependências. (Ótimo para estrutura!) | `chore/estrutura-base` |
| **`refactor/`** | Reorganização/limpeza de código (sem mudar comportamento). | `refactor/separar-handlers` |
| **`docs/`** | Atualização de documentação. | `docs/atualizar-readme` |
| **`test/`** | Adicionar ou refatorar testes. | `test/checkout-v2` |

---

## 📝 Regras Rápidas

* Sempre use: **`prefixo/nome-descritivo-com-hifens`**
* Tudo em **minúsculas**.