package com.jewelbackend.backend.setup.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.common.validator.ValidatorFactory;
import com.jewelbackend.backend.setup.dao.DaoFactory;
import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.mapper.MapperFactory;
import com.jewelbackend.backend.setup.models.Item;

@Service
public class ItemService extends BaseService {

    public ItemService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<ItemResponseDTO> findAllItems(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> itemPage = getDaoFactory().getItemDao().findAll(pageRequest);
        List<Item> items = itemPage.getContent();
        return items.stream().map(e -> getMapperFactory().getItemMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public ItemResponseDTO findItemById(String id) throws NotPresentException {
        Optional<Item> item = getDaoFactory().getItemDao().findById(Integer.parseInt(id));
        if (!item.isPresent())
            throw new NotPresentException("Item with id: " + id + " does not exist");

        return getMapperFactory().getItemMapper().domainToResponse(item.get());
    }

    public ItemResponseDTO saveItem(ItemRequestDTO itemRequestDTO) throws InvalidInputException {
        String validateItem = getValidatorFactory().getItemValidator().validateItem(itemRequestDTO);
        if (validateItem != null) {
            throw new InvalidInputException(validateItem);
        }

        List<Item> duplicateItem = getDaoFactory().getItemDao().findByItemName(itemRequestDTO.getItemName());
        // Validations from DB
        if (!duplicateItem.isEmpty()
                && duplicateItem.get(0).getCategory().getCategoryCode().equals(itemRequestDTO.getCategoryId())) {
            throw new InvalidInputException(
                    String.format("Item with name %s already exists", itemRequestDTO.getItemName()));
        }

        Item item = getMapperFactory().getItemMapper().requestToDomain(itemRequestDTO);
        item.setCategory(getDaoFactory().getCategoryDao().findById(itemRequestDTO.getCategoryId()).orElse(null));
        item.setMetalType(getDaoFactory().getMetalTypeDao().findById(itemRequestDTO.getMetalName()).orElse(null));
        item.setKarigar(getDaoFactory().getKarigarDao().findById(itemRequestDTO.getKarigarId()).orElse(null));
        item = getDaoFactory().getItemDao().save(item);
        return getMapperFactory().getItemMapper().domainToResponse(item);
    }

    public ItemResponseDTO updateItem(ItemRequestDTO itemRequestDTO) throws NotPresentException {
        Item item = getDaoFactory().getItemDao().findById(itemRequestDTO.getId()).orElse(null);
        if (item == null)
            throw new NotPresentException("item Not found");
        Item updatedItem = getMapperFactory().getItemMapper().requestToDomain(itemRequestDTO);
        updatedItem.setCategory(getDaoFactory().getCategoryDao().findById(itemRequestDTO.getCategoryId()).orElse(null));
        updatedItem
                .setMetalType(getDaoFactory().getMetalTypeDao().findById(itemRequestDTO.getMetalName()).orElse(null));
        updatedItem.setKarigar(getDaoFactory().getKarigarDao().findById(itemRequestDTO.getKarigarId()).orElse(null));
        updatedItem = getDaoFactory().getItemDao().save(updatedItem);
        return getMapperFactory().getItemMapper().domainToResponse(updatedItem);
    }

    public void deleteItem(String id) throws NotPresentException {
        Item item = getDaoFactory().getItemDao().findById(Integer.parseInt(id)).orElse(null);
        if (item == null)
            throw new NotPresentException(String.format("Item with %s not found", id));
        getDaoFactory().getItemDao().delete(item);
    }

}
