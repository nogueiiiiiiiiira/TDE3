# Trabalho de Sistemas Operacionais - Concorrencia e Deadlock

## Descricao do Projeto
Este projeto implementa tres problemas classicos de concorrencia em sistemas operacionais:
1. Jantar dos Filosofos - Problema de sincronizacao e deadlock
2. Contador Concorrente - Condicao de corrida e semaforos
3. Deadlock Simples - Reproducao e correcao de impasses

## Integrantes do Grupo
- [Nomes dos integrantes]

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
- Versao com deadlock: Eventualmente trava quando todos os filosofos pegam o garfo esquerdo simultaneamente
- Versao corrigida: Execucao continua sem impasses devido a hierarquia de recursos

### Contador Concorrente
- Sem controle: Valor final incorreto (condicao de corrida)
- Com semaforo: Valor correto (2.000.000) com overhead de tempo

### Deadlock Simples
- Reproducao: Threads travam em espera circular
- Correcao: Execucao normal com ordem global

## Conclusoes

1. Deadlock requer todas as 4 condicoes de Coffman simultaneamente
2. Hierarquia de recursos e eficaz para quebrar espera circular
3. Semaforos resolvem condicoes de corrida mas impactam performance
4. Ordem de aquisicao e crucial para evitar impasses

## Referencias
1. Wikipedia - Dining Philosophers Problem
2. Oracle Documentation - java.util.concurrent.Semaphore
3. Wikipedia - Deadlock (Computer Science)
4. Condicoes de Coffman para Deadlock

Link do Video Explicativo: [inserir URL aqui]
```