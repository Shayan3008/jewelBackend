package com.jewelbackend.backend.setup.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.setup.dto.response.ItemResponseLovDto;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.Item;

import javax.imageio.ImageIO;

@Service
public class ItemService extends BaseService {

    @Value("${image.storage.directory}")
    private String imageDirPath;

    public ItemService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
                       AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<ItemResponseDTO> findAllItems(int page, int size, String search) throws ParseException {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> itemPage = null;
        List<Item> items = null;
        if (search.isBlank()) {

            itemPage = getDaoFactory().getItemDao().findAll(pageRequest);
            items = itemPage.getContent();
        } else {
            CriteriaFilter<Item> criteriaFilter = new CriteriaFilter<>();
            items = criteriaFilter.getEntitiesByCriteriaForSearch(Item.class, HelperUtils.listToMap(search), getEntityManager(), size, page,
                    new ArrayList<>());
        }
        return items.stream().map(e -> {
                    try {
                        return getMapperFactory().getItemMapper().domainToResponse(e);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.toList());
    }

    public ItemResponseDTO findItemById(String id) throws NotPresentException, IOException {
        Optional<Item> item = getDaoFactory().getItemDao().findById(Integer.parseInt(id));
        if (!item.isPresent())
            throw new NotPresentException("Item with id: " + id + " does not exist");

        return getMapperFactory().getItemMapper().domainToResponse(item.get());
    }

    public ItemResponseDTO saveItem(ItemRequestDTO itemRequestDTO) throws InvalidInputException, IOException {
        String validateItem = getValidatorFactory().getItemValidator().validateItem(itemRequestDTO);
        if (validateItem != null) {
            throw new InvalidInputException(validateItem);
        }

        // List<Item> duplicateItem =
        // getDaoFactory().getItemDao().findByItemName(itemRequestDTO.getItemName());
        // Validations from DB
        // if (!duplicateItem.isEmpty()
        // &&
        // duplicateItem.get(0).getCategory().getCategoryCode().equals(itemRequestDTO.getCategoryId()))
        // {
        // throw new InvalidInputException(
        // String.format("Item with name %s already exists",
        // itemRequestDTO.getItemName()));
        // }

        Item item = getMapperFactory().getItemMapper().requestToDomain(itemRequestDTO);
        item.setCategory(getDaoFactory().getCategoryDao().findById(itemRequestDTO.getCategoryId()).orElse(null));
        item.setMetalType(getDaoFactory().getMetalTypeDao().findById(itemRequestDTO.getMetalName()).orElse(null));
        item.setKarigar(getDaoFactory().getKarigarDao().findById(itemRequestDTO.getKarigarId()).orElse(null));
        item = getDaoFactory().getItemDao().save(item);
        return getMapperFactory().getItemMapper().domainToResponse(item);
    }

    public ItemResponseDTO updateItem(ItemRequestDTO itemRequestDTO) throws NotPresentException, IOException {
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

    public List<ItemResponseDTO> findItemByCategoryCode(Integer categoryId) throws NotPresentException {
        Optional<Category> category = getDaoFactory().getCategoryDao().findById(categoryId);
        if (!category.isPresent()) {
            throw new NotPresentException("Category with id not present" + categoryId);
        }

        List<Item> items = getDaoFactory().getItemDao().findByCategory(category.get());
        return items.stream().map(e -> {
                    try {
                        return getMapperFactory().getItemMapper().domainToResponse(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<ItemResponseLovDto> getAllItemsLov(Integer categoryId) throws NotPresentException {
        Optional<Category> category = getDaoFactory().getCategoryDao().findById(categoryId);
        if (!category.isPresent()) {
            throw new NotPresentException("Category with id not present" + categoryId);
        }
        return getDaoFactory().getItemDao().findByCategory(category.get()).stream()
                .map(e -> {
                    ItemResponseLovDto itemResponseLovDto = new ItemResponseLovDto();
                    itemResponseLovDto.setId(e.getId());
                    itemResponseLovDto.setDesignNo(e.getDesignNo());
                    return itemResponseLovDto;
                }).collect(Collectors.toList());
    }

    //    Method will take images from item and upload into directory.
    public void uploadItemImageToDir() throws IOException {
//        var items = getDaoFactory().getItemDao().getAllItemWhoHaveImages();
//        for (var item : items) {
//            if(item.getItemImage() == null)
//                continue;
//            String fileName = UUID.randomUUID() + "_" + item.getDesignNo() + ".jpg";
//            // Create the directory if it does not exist
//            Path uploadPath = Paths.get(imageDirPath);
//            if (!Files.exists(uploadPath)) {
//                HelperUtils.logMessage(Level.ERROR, "DIRECTORY NOT FOUND");
//                return;
//            }
//
//            // Generate a unique filename
//
//            Path filePath = Paths.get(imageDirPath, fileName);
//
//
//            // Convert byte array to BufferedImage
//            ByteArrayInputStream bais = new ByteArrayInputStream(item.getItemImage());
//            BufferedImage bufferedImage = ImageIO.read(bais);
//
//            // Save the BufferedImage to the specified path
//            File outputFile = new File(filePath.toString());
//            ImageIO.write(bufferedImage, "jpg", outputFile);
//            item.setItemImagePath(filePath.toString());
//            item.setItemImage(null);
//            getDaoFactory().getItemDao().save(item);
        }


    }

