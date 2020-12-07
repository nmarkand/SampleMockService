package com.be.mock.service;

import com.be.mock.service.db.entity.DB;
import com.be.mock.service.repo.DbRepository;
import com.be.mock.service.service.DbServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) /* It is required because we have use annotations based configuration @Captor for argument captor */
public class DbServiceImplTest {

    DbRepository dbRepository;
    DbServiceImpl dbServiceImpl;

    @Captor
    ArgumentCaptor<Long> argumentCaptor;

    @Test
    public void findAllTest_Spy() {
        //Spies are known as partially mock objects. It means spy creates a partial object and
        // you can call all the real underlying methods of the object. A Mockito spy is a partial mock.
        // We can mock a part of the object by stubbing few methods, while real method invocations will
        // be used for the other. By saying so, we can conclude that calling a method on a spy will
        // invoke the actual method, unless we explicitly stub the method, and therefore the term partial mock.

        //For partial mocking, use it to test 3rd party APIs and legacy code. You won’t require partial
        // mocks for new, test-driven, and well-designed code that follows the Single Responsibility Principle.
        //partial mocks were considered as code smells. Primarily because if you need to partially mock a
        // class while ignoring the rest of its behavior, then this class is violating the Single
        // Responsibility Principle, since the code was likely doing more than one thing.

        dbRepository = Mockito.spy(DbRepository.class);
        dbServiceImpl = new DbServiceImpl(dbRepository);

        List<DB> dbList = dbServiceImpl.findAllDb();

        assertTrue(dbList.isEmpty());
        Mockito.verify(dbRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findAllTest_Spy_with_Stubbing() {
        dbRepository = Mockito.spy(DbRepository.class);
        dbServiceImpl = new DbServiceImpl(dbRepository);

        when(dbRepository.findAll()).thenReturn(Arrays.asList(new DB()));

        List<DB> dbList = dbServiceImpl.findAllDb();

        assertTrue(!dbList.isEmpty());
        Mockito.verify(dbRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void saveTest_Spy_without_Stubbing() {
        dbRepository = Mockito.mock(DbRepository.class);
        dbServiceImpl = Mockito.spy(new DbServiceImpl(dbRepository));

        DB db = dbServiceImpl.storeDb(new DB());
        assertNull(db);
    }

    @Test
    public void saveTest_Spy_mock_with_Stubbing() {
        dbRepository = Mockito.spy(DbRepository.class);
        dbServiceImpl = Mockito.mock(DbServiceImpl.class);

        DB db = dbServiceImpl.storeDb(new DB());
        assertNull(db);
    }

    @Test
    public void saveTest_Spy_mock_mock_with_Stubbing() {
        dbRepository = Mockito.mock(DbRepository.class);
        dbServiceImpl = Mockito.mock(DbServiceImpl.class);

        DB db = dbServiceImpl.storeDb(new DB());
        assertNull(db);
    }

    @Test
    public void saveTest_Spy_Spy_without_Stubbing() {
        /* It will actually call real method but jpa repository throws exception internally because
        no spring context and no db created and hence db value is null */
        dbRepository = Mockito.spy(DbRepository.class);
        dbServiceImpl = Mockito.spy(new DbServiceImpl(dbRepository));

        DB db = dbServiceImpl.storeDb(new DB());
        assertNull(db);
    }

    @Test
    public void findAllTest_Mock() {
        dbRepository = mock(DbRepository.class);
        dbServiceImpl = new DbServiceImpl(dbRepository);

        List<DB> dbList = Arrays.asList(new DB());
        when(dbRepository.findAll()).thenReturn(dbList);

        List<DB> dbs = dbServiceImpl.findAllDb();

        assertNotNull(dbs);
        assertTrue(!dbs.isEmpty());
        Mockito.verify(dbRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findByIdTest_Spy() {
        dbRepository = Mockito.spy(DbRepository.class);
        dbServiceImpl = new DbServiceImpl(dbRepository);

        Optional<DB> db = dbServiceImpl.findDbById(new Long(5));

        assertNotNull(db);
        assertThat(false, is(db.isPresent()));
        Mockito.verify(dbRepository, Mockito.times(1)).findById(argumentCaptor.capture());

        assertThat(new Long(5), is(argumentCaptor.getValue()));
    }

    @Test
    public void findByIdTest_Spy_with_stubbing() {
        dbRepository = Mockito.spy(DbRepository.class);
        dbServiceImpl = new DbServiceImpl(dbRepository);

        when(dbRepository.findById(new Long(5))).thenReturn(Optional.of(new DB()));
        Optional<DB> db = dbServiceImpl.findDbById(new Long(5));

        assertNotNull(db);
        assertThat(true, is(db.isPresent()));
        Mockito.verify(dbRepository, Mockito.times(1)).findById(argumentCaptor.capture());

        assertThat(new Long(5), is(argumentCaptor.getValue()));
    }

    @Test
    public void findByIdTest_Mock() {
        dbRepository = mock(DbRepository.class);
        dbServiceImpl = new DbServiceImpl(dbRepository);

        when(dbRepository.findById(new Long(5))).thenReturn(Optional.of(new DB()));
        Optional<DB> db = dbServiceImpl.findDbById(new Long(5));

        assertNotNull(db);
        assertThat(true, is(db.isPresent()));
        Mockito.verify(dbRepository, Mockito.times(1)).findById(argumentCaptor.capture());

        assertThat(new Long(5), is(argumentCaptor.getValue()));
    }

    @Test
    public void spyTestInGeneral() {
        /* When we use spy, it calls actual methods of the object. Here we are spying ArrayList class.
        when add method called on the spy it calls real add method and add elements to the array list,
        and hence size (again call to real size() method of array list class) of the array list is 4. */

        List<Integer> intList = spy(ArrayList.class);
        intList.add(10);
        intList.add(20);
        intList.add(30);
        intList.add(40);
        assertThat(intList.size(),is(4));

    /*****************************************************/

        /* When we use mock, it calls mock methods of the object. Here we are mocking ArrayList class.
        when add method called on the mock it calls mock add method and doe not add elements to the array list,
        and hence size (again call to mock size() method of array list class) of the array list is 0. */

        intList = mock(ArrayList.class);
        intList.add(10);
        intList.add(20);
        intList.add(30);
        intList.add(40);
        assertThat(intList.size(),is(0));

        //stubbing is possible to customize behaviour of mock object's methods.
        when(intList.size()).thenReturn(9);
        assertThat(intList.size(),is(9));

    /*****************************************************/

        intList = spy(ArrayList.class);
        intList.add(10);
        intList.add(20);
        intList.add(30);
        intList.add(40);

        //stubbing is possible to customize behaviour of spy object's methods then it will not call real method.
        when(intList.size()).thenReturn(0);
        assertThat(intList.size(),is(0));

        //again isEmpty() is called which is a real method and returns false as list.add() method added 4 elements to the list.
        assertThat(intList.isEmpty(), is(false));
    }

}
