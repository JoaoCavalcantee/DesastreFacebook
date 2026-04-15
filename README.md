# Sistema de Manutenção Segura de Rede

Projeto acadêmico da matéria de arquitetura de software, com motivação de resolver algum desastre ocasionado por software, que no caso foi o **problema de manutenção do Facebook em 2021** que ocasionou numa ausencia de 6H do serviço devido a uma falha humana, e como humanos sabemos que vamos falhar uma hora ou outra, e a ideia do software é fazer um simulador que tem como objetivo precaver esse tipo de problema

O sistema opera via terminal (CLI) e oferece as seguintes simulações:
* **Manutenção:** Aplicação de novas configurações de rede (IP e Rota) ao servidor.
* **Validação de Segurança:** Bloqueio de configurações incompletas (simulando a falha de manutenção), gerando eventos de alerta.
* **Histórico de Mudanças:** Registro automático de versões anteriores a cada nova atualização bem-sucedida.
* **Rollback:** Recuperação imediata para uma versão estável salva no histórico em caso de falha crítica (o "Plano B" que faltou no incidente real).

Arquitetura Orientada a Objetos

**A Máquina e seus Dados (`Servidor` e `ConfiguracaoServidor`):** São as entidades que guardam as informações de infraestrutura. Elas representam o computador físico e os parâmetros exatos que ele precisa para se conectar à internet.

* **Os Avisos do Sistema (`Status` e `Evento`):** Funcionam como a camada de observabilidade. A função deles é monitorar a manutenção e notificar o usuário sobre o estado do servidor (ex: se está "OK" ou se entrou em "FALHA" após uma alteração).

* **O Mecanismo de Segurança (`HistoricoMudancas` e `Rollback`):** Representam o subsistema de resiliência. O histórico atua como um registro das configurações estáveis. Se uma configuração quebrar o servidor, o componente de Rollback consulta esse registro e reverte as alterações, restaurando o sistema para o último estado seguro.
## 💻 Como Executar -> javac Sistema.java

## Diagramas:

MER: <br>
<img width="791" height="349" alt="image" src="https://github.com/user-attachments/assets/bc004182-2234-488a-af1c-90313f063943" />

Diagrama de Classes: <br>
<img width="2816" height="1536" alt="Diagrama de classes desastres" src="https://github.com/user-attachments/assets/6ccf04b1-d8fc-4e10-a98d-4d3651d2e3ca" />

Diagrama de Atividades: <br>
<img width="2816" height="1536" alt="diagrama de atividades desastres" src="https://github.com/user-attachments/assets/ca063c86-89e5-4557-88cc-16ad51f9bf63" />
