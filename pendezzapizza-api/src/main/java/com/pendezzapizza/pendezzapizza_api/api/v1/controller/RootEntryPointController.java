package com.pendezzapizza.pendezzapizza_api.api.v1.controller;

import com.pendezzapizza.pendezzapizza_api.api.v1.PendezzaLinks;
import com.pendezzapizza.pendezzapizza_api.core.security.PendezzaPizzaSecurity;
import io.swagger.v3.oas.annotations.Operation;
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
    private PendezzaLinks pendezzaLinks;

    @Autowired
    private PendezzaPizzaSecurity pendezzaPizzaSecurity;

    @Operation(hidden = true)
    @GetMapping
    public RootEntryPointModel root() {
        var rootEntryPointModel = new RootEntryPointModel();

        if (pendezzaPizzaSecurity.canCreateOrders()) {
            rootEntryPointModel.add(pendezzaLinks.linkToOrders("orders"));
        }

        if (pendezzaPizzaSecurity.canConsultRestaurants()) {
            rootEntryPointModel.add(pendezzaLinks.linkToRestaurants("restaurants"));
        }

        if (pendezzaPizzaSecurity.canConsultUsersGroupsPermissions()) {
            rootEntryPointModel.add(pendezzaLinks.linkToGroups("groups"));
            rootEntryPointModel.add(pendezzaLinks.linkToUsers("users"));
            rootEntryPointModel.add(pendezzaLinks.linkToPermissions("permissions"));
        }

        if (pendezzaPizzaSecurity.canConsultPaymentMethods()) {
            rootEntryPointModel.add(pendezzaLinks.linkToPaymentMethods("payment-methods"));
        }

        if (pendezzaPizzaSecurity.canConsultStates()) {
            rootEntryPointModel.add(pendezzaLinks.linkToStates("states"));
        }

        if (pendezzaPizzaSecurity.canConsultCities()) {
            rootEntryPointModel.add(pendezzaLinks.linkToCities("cities"));
        }

        if (pendezzaPizzaSecurity.canConsultStatistics()) {
            rootEntryPointModel.add(pendezzaLinks.linkToStatistics("statistics"));
        }

        return rootEntryPointModel;
    }

    public static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
    }
}