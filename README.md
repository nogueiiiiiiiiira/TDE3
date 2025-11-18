# Trabalho de Sistemas Operacionais - Concorrência e Deadlock

## Descrição do Projeto
Este projeto implementa três problemas clássicos de concorrência em sistemas operacionais, demonstrando problemas de sincronização, deadlock e suas respectivas soluções.

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

## Parte 1: Jantar dos Filósofos

### Problema Original
Cinco filósofos alternam entre pensar e comer. Para comer, precisam de dois garfos (esquerdo e direito), compartilhados com vizinhos. O protocolo ingênuo causa deadlock quando todos pegam simultaneamente o garfo da esquerda.

### Condições de Coffman no Deadlock:
1. **Exclusão Mútua** - Garfos são recursos exclusivos
2. **Manter-e-Esperar** - Filósofos seguram um garfo e esperam outro
3. **Não-preempção** - Não se pode tirar garfos à força
4. **Espera Circular** - Cada filósofo espera pelo próximo

### Solução Implementada: Hierarquia de Recursos
Atribui ordem numérica aos garfos e sempre adquire o garfo de menor número primeiro, quebrando a condição de **Espera Circular**.

### Pseudocódigo da Solução
```
Para cada filósofo p:
  left = min(garfo_esquerdo[p], garfo_direita[p])
  right = max(garfo_esquerdo[p], garfo_direita[p])
  
  Loop infinito:
    pensar()
    estado[p] <- "com fome"
    adquirir(left)     // Sempre menor índice primeiro
    adquirir(right)    // Depois maior índice
    estado[p] <- "comendo"
    comer()
    liberar(right)
    liberar(left)
    estado[p] <- "pensando"
```

### Arquivos e Funções
- **JantarFilosofos.java**: Controla a simulação
  - `executarSemDeadlock()`: Versão com solução
  - `executarComDeadlock()`: Versão problemática
- **Filosofo.java**: Filósofo com hierarquia de recursos
  - `run()`: Lógica principal com ordem global
  - `pensar()`, `comer()`: Simulação de atividades
- **FilosofoComDeadlock.java**: Filósofo com protocolo ingênuo
  - Limitado a 5 iterações para demonstração
- **Garfo.java**: Recurso compartilhado com identificador

## Parte 2: Contador Concorrente

### Problema: Condição de Corrida
Múltiplas threads incrementando um contador compartilhado sem sincronização resulta em valores incorretos devido a atualizações não atômicas.

### Solução: Semáforo Binário Justo
- `Semaphore(1, true)`: Modo FIFO para garantir fairness
- Garante exclusão mútua na seção crítica
- Preserva visibilidade entre threads (happens-before)

### Pseudocódigo
```
// Versão com condição de corrida
contador = 0
Para cada thread t em T threads:
  Para i de 0 a M-1:
    contador++  // Operação não-atômica

// Versão corrigida
semaforo = Semaphore(1, true)  // Binário, justo
contador = 0
Para cada thread t em T threads:
  Para i de 0 a M-1:
    semaforo.acquire()
    contador++
    semaforo.release()
```

### Arquivo e Funções
- **ContadorConcorrente.java**: Demonstra condição de corrida
  - `executarTeste()`: Executa ambos cenários
  - `executarSemSincronizacao()`: Mostra problema
  - `executarComSincronizacao()`: Mostra solução
  - 8 threads com 250.000 incrementos cada

### Resultados Típicos
- **Sem sincronização**: ~1.250.000 (perda de incrementos)
- **Com semáforo**: 2.000.000 (valor correto)
- **Trade-off**: Correção com overhead de tempo

## Parte 3: Deadlock Simples

### Cenário de Deadlock
- Thread 1: Lock A → Lock B
- Thread 2: Lock B → Lock A
- Resulta em espera circular e impasse

### Condições de Coffman Presentes
1. ✅ **Exclusão Mútua** - Locks são exclusivos
2. ✅ **Manter-e-Esperar** - Seguram um recurso, esperam outro
3. ✅ **Não-preempção** - Recursos não são tomados à força
4. ✅ **Espera Circular** - Ciclo de dependência formado

### Correção: Ordem Global de Aquisicação
Sempre adquirir Lock A antes de Lock B, eliminando a possibilidade de ciclo de espera.

### Pseudocódigo
```
// Cenário com Deadlock
Thread 1:
  adquirir(LOCK_A)
  adquirir(LOCK_B)  // Trava se Thread 2 tem B

Thread 2:
  adquirir(LOCK_B)  
  adquirir(LOCK_A)  // Trava se Thread 1 tem A

// Cenário Corrigido
Thread 1 e Thread 2:
  adquirir(LOCK_A)  // Sempre A primeiro
  adquirir(LOCK_B)  // Depois B
```

### Arquivo e Funções
- **DeadlockSimples.java**: Reproduz e corrige deadlock
  - `reproduzirDeadlock()`: Demonstra problema
  - `corrigirDeadlock()`: Aplica solução
  - `aguardar()`: Introduce delay para garantir deadlock

## Conclusões

1. **Deadlock requer 4 condições simultaneamente** - Basta quebrar uma para prevenir
2. **Hierarquia de recursos é eficaz** - Elimina espera circular impondo ordem global
3. **Semáforos resolvem condições de corrida** - Garantem correção com overhead de performance
4. **Ordem de aquisição é crucial** - Evita formação de ciclos de espera
5. **Fairness previne starvation** - FIFO garante justiça em cenários concorrentes

## Referências
1. Wikipedia - Dining Philosophers Problem
2. Oracle Documentation - java.util.concurrent.Semaphore
3. Wikipedia - Deadlock (Computer Science)
4. Condições de Coffman para Deadlock