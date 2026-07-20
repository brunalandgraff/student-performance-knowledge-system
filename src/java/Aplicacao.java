// Ponto de entrada da aplicacao Java.
// A logica pedagogica fica em Prolog; esta classe apenas inicia o menu.
public class Aplicacao {

    public static void main(String[] args) {
        String caminhoSistema = "../prolog/sistema.pl";

        if (args.length > 0 && !args[0].trim().isEmpty()) {
            caminhoSistema = args[0].trim();
        }

        Menu menu = new Menu(caminhoSistema);
        menu.iniciar();
    }
}
