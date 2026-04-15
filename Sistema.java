import java.util.ArrayList;
import java.util.Scanner;

class ConfiguracaoServidor {
    String versao;
    String ip;
    String rota;

    ConfiguracaoServidor(String versao, String ip, String rota) {
        this.versao = versao;
        this.ip     = ip;
        this.rota   = rota;
    }

    void exibir() {
        System.out.println("  Versao : " + versao);
        System.out.println("  IP     : " + ip);
        System.out.println("  Rota   : " + rota);
    }

    ConfiguracaoServidor copiar() {
        return new ConfiguracaoServidor(versao, ip, rota);
    }
}

class Status {
    String valor = "OK";

    void atualizar(String novoValor) {
        valor = novoValor;
        System.out.println("\n  >> Status atualizado para: " + valor);
    }
}

class Evento {
    String tipo;
    String descricao;

    Evento(String tipo, String descricao) {
        this.tipo      = tipo;
        this.descricao = descricao;
    }

    void exibir() {
        System.out.println("\n  [EVENTO - " + tipo + "] " + descricao);
    }
}

class HistoricoMudancas {
    ArrayList<ConfiguracaoServidor> versoes = new ArrayList<>();

    void salvar(ConfiguracaoServidor config) {
        versoes.add(config.copiar());
        System.out.println("  >> Versão '" + config.versao + "' salva no histórico.");
    }

    void listar() {
        if (versoes.isEmpty()) {
            System.out.println("  Nenhuma versão no histórico ainda.");
            return;
        }
        for (int i = 0; i < versoes.size(); i++) {
            System.out.println("\n  [" + i + "] ---");
            versoes.get(i).exibir();
        }
    }

    ConfiguracaoServidor obter(int indice) {
        if (indice >= 0 && indice < versoes.size())
            return versoes.get(indice);
        return null;
    }
}

class Rollback {
    HistoricoMudancas historico;

    Rollback(HistoricoMudancas historico) {
        this.historico = historico;
    }

    void executar(Servidor servidor, Status status, Scanner sc) {
        if (historico.versoes.isEmpty()) {
            System.out.println("  Nenhuma versão disponível para rollback.");
            return;
        }

        System.out.println("\n  Versões disponíveis para restaurar:");
        historico.listar();

        System.out.print("\n  Digite o número da versão: ");
        int idx;
        try {
            idx = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Entrada inválida. Rollback cancelado.");
            return;
        }

        ConfiguracaoServidor anterior = historico.obter(idx);
        if (anterior == null) {
            System.out.println("  Versão não encontrada. Rollback cancelado.");
            return;
        }

        servidor.configuracao = anterior.copiar();
        status.atualizar("OK");
        System.out.println("  >> Rollback concluído! Configuração restaurada:");
        servidor.configuracao.exibir();
    }
}

class Servidor {
    String nome;
    ConfiguracaoServidor configuracao;

    Servidor(String nome, ConfiguracaoServidor configuracao) {
        this.nome          = nome;
        this.configuracao  = configuracao;
    }

    Evento aplicarConfiguracao(ConfiguracaoServidor nova, HistoricoMudancas historico, Status status) {
        System.out.println("\n  Verificando nova configuração antes de aplicar...");

        if (nova.ip.isEmpty() || nova.rota.isEmpty()) {
            status.atualizar("FALHA");
            return new Evento("FALHA", "IP ou Rota em branco. Configuração rejeitada para evitar queda da rede.");
        }

        historico.salvar(this.configuracao);

        this.configuracao = nova;
        status.atualizar("OK");
        return new Evento("SUCESSO", "Nova configuração aplicada com segurança.");
    }

    void exibir() {
        System.out.println("\n  Servidor : " + nome);
        configuracao.exibir();
    }
}

public class Sistema {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("  SISTEMA DE MANUTENÇÃO SEGURA DE REDE");
        System.out.println("  Baseado no incidente do Facebook (2021)");
        System.out.println("=================================================");

        ConfiguracaoServidor configInicial = new ConfiguracaoServidor("1.0", "10.0.0.1", "10.0.0.254");
        Servidor servidor                  = new Servidor("Servidor-Principal", configInicial);
        Status status                      = new Status();
        HistoricoMudancas historico        = new HistoricoMudancas();
        Rollback rollback                  = new Rollback(historico);

        System.out.println("\n[Sistema iniciado]");
        servidor.exibir();

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("  [1] Realizar manutenção");
            System.out.println("  [2] Ver histórico");
            System.out.println("  [3] Executar rollback");
            System.out.println("  [4] Ver status e configuração atual");
            System.out.println("  [0] Sair");
            System.out.print("  Opção: ");

            String opcao = sc.nextLine().trim();

            switch (opcao) {

                case "1":
                    System.out.println("\n--- MANUTENÇÃO ---");
                    System.out.println("  Configuração atual:");
                    servidor.configuracao.exibir();

                    System.out.print("\n  Nova versão: ");
                    String versao = sc.nextLine().trim();

                    System.out.print("  Novo IP (deixe em branco para simular erro humano): ");
                    String ip = sc.nextLine().trim();

                    System.out.print("  Nova rota: ");
                    String rota = sc.nextLine().trim();

                    ConfiguracaoServidor nova = new ConfiguracaoServidor(versao, ip, rota);

                    Evento evento = servidor.aplicarConfiguracao(nova, historico, status);
                    evento.exibir();

                    if (evento.tipo.equals("FALHA")) {
                        System.out.print("\n  Deseja executar rollback? [s/n]: ");
                        if (sc.nextLine().trim().equalsIgnoreCase("s")) {
                            rollback.executar(servidor, status, sc);
                        }
                    }
                    break;

                case "2":
                    System.out.println("\n--- HISTÓRICO DE MUDANÇAS ---");
                    historico.listar();
                    break;

                case "3":
                    System.out.println("\n--- ROLLBACK ---");
                    rollback.executar(servidor, status, sc);
                    break;

                case "4":
                    System.out.println("\n--- STATUS E CONFIGURAÇÃO ATUAL ---");
                    System.out.println("  Status : " + status.valor);
                    servidor.exibir();
                    break;

                case "0":
                    System.out.println("\n  Sistema encerrado.");
                    rodando = false;
                    break;

                default:
                    System.out.println("  Opção inválida.");
            }
        }

        sc.close();
    }
}
