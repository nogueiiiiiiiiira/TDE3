package Contador;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class ContadorConcorrente {
    static int contadorSemSincronizacao = 0;
    static int contadorComSincronizacao = 0;
    static int totalThreads = 8;
    static int incrementosPorThread = 250000;
    static Semaphore semaforo = new Semaphore(1, true);

    public void executarSemControle() {
        contadorSemSincronizacao = 0;
        ArrayList<Thread> listaDeThreads = new ArrayList<Thread>();

        int indiceCriacaoThread = 0;
        while (indiceCriacaoThread < totalThreads) {
            Thread threadNova = new Thread(new Runnable() {
                public void run() {
                    int indiceIncremento = 0;
                    while (indiceIncremento < incrementosPorThread) {
                        contadorSemSincronizacao = contadorSemSincronizacao + 1;
                        indiceIncremento = indiceIncremento + 1;
                    }
                }
            });
            listaDeThreads.add(threadNova);
            indiceCriacaoThread = indiceCriacaoThread + 1;
        }

        long inicio = System.nanoTime();
        int indiceInicioThread = 0;
        while (indiceInicioThread < totalThreads) {
            Thread threadParaIniciar = listaDeThreads.get(indiceInicioThread);
            threadParaIniciar.start();
            indiceInicioThread = indiceInicioThread + 1;
        }
        try {
            int indiceJuncaoThread = 0;
            while (indiceJuncaoThread < totalThreads) {
                Thread threadParaAguardar = listaDeThreads.get(indiceJuncaoThread);
                threadParaAguardar.join();
                indiceJuncaoThread = indiceJuncaoThread + 1;
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        long fim = System.nanoTime();

        int valorEsperado = totalThreads * incrementosPorThread;
        int valorObtido = contadorSemSincronizacao;
        double tempoDecorrido = (fim - inicio) / 1000000000.0;
        System.out.println("Sem controle: Esperado = " + valorEsperado
                + ", Obtido = " + valorObtido
                + ", Tempo = " + tempoDecorrido + "s");
    }

    public void executarComSemaforo() {
        contadorComSincronizacao = 0;
        ArrayList<Thread> listaDeThreads = new ArrayList<Thread>();

        int indiceCriacaoThread = 0;
        while (indiceCriacaoThread < totalThreads) {
            Thread threadNova = new Thread(new Runnable() {
                public void run() {
                    int indiceIncremento = 0;
                    while (indiceIncremento < incrementosPorThread) {
                        try {
                            semaforo.acquire();
                            contadorComSincronizacao = contadorComSincronizacao + 1;
                            semaforo.release();
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                        indiceIncremento = indiceIncremento + 1;
                    }
                }
            });
            listaDeThreads.add(threadNova);
            indiceCriacaoThread = indiceCriacaoThread + 1;
        }

        long inicio = System.nanoTime();
        int indiceInicioThread = 0;
        while (indiceInicioThread < totalThreads) {
            Thread threadParaIniciar = listaDeThreads.get(indiceInicioThread);
            threadParaIniciar.start();
            indiceInicioThread = indiceInicioThread + 1;
        }
        try {
            int m = 0;
            while (m < totalThreads) {
                Thread threadParaAguardar = listaDeThreads.get(m);
                threadParaAguardar.join();
                m = m + 1;
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        
        long fim = System.nanoTime();

        int valorEsperado = totalThreads * incrementosPorThread;
        int valorObtido = contadorComSincronizacao;
        double tempoDecorrido = (fim - inicio) / 1000000000.0;
        System.out.println("Com semaforo: Esperado = " + valorEsperado
                + ", Obtido = " + valorObtido
                + ", Tempo = " + tempoDecorrido + "s");
    }

    public void executarTeste() {
        System.out.println("\n=== CONTADOR CONCORRENTE ===");
        String textoThreads = "Threads: " + totalThreads;
        String textoIncrementos = ", Incrementos por thread: " + incrementosPorThread;
        System.out.println(textoThreads + textoIncrementos);
        executarSemControle();
        try {
            executarComSemaforo();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}