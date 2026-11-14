package com.pdm.dailydev.data

import com.pdm.dailydev.model.Assunto
import com.pdm.dailydev.model.Licao
import com.pdm.dailydev.model.Questao
import java.util.UUID

class LicaoRepositoryLocal : LicaoRepository {

    // Assuntos simulando registros da tabela 'assuntos' no banco
    private val assuntoVariaveis = Assunto(nome = "Variáveis e Tipos")
    private val assuntoCondicionais = Assunto(nome = "Condicionais")
    private val assuntoLoops = Assunto(nome = "Laços de Repetição")
    private val assuntoFuncoes = Assunto(nome = "Funções")
    private val assuntoEstruturas = Assunto(nome = "Estruturas de Dados")
    private val assuntoPoo = Assunto(nome = "Orientação a Objetos")
    private val assuntoGit = Assunto(nome = "Git")
    private val assuntoSql = Assunto(nome = "Banco de Dados e SQL")
    private val assuntoRedes = Assunto(nome = "HTTP e APIs")
    private val assuntoArquitetura = Assunto(nome = "Arquitetura Android")

    private val assuntosHardcoded = listOf(
        assuntoVariaveis,
        assuntoCondicionais,
        assuntoLoops,
        assuntoFuncoes,
        assuntoEstruturas,
        assuntoPoo,
        assuntoGit,
        assuntoSql,
        assuntoRedes,
        assuntoArquitetura
    )

