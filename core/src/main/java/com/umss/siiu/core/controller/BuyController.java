/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.controller;

import com.umss.siiu.core.dto.BuyDto;
import com.umss.siiu.core.model.Buy;
import com.umss.siiu.core.service.BuyService;
import com.umss.siiu.core.service.GenericService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buys")
public class BuyController extends GenericController<Buy, BuyDto> {
    private BuyService service;

    public BuyController(BuyService service) {
        this.service = service;
    }

    /*
    Especificar en el hijo cuando el generico no tiene especificado los tipos de MApping
    @Override
    @DeleteMapping(value = "/{id}")
    protected void deleteElement(Long id) {
        super.deleteElement(id);
    }*/

    //    @PreAuthorize("hasRole('GENERAL')")
    @GetMapping("/user")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String onlyUsers() {
        return "You are have ROLE: USER";
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String onlyAdmins() {
        return "You are have ROLE: ADMIN";
    }

    @Override
    protected GenericService getService() {
        return service;
    }
}
