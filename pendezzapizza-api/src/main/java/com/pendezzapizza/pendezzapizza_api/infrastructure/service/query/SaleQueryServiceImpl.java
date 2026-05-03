package com.pendezzapizza.pendezzapizza_api.infrastructure.service.query;

import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.DailySale;
import com.pendezzapizza.pendezzapizza_api.domain.model.enuns.OrderStatus;
import com.pendezzapizza.pendezzapizza_api.domain.service.SaleQueryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementação das querys de serviço
 *
 * <p>Todas as funções relacionadas a projeção de um meio para visualisar as vendas</p>
 */
@Repository
public class SaleQueryServiceImpl implements SaleQueryService {

//    ==============================================
//  TODO : A ideia é eventualmente adicionar ainda mais informação aqui dentro, como os ids dos pedidos, o nome deles, os clientes que mais pedem, se possível as datas sazonais (mais no inverno e etc)
//  Se for viavel, conversar com o front para deixar isso tudo opcional com o front end - adicionar vários ckeckboxes em que eu posso selecionar o que vai aparecer no pdf para mim
//    ==============================================

    @PersistenceContext
    private EntityManager manager;

//    a ideia é, a nossa venda diaria recebe 3 parâmetros, dataCriacao , total de vendas e valor/fatura total
//    Para escrever isso em SQL nós podemos fazer da seguinte forma:

//    select date(p.data_criacao) as data_criacao ,
//      count(p.id) as total_vendas,
//      sum(p.valor_total) as total_faturado
//    from pedido p
//    group by date(p.data_criacao)

    //    Agora basta transformar isso em código no criteria:
    @Override
    public List<DailySale> viewDailySales(DailySalesFilter filter , String timeOffSet) {
        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(DailySale.class);
//        from pedido
        var root = query.from(Order.class);
        var predicates = new ArrayList<Predicate>();

//        Aqui a gente ta especificando que nós queremos a coluna dataCriacao no tipo date e transformando em um LocalDate, e não em um utc comum
//        isso representa o "date(p.data_criacao)", porém neste exemplo nós não estamos considerando o fuso horário de Brasília, então vamos transformar "date(p.data_criacao)" em "date(convert_tz(p.data_criacao , '+0:00' , '-3:00'))"
        var functionConvertTzCreationDate = builder.function(
                "convert_tz" , Date.class ,root.get("creationDate") ,
                builder.literal("+00:00") , builder.literal(timeOffSet))   ;

        var functionDateCreationDate = builder.function(
                "date" , Date.class , functionConvertTzCreationDate );

//        E aqui por fim nós terminamos de construir a estrutura final que recebe a função passando para LocalDate, um count na quantidade de id de produtos daquele dia 'count(id)' e depois 'group by data_criacao' e por fim, a soma nas vendas daquele dia
//        Detalhe importante, isso só funciona por que nós conseguimos criar a classe com isso, já que nós passamos o @AllArgsConstruct no VendaDiaria.class e agora ele pode receber esse construtor
        var selection = builder.construct(DailySale.class ,
                functionDateCreationDate ,
                builder.count(root.get("id")) ,
                builder.sum(root.get("totalCost"))
        );

        if (filter.getRestaurantId() != null) {
            predicates.add(builder.equal(root.get("restaurant").get("id") , filter.getRestaurantId()));
        }

        if (filter.getStartCreationDate() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("creationDate") ,
                    filter.getStartCreationDate()));
        }
        if (filter.getEndCreationDate() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("creationDate") ,
                    filter.getEndCreationDate()));
        }

//        Evitar pedidos Cancelados ou Criados
        predicates.add(root.get("orderStatus").
                in(OrderStatus.DELIVERED , OrderStatus.CONFIRMED));

        query.select(selection);

        query.where(predicates.toArray(new Predicate[0]));

//        Aqui nós fazemos o group by
        query.groupBy(functionDateCreationDate);

//        Por via de curiosidade, vendo o query de SQL que o hibernate gerou, é exatamente o que nós passamos :
//        Hibernate: select date(p1_0.data_criacao),count(p1_0.id),sum(p1_0.valor_total) from pedido p1_0 group by 1
        return manager.createQuery(query).getResultList();
    }
}