    private val licoesHardcoded: List<Licao> = listOf(
        // 1
        Licao(
            titulo = "Variáveis Imutáveis vs Mutáveis",
            textoExplicacao = "Em Kotlin, 'val' cria uma referência imutável (somente leitura) e 'var' cria uma variável mutável. Prefira sempre 'val' para evitar efeitos colaterais inesperados.",
            assunto = assuntoVariaveis,
            dificuldade = 1,
            questoes = listOf(
                Questao(
                    enunciado = "Qual palavra-chave cria uma variável imutável em Kotlin?",
                    alternativas = listOf("var", "val", "let", "const"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "O que ocorre ao tentar reatribuir um valor a uma variável declarada com 'val'?",
                    alternativas = listOf("Atualiza normalmente", "Erro de compilação", "Converte para null"),
                    indiceCorreto = 1
                )
            )
        ),
        // 2
        Licao(
            titulo = "Null Safety na Prática",
            textoExplicacao = "O sistema de tipos do Kotlin distingue tipos não-nulos (String) de anuláveis (String?). O operador '?.' executa chamadas seguras e '?:' fornece um valor padrão caso seja nulo.",
            assunto = assuntoVariaveis,
            dificuldade = 2,
            questoes = listOf(
                Questao(
                    enunciado = "Como declarar uma String que aceita valor nulo em Kotlin?",
                    alternativas = listOf("String!", "String?", "Nullable<String>"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual operador retorna um valor padrão quando a expressão à esquerda é nula?",
                    alternativas = listOf("!!", "?.", "?:"),
                    indiceCorreto = 2
                )
            )
        ),
        // 3
        Licao(
            titulo = "Expressões If e When",
            textoExplicacao = "Em Kotlin, 'if' e 'when' são expressões que retornam valores diretamente. O 'when' substitui o switch tradicional aceitando intervalos, tipos e múltiplas condições.",
            assunto = assuntoCondicionais,
            dificuldade = 2,
            questoes = listOf(
                Questao(
                    enunciado = "Qual estrutura substitui o 'switch' tradicional em Kotlin?",
                    alternativas = listOf("match", "when", "select"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Ao atribuir o resultado de um 'if' a uma variável, o que é obrigatório?",
                    alternativas = listOf("Ter um bloco else", "Usar var", "Usar return explícito"),
                    indiceCorreto = 0
                )
            )
        ),
        // 4
        Licao(
            titulo = "Operadores Lógicos e Curto-Circuito",
            textoExplicacao = "Os operadores '&&' (E) e '||' (OU) usam avaliação em curto-circuito: se o primeiro operando já definir o resultado final, o segundo nem chega a ser avaliado.",
            assunto = assuntoCondicionais,
            dificuldade = 3,
            questoes = listOf(
                Questao(
                    enunciado = "Na expressão 'false && verificarBanco()', a função verificarBanco() é chamada?",
                    alternativas = listOf("Sim, sempre", "Não, devido ao curto-circuito do &&", "Apenas em modo debug"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual operador inverte o valor lógico de um Boolean?",
                    alternativas = listOf("~", "!", "not()"),
                    indiceCorreto = 1
                )
            )
        ),
        // 5
        Licao(
            titulo = "Loops e Intervalos (Ranges)",
            textoExplicacao = "Use '1..5' para intervalos inclusivos, '1 until 5' para excluir o último número e 'downTo' para contagens regressivas.",
            assunto = assuntoLoops,
            dificuldade = 2,
            questoes = listOf(
                Questao(
                    enunciado = "Quantas vezes 'for (i in 0 until 4)' executa?",
                    alternativas = listOf("3 vezes", "4 vezes", "5 vezes"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual comando encerra imediatamente um loop antes do fim?",
                    alternativas = listOf("continue", "break", "skip"),
                    indiceCorreto = 1
                )
            )
        ),
        // 6
        Licao(
            titulo = "While vs Do-While",
            textoExplicacao = "O laço 'while' verifica a condição antes de executar o bloco. Já o 'do-while' executa o bloco pelo menos uma vez antes de testar a condição.",
            assunto = assuntoLoops,
            dificuldade = 3,
            questoes = listOf(
                Questao(
                    enunciado = "Qual laço garante que o bloco de código seja executado no mínimo uma vez?",
                    alternativas = listOf("for", "while", "do-while"),
                    indiceCorreto = 2
                ),
                Questao(
                    enunciado = "O que o comando 'continue' faz dentro de um loop?",
                    alternativas = listOf("Encerra o loop", "Pula para a próxima iteração", "Reinicia o contador"),
                    indiceCorreto = 1
                )
            )
        ),
        // 7
        Licao(
            titulo = "Funções e Parâmetros Padrão",
            textoExplicacao = "Kotlin permite definir valores padrão para parâmetros e passar argumentos nomeados, reduzindo a necessidade de múltiplas sobrecargas de funções.",
            assunto = assuntoFuncoes,
            dificuldade = 3,
            questoes = listOf(
                Questao(
                    enunciado = "Qual tipo de retorno indica que uma função em Kotlin não retorna valor útil?",
                    alternativas = listOf("Void", "Unit", "Nothing"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual recurso permite omitir argumentos opcionais na chamada de uma função?",
                    alternativas = listOf("Parâmetros padrão (default arguments)", "Varargs", "Infix"),
                    indiceCorreto = 0
                )
            )
        ),
        // 8
        Licao(
            titulo = "Lambdas e Closures",
            textoExplicacao = "Funções de alta ordem recebem ou retornam outras funções. Quando uma lambda captura e mantém acesso a variáveis do escopo externo, forma-se uma Closure.",
            assunto = assuntoFuncoes,
            dificuldade = 4,
            questoes = listOf(
                Questao(
                    enunciado = "O que caracteriza uma função de alta ordem (Higher-Order Function)?",
                    alternativas = listOf("Receber ou retornar funções", "Ser recursiva", "Ser assíncrona"),
                    indiceCorreto = 0
                ),
                Questao(
                    enunciado = "Qual é o nome padrão do parâmetro único implícito dentro de uma lambda em Kotlin?",
                    alternativas = listOf("this", "it", "self"),
                    indiceCorreto = 1
                )
            )
        ),
        // 9
        Licao(
            titulo = "Listas e Conjuntos (List vs Set)",
            textoExplicacao = "List mantém a ordem de inserção e permite elementos duplicados. Set armazena apenas elementos únicos, descartando duplicatas automaticamente.",
            assunto = assuntoEstruturas,
            dificuldade = 4,
            questoes = listOf(
                Questao(
                    enunciado = "Qual estrutura garante automaticamente elementos sem repetição?",
                    alternativas = listOf("List", "Array", "Set"),
                    indiceCorreto = 2
                ),
                Questao(
                    enunciado = "Em Kotlin, 'listOf(1, 2, 3)' cria uma lista de qual tipo?",
                    alternativas = listOf("Mutável", "Somente leitura (imutável)", "Sincronizada"),
                    indiceCorreto = 1
                )
            )
        ),
        // 10
        Licao(
            titulo = "Mapas e Operações de Coleção",
            textoExplicacao = "Map associa chaves únicas a valores. Funções como 'filter' selecionam elementos e 'map' transformam cada item em uma nova coleção.",
            assunto = assuntoEstruturas,
            dificuldade = 5,
            questoes = listOf(
                Questao(
                    enunciado = "Qual função transforma cada item de uma lista gerando outra lista do mesmo tamanho?",
                    alternativas = listOf("filter", "map", "find"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "O que acontece ao inserir uma chave já existente em um MutableMap?",
                    alternativas = listOf("Lança exceção", "Sobrescreve o valor anterior", "Ignora a operação"),
                    indiceCorreto = 1
                )
            )
        ),
        // 11
        Licao(
            titulo = "Data Classes e Encapsulamento",
            textoExplicacao = "Data classes geram automaticamente equals(), hashCode(), toString() e copy(), sendo ideais para representar modelos de domínio imutáveis.",
            assunto = assuntoPoo,
            dificuldade = 4,
            questoes = listOf(
                Questao(
                    enunciado = "Qual método gerado em uma 'data class' permite clonar o objeto alterando apenas alguns campos?",
                    alternativas = listOf("clone()", "copy()", "duplicate()"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual modificador restringe o acesso a uma propriedade apenas dentro da própria classe?",
                    alternativas = listOf("internal", "protected", "private"),
                    indiceCorreto = 2
                )
            )
        ),
        // 12
        Licao(
            titulo = "Interfaces e Polimorfismo",
            textoExplicacao = "Interfaces definem contratos sem acoplar o código a uma implementação específica, permitindo trocar repositórios locais por remotos facilmente.",
            assunto = assuntoPoo,
            dificuldade = 5,
            questoes = listOf(
                Questao(
                    enunciado = "Qual princípio SOLID recomenda depender de abstrações (interfaces) em vez de classes concretas?",
                    alternativas = listOf("Single Responsibility", "Dependency Inversion", "Liskov Substitution"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Uma classe em Kotlin pode implementar quantas interfaces?",
                    alternativas = listOf("Apenas uma", "No máximo duas", "Múltiplas interfaces"),
                    indiceCorreto = 2
                )
            )
        ),
        // 13
        Licao(
            titulo = "Sealed Interfaces e Modelagem de Estados",
            textoExplicacao = "Sealed interfaces restringem as implementações possíveis a uma hierarquia fechada, permitindo que o compilador verifique exaustivamente todos os casos no 'when'.",
            assunto = assuntoPoo,
            dificuldade = 6,
            questoes = listOf(
                Questao(
                    enunciado = "Qual é a principal vantagem de usar 'sealed interface' para estados de UI?",
                    alternativas = listOf(
                        "Garantir que o 'when' cubra todos os estados possíveis em tempo de compilação",
                        "Salvar o estado automaticamente no banco",
                        "Evitar o uso de ViewModel"
                    ),
                    indiceCorreto = 0
                ),
                Questao(
                    enunciado = "Qual palavra-chave cria uma instância única (singleton) para um estado sem parâmetros?",
                    alternativas = listOf("data class", "data object", "companion"),
                    indiceCorreto = 1
                )
            )
        ),
        // 14
        Licao(
            titulo = "Branches e Merge no Git",
            textoExplicacao = "Branches isolam o desenvolvimento de novas funcionalidades. 'git switch -c' cria e troca de branch; 'git merge' integra as alterações.",
            assunto = assuntoGit,
            dificuldade = 3,
            questoes = listOf(
                Questao(
                    enunciado = "Qual comando cria uma nova branch 'feature' e já muda para ela?",
                    alternativas = listOf("git branch feature", "git checkout -b feature", "git merge feature"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Quando ocorre um conflito de merge?",
                    alternativas = listOf(
                        "Quando duas branches modificam as mesmas linhas de forma divergente",
                        "Quando há arquivos não rastreados",
                        "Quando o repositório remoto está vazio"
                    ),
                    indiceCorreto = 0
                )
            )
        ),
        // 15
        Licao(
            titulo = "Fetch, Pull e Rebase",
            textoExplicacao = "'git fetch' apenas baixa as novidades do remoto sem alterar sua branch local. 'git pull' faz o fetch seguido de merge (ou rebase).",
            assunto = assuntoGit,
            dificuldade = 5,
            questoes = listOf(
                Questao(
                    enunciado = "Qual comando baixa atualizações remotas sem modificar automaticamente seus arquivos locais?",
                    alternativas = listOf("git pull", "git fetch", "git push"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "O que o 'git stash' faz com alterações locais não commitadas?",
                    alternativas = listOf("Apaga permanentemente", "Guarda temporariamente em uma pilha limpa", "Envia para o GitHub"),
                    indiceCorreto = 1
                )
            )
        ),
        // 16
        Licao(
            titulo = "Consultas SQL e Chaves Relacionais",
            textoExplicacao = "Chaves primárias (PK) identificam unicamente cada registro, enquanto chaves estrangeiras (FK) conectam tabelas relacionadas como 'licoes' e 'assuntos'.",
            assunto = assuntoSql,
            dificuldade = 4,
            questoes = listOf(
                Questao(
                    enunciado = "Qual cláusula SQL filtra os registros retornados por um SELECT?",
                    alternativas = listOf("GROUP BY", "WHERE", "ORDER BY"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual tipo de chave liga o campo 'assunto_id' da tabela lições ao 'id' da tabela assuntos?",
                    alternativas = listOf("Primary Key", "Foreign Key", "Unique Key"),
                    indiceCorreto = 1
                )
            )
        ),
        // 17
        Licao(
            titulo = "Joins e Agregações em SQL",
            textoExplicacao = "INNER JOIN combina linhas de duas tabelas onde há correspondência. Funções como COUNT, AVG e SUM agregam resultados agrupados por GROUP BY.",
            assunto = assuntoSql,
            dificuldade = 6,
            questoes = listOf(
                Questao(
                    enunciado = "Qual JOIN retorna todos os registros da tabela à esquerda, mesmo sem correspondência na tabela à direita?",
                    alternativas = listOf("INNER JOIN", "LEFT JOIN", "CROSS JOIN"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual cláusula é usada para filtrar resultados após uma agregação com GROUP BY?",
                    alternativas = listOf("WHERE", "HAVING", "LIMIT"),
                    indiceCorreto = 1
                )
            )
        ),
        // 18
        Licao(
            titulo = "Métodos HTTP e Status Codes",
            textoExplicacao = "APIs REST utilizam métodos semânticos: GET (buscar), POST (criar), PUT/PATCH (atualizar) e DELETE (remover), respondendo com códigos como 200, 201, 404 e 500.",
            assunto = assuntoRedes,
            dificuldade = 4,
            questoes = listOf(
                Questao(
                    enunciado = "Qual código de status HTTP indica que um novo recurso foi criado com sucesso?",
                    alternativas = listOf("200 OK", "201 Created", "204 No Content"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual família de códigos HTTP representa erros causados pela requisição do cliente (ex: 400, 401, 404)?",
                    alternativas = listOf("2xx", "3xx", "4xx", "5xx"),
                    indiceCorreto = 2
                )
            )
        ),
        // 19
        Licao(
            titulo = "Unidirectional Data Flow (UDF) no Compose",
            textoExplicacao = "No Jetpack Compose, o estado desce do ViewModel para a UI (StateFlow) e os eventos sobem da UI para o ViewModel através de callbacks.",
            assunto = assuntoArquitetura,
            dificuldade = 6,
            questoes = listOf(
                Questao(
                    enunciado = "No fluxo unidirecional de dados (UDF), quem deve ser o dono da regra de negócio e do estado da tela?",
                    alternativas = listOf("O Composable da tela", "O ViewModel", "A MainActivity"),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Por que o ViewModel sobrevive à rotação de tela no Android?",
                    alternativas = listOf(
                        "Porque seu escopo está atrelado ao ViewModelStore e não é destruído em mudanças de configuração",
                        "Porque ele salva tudo em SharedPreferences",
                        "Porque ele roda em outro aplicativo"
                    ),
                    indiceCorreto = 0
                )
            )
        ),
        // 20
        Licao(
            titulo = "Coroutines e Funções Suspend",
            textoExplicacao = "Funções marcadas com 'suspend' podem pausar sua execução sem bloquear a Main Thread, garantindo que chamadas de rede ou banco não congelem a interface.",
            assunto = assuntoArquitetura,
            dificuldade = 7,
            questoes = listOf(
                Questao(
                    enunciado = "O que acontece com a thread principal (UI Thread) quando uma função 'suspend' aguarda uma resposta da rede?",
                    alternativas = listOf(
                        "Fica bloqueada até a resposta chegar",
                        "Permanece livre para desenhar a interface enquanto a coroutine está suspensa",
                        "Lança NetworkOnMainThreadException"
                    ),
                    indiceCorreto = 1
                ),
                Questao(
                    enunciado = "Qual escopo cancela automaticamente suas coroutines quando o ViewModel é encerrado?",
                    alternativas = listOf("GlobalScope", "viewModelScope", "MainScope"),
                    indiceCorreto = 1
                )
            )
        )
    )

    override suspend fun obterTodosAssuntos(): List<Assunto> = assuntosHardcoded

    override suspend fun obterTodasLicoes(): List<Licao> = licoesHardcoded

    override suspend fun obterLicoesPorAssunto(assuntoId: UUID): List<Licao> {
        return licoesHardcoded.filter { it.assunto.id == assuntoId }
    }

    override suspend fun obterLicaoPorId(id: UUID): Licao? {
        return licoesHardcoded.find { it.id == id }
    }
}