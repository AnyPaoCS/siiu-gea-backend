/**
 * @author: Edson A. Terceros T.
 */

package com.umss.siiu.core.service;

import com.umss.siiu.core.model.market.ItemInstance;
import com.umss.siiu.core.repository.GenericRepository;
import com.umss.siiu.core.repository.ItemInstanceRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemInstanceServiceImpl extends GenericServiceImpl<ItemInstance> implements ItemInstanceService {
    private final ItemInstanceRepository repository;
    private final ItemService itemService;

    public ItemInstanceServiceImpl(ItemInstanceRepository repository, ItemService itemService) {
        this.repository = repository;
        this.itemService = itemService;
    }

    @Override
    protected GenericRepository<ItemInstance> getRepository() {
        return repository;
    }

    @Override
    public ItemInstance bunchSave(ItemInstance itemInstance) {
        // here make all objects save other than this resource
        if (itemInstance.getItem() != null) {
            // todo habria que distinguir si permitiremos guardar y  actualizar o ambos mirando el campo id
            // se podria buscar los fields que tienen anotacion OneToOne por ejemplo y persistir en profundidad
            // usando introspeccion de la clase
            itemService.save(itemInstance.getItem());
        }
        return super.bunchSave(itemInstance);
    }
}
