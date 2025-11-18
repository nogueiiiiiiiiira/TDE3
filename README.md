# Trabalho de Sistemas Operacionais - Concorrencia e Deadlock

## Descricao do Projeto
Este projeto implementa tres problemas classicos de concorrencia em sistemas operacionais:
1. Jantar dos Filosofos - Problema de sincronizacao e deadlock
2. Contador Concorrente - Condicao de corrida e semaforos
3. Deadlock Simples - Reproducao e correcao de impasses

## Estrutura do Projeto
```
projeto/
├── Main.java
├── README.md
└── src/
    ├── Outros/
    │   └── Menu.java
    ├── JantarFilosofos/
    │   ├── Filosofo.java
    │   ├── FilosofoComDeadlock.java
    │   ├── Garfo.java
    │   └── JantarFilosofos.java
    ├── Contador/
    │   └── ContadorConcorrente.java
    └── Deadlock/
        └── DeadlockSimples.java
```

## Analise Conceitual

### Parte 1: Jantar dos Filosofos

#### Problema Original
Cinco filosofos alternam entre pensar e comer. Para comer, precisam de dois garfos (esquerdo e direito), compartilhados com vizinhos. O protocolo ingenuo causa deadlock quando todos pegam simultaneamente o garfo da esquerda.

#### Condicoes de Coffman no Deadlock:
1. Exclusao Mutua - Garfos sao recursos exclusivos
2. Manter-e-Esperar - Filosofos seguram um garfo e esperam outro
3. Nao-preempcao - Nao se pode tirar garfos a forca
4. Espera Circular - Cada filosofo espera pelo proximo

#### Solucao Implementada: Hierarquia de Recursos
- Atribui ordem numerica aos garfos
- Sempre adquirir o garfo de menor numero primeiro
- Condicao quebrada: Espera Circular

### Parte 2: Contador Concorrente

#### Problema: Condicao de Corrida
Multiplas threads incrementando um contador compartilhado sem sincronizacao resulta em valores incorretos devido a atualizacoes nao atomicas.

#### Solucao: Semaforo Binario
- Semaphore(1, true) - Modo justo (FIFO)
- Garante exclusao mutua na secao critica
- Preserva visibilidade entre threads (happens-before)

#### Trade-offs:
- Elimina condicao de corrida
- Garante correcao do resultado
- Aumento no tempo de execucao
- Overhead de sincronizacao

### Parte 3: Deadlock Simples

#### Cenario de Deadlock
- Thread 1: Lock A -> Lock B
- Thread 2: Lock B -> Lock A
- Resulta em espera circular e impasse

#### Correcao: Ordem Global de Aquisicao
- Sempre adquirir Lock A antes de Lock B
- Elimina a possibilidade de ciclo de espera

## Resultados e Analises

### Jantar dos Filosofos
- Versao com deadlock: Eventualmente trava quando todos os filosofos pegam o garfo esquerdo simultaneamente (espera circular)
- Versao corrigida: Execucao continua indefinidamente sem impasses devido a hierarquia de recursos (ordem global de aquisicao)

### Contador Concorrente
- Sem sincronizacao: Valor final incorreto devido a condicao de corrida (perda de incrementos)
- Com semaforo: Valor correto com overhead de tempo devido a serializacao dos acessos
- FIFO garante que threads sao atendidas na ordem de chegada, evitando starvation

### Deadlock Simples
- Reproducao: Threads travam em espera circular (Thread 1 segura A e espera B, Thread 2 segura B e espera A)
- Correcao: Execucao normal com ordem global (sempre A antes de B), impossivel formar ciclo

## Pseudocódigo

### Parte 1: Jantar dos Filósofos
#### Protocolo com Deadlock (Ingênuo)
Implementado em `JantarFilosofos/FilosofoComDeadlock.java` (linhas 15-35):
```
Para cada filósofo p:
  Loop infinito:
    pensar()
    estado[p] <- "com fome"
    adquirir(garfo_esquerdo[p])  // Bloqueia até livre
    adquirir(garfo_direito[p])   // Bloqueia até livre
    estado[p] <- "comendo"
    comer()
    liberar(garfo_direito[p])
    liberar(garfo_esquerdo[p])
    estado[p] <- "pensando"
```
- Causa deadlock quando todos pegam garfo esquerdo simultaneamente (espera circular).

