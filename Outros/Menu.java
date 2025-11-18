package Outros;
import java.util.Scanner;
import Contador.ContadorConcorrente;
import Deadlock.DeadlockSimples;
import JantarFilosofos.JantarFilosofos;

public class Menu {
    private Scanner scanner;
    private JantarFilosofos jantarFilosofos;
    private ContadorConcorrente contadorConcorrente;
    private DeadlockSimples deadlockSimples;

    public Menu() {
        scanner = new Scanner(System.in);
        jantarFilosofos = new JantarFilosofos();
        contadorConcorrente = new ContadorConcorrente();
        deadlockSimples = new DeadlockSimples();
    }

    public void executar() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Jantar dos Filosofos");
            System.out.println("2 - Contador Concorrente");
            System.out.println("3 - Exemplo de Deadlock");
            System.out.println("0 - Sair do Programa");
            System.out.print("Digite sua escolha: ");
            
            try {
                int opcao = scanner.nextInt();

                if (opcao == 1) {
                    menuJantarFilosofos();
                } else if (opcao == 2) {
                    menuContadorConcorrente();
                } else if (opcao == 3) {
                    menuDeadlock();
                } else if (opcao == 0) {
                    System.out.println("Saindo do programa...");
                    break;
                } else {
                    System.out.println("Opcao invalida! Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Entrada invalida! Digite um numero.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void menuJantarFilosofos() {
        System.out.println("\n--- Jantar dos Filosofos ---");
        System.out.println("1 - Executar sem deadlock (com solucao)");
        System.out.println("2 - Executar com deadlock (para demonstrar o problema)");
        System.out.print("Escolha: ");
        
        try {
            int subOpcao = scanner.nextInt();
            if (subOpcao == 1) {
                jantarFilosofos.executarSemDeadlock();
            } else if (subOpcao == 2) {
                jantarFilosofos.executarComDeadlock();
            } else {
                System.out.println("Opcao invalida para Jantar dos Filosofos!");
            }
        } catch (Exception e) {
            System.out.println("Entrada invalida!");
            scanner.nextLine();
        }
    }

    private void menuContadorConcorrente() {
        try {
            contadorConcorrente.executarTeste();
        } catch (Exception e) {
            System.out.println("Teste interrompido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void menuDeadlock() {
        System.out.println("\n1 - Reproduzir deadlock");
        System.out.println("2 - Corrigir deadlock");
        
        try {
            int escolha = scanner.nextInt();
            if (escolha == 1) {
                deadlockSimples.reproduzirDeadlock();
            } else if (escolha == 2) {
                deadlockSimples.corrigirDeadlock();
            } else {
                System.out.println("Opcao invalida!");
            }
        } catch (Exception e) {
            System.out.println("Entrada invalida!");
            scanner.nextLine();
        }
    }
}