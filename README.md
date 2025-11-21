# Trabalho de Sistemas Operacionais - Concorrência e Deadlock

## Link do Youtube
https://youtu.be/RMOH83RD350

## Descrição do Projeto
Este projeto implementa **três problemas clássicos de concorrência** em sistemas operacionais, demonstrando problemas de sincronização, deadlock e suas respectivas soluções, conforme especificado na atividade.

## 🎯 **OBJETIVOS ATENDIDOS - MAPEAMENTO COMPLETO**

---

## **PARTE 1 — JANTAR DOS FILÓSOFOS** ✅

### **O que a atividade pediu:**
> *"Simular o problema com N=5, registrando estados 'pensando', 'com fome' e 'comendo', evidenciando por que o protocolo ingênuo entra em impasse."*

### **Como implementamos:**
- **`FilosofoComDeadlock.java`**: Versão problemática que demonstra o deadlock
- **Estados implementados**: `"pensando"`, `"com fome"`, `"comendo"`
- **N=5 filósofos** em mesa circular com 5 garfos compartilhados

### **Por que o deadlock ocorre:**
No protocolo ingênuo, todos os filósofos podem:
1. Pegar o garfo esquerdo simultaneamente
2. Tentar pegar o garfo direito (que já está ocupado pelo vizinho)
3. **Resultado**: Espera circular infinita que gera o **DEADLOCK**

### **Saída Esperada - Versão COM Deadlock:**
```
Filosofo 0 pegou o garfo 0
Filosofo 1 pegou o garfo 1  
Filosofo 2 pegou o garfo 2
Filosofo 3 pegou o garfo 3
Filosofo 4 pegou o garfo 4
... [SISTEMA TRAVADO - NINGUÉM PROGRIDE] ...
```
**🔴 O sistema entra em loop infinito travado - demonstração clássica do problema**

---

### **O que a atividade pediu:**
> *"Projetar um protocolo que evite impasse, explicando logicamente a estratégia escolhida e por que ela quebra uma das condições para deadlock."*

### **Solução Implementada: HIERARQUIA DE RECURSOS**
**Arquivo**: `Filosofo.java`

### **Por que escolhemos esta solução:**
- **Simplicidade**: Não requer coordenador externo
- **Eficácia**: Elimina garantidamente o deadlock
- **Justiça**: Evita starvation (inanição)

### **Como funciona:**
```java
// SEMPRE pegar o garfo de MENOR número primeiro
if (garfoEsquerdo.getNumero() < garfoDireito.getNumero()) {
    primeiroGarfo = garfoEsquerdo;    // Menor índice primeiro
    segundoGarfo = garfoDireito;      // Maior índice depois
} else {
    primeiroGarfo = garfoDireito;     // Menor índice primeiro  
    segundoGarfo = garfoEsquerdo;     // Maior índice depois
}
```

### **Qual condição de Coffman quebramos:**
**✅ ESPERA CIRCULAR** - Ao impor uma **ordem global** na aquisição dos recursos, é **impossível** formar um ciclo de dependência.

### **Análise das 4 Condições de Coffman:**
1. **❌ Exclusão Mútua** - PRESERVADA (garfos são recursos exclusivos)
2. **❌ Manter-e-Esperar** - PRESERVADA (filósofos seguram um garfo enquanto esperam outro)
3. **❌ Não-preempção** - PRESERVADA (não tiramos garfos à força)
4. **✅ Espera Circular** - **QUEBRADA** (ordem global elimina ciclos)

### **Saída Esperada - Versão SEM Deadlock:**
```
Filosofo 0 esta pensando
Filosofo 0 esta com fome e quer comer!
Filosofo 0 pegou o garfo 0
Filosofo 0 pegou o garfo 1
Filosofo 0 esta comendo!
Filosofo 0 terminou de comer e voltou a pensando
Filosofo 0 soltou o garfo 1
Filosofo 0 soltou o garfo 0
... [CICLO CONTÍNUO - SEM TRAVAMENTO] ...
```
**🟢 O sistema executa indefinidamente sem travar - solução funcional**

---

## **PARTE 2 — THREADS E SEMÁFOROS** ✅

### **O que a atividade pediu:**
> *"Demonstrar uma condição de corrida incrementando um contador compartilhado com múltiplas threads sem sincronização e, em seguida, corrigi-la com um semáforo binário."*

### **Implementação:**
**Arquivo**: `ContadorConcorrente.java`

### **Cenário de Teste:**
- **8 threads** executando simultaneamente
- **250.000 incrementos** por thread
- **Total esperado**: 2.000.000 incrementos

### **PROBLEMA - Condição de Corrida:**
```java
// ❌ VERSÃO PROBLEMÁTICA
contadorCompartilhado++;  // Operação NÃO-ATÔMICA
```

**O que acontece:**
1. Thread A lê valor: 100
2. Thread B lê valor: 100  
3. Thread A incrementa: 101
4. Thread B incrementa: 101
5. **Resultado**: Dois incrementos, mas contador só aumenta 1, o que gera **PERDA DE ATUALIZAÇÕES**

### **SOLUÇÃO - Semáforo Binário Justo:**
```java
// ✅ VERSÃO CORRIGIDA
semaforo.acquire();       // Bloqueia se necessário
contadorCompartilhado++;  // Seção crítica protegida
semaforo.release();       // Libera para próxima thread
```

