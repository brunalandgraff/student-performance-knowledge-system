import org.jpl7.Atom;
import org.jpl7.Float;
import org.jpl7.Integer;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

import java.util.Map;

// Classe responsavel pela comunicacao entre Java e Prolog usando JPL.
// A aplicacao Java nao implementa regras pedagogicas; apenas invoca predicados Prolog.
public class IntegradorProlog {

    private final String caminhoSistema;

    public IntegradorProlog(String caminhoSistema) {
        this.caminhoSistema = caminhoSistema;
    }

    public boolean carregarSistema() {
        Query consulta = new Query("consult", new Term[] {
                new Atom(caminhoSistema.replace("\\", "/"))
        });

        return consulta.hasSolution();
    }

    public String listar(String predicado) {
        Variable lista = new Variable("Lista");
        Query consulta = new Query(predicado, new Term[] { lista });
        Map<String, Term> solucao = consulta.oneSolution();

        if (solucao == null || !solucao.containsKey("Lista")) {
            return "[]";
        }

        return solucao.get("Lista").toString();
    }

    public String consultarAluno(int id) {
        Variable nome = new Variable("Nome");
        Variable media = new Variable("Media");
        Variable participacoes = new Variable("Participacoes");
        Variable estado = new Variable("Estado");

        Query consulta = new Query(
                "consultar_aluno",
                new Term[] {
                        new Integer(id),
                        nome,
                        media,
                        participacoes,
                        estado
                }
        );

        Map<String, Term> solucao = consulta.oneSolution();

        if (solucao == null) {
            return "Aluno nao encontrado.";
        }

        return "ID: " + id + "\n"
                + "Nome: " + limparAtom(solucao.get("Nome")) + "\n"
                + "Media: " + solucao.get("Media") + "\n"
                + "Participacoes: " + solucao.get("Participacoes") + "\n"
                + "Estado: " + limparAtom(solucao.get("Estado"));
    }

    public boolean adicionarAluno(int id, String nome) {
        Query consulta = new Query(
                "adicionar_aluno",
                new Term[] {
                        new Integer(id),
                        new Atom(nome)
                }
        );

        return consulta.hasSolution();
    }

    public boolean atualizarMedia(int id, double novaMedia) {
        Query consulta = new Query(
                "atualizar_media",
                new Term[] {
                        new Integer(id),
                        new Float(novaMedia)
                }
        );

        return consulta.hasSolution();
    }

    public boolean atualizarForum(int id, int participacoes) {
        Query consulta = new Query(
                "atualizar_forum",
                new Term[] {
                        new Integer(id),
                        new Integer(participacoes)
                }
        );

        return consulta.hasSolution();
    }

    public boolean removerAluno(int id) {
        Query consulta = new Query(
                "remover_aluno",
                new Term[] {
                        new Integer(id)
                }
        );

        return consulta.hasSolution();
    }

    private String limparAtom(Term termo) {
        String texto = termo.toString();

        while (texto.length() >= 2 && texto.startsWith("'") && texto.endsWith("'")) {
            texto = texto.substring(1, texto.length() - 1);
        }

        return texto;
    }
}
