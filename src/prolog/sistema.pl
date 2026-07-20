% ============================================================
% SISTEMA DE CONSULTAS PEDAGOGICAS - E-FOLIO B
% Unidade Curricular: Linguagens de Programacao
% Base de conhecimento e predicados de inferencia em Prolog
% ============================================================

:- dynamic aluno/2.
:- dynamic forum/2.
:- dynamic tarefa/2.
:- dynamic quiz/2.
:- dynamic media/2.
:- dynamic estado/2.

% Obtem o caminho absoluto da base a partir da localizacao deste ficheiro.
% Isto evita erros quando o sistema e chamado a partir do Java em java/src.
base_conhecimento_path(Caminho) :-
    source_file(base_conhecimento_path(_), Sistema),
    file_directory_name(Sistema, Diretorio),
    directory_file_path(Diretorio, 'base_conhecimento.pl', Caminho).

% Carrega a base de conhecimento persistente.
:- base_conhecimento_path(Base), consult(Base).

% ======================================
% CALCULO DA MEDIA DA TURMA
% ======================================

% Calcula dinamicamente a media da turma com base nos factos media/2.
media_turma(MediaTurma) :-
    findall(Media, media(_, Media), Medias),
    Medias \= [],
    sum_list(Medias, Soma),
    length(Medias, Total),
    MediaTurma is Soma / Total.

% ======================================
% CONSULTAS BASICAS SOBRE ALUNOS
% ======================================

% Um aluno esta em risco se tiver media inferior a 10
% ou menos de 3 participacoes no forum.
em_risco(ID) :-
    aluno(ID, _),
    media(ID, Media),
    forum(ID, Participacoes),
    (Media < 10 -> true ; Participacoes < 3).

% Um aluno e participativo se tiver pelo menos 3 participacoes.
participativo(ID) :-
    aluno(ID, _),
    forum(ID, Participacoes),
    Participacoes >= 3.

% Um aluno tem bom desempenho se a media for maior ou igual a 10.
bom_desempenho(ID) :-
    aluno(ID, _),
    media(ID, Media),
    Media >= 10.

% ======================================
% INFERENCIA BASEADA NA MEDIA DA TURMA
% ======================================

% Identifica alunos cuja media individual e maior ou igual a media da turma.
acima_media(ID) :-
    aluno(ID, _),
    media(ID, MediaAluno),
    media_turma(MediaTurma),
    MediaAluno >= MediaTurma.

% ======================================
% LISTAGENS DE ALUNOS
% ======================================

listar_em_risco(Lista) :-
    findall(ID, em_risco(ID), IDs),
    sort(IDs, Lista).

listar_participativos(Lista) :-
    findall(ID, participativo(ID), IDs),
    sort(IDs, Lista).

listar_bons(Lista) :-
    findall(ID, bom_desempenho(ID), IDs),
    sort(IDs, Lista).

listar_acima_media(Lista) :-
    findall(ID, acima_media(ID), IDs),
    sort(IDs, Lista).

% ======================================
% FUNCIONALIDADE BONUS
% ======================================

% Um aluno consistente tem participacao adequada,
% bom desempenho e estado final aprovado.
aluno_consistente(ID) :-
    aluno(ID, _),
    participativo(ID),
    bom_desempenho(ID),
    estado(ID, aprovado).

listar_consistentes(Lista) :-
    findall(ID, aluno_consistente(ID), IDs),
    sort(IDs, Lista).

% Alias com nome proximo do exemplo do enunciado.
alunos_consistentes(Lista) :-
    listar_consistentes(Lista).

% ======================================
% CONSULTA DETALHADA DE ALUNO
% ======================================

consultar_aluno(ID, Nome, Media, Participacoes, Estado) :-
    aluno(ID, Nome),
    media(ID, Media),
    forum(ID, Participacoes),
    estado(ID, Estado).

% ======================================
% PERSISTENCIA DA BASE DE CONHECIMENTO
% ======================================

