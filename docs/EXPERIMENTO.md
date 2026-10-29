# Roteiro do experimento (3 execuções no histórico)

O trabalho exige ver no histórico do GitHub Actions: **aprovada → reprovada → aprovada**.
Faça tudo em commits/PRs reais, com os dois integrantes participando.

## Execução 1 — Aprovada (linha de base)
1. Suba o repositório para o GitHub (branch `main`) e crie o segredo `DB_PASSWORD`.
2. Faça o push. Abra a aba **Actions** e confirme que os 4 gates ficaram verdes.
3. Registre: link da execução, print do resumo dos gates, cobertura (artefato `evidencias-qualidade`).

## Execução 2 — Reprovada (falha proposital)
1. Crie a branch `experimento-falha`.
2. Em `src/main/java/almoxarifado/service/RegrasEstoque.java`, método `podeRetirar`, troque:
   ```java
   return quantidadeSolicitada > 0 && estoqueAtual >= quantidadeSolicitada;
   ```
   por:
   ```java
   return quantidadeSolicitada > 0 && estoqueAtual > quantidadeSolicitada;
   ```
   (erro de fronteira: o sistema passa a recusar a retirada do estoque *exatamente* igual ao pedido).
3. Commit e push; abra um Pull Request para `main`.
4. **Resultado esperado:** o **Gate 3 (Testes)** reprova. Em testes locais com este defeito, 2 dos 53
   testes falharam; os candidatos são os que usam estoque igual à quantidade retirada:
   `permiteRetirarExatamenteOEstoqueTotal` (unitário) e `retiradaDoEstoqueTotalEhPermitida`
   (integração). Confirme os nomes reais no log da sua execução. O Gate 4 não executa,
   pois o job para no primeiro gate que falha.
5. Registre: link da execução vermelha, print do passo falho e do trecho do log com o teste que quebrou.

## Execução 3 — Aprovada após correção
1. Na mesma branch, reverta para `estoqueAtual >= quantidadeSolicitada`.
2. Commit e push; o PR é reexecutado e todos os gates ficam verdes.
3. Registre: link da execução verde e o commit da correção. Faça o merge.

## Variações opcionais (mostram gates diferentes reprovando)
| Falha proposital | Gate que reprova |
|------------------|------------------|
| Adicionar `import java.util.Date;` sem usar em qualquer classe | Gate 2 (análise estática) |
| Digitar um erro de sintaxe em qualquer `.java` | Gate 1 (build) |
| Remover/comentar vários testes de `RegrasEstoqueTest` | Gate 4 (cobertura) — ver observação abaixo |

> Observação sobre o Gate 4: como os testes de integração também cobrem `DatabaseConnection`,
> a cobertura só cai abaixo de 80% se você remover uma parte grande dos testes. Meça antes de usar
> esta variação.

## Importante para a defesa oral
Cada integrante deve saber explicar: o que é cada gate, por que o limite de 80%, por que a UI fica
fora da cobertura, o que o `FOR UPDATE` resolve e por que o teste de integração só roda em banco `_test`.