### **Por que usamos `Semaphore(1, true)`:**
- **`1`**: Semáforo binário (uma permissão)
- **`true`**: Modo **FAIR** (FIFO) - garante **justiça** e evita **starvation**

### **Saída Esperada - PARTE 2:**

#### **Execução SEM Sincronização:**
```
=== DEMONSTRANDO CONDIÇÃO DE CORRIDA (SEM SINCRONIZAÇÃO) ===
Esperado=2000000, Obtido=1250000, Tempo=0.150s
Nota: Valor obtido pode ser menor devido a condição de corrida (perda de incrementos).
```
**🔴 Resultado INCORRETO** - Demonstração da condição de corrida

#### **Execução COM Sincronização:**
```
=== VERSÃO CORRIGIDA COM SEMAFORO ===
Esperado=2000000, Obtido=2000000, Tempo=2.850s
Nota: Valor correto devido a exclusão mútua garantida pelo semáforo.
```
**🟢 Resultado CORRETO** - Solução funcional

### **Trade-off Identificado:**
- **SEM sincronização**: Rápido mas **INCORRETO**
- **COM sincronização**: Mais lento mas **CORRETO**
- **Overhead**: Tempo ~19x maior devido à coordenação entre threads

### **Garantias do Semáforo:**
- **Exclusão Mútua**: Apenas uma thread na seção crítica
- **Happens-before**: Visibilidade garantida entre threads
- **Fairness**: Ordem FIFO para evitar starvation

---

## **PARTE 3 — DEADLOCK** ✅

### **O que a atividade pediu:**
> *"Reproduzir o deadlock com duas threads e dois locks, explicar quais condições de Coffman se manifestaram e implementar uma correção."*

### **Implementação:**
**Arquivo**: `DeadlockSimples.java`

### **CENÁRIO COM DEADLOCK:**
```java
// Thread 1: A → B
synchronized (recursoA) {
    synchronized (recursoB) { ... }
}

// Thread 2: B → A  
synchronized (recursoB) {
    synchronized (recursoA) { ... }
}
```

### **ANÁLISE DAS CONDIÇÕES DE COFFMAN:**

1. **✅ Exclusão Mútua**: 
   - Locks `synchronized` garantem acesso exclusivo

2. **✅ Manter-e-Esperar**:
   - Thread 1: Tem A, espera B
   - Thread 2: Tem B, espera A

3. **✅ Não-preempção**:
   - Locks não podem ser tomados à força

4. **✅ Espera Circular**:
   - Thread 1 espera por Thread 2
   - Thread 2 espera por Thread 1
   - **CICLO COMPLETO** → **DEADLOCK**

### **Saída Esperada - COM Deadlock:**
```
=== REPRODUZINDO DEADLOCK ===
Thread 1 adquiriu recurso A
Thread 2 adquiriu recurso B
... [SISTEMA TRAVADO - DEADLOCK] ...
```

### **SOLUÇÃO: ORDEM GLOBAL DE AQUISIÇÃO**
**Estratégia**: Sempre adquirir **recursoA antes de recursoB**

```java
// AMBAS as threads seguem a MESMA ordem:
synchronized (recursoA) {
    synchronized (recursoB) { ... }
}
```

### **Qual condição quebramos:**
**✅ ESPERA CIRCULAR** - Ordem consistente elimina a possibilidade de ciclos

### **Saída Esperada - SEM Deadlock:**
```
=== CORRIGINDO DEADLOCK ===
Thread 1 adquiriu recurso A
Thread 1 adquiriu recurso B e terminou
Thread 2 adquiriu recurso A  
Thread 2 adquiriu recurso B e terminou
```
**🟢 Execução completa sem travamento**

---

### **Comportamentos Esperados:**

#### **Opção 1.1 - Jantar SEM Deadlock:**
- **COMPORTAMENTO**: Execução contínua infinita
- **OBSERVAÇÃO**: Filósofos alternam entre estados normalmente
- **RESULTADO**: Sistema NÃO trava

#### **Opção 1.2 - Jantar COM Deadlock:**
- **COMPORTAMENTO**: Travamento após alguns ciclos
- **OBSERVAÇÃO**: Filósofos ficam "com fome" eternamente
- **RESULTADO**: Sistema TRAVA (demonstração do problema)

#### **Opção 2 - Contador Concorrente:**
- **COMPORTAMENTO**: Mostra diferença valores com/sem sincronização
- **OBSERVAÇÃO**: Tempo de execução significativamente diferente
- **RESULTADO**: Demonstra condição de corrida e solução

#### **Opção 3.1 - Deadlock:**
- **COMPORTAMENTO**: Travamento imediato
- **OBSERVAÇÃO**: Threads não conseguem progredir
- **RESULTADO**: Deadlock reproduzido

#### **Opção 3.2 - Correção:**
- **COMPORTAMENTO**: Execução completa
- **OBSERVAÇÃO**: Ambas threads terminam normalmente
- **RESULTADO**: Deadlock resolvido

---

## 📚 **REFERÊNCIAS CONCEITUAIS IMPLEMENTADAS**

1. **Jantar dos Filósofos** - Problema clássico de Dijkstra (1965)
2. **Condições de Coffman** - Framework para análise de deadlock
3. **Semáforos** - Mecanismo de sincronização de Dijkstra
4. **Hierarquia de Recursos** - Estratégia de prevenção de deadlock
5. **Fairness** - Garantia de não-starvation em algoritmos concorrentes
