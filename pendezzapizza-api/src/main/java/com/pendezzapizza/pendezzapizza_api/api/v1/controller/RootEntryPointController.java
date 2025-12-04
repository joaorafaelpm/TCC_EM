package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaPizzaLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointController {

    @Autowired
    private PendezzaPizzaLinks links;

    @GetMapping
    public RootEntryPointModel root() {
        var rootModel = new RootEntryPointModel();

        rootModel.add(links.linkToOrders("orders"));
        rootModel.add(links.linkToRestaurants("restaurants"));
        rootModel.add(links.linkToGroups("groups"));
        rootModel.add(links.linkToUsers("users"));
        rootModel.add(links.linkToPermissions("permissions"));
        rootModel.add(links.linkToPaymentMethods("paymentMethods"));
        rootModel.add(links.linkToStates("states"));
        rootModel.add(links.linkToCities("cities"));
        rootModel.add(links.linkToStatistics("statistics"));

        return rootModel;
    }

    public static class RootEntryPointModel
            extends RepresentationModel<RootEntryPointModel> { }
}
