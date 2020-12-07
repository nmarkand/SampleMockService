package com.be.mock.service.service;

import java.util.List;

public interface RemoteService {
    public List<String> getElements();
    public String getElementTypeByName(String elementName);
    public void deleteElementsByName(String elementName);
    public void deleteInvalidElements();
    public void addElementTypeSuffix(List<String> elementNames);
}
