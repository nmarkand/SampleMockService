package com.be.mock.service.service;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessServiceImpl {

   private RemoteService remoteService;

    public BusinessServiceImpl(RemoteService remoteService) {
        this.remoteService = remoteService;
    }

    public List<String> getValidElements() {
        return remoteService.getElements();
    }

    public String getElementType(String elementType) {
        return remoteService.getElementTypeByName(elementType);
    }

    public void deleteElement() {
        List<String> names = remoteService
                .getElements().stream()
                .filter(element -> !element.equalsIgnoreCase("Nilay"))
                .collect(Collectors.toList());

        names.forEach(name -> remoteService.deleteElementsByName(name));
    }

    public void deleteInvalidElement() {
        List<String> invalidNames = remoteService
                .getElements().stream()
                .filter(element -> element.contains("Mockito"))
                .collect(Collectors.toList());

        remoteService.addElementTypeSuffix(invalidNames);

        invalidNames.forEach(invalidName -> remoteService.deleteInvalidElements());
    }


}