guardar_base :-
    base_conhecimento_path(Caminho),
    setup_call_cleanup(
        open(Caminho, write, Stream, [encoding(utf8)]),
        guardar_factos(Stream),
        close(Stream)
    ).

guardar_factos(Stream) :-
    format(Stream, '% ============================================================~n', []),
    format(Stream, '% BASE DE CONHECIMENTO - E-FOLIO B~n', []),
    format(Stream, '% Unidade Curricular: Linguagens de Programacao~n', []),
    format(Stream, '% ============================================================~n~n', []),

    format(Stream, ':- dynamic aluno/2.~n', []),
    format(Stream, ':- dynamic forum/2.~n', []),
    format(Stream, ':- dynamic tarefa/2.~n', []),
    format(Stream, ':- dynamic quiz/2.~n', []),
    format(Stream, ':- dynamic media/2.~n', []),
    format(Stream, ':- dynamic estado/2.~n~n', []),

    format(Stream, '% Alunos~n', []),
    forall(aluno(ID, Nome),
        format(Stream, 'aluno(~w, ~q).~n', [ID, Nome])),
    nl(Stream),

    format(Stream, '% Participacao no forum~n', []),
    forall(forum(ID, Participacoes),
        format(Stream, 'forum(~w, ~w).~n', [ID, Participacoes])),
    nl(Stream),

    format(Stream, '% Notas de tarefa~n', []),
    forall(tarefa(ID, Nota),
        format(Stream, 'tarefa(~w, ~q).~n', [ID, Nota])),
    nl(Stream),

    format(Stream, '% Notas de quiz~n', []),
    forall(quiz(ID, Nota),
        format(Stream, 'quiz(~w, ~q).~n', [ID, Nota])),
    nl(Stream),

    format(Stream, '% Medias~n', []),
    forall(media(ID, Media),
        format(Stream, 'media(~w, ~q).~n', [ID, Media])),
    nl(Stream),

    format(Stream, '% Estados finais~n', []),
    forall(estado(ID, Estado),
        format(Stream, 'estado(~w, ~q).~n', [ID, Estado])).

% ======================================
% ATUALIZACAO DO ESTADO DO ALUNO
% ======================================

% Depois de alteracoes dinamicas, o estado passa a refletir
% as regras principais deste e-folio B.
calcular_estado(ID, aprovado) :-
    participativo(ID),
    bom_desempenho(ID), !.

calcular_estado(ID, em_risco) :-
    em_risco(ID), !.

calcular_estado(ID, condicionado) :-
    aluno(ID, _), !.

calcular_estado(_, sem_dados).

atualizar_estado(ID) :-
    retractall(estado(ID, _)),
    calcular_estado(ID, NovoEstado),
    assertz(estado(ID, NovoEstado)).

% ======================================
% ATUALIZACAO DINAMICA DA BASE
% ======================================

adicionar_aluno(ID, Nome) :-
    integer(ID),
    ID > 0,
    atom(Nome),
    \+ aluno(ID, _),
    assertz(aluno(ID, Nome)),
    assertz(forum(ID, 0)),
    assertz(media(ID, 0)),
    assertz(estado(ID, em_risco)),
    guardar_base.

atualizar_media(ID, NovaMedia) :-
    aluno(ID, _),
    number(NovaMedia),
    NovaMedia >= 0,
    NovaMedia =< 20,
    retractall(media(ID, _)),
    assertz(media(ID, NovaMedia)),
    atualizar_estado(ID),
    guardar_base.

atualizar_forum(ID, NovasParticipacoes) :-
    aluno(ID, _),
    integer(NovasParticipacoes),
    NovasParticipacoes >= 0,
    retractall(forum(ID, _)),
    assertz(forum(ID, NovasParticipacoes)),
    atualizar_estado(ID),
    guardar_base.

remover_aluno(ID) :-
    aluno(ID, _),
    retractall(aluno(ID, _)),
    retractall(forum(ID, _)),
    retractall(tarefa(ID, _)),
    retractall(quiz(ID, _)),
    retractall(media(ID, _)),
    retractall(estado(ID, _)),
    guardar_base.
