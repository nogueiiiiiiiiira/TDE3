package Deadlock;

public class DeadlockSimples {
    static final Object recursoA = new Object();
    static final Object recursoB = new Object();

    public void reproduzirDeadlock() {
        System.out.println("\n=== REPRODUZINDO DEADLOCK ===");
        System.out.println("Condicoes de Coffman presentes:");
        System.out.println("1. Exclusao Mutua");
        System.out.println("2. Manter-e-Esperar");
        System.out.println("3. Nao-preempcao");
        System.out.println("4. Espera Circular");

        Thread threadPrimeira = new Thread(new Runnable() {
            public void run() {
                synchronized (recursoA) {
                    System.out.println("Thread 1 adquiriu recurso A");
                    aguardar(50);
                    synchronized (recursoB) {
                        System.out.println("Thread 1 adquiriu recurso B e terminou");
                    }
                }
            }
        });

        Thread threadSegunda = new Thread(new Runnable() {
            public void run() {
                synchronized (recursoB) {
                    System.out.println("Thread 2 adquiriu recurso B");
                    aguardar(50);
                    synchronized (recursoA) {
                        System.out.println("Thread 2 adquiriu recurso A e terminou");
                    }
                }
            }
        });

        threadPrimeira.start();
        threadSegunda.start();
    }

    public void corrigirDeadlock() {
        System.out.println("\n=== CORRIGINDO DEADLOCK ===");
        System.out.println("Solucao: Hierarquia de recursos (sempre adquirir recurso A antes de recurso B)");
        System.out.println("Condicao quebrada: Espera Circular");

        Thread threadPrimeira = new Thread(new Runnable() {
            public void run() {
                synchronized (recursoA) {
                    System.out.println("Thread 1 adquiriu recurso A");
                    aguardar(50);
                    synchronized (recursoB) {
                        System.out.println("Thread 1 adquiriu recurso B e terminou");
                    }
                }
            }
        });

        Thread threadSegunda = new Thread(new Runnable() {
            public void run() {
                synchronized (recursoA) {
                    System.out.println("Thread 2 adquiriu recurso A");
                    aguardar(50);
                    synchronized (recursoB) {
                        System.out.println("Thread 2 adquiriu recurso B e terminou");
                    }
                }
            }
        });

        threadPrimeira.start();
        threadSegunda.start();
    }

    void aguardar(long milissegundos) {
        try { 
            Thread.sleep(milissegundos); 
        } catch (InterruptedException e) { 
            Thread.currentThread().interrupt(); 
        }
    }
}