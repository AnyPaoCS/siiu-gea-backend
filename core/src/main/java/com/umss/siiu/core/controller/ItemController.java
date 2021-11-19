/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.controller;

import com.umss.siiu.core.dto.market.ItemDto;
import com.umss.siiu.core.model.Item;
import com.umss.siiu.core.service.GenericService;
import com.umss.siiu.core.service.ItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController extends GenericController<Item, ItemDto> {
    private ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @Override
    protected GenericService getService() {
        return service;
    }
}
