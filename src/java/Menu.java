import java.util.Scanner;

// Classe responsavel pela interface de texto com o utilizador.
// Toda a inferencia e gestao da base sao delegadas ao Prolog via JPL.
public class Menu {

    private final Scanner scanner;
    private final IntegradorProlog integrador;

    public Menu(String caminhoSistema) {
        this.scanner = new Scanner(System.in);
        this.integrador = new IntegradorProlog(caminhoSistema);
    }

    public void iniciar() {
        if (!carregarSistema()) {
            return;
        }

        int opcao;

        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opcao: ");
            executarOpcao(opcao);
        } while (opcao != 0);
    }

    private boolean carregarSistema() {
        try {
            if (integrador.carregarSistema()) {
                System.out.println("Sistema Prolog carregado com sucesso.");
                return true;
            }

            System.out.println("Erro: nao foi possivel carregar o sistema Prolog.");
            return false;
        } catch (RuntimeException e) {
            System.out.println("Erro ao iniciar a ligacao Java-Prolog.");
            System.out.println("Verifique se o SWI-Prolog esta instalado e se o PATH inclui a pasta bin.");
            System.out.println("Detalhe tecnico: " + e.getMessage());
            return false;
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println(" Sistema de Consultas Pedagogicas - E-folio B");
        System.out.println("==============================================");
        System.out.println("1  - Listar alunos em risco");
        System.out.println("2  - Listar alunos participativos");
        System.out.println("3  - Listar alunos com bom desempenho");
        System.out.println("4  - Listar alunos acima da media da turma");
        System.out.println("5  - Consultar aluno por ID");
        System.out.println("6  - Adicionar aluno");
        System.out.println("7  - Atualizar media de aluno");
        System.out.println("8  - Atualizar participacoes no forum");
        System.out.println("9  - Remover aluno da base de conhecimento");
        System.out.println("10 - Listar alunos consistentes (bonus)");
        System.out.println("0  - Sair");
        System.out.println("----------------------------------------------");
    }

    private void executarOpcao(int opcao) {
        try {
            switch (opcao) {
                case 1:
                    listar("Alunos em risco", "listar_em_risco");
                    break;
                case 2:
                    listar("Alunos participativos", "listar_participativos");
                    break;
                case 3:
                    listar("Alunos com bom desempenho", "listar_bons");
                    break;
                case 4:
                    listar("Alunos acima da media da turma", "listar_acima_media");
                    break;
                case 5:
                    consultarAluno();
                    break;
                case 6:
                    adicionarAluno();
                    break;
                case 7:
                    atualizarMedia();
                    break;
                case 8:
                    atualizarForum();
                    break;
                case 9:
                    removerAluno();
                    break;
                case 10:
                    listar("Alunos consistentes", "listar_consistentes");
                    break;
                case 0:
                    System.out.println("A encerrar a aplicacao.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } catch (RuntimeException e) {
            System.out.println("Erro durante a execucao da operacao.");
            System.out.println("Detalhe tecnico: " + e.getMessage());
        }
    }

    private void listar(String titulo, String predicado) {
        String resultado = integrador.listar(predicado);

        System.out.println();
        System.out.println(titulo + ":");
        System.out.println("IDs: " + resultado);
    }

    private void consultarAluno() {
        int id = lerInteiroPositivo("Introduza o ID do aluno: ");
        if (id <= 0) {
            return;
        }

        System.out.println();
        System.out.println(integrador.consultarAluno(id));
    }

    private void adicionarAluno() {
        int id = lerInteiroPositivo("Introduza o novo ID do aluno: ");
        if (id <= 0) {
            return;
        }

        String nome = lerTextoObrigatorio("Introduza o nome do aluno: ");
        if (nome.isEmpty()) {
            return;
        }

        boolean sucesso = integrador.adicionarAluno(id, nome);
        if (sucesso) {
            System.out.println("Aluno adicionado e base persistida com sucesso.");
        } else {
            System.out.println("Nao foi possivel adicionar o aluno. Verifique se o ID ja existe.");
        }
    }

    private void atualizarMedia() {
        int id = lerInteiroPositivo("Introduza o ID do aluno: ");
        if (id <= 0) {
            return;
        }

        double media = lerDouble("Introduza a nova media (0 a 20): ");
        if (media < 0 || media > 20) {
            System.out.println("Media invalida. O valor deve estar entre 0 e 20.");
            return;
        }

        boolean sucesso = integrador.atualizarMedia(id, media);
        if (sucesso) {
            System.out.println("Media atualizada e base persistida com sucesso.");
        } else {
            System.out.println("Nao foi possivel atualizar a media. Verifique se o aluno existe.");
        }
    }

    private void atualizarForum() {
        int id = lerInteiroPositivo("Introduza o ID do aluno: ");
        if (id <= 0) {
            return;
        }

        int participacoes = lerInteiro("Introduza o numero de participacoes no forum: ");
        if (participacoes < 0) {
            System.out.println("Numero invalido. As participacoes nao podem ser negativas.");
            return;
        }

        boolean sucesso = integrador.atualizarForum(id, participacoes);
        if (sucesso) {
            System.out.println("Participacoes atualizadas e base persistida com sucesso.");
        } else {
            System.out.println("Nao foi possivel atualizar as participacoes. Verifique se o aluno existe.");
        }
    }

    private void removerAluno() {
        int id = lerInteiroPositivo("Introduza o ID do aluno a remover: ");
        if (id <= 0) {
            return;
        }

        String confirmacao = lerTextoObrigatorio("Confirma a remocao? (s/n): ");
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Operacao cancelada.");
            return;
        }

        boolean sucesso = integrador.removerAluno(id);
        if (sucesso) {
            System.out.println("Aluno removido e base persistida com sucesso.");
        } else {
            System.out.println("Nao foi possivel remover o aluno. Verifique se o ID existe.");
        }
    }

    private int lerInteiroPositivo(String mensagem) {
        int valor = lerInteiro(mensagem);
        if (valor <= 0) {
            System.out.println("Valor invalido. Introduza um numero inteiro positivo.");
            return -1;
        }
        return valor;
    }

    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine().trim();

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double lerDouble(String mensagem) {
        System.out.print(mensagem);
        String entrada = scanner.nextLine().trim().replace(',', '.');

        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private String lerTextoObrigatorio(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();

        if (texto.isEmpty()) {
            System.out.println("Texto invalido. O campo nao pode ficar vazio.");
        }

        return texto;
    }
}
