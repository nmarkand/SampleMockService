package com.be.mock.service;

import com.be.mock.service.service.BusinessServiceImpl;
import com.be.mock.service.service.RemoteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class BusinessServiceImplTest {

    BusinessServiceImpl businessServiceImpl;

    RemoteService remoteService;

    @Before
    public void setUp() {
        remoteService = mock(RemoteService.class);
        businessServiceImpl = new BusinessServiceImpl(remoteService);
    }

    @Test
    public void getValidElementsTest_TDD() {
        when(remoteService.getElements()).thenReturn(Arrays.asList("Nilay", "TestSpring", "MockTest"));
        assertEquals(3,businessServiceImpl.getValidElements().size());
    }

    @Test
    public void getValidElementsTest_BDD() {
        given(remoteService.getElements()).willReturn(Arrays.asList("Nilay", "TestSpring", "MockTest"));
        assertThat(businessServiceImpl.getValidElements().size(), is(3));
    }

    /* Argument matchers are mainly used for performing flexible verification and stubbing in Mockito. It extends ArgumentMatchers
       class to access all the matcher functions. In some cases, we need more flexibility during the verification of argument values,
       so we should use argument matchers. For example anyString(), int() etc. */

    @Test
    public void getElementTypeByNameTestArgumentMatchers_TDD() {
        when(remoteService.getElementTypeByName(anyString())).thenReturn("MockTDDType");

        assertEquals("MockTDDType", businessServiceImpl.getElementType("MockTest"));
          // We can verify but it is not useful as we are calling//(stubbing)mocking behaviour of getElementTypeByName() method on mock object.
        //verify(remoteService, times(1)).getElementTypeByName("MockTest");
    }

    @Test
    public void getElementTypeByNameTestArgumentMatchers_BDD() {
        given(remoteService.getElementTypeByName(anyString())).willReturn("MockBDDType");

        assertThat(businessServiceImpl.getElementType("MockTest"), is("MockBDDType"));

        // We can verify but it is not useful as we are calling/(stubbing)mocking behaviour of getElementTypeByName() method on mock object.
        //then(remoteService).should(times(1)).getElementTypeByName("MockTest");
    }

    @Test
    public void deleteElementTest_TDD() {
        when(remoteService.getElements()).thenReturn(Arrays.asList("Nilay", "TestMockito", "TDDMockito"));

        businessServiceImpl.deleteElement();

        //Here verify method is useful to findout wheter deleteElementsByName() method called or not on mock object.
        //There is no other way around to findout whether deleteElementsByName() called or not ( and how many times called) internally within deleteElement() method.
        //Because deleteElementsByName() it does not return any value, so we can not write when().thenReturn() for deleteElementsByName().

        verify(remoteService, never()).deleteElementsByName("Nilay");
        verify(remoteService, times(1)).deleteElementsByName("TestMockito");
        verify(remoteService, times(1)).deleteElementsByName("TDDMockito");
    }

    @Test
    public void deleteElementTest_BDD() {
        given(remoteService.getElements()).willReturn(Arrays.asList("Nilay", "TestMockito", "TDDMockito"));

        businessServiceImpl.deleteElement();

        //Here verify method is useful to findout wheter deleteElementsByName() method called or not on mock object.
        //There is no other way around to findout whether deleteElementsByName() called or not ( and how many times called) internally within deleteElement() method.
        //Because deleteElementsByName() it does not return any value, so we can not write when().thenReturn() for deleteElementsByName().

        then(remoteService).should(never()).deleteElementsByName("Nilay");
        then(remoteService).should(times(1)).deleteElementsByName("TestMockito");
        then(remoteService).should(times(1)).deleteElementsByName("TDDMockito");
    }

    @Test
    public void deleteInvalidElementTest_TDD() {
        List<String> elements = Arrays.asList("Mockito Test","Mockito TDD","Mockito BDD");
        when(remoteService.getElements()).thenReturn(elements);

        businessServiceImpl.deleteInvalidElement();

        verify(remoteService, times(1)).addElementTypeSuffix(elements);
        verify(remoteService, times(3)).deleteInvalidElements();
    }


    /* ArgumentCaptor allows us to capture an argument passed to a method in order to inspect it. This is especially useful when
    we can't access the argument outside of the method we'd like to test.

    Here we do not know size and elements of string list passed to addElementTypeSuffix() method and hence we used argument captor */

    @Test
    public void deleteInvalidElementTestArgumentCaptor_TDD() {
        ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        when(remoteService.getElements()).thenReturn(Arrays.asList("Mockito Test","Mockito TDD","Mockito BDD", "Mock"));

        businessServiceImpl.deleteInvalidElement();

        verify(remoteService, times(1)).addElementTypeSuffix(argumentCaptor.capture());
        assertEquals(argumentCaptor.getValue().size(),3);
        assertTrue(argumentCaptor.getValue().containsAll(Arrays.asList("Mockito Test","Mockito TDD","Mockito BDD")));

        verify(remoteService, times(3)).deleteInvalidElements();
    }

    @Test
    public void deleteInvalidElementTestArgumentCaptor_BDD() {
        ArgumentCaptor<List<String>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        given(remoteService.getElements()).willReturn(Arrays.asList("Mockito Test","Mockito TDD","Mockito BDD", "Mock"));

        businessServiceImpl.deleteInvalidElement();

        then(remoteService).should(times(1)).addElementTypeSuffix(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().size(), is(3));
        assertTrue(argumentCaptor.getValue().containsAll(Arrays.asList("Mockito Test","Mockito TDD","Mockito BDD")));

        then(remoteService).should(times(3)).deleteInvalidElements();
    }
}