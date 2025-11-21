package JantarFilosofos;

public class Filosofo extends Thread {
    private int id;
    private Garfo garfoEsquerdo;
    private Garfo garfoDireito;
    private String estado = "pensando";

    public Filosofo(int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    public void run() {
        while (true) {
            pensar();
            estado = "com fome";
            System.out.println("Filosofo " + id + " esta " + estado + " e quer comer!");

            Garfo primeiroGarfo;
            Garfo segundoGarfo;
            if (garfoEsquerdo.getNumero() < garfoDireito.getNumero()) {
                primeiroGarfo = garfoEsquerdo;
                segundoGarfo = garfoDireito;
            } else {
                primeiroGarfo = garfoDireito;
                segundoGarfo = garfoEsquerdo;
            }

            synchronized (primeiroGarfo) {
                System.out.println("Filosofo " + id + " pegou o garfo " + primeiroGarfo.getNumero());
                synchronized (segundoGarfo) {
                    System.out.println("Filosofo " + id + " pegou o garfo " + segundoGarfo.getNumero());
                    estado = "comendo";
                    System.out.println("Filosofo " + id + " esta " + estado + "");
                    comer();
                    estado = "pensando";
                    System.out.println("Filosofo " + id + " terminou de comer e voltou a " + estado);
                }
                System.out.println("Filosofo " + id + " soltou o garfo " + segundoGarfo.getNumero());
            }
            System.out.println("Filosofo " + id + " soltou o garfo " + primeiroGarfo.getNumero());
        }
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