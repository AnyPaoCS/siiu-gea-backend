/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.controller;

import com.umss.siiu.core.dto.market.ItemInstanceDto;
import com.umss.siiu.core.model.market.ItemInstance;
import com.umss.siiu.core.service.ItemInstanceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iteminstances")
public class ItemInstanceController extends GenericController<ItemInstance, ItemInstanceDto> {
    private ItemInstanceService service;

    public ItemInstanceController(ItemInstanceService service) {
        this.service = service;
    }

    @Override
    protected ItemInstanceService getService() {
        return service;
    }
}
