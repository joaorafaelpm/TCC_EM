package com.pendezzapizza.pendezzapizza_api.core.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Collectors;

public class PageableTranslator {

    public static Pageable translate (Pageable pageable , Map<String , String> fieldsMapping) {
//        Aqui a gente basicamente mapeia todo conteúdo passado dentro do sort e nós transfomamos um camel case em entidade.parâmetro
//        Agora se nós passarmos algo como clienteNome, essa função traduz para cliente.nome
        var orders = pageable.getSort().stream()
                .filter(order -> fieldsMapping.containsKey(order.getProperty()))
                .map(order ->
                new Sort.Order(order.getDirection(), fieldsMapping.get(order.getProperty()))
        ).collect(Collectors.toList());
        return PageRequest.of(pageable.getPageNumber() , pageable.getPageSize() , Sort.by(orders));
    }

}