#### Protocolo Corrigido (Hierarquia de Recursos)
Implementado em `JantarFilosofos/Filosofo.java` (linhas 18-40):
```
Para cada filósofo p:
  left = min(garfo_esquerdo[p], garfo_direito[p])
  right = max(garfo_esquerdo[p], garfo_direito[p])
  Loop infinito:
    pensar()
    estado[p] <- "com fome"
    adquirir(left)   // Sempre menor índice primeiro
    adquirir(right)  // Depois maior
    estado[p] <- "comendo"
    comer()
    liberar(right)
    liberar(left)
    estado[p] <- "pensando"
```
- Elimina espera circular impondo ordem global.

### Parte 2: Contador Concorrente
#### Versão com Condição de Corrida
Implementado em `Contador/ContadorConcorrente.java` método `executarSemSincronizacao()` (linhas 25-30):
```
contador = 0
Para cada thread t em T threads:
  Para i de 0 a M-1:
    contador++  // Atualização não atômica
```
- Resulta em perda de incrementos devido a race condition.

#### Versão Corrigida com Semáforo
Implementado em `Contador/ContadorConcorrente.java` método `executarComSincronizacao()` (linhas 45-55):
```
semaforo = Semaphore(1, true)  // Binário, justo
contador = 0
Para cada thread t em T threads:
  Para i de 0 a M-1:
    semaforo.acquire()
    contador++
    semaforo.release()
```
- Garante exclusão mútua e valor correto.

### Parte 3: Deadlock Simples
#### Cenário com Deadlock
Implementado em `Deadlock/DeadlockSimples.java` método `executarDeadlock()` (linhas 9-25 e 27-43):
```
Thread 1:
  adquirir(LOCK_A)
  adquirir(LOCK_B)  // Pode travar se Thread 2 tem B
Thread 2:
  adquirir(LOCK_B)
  adquirir(LOCK_A)  // Pode travar se Thread 1 tem A
```
- Espera circular: Thread 1 segura A e espera B, Thread 2 segura B e espera A.

#### Correção por Ordem Global
Implementado em `Deadlock/DeadlockSimples.java` método `executarCorrecao()` (linhas 50-66 e 68-84):
```
Thread 1 e Thread 2:
  adquirir(LOCK_A)  // Sempre A primeiro
  adquirir(LOCK_B)  // Depois B
```
- Elimina ciclo de espera impondo hierarquia.

## Critérios de Avaliação

### Clareza Conceitual
- Explicação de impasse e inanição no Jantar dos Filósofos: README.md seção "Parte 1", relacionado a `FilosofoComDeadlock.java` (deadlock) e `Filosofo.java` (correção).
- Condição de corrida e papel de semáforos: README.md seção "Parte 2", demonstrado em `ContadorConcorrente.java` métodos `executarSemSincronizacao()` e `executarComSincronizacao()`.

### Correção do Protocolo
- Ausência de impasse na Parte 1: Verificado em `JantarFilosofos.java` método `executarSemDeadlock()` (execução infinita sem travar).
- Uso de fairness para evitar inanição: Semáforo justo (true) em `ContadorConcorrente.java` linha 6, garante FIFO e justiça.

### Demonstração Reproducible
- Condição de corrida e eliminação: `ContadorConcorrente.java` executa ambas versões, comparando resultados e tempos.
- Happens-before e visibilidade: Garantido pelo `Semaphore` em Java, explicado em README.md seção "Parte 2".

### Reprodução e Correção de Deadlock
- Reprodução: `DeadlockSimples.java` método `executarDeadlock()` demonstra travamento (espera circular).
- Mapeamento das 4 condições de Coffman: README.md seção "Parte 3", com exemplo em código.
- Correção fundamentada: Método `executarCorrecao()` quebra espera circular via hierarquia, relacionado à solução dos Filósofos.

## Conclusoes

1. Deadlock requer todas as 4 condicoes de Coffman simultaneamente
2. Hierarquia de recursos e eficaz para quebrar espera circular (ordem global)
3. Semaforos resolvem condicoes de corrida mas impactam performance (serializacao)
4. Ordem de aquisicao e crucial para evitar impasses (evita ciclos de espera)
5. FIFO nos semaforos garante justiça e evita starvation em cenarios concorrentes

## Referencias
1. Wikipedia - Dining Philosophers Problem
2. Oracle Documentation - java.util.concurrent.Semaphore
3. Wikipedia - Deadlock (Computer Science)
4. Condicoes de Coffman para Deadlock

Link do Video Explicativo:
```