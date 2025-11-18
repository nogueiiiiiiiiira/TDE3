package JantarFilosofos;
import java.util.ArrayList;

public class JantarFilosofos {

    public void executarSemDeadlock() {
        int numeroFilosofos = 5;
        ArrayList<Garfo> listaDeGarfos = new ArrayList<Garfo>();
        
        for (int indiceGarfo = 0; indiceGarfo < numeroFilosofos; indiceGarfo++) {
            Garfo garfoAtual = new Garfo(indiceGarfo);
            listaDeGarfos.add(garfoAtual);
            System.out.println("Garfo numero " + indiceGarfo + " foi colocado na mesa.");
        }

        ArrayList<Filosofo> listaDeFilosofos = new ArrayList<Filosofo>();
        for (int indiceFilosofo = 0; indiceFilosofo < numeroFilosofos; indiceFilosofo++) {
            Garfo garfoEsquerdo = listaDeGarfos.get(indiceFilosofo);
            int indiceDireito = (indiceFilosofo + 1) % numeroFilosofos;
            Garfo garfoDireito = listaDeGarfos.get(indiceDireito);
            Filosofo filosofoAtual = new Filosofo(indiceFilosofo, garfoEsquerdo, garfoDireito);
            listaDeFilosofos.add(filosofoAtual);
            System.out.println("Filosofo numero " + indiceFilosofo + " sentou-se na mesa com garfos " + garfoEsquerdo.getNumero() + " e " + garfoDireito.getNumero());
            filosofoAtual.start();
        }
        System.out.println("Simulacao sem deadlock iniciada! Os filosofos estao pensando e comendo sem travar.");
    }

    public void executarComDeadlock() {
        int numeroFilosofos = 5;
        ArrayList<Garfo> listaDeGarfos = new ArrayList<Garfo>();
        
        for (int indiceGarfo = 0; indiceGarfo < numeroFilosofos; indiceGarfo++) {
            Garfo garfoAtual = new Garfo(indiceGarfo);
            listaDeGarfos.add(garfoAtual);
            System.out.println("Garfo numero " + indiceGarfo + " foi colocado na mesa para possivel deadlock.");
        }

        ArrayList<FilosofoComDeadlock> listaDeFilosofosComDeadlock = new ArrayList<FilosofoComDeadlock>();
        for (int indiceFilosofo = 0; indiceFilosofo < numeroFilosofos; indiceFilosofo++) {
            Garfo garfoEsquerdo = listaDeGarfos.get(indiceFilosofo);
            int indiceDireito = (indiceFilosofo + 1) % numeroFilosofos;
            Garfo garfoDireito = listaDeGarfos.get(indiceDireito);
            FilosofoComDeadlock filosofoAtual = new FilosofoComDeadlock(indiceFilosofo, garfoEsquerdo, garfoDireito);
            listaDeFilosofosComDeadlock.add(filosofoAtual);
            System.out.println("Filosofo numero " + indiceFilosofo + " sentou-se na mesa com garfos " + garfoEsquerdo.getNumero() + " e " + garfoDireito.getNumero() + " (risco de deadlock)");
            filosofoAtual.start();
        }
        System.out.println("Simulacao com deadlock iniciada! Cuidado, os filosofos podem travar!");
    }
}