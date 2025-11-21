package JantarFilosofos;

public class FilosofoComDeadlock extends Thread {
    private int id;
    private Garfo garfoEsquerdo;
    private Garfo garfoDireito;
    private String estado = "pensando";

    public FilosofoComDeadlock(int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    public void run() {
        for (int i = 0; i < 5; i++) { // Limitado a 5 iteracoes para demonstracao
            pensar();
            estado = "com fome";
            System.out.println("Filosofo " + id + " esta " + estado + " e quer comer!");

            synchronized (garfoEsquerdo) {
                System.out.println("Filosofo " + id + " pegou o garfo " + garfoEsquerdo.getNumero());
                synchronized (garfoDireito) {
                    System.out.println("Filosofo " + id + " pegou o garfo " + garfoDireito.getNumero());
                    estado = "comendo";
                    System.out.println("Filosofo " + id + " esta " + estado + "");
                    comer();
                    estado = "pensando";
                    System.out.println("Filosofo " + id + " terminou de comer e voltou a " + estado);
                }
                System.out.println("Filosofo " + id + " soltou o garfo " + garfoDireito.getNumero());
            }
            System.out.println("Filosofo " + id + " soltou o garfo " + garfoEsquerdo.getNumero());
        }
        System.out.println("Filosofo " + id + " finalizou sua simulacao apos 5 iteracoes.");
    }

    private void pensar() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Pensamento interrompido para filosofo " + id);
        }
    }

    private void comer() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Refeicao interrompida para filosofo " + id);
        }
    }
}