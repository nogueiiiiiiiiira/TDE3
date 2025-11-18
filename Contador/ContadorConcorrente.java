package Contador;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ContadorConcorrente {
    static int contadorCompartilhado = 0;
    static final java.util.concurrent.Semaphore semaforo = new java.util.concurrent.Semaphore(1, true);

    public void executarTeste() {
        System.out.println("\n=== DEMONSTRANDO CONDICAO DE CORRIDA (SEM SINCRONIZACAO) ===");
        executarSemSincronizacao();

        // Reset contador
        contadorCompartilhado = 0;

        System.out.println("\n=== VERSAO CORRIGIDA COM SEMAFORO ===");
        executarComSincronizacao();
    }

    private void executarSemSincronizacao() {
        int numeroDeThreads = 8;
        int incrementosPorThread = 250_000;
        ExecutorService pool = Executors.newFixedThreadPool(numeroDeThreads);

        Runnable tarefa = new Runnable() {
            public void run() {
                for (int i = 0; i < incrementosPorThread; i++) {
                    contadorCompartilhado++; 
                }
            }
        };

        long tempoInicio = System.nanoTime();
        for (int i = 0; i < numeroDeThreads; i++) {
            pool.submit(tarefa);
        }
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long tempoFim = System.nanoTime();
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
        numeroDeThreads * incrementosPorThread, contadorCompartilhado, (tempoFim - tempoInicio) / 1000000000.0);
        System.out.println("Nota: Valor obtido pode ser menor devido a condicao de corrida (perda de incrementos).");
    }

    private void executarComSincronizacao() {
        int numeroDeThreads = 8;
        int incrementosPorThread = 250_000;
        ExecutorService pool = Executors.newFixedThreadPool(numeroDeThreads);

        Runnable tarefa = new Runnable() {
            public void run() {
                for (int i = 0; i < incrementosPorThread; i++) {
                    try {
                        semaforo.acquire();
                        contadorCompartilhado++;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        semaforo.release();
                    }
                }
            }
        };

        long tempoInicio = System.nanoTime();
        for (int i = 0; i < numeroDeThreads; i++) {
            pool.submit(tarefa);
        }
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long tempoFim = System.nanoTime();
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
        numeroDeThreads * incrementosPorThread, contadorCompartilhado, (tempoFim - tempoInicio) / 1000000000.0);
        System.out.println("Nota: Valor correto devido a exclusao mutua garantida pelo semaforo.");
    }
}
