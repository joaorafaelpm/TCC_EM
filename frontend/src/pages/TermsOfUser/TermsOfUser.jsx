import React from 'react'
import './TermsOfUser.css'

const TermsOfUser = () => {
  return (
    <section className="terms-hero" id="termos">
      <div className="terms-overlay">
        <div className="terms-container">

          {/* TEXTO DO CONTRATO */}
          <div className="terms-text">

            <h3>Contrato de Adesão</h3>
            <h1>Termos de Uso — Pendezza Pizza</h1>
            <p className="terms-updated">Última atualização: 1 de setembro de 2026</p>

            <p>
              Estes Termos de Uso regulam o acesso e a utilização da plataforma Pendezza Pizza
              ("Plataforma"), que conecta usuários, restaurantes parceiros e entregadores para
              intermediar pedidos de delivery. Ao criar uma conta ou utilizar a Plataforma, você
              declara que leu, compreendeu e concorda com todas as condições abaixo.
            </p>

            <section className="terms-section">
              <h2>1. Definições</h2>
              <ul className="terms-list">
                <li><strong>Usuário:</strong> pessoa que utiliza a Plataforma para realizar pedidos.</li>
                <li><strong>Dono do Restaurante:</strong> responsável principal pela conta de um estabelecimento cadastrado na Plataforma.</li>
                <li><strong>Subdono:</strong> conta com acesso administrativo a um restaurante, adicionada pelo Dono do Restaurante para auxiliar na gestão (cardápio, pedidos, estoque).</li>
                <li><strong>Plataforma:</strong> o sistema Pendezza Pizza, incluindo aplicativo, site e serviços associados.</li>
              </ul>
            </section>

            <section className="terms-section">
              <h2>2. Cadastro e Conta</h2>
              <p>
                Para utilizar a Plataforma, é necessário criar uma conta com informações verdadeiras,
                completas e atualizadas. Você é responsável por manter a confidencialidade de sua
                senha e por todas as atividades realizadas em sua conta.
              </p>
              <p>
                O uso de dados de terceiros ou a criação de contas falsas é proibido e pode resultar
                em suspensão imediata, sem aviso prévio.
              </p>
            </section>

            <section className="terms-section">
              <h2>3. Papéis dentro de um Restaurante (Dono e Subdonos)</h2>
              <p>
                O Dono do Restaurante é o único responsável pela gestão da sua conta e pelas
                permissões concedidas a Subdonos vinculados a ela. A Pendezza Pizza não se
                responsabiliza por ações realizadas por Subdonos adicionados pelo Dono do
                Restaurante, incluindo alterações de cardápio, preços, disponibilidade ou
                tratamento de pedidos.
              </p>
              <p>
                Cabe exclusivamente ao Dono do Restaurante revogar o acesso de um Subdono quando
                necessário. A Plataforma não realiza triagem prévia de pessoas adicionadas como
                Subdonos.
              </p>
            </section>

            <section className="terms-section">
              <h2>4. Pedidos, Pagamentos e Cancelamentos</h2>
              <p>
                Os pedidos realizados através da Plataforma são intermediados entre o Usuário e o
                restaurante parceiro. Preços, tempo de preparo e disponibilidade de itens são de
                responsabilidade de cada restaurante.
              </p>
              <p>
                Cancelamentos após a confirmação do preparo pelo restaurante podem não ser
                reembolsáveis, a critério do estabelecimento. Em caso de erro no pedido, entrega
                não realizada ou produto com problema, o Usuário deve contatar o suporte da
                Plataforma para mediação.
              </p>
            </section>

            <section className="terms-section">
              <h2>5. Privacidade e Proteção de Dados</h2>
              <p>
                A Pendezza Pizza coleta apenas os dados necessários para o funcionamento do
                serviço (nome, e-mail, telefone, endereço de entrega e histórico de pedidos).
                Esses dados são tratados em conformidade com a Lei Geral de Proteção de Dados
                (Lei nº 13.709/2018 — LGPD).
              </p>
              <p>
                Garantimos a adoção de medidas técnicas e administrativas razoáveis para proteger
                as informações sigilosas dos usuários contra acesso não autorizado, perda ou
                vazamento. Ainda assim, nenhum sistema é absolutamente livre de riscos, e o Usuário
                será notificado em caso de incidente de segurança relevante, conforme exigido por lei.
              </p>
              <p>
                Seus dados não são vendidos a terceiros. O compartilhamento ocorre apenas quando
                estritamente necessário para a execução do serviço (ex: envio do endereço de
                entrega ao restaurante e/ou entregador responsável pelo pedido).
              </p>
            </section>

            <section className="terms-section">
              <h2>6. Responsabilidades e Limitações</h2>
              <ul className="terms-list">
                <li>A Plataforma atua como intermediária e não prepara, manipula ou transporta os alimentos.</li>
                <li>Não nos responsabilizamos por atrasos causados por fatores externos ao nosso controle, como condições climáticas ou trânsito.</li>
                <li>Não nos responsabilizamos por reações alérgicas ou problemas de saúde decorrentes de informações incorretas ou incompletas fornecidas pelo restaurante sobre ingredientes.</li>
                <li>Não nos responsabilizamos por ações de Subdonos adicionados por um Dono de Restaurante, conforme descrito na Seção 3.</li>
              </ul>
            </section>

            <section className="terms-section">
              <h2>7. Propriedade Intelectual</h2>
              <p>
                Todo o conteúdo da Plataforma — incluindo marca, logotipo, layout e código-fonte —
                pertence à Pendezza Pizza ou a seus licenciantes, sendo proibida a reprodução total
                ou parcial sem autorização prévia.
              </p>
            </section>

            <section className="terms-section">
              <h2>8. Alterações destes Termos</h2>
              <p>
                Estes Termos podem ser atualizados periodicamente. Alterações relevantes serão
                comunicadas por e-mail ou aviso na Plataforma. O uso continuado após a atualização
                implica concordância com os novos termos.
              </p>
            </section>

            <section className="terms-section">
              <h2>9. Encerramento de Conta</h2>
              <p>
                O Usuário pode encerrar sua conta a qualquer momento. A Pendezza Pizza pode
                suspender ou encerrar contas que violem estes Termos, mediante notificação
                sempre que possível.
              </p>
            </section>

            <section className="terms-section">
              <h2>10. Foro e Legislação Aplicável</h2>
              <p>
                Estes Termos são regidos pelas leis da República Federativa do Brasil. Fica eleito
                o foro da comarca de Campinas, SP, para dirimir eventuais controvérsias, salvo
                disposição legal em contrário.
              </p>
            </section>

            <a href="/oauth2/iniciar-login" className="btn-terms">
              Li e aceito os Termos de Uso
            </a>

          </div>

          {/* IMAGEM LATERAL */}
          <div className="terms-image">
            <img src="/banner.png" alt="Termos de Uso Pendezza Pizza" />
          </div>

        </div>
      </div>
    </section>
  )
}

export default TermsOfUser